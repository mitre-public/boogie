package org.mitre.tdp.boogie.alg;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Comparator.comparing;
import static java.util.Objects.requireNonNull;
import static java.util.stream.Collectors.toList;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.alg.chooser.GraphBasedRouteChooser;
import org.mitre.tdp.boogie.alg.chooser.RouteChooser;
import org.mitre.tdp.boogie.alg.resolve.ApproachResolver;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.SectionResolver;
import org.mitre.tdp.boogie.alg.resolve.SidRunwayTransitionResolver;
import org.mitre.tdp.boogie.alg.resolve.StarRunwayTransitionResolver;
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

  /**
   * Split an input route string into matchable sections. See {@link SectionSplitter}.
   */
  private final SectionSplitter sectionSplitter;
  /**
   * Maintaining an internal instance of a {@link LookupService} for procedures which can be used to configure separate internal
   * resolvers in the presence of arrival/departure runway information.
   */
  private final LookupService<Procedure> procedureService;
  private final LookupService<Procedure> proceduresAtAirport;
  /**
   * Auto-configured composite {@link SectionResolver} for all of the standard section types.
   * <br>
   * 1. Fixes (Tailored/Waypoint/Navaids)
   * 2. Procedures (SID/STAR)
   * 3. Airways
   * 4. Airports
   * 5. Lat/Lon Literals
   */
  private final SectionResolver standardSectionResolver;
  /**
   * Function for turning a sequence of {@link ResolvedSection}s in to a sequence of legs - this is most commonly implemented as
   * the {@link GraphBasedRouteChooser}.
   */
  private final RouteChooser routeChooser;

  /**
   * Pre-canned implementations can be found here: {@link RouteExpanderFactory}. Otherwise this method is left public for others
   * to inject and override specific functionality.
   */
  public RouteExpander(
      SectionSplitter sectionSplitter,
      LookupService<Procedure> procedureService,
      LookupService<Procedure> proceduresAtAirport,
      SectionResolver sectionResolver,
      RouteChooser routeChooser
  ) {
    this.sectionSplitter = requireNonNull(sectionSplitter);
    this.procedureService = requireNonNull(procedureService);
    this.proceduresAtAirport = requireNonNull(proceduresAtAirport);
    this.standardSectionResolver = requireNonNull(sectionResolver);
    this.routeChooser = requireNonNull(routeChooser);
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
    List<ResolvedSection> resolvedSections = resolveSections(route, departureRunway, arrivalRunway);
    return chooseExpandedRoute(route, resolvedSections, departureRunway, arrivalRunway);
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
    List<ResolvedSection> resolvedSections = resolveSections(route, departureRunway, arrivalRunway);

    if (arrivalRunway != null && equipage != null && !resolvedSections.isEmpty()) {
      LOG.info("Attempting to resolve approach procedure with arrival runway {} and equipage {}.", arrivalRunway, equipage);
      ApproachResolver approachResolver = new ApproachResolver(arrivalRunway, equipage, proceduresAtAirport);

      ResolvedSection lastSection = resolvedSections.get(resolvedSections.size() - 1);
      approachResolver.apply(lastSection).ifPresent(resolvedSections::add);
    }

    return chooseExpandedRoute(route, resolvedSections, departureRunway, arrivalRunway);
  }

  /**
   * Attempts to resolve the sections based on the route, departure, and arrival runway including predictors for departure/arrival
   * runway transitions.
   */
  private List<ResolvedSection> resolveSections(String route, @Nullable String departureRunway, @Nullable String arrivalRunway) {
    checkArgument(route != null && !route.isEmpty(), "Route cannot be null or empty.");
    LOG.info("Beginning expansion of route {} with departure runway {} and arrival runway {}.", route, departureRunway, arrivalRunway);

    List<SectionSplit> sectionSplits = sectionSplitter.splits(route);
    LOG.info("Generated {} SectionSplits from route {}.", sectionSplits.size(), route);

    SidRunwayTransitionResolver sidRunway = new SidRunwayTransitionResolver(departureRunway, procedureService);
    StarRunwayTransitionResolver starRunway = new StarRunwayTransitionResolver(arrivalRunway, procedureService);

    List<ResolvedSection> resolvedSections = standardSectionResolver.compose(sidRunway).compose(starRunway).applyTo(sectionSplits);
    LOG.info("Returned {} ResolvedElements across {} ResolvedSections.", resolvedSections.stream().mapToInt(section -> section.elements().size()).sum(), resolvedSections.size());

    return resolvedSections;
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
}
