package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.util.Combinatorics;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.ListMultimap;

public class GraphicalLegReducer {
  /**
   * Functional to be used to generate a hash for a given leg.
   */
  private final LegHasher legHasher;
  /**
   * The reduced graph of legs for traversal.
   */
  private final SimpleDirectedGraph<Integer, DefaultEdge> reducedGraph;

  /**
   * Mapping from the hash of a given leg to the leg itself.
   */
  private ListMultimap<Integer, Leg> legMapping;
  /**
   * Mapping from a {@link Leg} to its source object.
   */
  private Map<Leg, Object> sourceObjectMapping;

  public GraphicalLegReducer() {
    this(LegHashers.byHashCode());
  }

  public GraphicalLegReducer(LegHasher legHasher) {
    this.legHasher = legHasher;
    this.reducedGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
    this.legMapping = ArrayListMultimap.create();
    this.sourceObjectMapping = new HashMap<>();
  }

  public SimpleDirectedGraph<Integer, DefaultEdge> reducedGraph() {
    return reducedGraph;
  }

  /**
   * Adds the given {@link LegPair} to the reduced graph. Multiple legs mapped to the same hash via the {@link #legHasher} are
   * stored in an {@link ArrayListMultimap} based on the order in which they were inserted into the graph.
   */
  public GraphicalLegReducer addLegPair(LegPair pair) {
    Integer prevHash = legHasher.hash(pair.previous()).orElseThrow(() -> new RuntimeException("Un-hashable leg: ".concat(pair.previous().toString())));
    Integer currHash = legHasher.hash(pair.current()).orElseThrow(() -> new RuntimeException("Un-hashable leg: ".concat(pair.current().toString())));

    legMapping.put(prevHash, pair.previous());
    legMapping.put(currHash, pair.current());

    sourceObjectMapping.putIfAbsent(pair.previous(), pair.getSourceObject().orElse(null));
    sourceObjectMapping.putIfAbsent(pair.current(), pair.getSourceObject().orElse(null));

    reducedGraph.addVertex(prevHash);
    reducedGraph.addVertex(currHash);

    if (!prevHash.equals(currHash)) {
      reducedGraph.addEdge(prevHash, currHash);
    }
    return this;
  }

  /**
   * Returns the list of all valid {@link LegPair}s from the reduced graph.
   */
  public List<LegPair> legPairs() {
    return reducedGraph.edgeSet().stream().map(edge -> {
      Leg previous = legMapping.get(reducedGraph.getEdgeSource(edge)).get(0);
      Leg current = legMapping.get(reducedGraph.getEdgeTarget(edge)).get(0);
      return new LegPairImpl(previous, current).setSourceObject(sourceObjectMapping.get(current));
    }).collect(Collectors.toList());
  }

  /**
   * Given the collection of all input leg pairs this method leverages the {@link #reducedGraph()} to build the full set
   * of {@link FlyableLeg} triples based on the contained leg references of the simplified graph.
   */
  public List<FlyableLeg> flyableLegs() {
    return reducedGraph.vertexSet().stream().flatMap(vertex -> {

      Set<DefaultEdge> incomingLegs = reducedGraph.incomingEdgesOf(vertex);
      Set<DefaultEdge> outgoingLegs = reducedGraph.outgoingEdgesOf(vertex);

      if (!outgoingLegs.isEmpty() && !incomingLegs.isEmpty()) {
        return Combinatorics.cartesianProduct(incomingLegs, outgoingLegs).stream()
            .map(pair -> {
              Leg incoming = legMapping.get(reducedGraph.getEdgeSource(pair.first())).get(0);
              Leg outgoing = legMapping.get(reducedGraph.getEdgeTarget(pair.second())).get(0);
              Leg current = legMapping.get(vertex).get(0);
              return new FlyableLeg(incoming, current, outgoing).setSourceObject(sourceObjectMapping.get(current));
            });
      } else {
        return outgoingLegs.isEmpty()
            ? incomingLegs.stream()
            .map(inc -> {
              Leg incoming = legMapping.get(reducedGraph.getEdgeSource(inc)).get(0);
              Leg current = legMapping.get(vertex).get(0);
              return new FlyableLeg(incoming, current, null).setSourceObject(sourceObjectMapping.get(current));
            })
            : outgoingLegs.stream().map(out -> {
              Leg current = legMapping.get(vertex).get(0);
              Leg outgoing = legMapping.get(reducedGraph.getEdgeTarget(out)).get(0);
              return new FlyableLeg(null, current, outgoing).setSourceObject(sourceObjectMapping.get(current));
            });
      }
    }).collect(Collectors.toList());
  }
}
