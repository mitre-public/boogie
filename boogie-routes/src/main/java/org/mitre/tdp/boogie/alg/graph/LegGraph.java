package org.mitre.tdp.boogie.alg.graph;

import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.jgrapht.GraphPath;
import org.jgrapht.alg.shortestpath.DijkstraShortestPath;
import org.jgrapht.alg.shortestpath.NegativeCycleDetectedException;
import org.jgrapht.graph.DefaultWeightedEdge;
import org.jgrapht.graph.SimpleDirectedWeightedGraph;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.alg.resolve.ResolvedRoute;
import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;
import org.mitre.tdp.boogie.models.ExpandedRoute;
import org.mitre.tdp.boogie.util.Combinatorics;

/**
 * Graph structure containing all of the elements resolved in the {@link ResolvedRoute}.
 */
public class LegGraph extends SimpleDirectedWeightedGraph<SectionSplitLeg, DefaultWeightedEdge> {

  /**
   * The minimum resolved section index from the original route string. The section associated with this
   * index will become the initial element of the {@link ExpandedRoute}.
   */
  private int mindex;
  /**
   * The maximum resolved section index from the original route string. The section associated with this
   * index will become the final element of the {@link ExpandedRoute}
   */
  private int maxdex;

  LegGraph() {
    super(DefaultWeightedEdge.class);
    this.mindex = Integer.MAX_VALUE;
    this.maxdex = Integer.MIN_VALUE;
  }

  @Override
  public boolean addEdge(SectionSplitLeg l1, SectionSplitLeg l2, DefaultWeightedEdge edge) {
    boolean inserted = super.addEdge(l1, l2, edge);
    updateMinMax(l1, l2);
    return inserted;
  }

  private void updateMinMax(SectionSplitLeg l1, SectionSplitLeg l2) {
    this.maxdex = Math.max(maxdex, Math.max(l1.sectionSplit().index(), l2.sectionSplit().index()));
    this.mindex = Math.min(mindex, Math.min(l1.sectionSplit().index(), l2.sectionSplit().index()));
  }

  /**
   * Performs the shortest path computation on all candidate paths between matched sections from the
   * initial and final resolved sections.
   */
  public GraphPath<SectionSplitLeg, DefaultWeightedEdge> shortestPath() {
    DijkstraShortestPath<SectionSplitLeg, DefaultWeightedEdge> dijkstra = new DijkstraShortestPath<>(this);

    Map<Integer, List<SectionSplitLeg>> legsByIndex = vertexSet().stream()
        .collect(Collectors.groupingBy(ssl -> ssl.sectionSplit().index()));

    // cartesian product of all verticies at the start and end indices take
    // those verticies and calc all shortest paths take the minimum of those
    List<SectionSplitLeg> slegs = legsByIndex.get(mindex);
    List<SectionSplitLeg> elegs = legsByIndex.get(maxdex);
    Iterator<Pair<SectionSplitLeg, SectionSplitLeg>> iter = Combinatorics.cartesianProduct(slegs, elegs);

    GraphPath<SectionSplitLeg, DefaultWeightedEdge> shortest = null;
    double min = Double.MAX_VALUE;

    while (iter.hasNext()) {
      Pair<SectionSplitLeg, SectionSplitLeg> pair = iter.next();

      SectionSplitLeg start = pair.first();
      SectionSplitLeg end = pair.second();

      GraphPath<SectionSplitLeg, DefaultWeightedEdge> path;
      try {
        path = dijkstra.getPath(start, end);
      } catch (NegativeCycleDetectedException e) {
        e.printStackTrace();
        throw e;
      }

      double weight = path.getWeight();

      if (weight < min) {
        shortest = path;
        min = weight;
      }
    }

    return shortest;
  }
}