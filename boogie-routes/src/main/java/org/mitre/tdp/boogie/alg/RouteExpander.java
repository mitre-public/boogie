package org.mitre.tdp.boogie.alg;

import static com.google.common.base.Preconditions.checkNotNull;

import java.io.Serializable;
import java.util.List;
import java.util.Optional;
import javax.annotation.Nonnull;

import org.jgrapht.GraphPath;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.mitre.tdp.boogie.alg.approach.ApproachPredictor;
import org.mitre.tdp.boogie.alg.approach.impl.NoApproachPredictor;
import org.mitre.tdp.boogie.alg.graph.LegGraphFactory;
import org.mitre.tdp.boogie.alg.graph.RouteLegGraph;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.alg.resolve.ResolvedRoute;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.alg.resolve.resolver.RouteResolver;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.alg.split.SectionSplitter;
import org.mitre.tdp.boogie.models.ExpandedRoute;
import org.mitre.tdp.boogie.util.Iterators;

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
 * <p>2) {@link RouteResolver} to associate split sections of the route with infrastructure
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
public final class RouteExpander implements Serializable {
  /**
   * The {@link SectionSplitter} to use in splitting the route string into {@link SectionSplit}.
   */
  private final SectionSplitter sectionSplitter;
  /**
   * The {@link RouteResolver} to use for matching section splits to infrastructure elements.
   */
  private final RouteResolver routeResolver;
  /**
   * The {@link ApproachPredictor} to use in route resolution.
   */
  private final ApproachPredictor approachPredictor;

  public RouteExpander(
      SectionSplitter sectionSplitter,
      RouteResolver routeResolver
  ) {
    this(sectionSplitter, routeResolver, new NoApproachPredictor());
  }

  public RouteExpander(
      SectionSplitter sectionSplitter,
      RouteResolver routeResolver,
      ApproachPredictor approachPredictor
  ) {
    this.sectionSplitter = checkNotNull(sectionSplitter);
    this.routeResolver = checkNotNull(routeResolver);
    this.approachPredictor = checkNotNull(approachPredictor);
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

    List<SectionSplit> splits = sectionSplitter.splits(route);

    ResolvedRoute resolved = routeResolver.apply(splits);

    if (!Iterators.checkMatchCount(resolved.sections(), s -> !s.allLegs().isEmpty())) {
      return Optional.empty();
    }

    Optional<ResolvedSection> approach = approachPredictor.predictAndCheck(
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
}
