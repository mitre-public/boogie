package org.mitre.tdp.boogie.alg;

import java.util.Collection;
import java.util.List;
import javax.annotation.Nonnull;

import com.google.common.base.Preconditions;
import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.approach.ApproachPredictor;
import org.mitre.tdp.boogie.alg.approach.impl.NoApproachPredictor;
import org.mitre.tdp.boogie.alg.graph.LegGraph;
import org.mitre.tdp.boogie.alg.graph.LegGraphFactory;
import org.mitre.tdp.boogie.alg.graph.ProcedureGraph;
import org.mitre.tdp.boogie.alg.resolve.ResolvedRoute;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.SectionResolver;
import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.alg.split.SectionSplitter;
import org.mitre.tdp.boogie.models.ExpandedRoute;
import org.mitre.tdp.boogie.service.LookupService;
import org.mitre.tdp.boogie.service.impl.AirportService;
import org.mitre.tdp.boogie.service.impl.AirwayService;
import org.mitre.tdp.boogie.service.impl.FixService;
import org.mitre.tdp.boogie.service.impl.ProcedureGraphService;

/**
 * The core route inflation algorithm in TDP. This class is configured with a single
 * cycle of procedure data assumed to be overlapping with the date associated with
 * the provided route string.
 * <p>
 * The algorithm leverages the component algorithms present in order:
 * <p>
 * 1) {@link SectionSplitter} to split the provided route string into its components
 * <p>
 * 2) {@link SectionResolver} to associate split sections of the route with infrastructure
 * (e.g. procedures, waypoints, etc.)
 * <p>
 * 2.5) (Optional) Allows configuration of a {@link ApproachPredictor} object which will
 * modify the resolved output of step 2 with an additional section of candidate approach
 * procedures.
 * <p>
 * 3) {@link LegGraph} to determine the path the flight most likely took through
 * the resolved infrastructure elements (e.g. resolving references to navaids that
 * appear with the same name in multiple ICAO regions)
 * <p>
 * The class then finishes by publishing the output to the {@link ExpandedRoute}
 * object which may then be tagged with more source specific information.
 * <p>
 * Note - This class does not try to do things like re-name or re-map route element names
 * that may be in error (e.g. filing GOLEM.GNDLF1.ATL instead of KATL) so some upstream
 * route string to infrastructure name standardization may be necessary on the user side
 * to get a more complete set of expansions.
 */
public interface RouteExpander {

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
  ProcedureGraphService procedureService();

  default ApproachPredictor approachPredictor() {
    return new NoApproachPredictor();
  }

  /**
   * Takes the argument route string and expands it against the infrastructure data in the
   * provided services.
   */
  default ExpandedRoute expand(@Nonnull String route) {
    Preconditions.checkArgument(!route.isEmpty());

    List<SectionSplit> splits = SectionSplitter.splits(route);

    SectionResolver resolver = SectionResolver.with(this);
    ResolvedRoute resolved = resolver.resolve(splits);

    ResolvedSection approach = approachPredictor().predictAndCheck(
        resolved.sectionAt(resolved.sectionCount() - 2),
        resolved.sectionAt(resolved.sectionCount() - 1));

    resolved.insert(approach);

    LegGraph graph = LegGraphFactory.build(resolved);
    GraphPath<SectionSplitLeg, DefaultWeightedEdge> shortestPath = graph.shortestPath();

    return new ExpandedRoute(route, shortestPath.getVertexList());
  }

  /**
   * Builds a default implementation of the RouteExpander with no configured approach prediction.
   */
  static RouteExpander with(Collection<? extends Fix> fixes, Collection<? extends Airway> airways, Collection<? extends Airport> airports, Collection<? extends Transition> transitions) {
    FixService fs = FixService.with(fixes);
    AirwayService ws = AirwayService.with(airways);
    AirportService as = AirportService.with(airports);
    ProcedureGraphService ps = ProcedureGraphService.with(transitions);
    return new RouteExpander() {

      @Override
      public LookupService<Fix> fixService() {
        return fs;
      }

      @Override
      public LookupService<Airway> airwayService() {
        return ws;
      }

      @Override
      public LookupService<Airport> airportService() {
        return as;
      }

      @Override
      public ProcedureGraphService procedureService() {
        return ps;
      }
    };
  }
}
