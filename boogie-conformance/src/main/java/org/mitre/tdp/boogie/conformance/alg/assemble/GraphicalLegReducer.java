package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.util.Combinatorics;

/**
 * A reduced leg graph provides a graphical wrapper around an collection of {@link LegPair}s. Its primary purpose is to
 * de-duplicate/remove functionally replicate legs from a collection of input leg pairs.
 */
public class GraphicalLegReducer {

  /**
   * Mapping from the {@link ReducibleLeg} wrapper to the actual leg reference.
   */
  private final Map<ReducibleLeg, Leg> legMapping;
  /**
   * Mapping from a {@link Leg} to its source object.
   */
  private final Map<Leg, Object> sourceObjectMapping;
  /**
   * The reduced graph of legs for traversal.
   */
  private final SimpleDirectedGraph<ReducibleLeg, DefaultEdge> reducedGraph;

  public GraphicalLegReducer(
      Map<ReducibleLeg, Leg> legMapping,
      Map<Leg, Object> sourceObjectMapping,
      SimpleDirectedGraph<ReducibleLeg, DefaultEdge> reducedGraph) {
    this.legMapping = legMapping;
    this.sourceObjectMapping = sourceObjectMapping;
    this.reducedGraph = reducedGraph;
  }

  public SimpleDirectedGraph<ReducibleLeg, DefaultEdge> reducedGraph() {
    return reducedGraph;
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
              Leg incoming = legMapping.get(reducedGraph.getEdgeSource(pair.first()));
              Leg outgoing = legMapping.get(reducedGraph.getEdgeTarget(pair.second()));
              Leg current = legMapping.get(vertex);
              return new FlyableLeg(incoming, current, outgoing).setSourceObject(sourceObjectMapping.get(current));
            });
      } else {
        return outgoingLegs.isEmpty()
            ? incomingLegs.stream()
            .map(inc -> {
              Leg incoming = legMapping.get(reducedGraph.getEdgeSource(inc));
              Leg current = legMapping.get(vertex);
              return new FlyableLeg(incoming, current, null).setSourceObject(sourceObjectMapping.get(current));
            })
            : outgoingLegs.stream().map(out -> {
              Leg current = legMapping.get(vertex);
              Leg outgoing = legMapping.get(reducedGraph.getEdgeTarget(out));
              return new FlyableLeg(null, current, outgoing).setSourceObject(sourceObjectMapping.get(current));
            });
      }

    }).collect(Collectors.toList());
  }

  /**
   * Creates a new {@link GraphicalLegReducer} from the given input collection of leg pairs. Subsequent leg pairs referencing
   * the same effective leg will be retained in insert order.
   *
   * e.g. If flightplan-based references should be preferred then add the flightplan generated legs first.
   */
  public static GraphicalLegReducer with(List<? extends LegPair> legPairs) {
    Map<ReducibleLeg, Leg> legMapping = new HashMap<>();
    Map<Leg, Object> sourceObjectMapping = new HashMap<>();
    SimpleDirectedGraph<ReducibleLeg, DefaultEdge> graph = new SimpleDirectedGraph<>(DefaultEdge.class);

    legPairs.forEach(pair -> {
      ReducibleLeg prev = new ReducibleLeg(pair.previous());
      ReducibleLeg curr = new ReducibleLeg(pair.current());

      legMapping.putIfAbsent(prev, pair.previous());
      legMapping.putIfAbsent(curr, pair.current());

      sourceObjectMapping.putIfAbsent(pair.previous(), pair.getSourceObject().orElse(null));
      sourceObjectMapping.putIfAbsent(pair.current(), pair.getSourceObject().orElse(null));

      graph.addVertex(prev);
      graph.addVertex(curr);

      if (!prev.equals(curr)) {
        graph.addEdge(prev, curr);
      }
    });

    return new GraphicalLegReducer(legMapping, sourceObjectMapping, graph);
  }

  /**
   * Simple wrapper class for leg information allowing us to reduce common leg references across a collection of input leg sources.
   */
  static class ReducibleLeg {

    private final String identifier;
    private final Double latitude;
    private final Double longitude;
    private final PathTerm legType;

    public ReducibleLeg(Leg leg) {
      Optional<Fix> pathTerminator = Optional.ofNullable(leg.pathTerminator());
      this.identifier = pathTerminator.map(Fix::identifier).orElse(Integer.toString(leg.hashCode()));
      this.latitude = pathTerminator.map(Fix::latitude).orElse(null);
      this.longitude = pathTerminator.map(Fix::longitude).orElse(null);
      this.legType = leg.type();
    }

    @Override
    public boolean equals(Object o) {
      if (this == o) {
        return true;
      }
      if (o == null || getClass() != o.getClass()) {
        return false;
      }
      ReducibleLeg that = (ReducibleLeg) o;
      return Objects.equals(identifier, that.identifier) &&
          Objects.equals(latitude, that.latitude) &&
          Objects.equals(longitude, that.longitude) &&
          legType == that.legType;
    }

    @Override
    public int hashCode() {
      return Objects.hash(identifier, latitude, longitude, legType);
    }
  }
}
