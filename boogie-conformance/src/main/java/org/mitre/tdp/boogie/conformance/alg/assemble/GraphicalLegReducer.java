package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Objects;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

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

  public GraphicalLegReducer() {
    this.legMapping = new HashMap<>();
    this.sourceObjectMapping = new HashMap<>();
    this.reducedGraph = new SimpleDirectedGraph<>(DefaultEdge.class);
  }

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
   * Adds the given {@link LegPair} to the reduced graph. If the leg references {@link ReducibleLeg}s which already exist in
   * the graph the precedence is given based on insertion order.
   */
  public GraphicalLegReducer addLegPair(LegPair pair) {
    ReducibleLeg prev = new ReducibleLeg(pair.previous());
    ReducibleLeg curr = new ReducibleLeg(pair.current());

    legMapping.putIfAbsent(prev, pair.previous());
    legMapping.putIfAbsent(curr, pair.current());

    sourceObjectMapping.putIfAbsent(pair.previous(), pair.getSourceObject().orElse(null));
    sourceObjectMapping.putIfAbsent(pair.current(), pair.getSourceObject().orElse(null));

    reducedGraph.addVertex(prev);
    reducedGraph.addVertex(curr);

    if (!prev.equals(curr)) {
      reducedGraph.addEdge(prev, curr);
    }
    return this;
  }

  /**
   * Returns the list of all valid {@link LegPair}s from the reduced graph.
   */
  public List<LegPair> legPairs() {
//    return reducedGraph.vertexSet().stream().flatMap(vertex -> {
//      Set<DefaultEdge> incomingLegs = reducedGraph.incomingEdgesOf(vertex);
//      Set<DefaultEdge> outgoingLegs = reducedGraph.outgoingEdgesOf(vertex);
//
//      Leg current = legMapping.get(vertex);
//      if (!incomingLegs.isEmpty() && !outgoingLegs.isEmpty()){
//        return Stream.concat(
//            incomingLegs.stream().map(edge -> new LegPairImpl(legMapping.get(reducedGraph.getEdgeSource(edge)), current).setSourceObject(sourceObjectMapping.get(current))),
//            outgoingLegs.stream().map(edge -> new LegPairImpl(current, legMapping.get(reducedGraph.getEdgeTarget(edge))).setSourceObject())
//        )
//      }
//    })

    return reducedGraph.edgeSet().stream().map(edge -> {
      Leg previous = legMapping.get(reducedGraph.getEdgeSource(edge));
      Leg current = legMapping.get(reducedGraph.getEdgeTarget(edge));
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
    GraphicalLegReducer reducer = new GraphicalLegReducer();
    legPairs.forEach(reducer::addLegPair);
    return reducer;
  }

  /**
   * Simple wrapper class for leg information allowing us to reduce common leg references across a collection of input leg sources.
   */
  public static class ReducibleLeg {

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

    public String identifier() {
      return identifier;
    }

    public Double latitude() {
      return latitude;
    }

    public Double longitude() {
      return longitude;
    }

    public PathTerm legType() {
      return legType;
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
