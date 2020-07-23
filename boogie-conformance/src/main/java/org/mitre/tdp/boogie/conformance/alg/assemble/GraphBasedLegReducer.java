package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.mitre.tdp.boogie.Leg;

/**
 * Graphical instance of a leg reducer. It merges common legs where repeated in the graphical structure of their routes based
 * on the explicitly referenced {@link Leg#pathTerminator()} and {@link Leg#type()} via {@link ReducibleLeg} decorating leg
 * implementations with a standard hashcode and equals method.
 */
public interface GraphBasedLegReducer extends ConsecutiveLegReducer {

  @Override
  default List<ConsecutiveLegs> reduce(List<? extends ConsecutiveLegs> legs) {
    SimpleDirectedGraph<ReducibleLeg, LegPair> reductionGraph = buildReductionGraph(legs);
    return reductionGraphToTriples(reductionGraph);
  }

  /**
   * Converts the input {@link ConsecutiveLegs} into pairwise leg components based on subsets of definitions of the leg features.
   */
  default SimpleDirectedGraph<ReducibleLeg, LegPair> buildReductionGraph(List<? extends ConsecutiveLegs> legs) {
    SimpleDirectedGraph<ReducibleLeg, LegPair> reductionGraph = new SimpleDirectedGraph<>(LegPair.class);

    legs.stream()
        .map(this::toLegPairs)
        .flatMap(Collection::stream)
        .forEach(leg -> {
          Optional<ReducibleLeg> prev = leg.previous().map(ReducibleLeg::new);
          Optional<ReducibleLeg> next = leg.next().map(ReducibleLeg::new);

          prev.ifPresent(reductionGraph::addVertex);
          next.ifPresent(reductionGraph::addVertex);

          ReducibleLeg current = new ReducibleLeg(leg.current());
          reductionGraph.addVertex(current);

          prev.filter(p -> !p.equals(current)).ifPresent(p -> reductionGraph.addEdge(p, current, leg));
          next.filter(n -> !current.equals(n)).ifPresent(n -> reductionGraph.addEdge(current, n, leg));
        });
    return reductionGraph;
  }

  /**
   * Iterates through the vertex set of the graph checking the inbound and outbound legs from any given leg building them into
   * all combinations of triples (inbound, current, outbound).
   */
  default List<ConsecutiveLegs> reductionGraphToTriples(SimpleDirectedGraph<ReducibleLeg, LegPair> reductionGraph) {
    // re-assemble the triples
    ConnectivityInspector<ReducibleLeg, LegPair> connectivityInspector = new ConnectivityInspector<>(reductionGraph);
    return reductionGraph.vertexSet().stream().flatMap(vertex -> {

      // weak connectivity so have to check directional edge containment
      Set<ReducibleLeg> connected = connectivityInspector.connectedSetOf(vertex);

//      Set<ReducibleLeg> incomingLegs = connected.stream().filter(leg -> reductionGraph.containsEdge(leg, vertex)).collect(Collectors.toSet());
//      Set<ReducibleLeg> outgoingLegs = connected.stream().filter(leg -> reductionGraph.containsEdge(vertex, leg)).collect(Collectors.toSet());

      // all combinations of incoming and outgoing legs through the given fix
//      if (!outgoingLegs.isEmpty() && !incomingLegs.isEmpty()) {
      return Stream.concat(
          connected.stream().filter(leg -> reductionGraph.containsEdge(leg, vertex))
              .map(leg -> new LegPair(leg.leg(), vertex.leg(), false).setSourceObject(reductionGraph.getEdge(leg, vertex).getSourceObject().orElse(null))),
          connected.stream().filter(leg -> reductionGraph.containsEdge(vertex, leg))
              .map(leg -> new LegPair(vertex.leg(), leg.leg(), false).setSourceObject(reductionGraph.getEdge(vertex, leg).getSourceObject().orElse(null))));

//        return Combinatorics.cartesianProduct(incomingLegs, outgoingLegs).stream()
//            .flatMap(pair -> Stream.of(
//                new LegPair(pair.first().leg(), vertex.leg(), false).setSourceObject(reductionGraph.getEdge(pair.first(), vertex).getSourceObject().orElse(null)),
//                new LegPair(vertex.leg(), pair.second().leg(), false).setSourceObject(reductionGraph.getEdge(pair.first(), vertex).getSourceObject().orElse(null))
//            ));
      //}
      // otherwise either incoming or outgoing
//      else {
//        return Stream.empty();
//        return outgoingLegs.isEmpty()
//            ? incomingLegs.stream().map(incoming -> new LegTriple(incoming.leg(), vertex.leg(), null).setSourceObject(reductionGraph.getEdge(incoming, vertex).getSourceObject().orElse(null)))
//            : outgoingLegs.stream().map(outgoing -> new LegTriple(null, vertex.leg(), outgoing.leg()).setSourceObject(reductionGraph.getEdge(vertex, outgoing).getSourceObject().orElse(null)));
//      }
    }).collect(Collectors.toList());
  }

  /**
   * Converts the incoming {@link ConsecutiveLegs} to a list of {@link LegPair}s which can be inserted into the graph for de-dep
   * before being converted back to triples on output.
   */
  default List<LegPair> toLegPairs(ConsecutiveLegs consecutiveLegs) {
    if (consecutiveLegs instanceof LegPair) {
      return Collections.singletonList((LegPair) consecutiveLegs);
    } else {
      return Stream.of(
          consecutiveLegs.previous()
              .map(previous -> new LegPair(previous, consecutiveLegs.current(), false).setSourceObject(consecutiveLegs.getSourceObject().orElse(null)))
              .orElse(null),
          consecutiveLegs.next()
              .map(next -> new LegPair(consecutiveLegs.current(), next, false).setSourceObject(consecutiveLegs.getSourceObject().orElse(null)))
              .orElse(null))
          .filter(Objects::nonNull)
          .collect(Collectors.toList());
    }
  }

  /**
   * Generates a new anonymous instance of a {@link GraphBasedLegReducer} which prefers the edge already in the graph over the
   * next edge when an edge between two vertices already exists.
   */
  static GraphBasedLegReducer newInstance() {
    return new GraphBasedLegReducer() {};
  }
}
