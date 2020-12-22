package org.mitre.tdp.boogie.conformance.alg.assign;

import java.util.List;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultEdge;
import org.jgrapht.graph.SimpleDirectedGraph;
import org.mitre.tdp.boogie.conformance.alg.assign.dp.ViterbiTagger;
import org.mitre.tdp.boogie.conformance.alg.assign.link.LinkingStrategy;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Graphical structure for storing consecutive leg connections as potential transitions. This class is used to determine the
 * set of downstream legs which are available to transition to from a given {@link FlyableLeg}.
 *
 * The nodes of the graph are {@link FlyableLeg}s with edges added between them typically via external {@link LinkingStrategy}.
 * When querying for the {@link #downstreamLegsOf(FlyableLeg)} this class returns all connected legs via directed edges.
 */
public final class LegTransitionGraph extends SimpleDirectedGraph<FlyableLeg, DefaultEdge> {

  private final Logger LOG = LoggerFactory.getLogger(LegTransitionGraph.class);

  private ConnectivityInspector<FlyableLeg, DefaultEdge> connectivityInspector;
  private AllDirectedPaths<FlyableLeg, DefaultEdge> allDirectedPaths;

  public LegTransitionGraph() {
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
   * Returns the set of {@link FlyableLeg} downstream of the input flyableLeg. These are considered to be the set of available
   * transition targets from a given leg in the {@link RouteAssigner}/{@link ViterbiTagger}.
   *
   * The edges of the graph are directed - however the used implementation of {@link ConnectivityInspector} treats all edges
   * as undirected for the purpose of determining a connected set around the provided vertex (FlyableLeg). Therefore we check
   * via {@link SimpleDirectedGraph#containsEdge(Object, Object)} returns true for the proposed edge (from source->target)
   * before returning the vertex as connected.
   *
   * Note this returned list of flyable legs does <i>not</i> include the edge used to query.
   */
  public List<FlyableLeg> downstreamLegsOf(FlyableLeg flyableLeg) {
    // weakly connected components
    return connectivityInspector().connectedSetOf(flyableLeg).stream()
        // need to check the directional edge exists
        .filter(leg -> this.containsEdge(flyableLeg, leg))
        .collect(Collectors.toList());
  }
}
