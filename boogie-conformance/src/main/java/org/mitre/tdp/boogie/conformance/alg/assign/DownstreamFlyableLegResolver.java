package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assemble.LegPair;
import org.mitre.tdp.boogie.conformance.alg.assemble.ReducedLegGraph;
import org.mitre.tdp.boogie.util.Combinatorics;

/**
 * Graphical structure for storing consecutive leg connections as potential transitions. This class is used to determine the
 * set of downstream legs which are available to transition to from the currently assigned leg.
 *
 * This class inserts as nodes each of the {@link Leg}s within the supplied {@link FlyableLeg} records, while the top
 * level consecutive legs become directed edges. When querying for the {@link #downstreamLegsOf(FlyableLeg)} this class
 * returns all the legs which provide previous->next transitions from the current leg of the supplied consecutiveLegs.
 */
public class DownstreamFlyableLegResolver extends SimpleDirectedGraph<FlyableLeg, DefaultEdge> {

  private ConnectivityInspector<FlyableLeg, DefaultEdge> connectivityInspector;

  public DownstreamFlyableLegResolver() {
    super(DefaultEdge.class);
  }

  /**
   * The configured {@link ConnectivityInspector} for the graph.
   */
  public ConnectivityInspector<FlyableLeg, DefaultEdge> connectivityInspector() {
    if (connectivityInspector == null) {
      this.connectivityInspector = new ConnectivityInspector<>(this);
    }
    return connectivityInspector;
  }

  /**
   * Returns the set of {@link FlyableLeg} downstream of the input consecutiveLegs. These are considered to be the set of
   * available transition targets from a given leg in the {@link ScoreBasedRouteResolver}.
   *
   * The graph stores consecutive legs as directed edges rather than as vertices. Due to the implementation of {@link ConnectivityInspector}
   * the directionality of edge from source to target vertex must be checked before returning the input#current() to this::connected() edge.
   */
  public List<FlyableLeg> downstreamLegsOf(FlyableLeg consecutiveLegs) {
    // weakly connected components
    return connectivityInspector().connectedSetOf(consecutiveLegs).stream()
        // need to check the directional edge exists
        .filter(leg -> this.containsEdge(consecutiveLegs, leg))
        .collect(Collectors.toList());
  }

  /**
   * Generates a new {@link DownstreamFlyableLegResolver} from the input collection of {@link FlyableLeg}s.
   */
  public static DownstreamFlyableLegResolver withFlyableLegs(List<? extends FlyableLeg> conformableLegs) {
    DownstreamFlyableLegResolver downstreamFlyableLegResolver = new DownstreamFlyableLegResolver();

    Combinatorics.pairwiseCombos(conformableLegs).forEachRemaining(pair -> {
      downstreamFlyableLegResolver.addVertex(pair.first());
      downstreamFlyableLegResolver.addVertex(pair.second());

      addEdgeIfMatching(pair.first(), pair.second(), downstreamFlyableLegResolver);
      addEdgeIfMatching(pair.second(), pair.first(), downstreamFlyableLegResolver);
    });

    return downstreamFlyableLegResolver;
  }

  /**
   * Generates a new {@link DownstreamFlyableLegResolver} from the input list of {@link FlyableLeg}. This graph can then be used to
   * resolve the downstream edges of a given {@link FlyableLeg} edge.
   */
  public static DownstreamFlyableLegResolver fromLegPairs(List<? extends LegPair> legPairs) {
    return DownstreamFlyableLegResolver.withFlyableLegs(ReducedLegGraph.with(legPairs).flyableLegs());
  }

  private static void addEdgeIfMatching(FlyableLeg t1, FlyableLeg t2, DownstreamFlyableLegResolver resolver) {
    if (t2.previous().filter(t1.current()::equals).isPresent() && t1.next().filter(t2.current()::equals).isPresent()) {
      resolver.addEdge(t1, t2);
    }
  }
}
