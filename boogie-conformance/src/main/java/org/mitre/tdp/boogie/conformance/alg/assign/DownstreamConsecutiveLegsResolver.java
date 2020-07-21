package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;

/**
 * Graphical structure for storing consecutive leg connections as potential transitions. This class is used to determine the
 * set of downstream legs which are available to transition to from the currently assigned leg.
 *
 * This class inserts as nodes each of the {@link Leg}s within the supplied {@link ConsecutiveLegs} records, while the top
 * level consecutive legs become directed edges. When querying for the {@link #downstreamLegsOf(ConsecutiveLegs)} this class
 * returns all the legs which provide previous->next transitions from the current leg of the supplied consecutiveLegs.
 */
public class DownstreamConsecutiveLegsResolver extends SimpleDirectedGraph<Leg, ConsecutiveLegs> {

  private ConnectivityInspector<Leg, ConsecutiveLegs> connectivityInspector;

  public DownstreamConsecutiveLegsResolver() {
    super(ConsecutiveLegs.class);
  }

  /**
   * The configured {@link ConnectivityInspector} for the graph.
   */
  public ConnectivityInspector<Leg, ConsecutiveLegs> connectivityInspector() {
    if (connectivityInspector == null) {

      this.connectivityInspector = new ConnectivityInspector<>(this);
    }
    return connectivityInspector;
  }

  /**
   * Returns the set of {@link ConsecutiveLegs} downstream of the input consecutiveLegs. These are considered to be the set of
   * available transition targets from a given leg in the {@link ScoreBasedRouteResolver}.
   *
   * The graph stores consecutive legs as directed edges rather than as vertices. Due to the implementation of {@link ConnectivityInspector}
   * the directionality of edge from source to target vertex must be checked before returning the input#current() to this::connected() edge.
   */
  public List<ConsecutiveLegs> downstreamLegsOf(ConsecutiveLegs consecutiveLegs) {
    // weakly connected components
    return connectivityInspector().connectedSetOf(consecutiveLegs.current()).stream()
        // need to check the directional edge exists
        .filter(leg -> this.containsEdge(consecutiveLegs.current(), leg))
        // if so get it and return the actual source ConsecutiveLegs edge
        .map(leg -> this.getEdge(consecutiveLegs.current(), leg))
        .collect(Collectors.toList());
  }

  /**
   * Generates a new {@link DownstreamConsecutiveLegsResolver} from the input list of {@link ConsecutiveLegs}. This graph can then be used to
   * resolve the downstream edges of a given {@link ConsecutiveLegs} edge.
   */
  public static DownstreamConsecutiveLegsResolver from(List<? extends ConsecutiveLegs> consecutiveLegsList) {
    DownstreamConsecutiveLegsResolver downstreamConsecutiveLegsResolver = new DownstreamConsecutiveLegsResolver();
    consecutiveLegsList.forEach(consecutiveLegs -> {
      downstreamConsecutiveLegsResolver.addVertex(consecutiveLegs.current());
      consecutiveLegs.previous().ifPresent(downstreamConsecutiveLegsResolver::addVertex);
      consecutiveLegs.next().ifPresent(downstreamConsecutiveLegsResolver::addVertex);

      consecutiveLegs.previous()
          .filter(previous -> !previous.equals(consecutiveLegs.current()))
          .ifPresent(previous -> addNewEdge(previous, consecutiveLegs.current(), consecutiveLegs, downstreamConsecutiveLegsResolver));

      consecutiveLegs.next()
          .filter(next -> !next.equals(consecutiveLegs.current()))
          .ifPresent(next -> addNewEdge(consecutiveLegs.current(), next, consecutiveLegs, downstreamConsecutiveLegsResolver));
    });
    return downstreamConsecutiveLegsResolver;
  }

  /**
   * Adds a new edge to the graph if one doesnt already exist between the vertex pair. If one does it resolves the conflict
   * based first on whether the new or old edge contains the target vertex as its {@link ConsecutiveLegs#current()} leg and
   * in then in the case of them both containing it it prefers the previously inserted edge.
   */
  private static void addNewEdge(Leg source, Leg target, ConsecutiveLegs newEdge, SimpleDirectedGraph<Leg, ConsecutiveLegs> graph) {
    if (graph.containsEdge(source, target)) {
      ConsecutiveLegs currentEdge = graph.getEdge(source, target);

      ConsecutiveLegs preferredEdge = target.equals(newEdge.current())
          ? target.equals(currentEdge.current())
          ? currentEdge
          : newEdge
          : currentEdge;

      if (!preferredEdge.equals(currentEdge)) {
        graph.removeEdge(currentEdge);
        graph.addEdge(source, target, newEdge);
      }
    } else {
      graph.addEdge(source, target, newEdge);
    }
  }
}
