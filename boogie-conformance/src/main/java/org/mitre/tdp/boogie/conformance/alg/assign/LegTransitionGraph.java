package org.mitre.tdp.boogie.conformance.alg.assign;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.connectivity.ConnectivityInspector;
import org.jgrapht.alg.shortestpath.AllDirectedPaths;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.mitre.tdp.boogie.conformance.alg.assign.combine.CompositeLeg;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Graphical structure for storing consecutive leg connections as potential transitions. This class is used to determine the
 * set of downstream legs which are available to transition to from a given {@link FlyableLeg}.
 */
public final class LegTransitionGraph extends SimpleDirectedWeightedGraph<FlyableLeg, DefaultWeightedEdge> {

  private static final Logger LOG = LoggerFactory.getLogger(LegTransitionGraph.class);

  /**
   * The mapping from the representative {@link FlyableLeg} which was inserted into the graph as a vertex to it's union cohort.
   * <p>
   * This mapping is maintained here for applications which want traceability from the assigned representative legs to their
   * explicit sources.
   */
  private final transient Map<FlyableLeg, CompositeLeg> representativeMap;

  private transient ConnectivityInspector<FlyableLeg, DefaultWeightedEdge> connectivityInspector;
  private transient AllDirectedPaths<FlyableLeg, DefaultWeightedEdge> allDirectedPaths;

  public LegTransitionGraph(Map<FlyableLeg, CompositeLeg> representativeMap) {
    super(DefaultWeightedEdge.class);
    this.representativeMap = representativeMap;
  }

  /**
   * Returns the {@link }
   */
  public CompositeLeg unionFor(FlyableLeg flyableLeg) {
    return checkNotNull(representativeMap.get(flyableLeg));
  }

  /**
   * The configured {@link ConnectivityInspector} for the graph.
   */
  public ConnectivityInspector<FlyableLeg, DefaultWeightedEdge> connectivityInspector() {
    if (connectivityInspector == null) {
      this.connectivityInspector = new ConnectivityInspector<>(this);
      LOG.info("Number of connected sets: {}", connectivityInspector.connectedSets().size());
    }
    return connectivityInspector;
  }

  /**
   * The configured {@link AllDirectedPaths} object associated with the graph.
   */
  public AllDirectedPaths<FlyableLeg, DefaultWeightedEdge> allDirectedPaths() {
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

  @Override
  public boolean equals(Object that) {
    return super.equals(that);
  }

  @Override
  public int hashCode() {
    return super.hashCode();
  }
}
