package org.mitre.tdp.boogie.alg;

import java.io.Serializable;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
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
import org.mitre.tdp.boogie.alg.split.IfrFormatSectionSplitter;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.alg.split.SectionSplitter;
import org.mitre.tdp.boogie.models.ExpandedRoute;
import org.mitre.tdp.boogie.service.LookupService;
import org.mitre.tdp.boogie.service.ProcedureService;
import org.mitre.tdp.boogie.service.impl.AirportService;
import org.mitre.tdp.boogie.service.impl.AirwayService;
import org.mitre.tdp.boogie.service.impl.FixService;
import org.mitre.tdp.boogie.service.impl.ProcedureGraphService;
import org.mitre.tdp.boogie.utils.Iterators;

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
public class RouteExpander implements Serializable {
  /**
   * Returns a configured service for {@link Fix} objects.
   */
  private final LookupService<Fix> fixService;
  /**
   * Returns a configured service for {@link Airway} objects.
   */
  private final LookupService<Airway> airwayService;
  /**
   * Returns a configured service for {@link Airport} objects.
   */
  private final LookupService<Airport> airportService;
  /**
   * Returns a configured service for {@link ProcedureGraph} objects.
   */
  private final ProcedureService procedureService;
  /**
   * The {@link SectionSplitter} to use in splitting the route string into {@link SectionSplit}.
   */
  private final SectionSplitter sectionSplitter;
  /**
   * The {@link SectionResolver} to use for matching section splits to infrastructure elements.
   */
  private final SectionResolver sectionResolver;
  /**
   * The {@link ApproachPredictor} to use in route resolution.
   */
  private ApproachPredictor approachPredictor = new NoApproachPredictor();
  /**
   * The {@link RunwayPredictor} to use when resolving the predicted arrival runway.
   */
  private RunwayPredictor arrivalRunwayPredictor = RunwayPredictor.noop();
  /**
   * The {@link RunwayPredictor} to use when resolving the predicted departure runway.
   */
  private RunwayPredictor departureRunwayPredictor = RunwayPredictor.noop();

  public RouteExpander(
      LookupService<Fix> fixService,
      LookupService<Airway> airwayService,
      LookupService<Airport> airportService,
      ProcedureService procedureService,
      SectionSplitter sectionSplitter,
      SectionResolver sectionResolver) {
    this.fixService = fixService;
    this.airwayService = airwayService;
    this.airportService = airportService;
    this.procedureService = procedureService;
    this.sectionSplitter = sectionSplitter;
    this.sectionResolver = sectionResolver == null ? SectionResolver.with(this) : sectionResolver;
  }

  public LookupService<Fix> fixService() {
    return fixService;
  }

  public LookupService<Airway> airwayService() {
    return airwayService;
  }

  public LookupService<Airport> airportService() {
    return airportService;
  }

  public ProcedureService procedureService() {
    return procedureService;
  }

  public SectionResolver sectionResolver() {
    return sectionResolver;
  }

  public SectionSplitter sectionSplitter() {
    return sectionSplitter;
  }

  public ApproachPredictor approachPredictor() {
    return approachPredictor;
  }

  public RouteExpander setApproachPredictor(ApproachPredictor approachPredictor) {
    this.approachPredictor = approachPredictor;
    return this;
  }

  public RunwayPredictor arrivalRunwayPredictor() {
    return arrivalRunwayPredictor;
  }

  public RouteExpander setArrivalRunwayPredictor(RunwayPredictor arrivalRunwayPredictor) {
    this.arrivalRunwayPredictor = arrivalRunwayPredictor;
    return this;
  }

  public RunwayPredictor departureRunwayPredictor() {
    return departureRunwayPredictor;
  }

  public RouteExpander setDepartureRunwayPredictor(RunwayPredictor departureRunwayPredictor) {
    this.departureRunwayPredictor = departureRunwayPredictor;
    return this;
  }

  /**
   * Takes the argument route string and expands it against the infrastructure data in the
   * provided services.
   *
   * This class may occasionally return {@link Optional#empty()} when the input route cannot
   * be resolved to two or more sections (enough to actually make a route).
   */
  public Optional<ExpandedRoute> expand(@Nonnull String route) {
    Preconditions.checkArgument(!route.isEmpty(), "Route cannot be empty.");

    List<SectionSplit> splits = sectionSplitter().splits(route);

    ResolvedRoute resolved = sectionResolver().resolve(splits);

    if (!Iterators.checkMatchCount(resolved.sections(), s -> !s.elements().isEmpty())) {
      return Optional.empty();
    }

    Optional<ResolvedSection> approach = approachPredictor().predictAndCheck(
        resolved.sectionAt(resolved.sectionCount() - 2),
        resolved.sectionAt(resolved.sectionCount() - 1));

    approach.ifPresent(resolved::insert);

    RouteLegGraph graph = LegGraphFactory.build(resolved);
    GraphPath<GraphableLeg, DefaultWeightedEdge> shortestPath = graph.shortestPath();

    if (shortestPath == null) {
      return Optional.empty();
    }

    return Optional.of(new ExpandedRoute(route, shortestPath.getVertexList()));
  }

  /**
   * Builds a new instance of the route expander with the given lookup services.
   */
  public static RouteExpander with(
      LookupService<Fix> fixService,
      LookupService<Airway> airwayService,
      LookupService<Airport> airportService,
      ProcedureService procedureService) {
    return new RouteExpander(fixService, airwayService, airportService, procedureService, new IfrFormatSectionSplitter(), null);
  }

  /**
   * Builds a default implementation of the RouteExpander with no configured approach prediction.
   */
  public static RouteExpander with(Collection<? extends Fix> fixes, Collection<? extends Airway> airways, Collection<? extends Airport> airports, Collection<? extends Transition> transitions) {
    FixService fs = FixService.with(fixes);
    AirwayService ws = AirwayService.with(airways);
    AirportService as = AirportService.with(airports);
    ProcedureGraphService ps = ProcedureGraphService.withTransitions(transitions);
    return with(fs, ws, as, ps);
  }
}
