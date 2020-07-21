package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.util.Combinatorics;

/**
 * Graphical instance of a leg reducer. It merges common legs where repeated in the graphical structure of their routes based
 * on the explicitly referenced {@link Leg#pathTerminator()} and {@link Leg#type()} via {@link ReducibleLeg} decorating leg
 * implementations with a standard hashcode and equals method.
 */
public interface GraphBasedLegReducer extends ConsecutiveLegReducer {

  /**
   * In the case of an already inserted edge this resolves which edge to prefer between the already inserted one and the next
   * to be inserted.
   *
   * Note an implicit preference already exists here as we want to keep as an edge the edge which references the target leg as
   * its {@link ConsecutiveLegs#current()} leg - this method is only used to resolve instances where both candidate legs ref
   * the target leg as their current leg.
   *
   * E.g. Consecutive legs from subsequent flightplans referencing the same fix in their route might want to prefer the route
   * which was filed first referencing the leg.
   */
  LegPair preferredLegs(LegPair cl1, LegPair cl2);

  @Override
  default List<ConsecutiveLegs> reduce(List<? extends ConsecutiveLegs> legs) {
    SimpleDirectedGraph<ReducibleLeg, LegPair> reductionGraph = new SimpleDirectedGraph<>(LegPair.class);

    legs.stream()
        .map(this::toLegPairs)
        .flatMap(Collection::stream)
        .forEach(leg -> {
          Optional<ReducibleLeg> prev = leg.previous().map(ReducibleLeg::new);
          Optional<ReducibleLeg> next = leg.next().map(ReducibleLeg::new);

          ReducibleLeg current = new ReducibleLeg(leg.current());

          prev.filter(p -> !p.equals(current)).ifPresent(p -> addNewEdge(p, current, leg, reductionGraph));
          next.filter(n -> !current.equals(n)).ifPresent(n -> addNewEdge(current, n, leg, reductionGraph));
        });

    // re-assemble the triples
    ConnectivityInspector<ReducibleLeg, LegPair> connectivityInspector = new ConnectivityInspector<>(reductionGraph);
    return reductionGraph.vertexSet().stream().flatMap(vertex -> {

      // weak connectivity so have to check directional edge containment
      Set<ReducibleLeg> connected = connectivityInspector.connectedSetOf(vertex);

      Set<ReducibleLeg> outgoingLegs = connected.stream().filter(leg -> reductionGraph.containsEdge(vertex, leg)).collect(Collectors.toSet());
      Set<ReducibleLeg> incomingLegs = connected.stream().filter(leg -> reductionGraph.containsEdge(leg, vertex)).collect(Collectors.toSet());

      return Combinatorics.cartesianProduct(incomingLegs, outgoingLegs).stream()
          .map(pair -> new LegTriple(pair.first().leg(), vertex.leg(), pair.second().leg()).setSourceObject(reductionGraph.getEdge(pair.first(), vertex).getSourceObject().orElse(null)));
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
   * Adds the given edge (consecutiveLegs) to the graph between the provided vertices (reducibleLegs) if there isn't already an
   * edge present between them. If there is it overrides it with the result of {@link #preferredLegs(LegPair, LegPair)}.
   */
  default void addNewEdge(ReducibleLeg source, ReducibleLeg target, LegPair newEdge, SimpleDirectedGraph<ReducibleLeg, LegPair> graph) {
    if (graph.containsEdge(source, target)) {
      LegPair currentEdge = graph.getEdge(source, target);

      ReducibleLeg newCurrent = new ReducibleLeg(newEdge.current());
      ReducibleLeg currentCurrent = new ReducibleLeg(currentEdge.current());

      LegPair preferredEdge = target.equals(newCurrent)
          ? target.equals(currentCurrent)
          ? preferredLegs(currentEdge, newEdge)
          : newEdge
          : currentEdge;

      if (!preferredEdge.equals(currentEdge)) {
        addNewVertex(target, graph, true);

        graph.removeEdge(currentEdge);
        graph.addEdge(source, target, newEdge);
      }
    } else {
      addNewVertex(source, graph, false);
      addNewVertex(target, graph, false);

      graph.addEdge(source, target, newEdge);
    }
  }

  default void addNewVertex(ReducibleLeg newVertex, SimpleDirectedGraph<ReducibleLeg, LegPair> graph, boolean override) {
    if (!graph.containsVertex(newVertex) || override) {
      graph.addVertex(newVertex);
      graph.vertexSet().stream()
          .filter(newVertex::equals)
          .forEach(vertex -> vertex.setLeg(newVertex.leg()));
    }
  }

  /**
   * Generates a new anonymous instance of a {@link GraphBasedLegReducer} which prefers the edge already in the graph over the
   * next edge when an edge between two vertices already exists.
   */
  static GraphBasedLegReducer newInstance() {
    return withPreference((l1, l2) -> l1);
  }

  /**
   * Returns an anonymous instance of an {@link GraphBasedLegReducer} which uses the given preference to decide which edge to
   * keep when there are ties between vertices.
   */
  static GraphBasedLegReducer withPreference(BiFunction<LegPair, LegPair, LegPair> preference) {
    return preference::apply;
  }
}
