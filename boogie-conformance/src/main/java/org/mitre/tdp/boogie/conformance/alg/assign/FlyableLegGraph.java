package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.ViterbiTagger;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Graphical structure for storing consecutive leg connections as potential transitions. This class is used to determine the
 * set of downstream legs which are available to transition to from the currently assigned leg.
 *
 * This class inserts as nodes each of the {@link Leg}s within the supplied {@link FlyableLeg} records, while the top
 * level consecutive legs become directed edges. When querying for the {@link #downstreamLegsOf(FlyableLeg)} this class
 * returns all the legs which provide previous->next transitions from the current leg of the supplied consecutiveLegs.
 */
public final class FlyableLegGraph extends SimpleDirectedGraph<FlyableLeg, DefaultEdge> {

  private final Logger LOG = LoggerFactory.getLogger(FlyableLegGraph.class);

  private ConnectivityInspector<FlyableLeg, DefaultEdge> connectivityInspector;
  private AllDirectedPaths<FlyableLeg, DefaultEdge> allDirectedPaths;

  public FlyableLegGraph() {
    super(DefaultEdge.class);
  }

  /**
   * The configured {@link ConnectivityInspector} for the graph.
   */
  public ConnectivityInspector<FlyableLeg, DefaultEdge> connectivityInspector() {
    if (connectivityInspector == null) {
      this.connectivityInspector = new ConnectivityInspector<>(this);
      LOG.info("Number of connected sets: {}", connectivityInspector.connectedSets().size());
    }
    return connectivityInspector;
  }

  /**
   * The configured {@link AllDirectedPaths} object associated with the graph.
   */
  public AllDirectedPaths<FlyableLeg, DefaultEdge> allDirectedPaths() {
    if (allDirectedPaths == null) {
      this.allDirectedPaths = new AllDirectedPaths<>(this);
    }
    return allDirectedPaths;
  }

  public List<List<FlyableLeg>> allDirectedPaths(FlyableLeg start, FlyableLeg end) {
    return allDirectedPaths(start, end, true);
  }

  /**
   * Returns a list of lists containing all directed paths between the provided start and end leg.
   */
  public List<List<FlyableLeg>> allDirectedPaths(FlyableLeg start, FlyableLeg end, boolean simple) {
    return allDirectedPaths().getAllPaths(start, end, simple, vertexSet().size()).stream().map(GraphPath::getVertexList).collect(Collectors.toList());
  }

  /**
   * Returns the set of {@link FlyableLeg} downstream of the input consecutiveLegs. These are considered to be the set of
   * available transition targets from a given leg in the {@link AssignmentAlgorithm}/{@link ViterbiTagger}.
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
}
