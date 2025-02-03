package org.mitre.tdp.boogie.alg.facade;

import static java.util.Objects.requireNonNull;
import static java.util.Optional.of;
import static java.util.stream.Collectors.toList;

import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.CategoryAndType;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.alg.LookupService;
import org.mitre.tdp.boogie.alg.ResolvedLeg;
import org.mitre.tdp.boogie.alg.RouteContext;
import org.mitre.tdp.boogie.alg.RouteExpander;
import org.mitre.tdp.boogie.alg.chooser.RouteChooser;
import org.mitre.tdp.boogie.alg.chooser.graph.TokenMapper;
import org.mitre.tdp.boogie.alg.resolve.RouteTokenResolver;
import org.mitre.tdp.boogie.alg.split.RouteTokenizer;
import org.mitre.tdp.boogie.fn.QuadFunction;
import org.mitre.tdp.boogie.fn.TriFunction;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * This class provides a more domain-data-driven (DDD?) facade/wrapper around a {@link RouteExpander} which more closely matches
 * the operational side of use clients expect.
 *
 * <p>It also serves as a good example for clients which want to configure their own {@link RouteExpander} explicitly along with
 * its input {@link RouteContext}.
 */
public final class FluentRouteExpander implements
    QuadFunction<String, String, String, RequiredNavigationEquipage[], Optional<ExpandedRoute>>,
    TriFunction<String, String, String, Optional<ExpandedRoute>>,
    Function<String, Optional<ExpandedRoute>> {

  private static final Logger LOG = LoggerFactory.getLogger(FluentRouteExpander.class);

  private static final Function<List<ResolvedLeg>, List<ExpandedRouteLeg>> toExpandedLegs = toExpandedLegs();

  private static final RouteSummarizer routeSummarizer = new RouteSummarizer();

  private final LookupService<Procedure> procedureService;

  private final LookupService<Procedure> proceduresAtAirport;

  private final RouteExpander routeExpander;

  private FluentRouteExpander(Builder builder) {
    this.procedureService = requireNonNull(builder.proceduresByName);
    this.proceduresAtAirport = requireNonNull(builder.proceduresByAirport);
    this.routeExpander = builder.routeExpander.build();
  }

  /**
   * Standard builder for a route expander instance. See additional setup docs on the {@link Builder} methods.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates an in-memory version of a {@link FluentRouteExpander} given the provided infrastructure data and using the {@link Builder}
   * defaults for the functional components of the expander.
   *
   * <p>This is provided alongside the normal {@link #builder()} method for convenience when working with purely in-memory infra
   * data. The generated {@link RouteTokenResolver} can be configured with {@link Builder#routeTokenResolver(UnaryOperator)}.
   *
   * @param uAirports   all user-defined airport implementation to use in the expansion
   * @param uProcedures all user-defined procedure implementations to use in the expansion
   * @param uAirways    all user-defined airway implementations to use in the expansion
   * @param uFixes      all user-defined fix implementations to use in the expansion
   */
  public static <A extends Airport, P extends Procedure, W extends Airway, F extends Fix> FluentRouteExpander.Builder inMemoryBuilder(
      Collection<A> uAirports,
      Collection<P> uProcedures,
      Collection<W> uAirways,
      Collection<F> uFixes
  ) {

    // this cast is annoying for clients and they often bring their own objects, give them (one) hook to let us handle it
    Collection<Airport> airports = (Collection<Airport>) uAirports;
    Collection<Procedure> procedures = (Collection<Procedure>) uProcedures;
    Collection<Airway> airways = (Collection<Airway>) uAirways;
    Collection<Fix> fixes = (Collection<Fix>) uFixes;

    LookupService<Procedure> proceduresByName = LookupService.inMemory(procedures, p -> Stream.of(p.procedureIdentifier()));

    return FluentRouteExpander.builder()
        .proceduresByName(proceduresByName)
        .proceduresByAirport(LookupService.inMemory(procedures, p -> Stream.of(p.airportIdentifier())))
        .routeTokenResolver(
            RouteTokenResolver.standard(
                LookupService.inMemory(airports, a -> Stream.of(a.airportIdentifier())),
                proceduresByName,
                LookupService.inMemory(airways, a -> Stream.of(a.airwayIdentifier())),
                LookupService.inMemory(fixes, f -> Stream.of(f.fixIdentifier()))
            )
        );
  }

  // for now this is fine to keep the effective contract...
  private static Function<List<ResolvedLeg>, List<ExpandedRouteLeg>> toExpandedLegs() {

    DirectToConverter directToConverter = new DirectToConverter();
    ResolvedLegConverter resolvedLegConverter = new ResolvedLegConverter();
    RedundantLegCombiner redundantLegCombiner = new RedundantLegCombiner();

    return list -> redundantLegCombiner.apply(directToConverter.apply(list).stream().map(resolvedLegConverter).collect(toList()));
  }

  /**
   * Returns the result of applying the route expander to the provided route string with no departure/arrival runway information.
   *
   * @param route the route to expand, SID/STAR expansion will start/stop at the beginning/end of the common portions of the
   *              procedures
   * @return This class may occasionally return {@link Optional#empty()} when the input route either doesn't contain enough information to
   * build a path or if too many of its component elements can't be found within the provided {@link LookupService}s.
   */
  @Override
  public Optional<ExpandedRoute> apply(String route) {
    return apply(route, null, null);
  }

  /**
   * Takes the argument route string and expands it against the infrastructure data in the provided services.
   *
   * @param route           the route to expand
   * @param departureRunway the departure runway used, if provided the appropriate departure runway transition will be included
   *                        in the final expanded route
   * @param arrivalRunway   the arrival runway used, if provided the appropriate arrival runway transition will be included in the
   *                        final expanded route
   * @return This class may occasionally return {@link Optional#empty()} when the input route either doesn't contain enough information to
   * build a path or if too many of its component elements can't be found within the provided {@link LookupService}s.
   */
  @Override
  public Optional<ExpandedRoute> apply(String route, @Nullable String departureRunway, @Nullable String arrivalRunway) {
    return apply(route, departureRunway, arrivalRunway, new RequiredNavigationEquipage[]{});
  }

  /**
   * Takes the argument route string and expands it against the infrastructure data in the provided services.
   *
   * @param route           the route to expand
   * @param departureRunway the departure runway used, if provided the appropriate departure runway transition will be included
   *                        in the final expanded route
   * @param arrivalRunway   the arrival runway used, if provided the appropriate arrival runway transition will be included in the
   *                        final expanded route
   * @param equipage        the equipage of the aircraft, if provided (along with an arrival runway) this will determine the type of
   *                        approach procedure included in the final expansion, the varargs list represents the preference order (e.g. {@code RNP > RNAV}),
   *                        if the listing is incomplete (doesn't cover all options) procedures with unlisted equipages will be ignored (not returned as
   *                        candidates) and not be included in the final expansion
   * @return This class may occasionally return {@link Optional#empty()} when the input route either doesn't contain enough information to
   * build a path or if too many of its component elements can't be found within the provided {@link LookupService}s.
   */
  @Override
  public Optional<ExpandedRoute> apply(String route, @Nullable String departureRunway, @Nullable String arrivalRunway, @Nullable RequiredNavigationEquipage... equipage) {

    RouteDetails details = RouteDetails.builder()
        .departureRunway(departureRunway)
        .arrivalRunway(arrivalRunway)
        .equipagePreference(equipage)
        .build();

    return expand(route, details);
  }

  /**
   * This method is the main entry point to the flight plan expander.
   * @param route the string which includes the airports on the start/end for now.
   * @param details the route details object which has e.g., runways ...etc.
   * @return the expanded route if we can do it.
   */
  public Optional<ExpandedRoute> expand(String route, RouteDetails details) {

    logInputs(route, details);

    RouteContext context = RouteContext.standard()
        .proceduresByName(procedureService)
        .departureRunway(details.departureRunway().orElse(null))
        .arrivalRunway(details.arrivalRunway().orElse(null))
        .proceduresByAirport(proceduresAtAirport)
        .equipagePreference(details.equipagePreference())
        .defaultSid(details.defaultSid().orElse(null))
        .defaultStar(details.defaultStar().orElse(null))
        .categoryAndType(details.categoryAndType().orElse(CategoryAndType.NULL))
        .build();

    return of(routeExpander.expand(route, context))
        .filter(l -> !l.isEmpty())
        .map(this::createExpandedRoute)
        .map(expandedRoute -> expandedRoute
            .updateSummary(routeSummary -> routeSummary.toBuilder()
                .route(route)
                .departureRunway(details.departureRunway().orElse(null))
                .arrivalRunway(details.arrivalRunway().orElse(null))
                .build()
            ));
  }

  private void logInputs(String route, RouteDetails details) {
    if (LOG.isDebugEnabled()) {
      LOG.debug("- Beginning expansion of route: {}", route);
      LOG.debug(String.format("  %20s %20s %30s", "Departure Runway", "Arrival Runway", "Equipage Preferences"));
      LOG.debug(
          String.format("  %20s %20s %30s",
              details.departureRunway().orElse("None"),
              details.arrivalRunway().orElse("None"),
              details.equipagePreference().stream().map(Enum::name).collect(Collectors.joining(">"))
          )
      );
    }
  }

  private ExpandedRoute createExpandedRoute(List<ResolvedLeg> legs) {
    return new ExpandedRoute(routeSummarizer.apply(legs).orElse(null), toExpandedLegs.apply(legs));
  }

  public static final class Builder {

    private LookupService<Procedure> proceduresByName = LookupService.noop();

    private LookupService<Procedure> proceduresByAirport = LookupService.noop();

    private RouteExpander.Standard.Builder routeExpander = RouteExpander.standard();

    private Builder() {
    }

    /**
     * Methodology for tokenizing the input route string such that each token can be resolved with a section resolver.
     *
     * <p>The pre-defined set of implementations live in the {@link RouteTokenizer} interface.
     *
     * <p>Default: {@link RouteTokenizer#faaIfrFormat()}
     */
    public Builder routeTokenizer(RouteTokenizer routeTokenizer) {
      this.routeExpander.routeTokenizer(routeTokenizer);
      return this;
    }

    /**
     * Required to infer appropriate arrival/departure runway transitions when given an arrival or departure runway.
     *
     * <p>In-memory lookup services can be built from collections of objects {@link LookupService#inMemory(Iterable, Function)}.
     *
     * @param proceduresByName lookup service for procedures by their given identifier, e.g. {@code GNDLF2} or {@code CHPPR1}.
     *                         Default: {@link LookupService#noop()}
     */
    public Builder proceduresByName(LookupService<Procedure> proceduresByName) {
      this.proceduresByName = requireNonNull(proceduresByName);
      return this;
    }

    /**
     * Required to infer an appropriate approach procedures when given an arrival runway and list of preferred equipages for the
     * approach.
     *
     * <p>In-memory lookup services can be built from collections of objects {@link LookupService#inMemory(Iterable, Function)}.
     *
     * @param proceduresByAirport lookup service for procedures by the identifier of the airport they serve, e.g. {@code KATL}
     *                            or {@code WSSS}. Default {@link LookupService#noop()}
     */
    public Builder proceduresByAirport(LookupService<Procedure> proceduresByAirport) {
      this.proceduresByAirport = requireNonNull(proceduresByAirport);
      return this;
    }

    /**
     * The section resolver implementation to use when resolving tokenized sections of the input route string to infrastructure
     * elements.
     *
     * <p>There is no default value for this field as a variety of infrastructure lookup services need to be provided to power the
     * section resolver.
     *
     * @param routeTokenResolver the token resolver implementation to use, default
     *                           {@link RouteTokenResolver#standard(LookupService, LookupService, LookupService, LookupService)}
     */
    public Builder routeTokenResolver(RouteTokenResolver routeTokenResolver) {
      this.routeExpander.routeTokenResolver(routeTokenResolver);
      return this;
    }

    /**
     * Allows for the customization of an already-configured section resolver implementation (will throw an exception if one isn't
     * already present). This is provided mainly to allow simple decoration of a default-configured resolver (e.g. make it surly).
     *
     * @param routeTokenResolverConfigurer transform operator to decorate an already-configured resolver in additional functionality
     */
    public Builder routeTokenResolver(UnaryOperator<RouteTokenResolver> routeTokenResolverConfigurer) {
      requireNonNull(routeTokenResolverConfigurer, "The configuration function should be non-null.");
      this.routeExpander.routeTokenResolver(routeTokenResolverConfigurer);
      return this;
    }

    /**
     * Defines how the expander chooses the appropriate flyable route through the resolved infrastructure candidates across all
     * tokens in the route string.
     *
     * @param routeChooser the route chooser implementation to use, default {@link RouteChooser#graphical(TokenMapper)} with
     *                     {@link TokenMapper#standard()}
     */
    public Builder routeChooser(RouteChooser routeChooser) {
      this.routeExpander.routeChooser(routeChooser);
      return this;
    }

    public FluentRouteExpander build() {
      return new FluentRouteExpander(this);
    }
  }
}
