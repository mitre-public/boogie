package org.mitre.tdp.boogie.alg;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.approach.ApproachPredictor;
import org.mitre.tdp.boogie.alg.approach.impl.NoApproachPredictor;
import org.mitre.tdp.boogie.alg.graph.LegGraphFactory;
import org.mitre.tdp.boogie.alg.graph.ProcedureGraph;
import org.mitre.tdp.boogie.alg.graph.RouteLegGraph;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.alg.resolve.ResolvedRoute;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.RunwayPredictor;
import org.mitre.tdp.boogie.alg.resolve.SectionResolver;
import org.mitre.tdp.boogie.alg.resolve.SidRunwayTransitionFilter;
import org.mitre.tdp.boogie.alg.resolve.StarRunwayTransitionFilter;
import org.mitre.tdp.boogie.alg.resolve.element.ProcedureElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.alg.split.SectionSplitter;
import org.mitre.tdp.boogie.models.ExpandedRoute;
import org.mitre.tdp.boogie.service.LookupService;
import org.mitre.tdp.boogie.service.ProcedureService;
import org.mitre.tdp.boogie.service.impl.AirportService;
import org.mitre.tdp.boogie.service.impl.AirwayService;
import org.mitre.tdp.boogie.service.impl.FixService;
import org.mitre.tdp.boogie.service.impl.ProcedureGraphService;

import com.google.common.base.Preconditions;

/**
 * The core route inflation algorithm in TDP. This class is configured with a single
 * cycle of procedure data assumed to be overlapping with the date associated with
 * the provided route string.
 *
 * <p>The algorithm leverages the component algorithms present in order:
 *
 * <p>1) {@link SectionSplitter} to split the provided route string into its components
 *
 * <p>2) {@link SectionResolver} to associate split sections of the route with infrastructure
 * (e.g. procedures, waypoints, etc.)
 *
 * <p>2.5) (Optional) Allows configuration of a {@link ApproachPredictor} object which will
 * modify the resolved output of step 2 with an additional section of candidate approach
 * procedures.
 *
 * <p>3) {@link RouteLegGraph} to determine the path the flight most likely took through
 * the resolved infrastructure elements (e.g. resolving references to navaids that
 * appear with the same name in multiple ICAO regions)
 *
 * <p>The class then finishes by publishing the output to the {@link ExpandedRoute}
 * object which may then be tagged with more source specific information.
 *
 * <p>Note - This class does not try to do things like re-name or re-map route element names
 * that may be in error (e.g. filing GOLEM.GNDLF1.ATL instead of KATL) so some upstream
 * route string to infrastructure name standardization may be necessary on the user side
 * to get a more complete set of expansions.
 */
public interface RouteExpander extends Serializable {

  /**
   * Builds a new instance of the route expander with the given lookup services.
   */
  static RouteExpander with(
      LookupService<Fix> fixService,
      LookupService<Airway> airwayService,
      LookupService<Airport> airportService,
      ProcedureService procedureService) {
    return new RouteExpander() {
      @Override
      public LookupService<Fix> fixService() {
        return fixService;
      }

      @Override
      public LookupService<Airway> airwayService() {
        return airwayService;
      }

      @Override
      public LookupService<Airport> airportService() {
        return airportService;
      }

      @Override
      public ProcedureService procedureService() {
        return procedureService;
      }
    };
  }

  /**
   * Builds a default implementation of the RouteExpander with no configured approach prediction.
   */
  static RouteExpander with(Collection<? extends Fix> fixes, Collection<? extends Airway> airways, Collection<? extends Airport> airports, Collection<? extends Transition> transitions) {
    FixService fs = FixService.with(fixes);
    AirwayService ws = AirwayService.with(airways);
    AirportService as = AirportService.with(airports);
    ProcedureGraphService ps = ProcedureGraphService.withTransitions(transitions);
    return with(fs, ws, as, ps);
  }

  /**
   * Returns a configured service for {@link Fix} objects.
   */
  LookupService<Fix> fixService();

  /**
   * Returns a configured service for {@link Airway} objects.
   */
  LookupService<Airway> airwayService();

  /**
   * Returns a configured service for {@link Airport} objects.
   */
  LookupService<Airport> airportService();

  /**
   * Returns a configured service for {@link ProcedureGraph} objects.
   */
  ProcedureService procedureService();

  /**
   * The {@link ApproachPredictor} to use in route resolution.
   */
  default ApproachPredictor approachPredictor() {
    return new NoApproachPredictor();
  }

  /**
   * The {@link RunwayPredictor} to use when resolving the predicted arrival runway.
   */
  default RunwayPredictor arrivalRunwayPredictor() {
    return RunwayPredictor.noop();
  }

  /**
   * The {@link RunwayPredictor} to use when resolving the predicted departure runway.
   */
  default RunwayPredictor departureRunwayPredictor() {
    return RunwayPredictor.noop();
  }

  /**
   * The {@link SectionResolver} to use for matching section splits to infrastructure elements.
   */
  default SectionResolver sectionResolver() {
    return SectionResolver.with(this);
  }

  /**
   * The {@link SectionSplitter} to use in splitting the route string into {@link SectionSplit}.
   */
  default SectionSplitter sectionSplitter() {
    return SectionSplitter.newInstance();
  }

  /**
   * Takes the argument route string and expands it against the infrastructure data in the
   * provided services.
   */
  default ExpandedRoute expand(@Nonnull String route) {
    Preconditions.checkArgument(!route.isEmpty(), "Route cannot be empty.");

    List<SectionSplit> splits = sectionSplitter().splits(route);

    ResolvedRoute resolved = sectionResolver().resolve(splits);

    setRunwayTransitionFilters(resolved);

    ResolvedSection approach = approachPredictor().predictAndCheck(
        resolved.sectionAt(resolved.sectionCount() - 2),
        resolved.sectionAt(resolved.sectionCount() - 1));

    resolved.insert(approach);

    RouteLegGraph graph = LegGraphFactory.build(resolved);
    GraphPath<GraphableLeg, DefaultWeightedEdge> shortestPath = graph.shortestPath();

    return new ExpandedRoute(route, shortestPath.getVertexList());
  }

  /**
   * Set the element transition filters based on the supplied arrival runway predictions.
   */
  default void setRunwayTransitionFilters(ResolvedRoute resolvedRoute) {
    resolvedRoute.sections().forEach(resolvedSection -> resolvedSection.elements().stream()
        .filter(resolvedElement -> resolvedElement instanceof ProcedureElement)
        .map(ProcedureElement.class::cast)
        .forEach(procedureElement -> {
          arrivalRunwayPredictor().predictedRunway()
              .ifPresent(arrivalRunway -> procedureElement.setTransitionFilter(new StarRunwayTransitionFilter(arrivalRunway)));

          departureRunwayPredictor().predictedRunway()
              .ifPresent(departureRunway -> procedureElement.setTransitionFilter(new SidRunwayTransitionFilter(departureRunway)));
        }));
  }
}
