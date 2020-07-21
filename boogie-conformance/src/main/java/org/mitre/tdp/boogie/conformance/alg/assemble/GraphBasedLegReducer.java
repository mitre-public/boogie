package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.function.BiFunction;

import org.jgrapht.graph.SimpleDirectedGraph;
import org.mitre.tdp.boogie.Leg;

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
  ConsecutiveLegs preferredLegs(ConsecutiveLegs cl1, ConsecutiveLegs cl2);

  @Override
  default List<ConsecutiveLegs> reduce(List<? extends ConsecutiveLegs> legs) {
    SimpleDirectedGraph<ReducibleLeg, ConsecutiveLegs> reductionGraph = new SimpleDirectedGraph<>(ConsecutiveLegs.class);

    legs.forEach(leg -> {
      Optional<ReducibleLeg> prev = leg.previous().map(ReducibleLeg::new);
      Optional<ReducibleLeg> next = leg.next().map(ReducibleLeg::new);

      prev.ifPresent(reductionGraph::addVertex);
      next.ifPresent(reductionGraph::addVertex);

      ReducibleLeg current = new ReducibleLeg(leg.current());
      reductionGraph.addVertex(current);

      prev.filter(p -> !p.equals(current)).ifPresent(p -> addNewEdge(p, current, leg, reductionGraph));
      next.filter(n -> !current.equals(n)).ifPresent(n -> addNewEdge(current, n, leg, reductionGraph));
    });

    return new ArrayList<>(reductionGraph.edgeSet());
  }

  /**
   * Adds the given edge (consecutiveLegs) to the graph between the provided vertices (reducibleLegs) if there isn't already an
   * edge present between them. If there is it overrides it with the result of {@link #preferredLegs(ConsecutiveLegs, ConsecutiveLegs)}.
   */
  default void addNewEdge(ReducibleLeg source, ReducibleLeg target, ConsecutiveLegs newEdge, SimpleDirectedGraph<ReducibleLeg, ConsecutiveLegs> graph) {
    if (graph.containsEdge(source, target)) {
      ConsecutiveLegs currentEdge = graph.getEdge(source, target);

      ReducibleLeg newCurrent = new ReducibleLeg(newEdge.current());
      ReducibleLeg currentCurrent = new ReducibleLeg(currentEdge.current());

      ConsecutiveLegs preferredEdge = target.equals(newCurrent)
          ? target.equals(currentCurrent)
          ? preferredLegs(currentEdge, newEdge)
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
  static GraphBasedLegReducer withPreference(BiFunction<ConsecutiveLegs, ConsecutiveLegs, ConsecutiveLegs> preference) {
    return preference::apply;
  }
}
