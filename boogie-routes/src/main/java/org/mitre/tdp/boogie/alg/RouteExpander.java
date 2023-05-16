package org.mitre.tdp.boogie.alg;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;
import static org.mitre.caasd.commons.collect.HashedLinkedSequence.newHashedLinkedSequence;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.function.UnaryOperator;
import java.util.stream.Stream;
import javax.annotation.Nullable;

import org.mitre.caasd.commons.collect.HashedLinkedSequence;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.alg.chooser.RouteChooser;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.SectionResolver;
import org.mitre.tdp.boogie.alg.resolve.infer.SectionInferrer;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.alg.split.SectionSplitter;
import org.mitre.tdp.boogie.fn.QuadFunction;
import org.mitre.tdp.boogie.fn.TriFunction;
import org.mitre.tdp.boogie.util.Iterators;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

public final class RouteExpander implements
    QuadFunction<String, String, String, RequiredNavigationEquipage[], Optional<ExpandedRoute>>,
    TriFunction<String, String, String, Optional<ExpandedRoute>>,
    Function<String, Optional<ExpandedRoute>> {

  private static final Logger LOG = LoggerFactory.getLogger(RouteExpander.class);

  private final SectionSplitter sectionSplitter;

  private final LookupService<Procedure> procedureService;

  private final LookupService<Procedure> proceduresAtAirport;

  private final SectionResolver standardSectionResolver;

  private final RouteChooser routeChooser;

  private RouteExpander(Builder builder) {
    this.sectionSplitter = requireNonNull(builder.sectionSplitter);
    this.procedureService = requireNonNull(builder.proceduresByName);
    this.proceduresAtAirport = requireNonNull(builder.proceduresByAirport);
    this.standardSectionResolver = requireNonNull(builder.sectionResolver);
    this.routeChooser = requireNonNull(builder.routeChooser);
  }

  /**
   * Standard builder for a route expander instance.
   */
  public static Builder builder() {
    return new Builder();
  }

  /**
   * Creates an in-memory version of a {@link RouteExpander} given the provided infrastructure data and using the {@link Builder}
   * defaults for the functional components of the expander.
   *
   * <p>This is provided alongside the normal {@link #builder()} method for convenience when working with purely in-memory infra
   * data. The generated {@link SectionResolver} can be configured with {@link Builder#sectionResolver(UnaryOperator)}.
   *
   * @param uAirports   all user-defined airport implementation to use in the expansion
   * @param uProcedures all user-defined procedure implementations to use in the expansion
   * @param uAirways    all user-defined airway implementations to use in the expansion
   * @param uFixes      all user-defined fix implementations to use in the expansion
   */
  public static <A extends Airport, P extends Procedure, W extends Airway, F extends Fix> RouteExpander.Builder inMemoryBuilder(
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

    return RouteExpander.builder()
        .proceduresByName(proceduresByName)
        .proceduresByAirport(LookupService.inMemory(procedures, p -> Stream.of(p.airportIdentifier())))
        .sectionResolver(
            SectionResolver.standard(
                LookupService.inMemory(airports, a -> Stream.of(a.airportIdentifier())),
                proceduresByName,
                LookupService.inMemory(airways, a -> Stream.of(a.airwayIdentifier())),
                LookupService.inMemory(fixes, f -> Stream.of(f.fixIdentifier()))
            )
        );
  }

  /**
   * Returns the result of applying the route expander to the provided route string with no departure/arrival runway information.
   * <br>
   *
   * @param route the route to expand, SID/STAR expansion will start/stop at the beginning/end of the common portions of the
   *              procedures
   *              <br>
   *              This class may occasionally return {@link Optional#empty()} when the input route either doesn't contain enough information to
   *              build a path or if too many of its component elements can't be found within the provided {@link LookupService}s.
   */
  @Override
  public Optional<ExpandedRoute> apply(String route) {
    return apply(route, null, null);
  }

  /**
   * Takes the argument route string and expands it against the infrastructure data in the provided services.
   * <br>
   *
   * @param route           the route to expand
   * @param departureRunway the departure runway used, if provided the appropriate departure runway transition will be included
   *                        in the final expanded route
   * @param arrivalRunway   the arrival runway used, if provided the appropriate arrival runway transition will be included in the
   *                        final expanded route
   *                        <br>
   *                        This class may occasionally return {@link Optional#empty()} when the input route either doesn't contain enough information to
   *                        build a path or if too many of its component elements can't be found within the provided {@link LookupService}s.
   */
  @Override
  public Optional<ExpandedRoute> apply(String route, @Nullable String departureRunway, @Nullable String arrivalRunway) {
    return apply(route, departureRunway, arrivalRunway, null);
  }

  /**
   * Takes the argument route string and expands it against the infrastructure data in the provided services.
   * <br>
   *
   * @param route           the route to expand
   * @param departureRunway the departure runway used, if provided the appropriate departure runway transition will be included
   *                        in the final expanded route
   * @param arrivalRunway   the arrival runway used, if provided the appropriate arrival runway transition will be included in the
   *                        final expanded route
   * @param equipage        the equipage of the aircraft, if provided (along with an arrival runway) this will determine the type of
   *                        approach procedure included in the final expansion, the varargs list represents the preference order (e.g. RNP > RNAV), if
   *                        the listing is incomplete (doesnt cover all options) procedures with unlisted equipages will be ignored (not returned as
   *                        candidates) and not be included in the final expansion
   *                        <br>
   *                        This class may occasionally return {@link Optional#empty()} when the input route either doesn't contain enough information to
   *                        build a path or if too many of its component elements can't be found within the provided {@link LookupService}s.
   */
  @Override
  public Optional<ExpandedRoute> apply(String route, @Nullable String departureRunway, @Nullable String arrivalRunway, @Nullable RequiredNavigationEquipage... equipage) {

    checkArgument(route != null && !route.isEmpty(), "Route cannot be null or empty.");
    LOG.info("Beginning expansion of route {} with context: [arrival runway {}, departure runway {}]", route, arrivalRunway, departureRunway);

    List<SectionSplit> sectionSplits = sectionSplitter.splits(route);
    LOG.info("Generated {} SectionSplits from route {}.", sectionSplits.size(), route);

    HashedLinkedSequence<ResolvedSection> initial = newHashedLinkedSequence(standardSectionResolver.applyTo(sectionSplits));
    LOG.info("Resolved {} Elements across {} Sections.", initial.stream().mapToInt(section -> section.elements().size()).sum(), initial.size());

    RouteContext context = RouteContext.standard()
        .proceduresByName(procedureService)
        .departureRunway(departureRunway)
        .arrivalRunway(arrivalRunway)
        .proceduresByAirport(proceduresAtAirport)
        .equipagePreference(equipage)
        .build();

    for (SectionInferrer inferrer : context.inferrers()) {
      appendInferredSections(initial, inferrer);
    }

    LOG.info("Resolved {} Elements across {} Sections.", initial.stream().mapToInt(section -> section.elements().size()).sum(), initial.size());
    return chooseExpandedRoute(route, new ArrayList<>(initial), departureRunway, arrivalRunway);
  }

  private void appendInferredSections(HashedLinkedSequence<ResolvedSection> sequence, SectionInferrer inferrer) {
    inferrer.inferAcross(sequence).forEach((start, inferred) -> {
      ResolvedSection previous = start;

      for (ResolvedSection section : inferred) {
        sequence.insertAfter(section, previous);
        previous = section;
      }
    });
  }

  private Optional<ExpandedRoute> chooseExpandedRoute(String route, List<ResolvedSection> resolvedSections, @Nullable String departureRunway, @Nullable String arrivalRunway) {
    if (!Iterators.checkMatchCount(resolvedSections, s -> !s.allLegs().isEmpty())) {
      LOG.info("Returning empty - no ResolvedSections with legs in their ResolvedElements.");
      return Optional.empty();
    } else {

      List<ResolvedSection> sortedByIndex = resolvedSections.stream()
          .sorted(comparing(ResolvedSection::sectionSplit))
          .collect(toList());

      ExpandedRoute expandedRoute = routeChooser.chooseRoute(sortedByIndex);

      // tag-on user-supplied expansion information (which may not have been summarized internally)
      ExpandedRoute updated = expandedRoute
          .updateSummary(routeSummary -> routeSummary.toBuilder()
              .route(route)
              .departureRunway(departureRunway)
              .arrivalRunway(arrivalRunway)
              .build()
          );

      return Optional.of(updated);
    }
  }

  public static final class Builder {

    private SectionSplitter sectionSplitter = SectionSplitter.faaIfrFormat();

    private LookupService<Procedure> proceduresByName = LookupService.noop();

    private LookupService<Procedure> proceduresByAirport = LookupService.noop();

    private SectionResolver sectionResolver;

    private RouteChooser routeChooser = RouteChooser.graphical();

    private Builder() {
    }

    /**
     * Methodology for tokenizing the input route string such that each token can be resolved with a section resolver.
     *
     * <p>The pre-defined set of implementations live in the {@link SectionSplitter} interface.
     *
     * <p>Default: {@link SectionSplitter#faaIfrFormat()}
     */
    public Builder sectionSplitter(SectionSplitter sectionSplitter) {
      this.sectionSplitter = requireNonNull(sectionSplitter);
      return this;
    }

    /**
     * A lookup service for procedures by their given identifier, e.g. {@code GNDLF2} or {@code CHPPR1}.
     *
     * <p>This implementation is required to infer arrival/departure runway transitions based on a provided arrival or departure
     * runway identifier (given as context during the expansion call).
     *
     * <p>In-memory lookup services can be built from collections of objects {@link LookupService#inMemory(Iterable, Function)}.
     *
     * <p>Default: {@link LookupService#noop()}
     */
    public Builder proceduresByName(LookupService<Procedure> proceduresByName) {
      this.proceduresByName = requireNonNull(proceduresByName);
      return this;
    }

    /**
     * A lookup service for procedures based on the identifier of the airport they serve, e.g. {@code KATL} or {@code WSSS}.
     *
     * <p>This implementation is required to infer an appropriate approach procedures when given an arrival runway and list of
     * preferred equipages for the approach.
     *
     * <p>In-memory lookup services can be built from collections of objects {@link LookupService#inMemory(Iterable, Function)}.
     *
     * <p>Default: {@link LookupService#noop()}
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
     * <p>Typically, clients use {@link SectionResolver#standard(LookupService, LookupService, LookupService, LookupService)}.
     */
    public Builder sectionResolver(SectionResolver sectionResolver) {
      this.sectionResolver = requireNonNull(sectionResolver);
      return this;
    }

    /**
     * Allows for the customization of an already-configured section resolver implementation (will throw an exception if one isn't
     * already present). This is provided mainly to allow simple decoration of a default-configured resolver (e.g. make it surly).
     *
     * @param sectionResolverConfigurer transform operator to decorate/wrap an already-configured resolver in additional functionality
     */
    public Builder sectionResolver(UnaryOperator<SectionResolver> sectionResolverConfigurer) {
      requireNonNull(sectionResolver, "There should already be a SectionResolver configured we want to transform.");
      requireNonNull(sectionResolverConfigurer, "The configuration function should be non-null.");

      this.sectionResolver = sectionResolverConfigurer.apply(sectionResolver);
      return this;
    }

    /**
     * Defines how the expander chooses the appropriate route through the resolved infrastructure candidates across all resolved
     * sections of the route string.
     *
     * <p>Default: {@link RouteChooser#graphical()}
     */
    public Builder routeChooser(RouteChooser routeChooser) {
      this.routeChooser = requireNonNull(routeChooser);
      return this;
    }

    public RouteExpander build() {
      return new RouteExpander(this);
    }
  }
}
