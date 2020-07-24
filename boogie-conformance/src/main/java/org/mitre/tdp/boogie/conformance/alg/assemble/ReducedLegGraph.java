package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.connectivity.GabowStrongConnectivityInspector;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.mitre.tdp.boogie.util.Combinatorics;

/**
 * A reduced leg graph provides a graphical wrapper around an collection of {@link LegPair}s. Its primary purpose is to
 * de-duplicate/remove functionally replicate legs from a collection of input leg pairs.
 */
public class ReducedLegGraph extends SimpleDirectedGraph<ReducibleLeg, LegPair> {

  private ConnectivityInspector<ReducibleLeg, LegPair> weakConnectivityInspector;
  private GabowStrongConnectivityInspector<ReducibleLeg, LegPair> strongConnectivityInspector;

  public ReducedLegGraph() {
    super(LegPair.class);
    this.weakConnectivityInspector = new ConnectivityInspector<>(this);
    this.strongConnectivityInspector = new GabowStrongConnectivityInspector<>(this);
  }

  public ConnectivityInspector<ReducibleLeg, LegPair> weakConnectivityInspector() {
    return weakConnectivityInspector;
  }

  public GabowStrongConnectivityInspector<ReducibleLeg, LegPair> strongConnectivityInspector() {
    return strongConnectivityInspector;
  }

  public List<FlyableLeg> allConformableLegs() {
    return vertexSet().stream().flatMap(vertex -> {

      // weak connectivity so have to check directional edge containment
      Set<ReducibleLeg> connected = weakConnectivityInspector.connectedSetOf(vertex);

      Set<ReducibleLeg> incomingLegs = connected.stream().filter(leg -> containsEdge(leg, vertex)).collect(Collectors.toSet());
      Set<ReducibleLeg> outgoingLegs = connected.stream().filter(leg -> containsEdge(vertex, leg)).collect(Collectors.toSet());

      if (!outgoingLegs.isEmpty() && !incomingLegs.isEmpty()) {
        return Combinatorics.cartesianProduct(incomingLegs, outgoingLegs).stream()
            .map(pair -> new FlyableLeg(pair.first().leg(), vertex.leg(), pair.second().leg()).setSourceObject(getEdge(pair.first(), vertex).getSourceObject().orElse(null)));
      }
      return Stream.empty();
//      // otherwise either incoming or outgoing
//      else {
//        return outgoingLegs.isEmpty()
//            ? incomingLegs.stream().map(incoming -> new FlyableLeg(incoming.leg(), vertex.leg(), null).setSourceObject(getEdge(incoming, vertex).getSourceObject().orElse(null)))
//            : outgoingLegs.stream().map(outgoing -> new FlyableLeg(null, vertex.leg(), outgoing.leg()).setSourceObject(getEdge(vertex, outgoing).getSourceObject().orElse(null)));
//      }

    }).collect(Collectors.toList());
  }

  public static ReducedLegGraph with(List<? extends LegPair> legPairs) {
    ReducedLegGraph reducedLegGraph = new ReducedLegGraph();

    legPairs.forEach(leg -> {
      ReducibleLeg prev = new ReducibleLeg(leg.previous());
      ReducibleLeg current = new ReducibleLeg(leg.current());

      reducedLegGraph.addVertex(prev);
      reducedLegGraph.addVertex(current);

      if (!reducedLegGraph.containsEdge(prev, current)) {
        reducedLegGraph.addEdge(prev, current, leg);
      }
    });

    return reducedLegGraph;
  }
}
