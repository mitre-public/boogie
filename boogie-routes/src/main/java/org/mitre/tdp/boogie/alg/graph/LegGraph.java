package org.mitre.tdp.boogie.alg.graph;

import java.util.Comparator;
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
  public DefaultWeightedEdge addEdge(SectionSplitLeg l1, SectionSplitLeg l2) {
    DefaultWeightedEdge edge = super.addEdge(l1, l2);
    updateMinMax(l1, l2);
    return edge;
  }

  private void updateMinMax(SectionSplitLeg l1, SectionSplitLeg l2) {
    this.maxdex = Math.max(maxdex, Math.max(l1.sectionSplit().index(), l2.sectionSplit().index()));
    this.mindex = Math.min(mindex, Math.min(l1.sectionSplit().index(), l2.sectionSplit().index()));
  }

  /**
   * Derived path comparator. Compare first by weight, and then break ties by using the overall path length
   * preferring longer (more fully expanded paths).
   */
  private Comparator<GraphPath<SectionSplitLeg, DefaultWeightedEdge>> pathComparator() {
    Comparator<GraphPath<SectionSplitLeg, DefaultWeightedEdge>> weight = Comparator.comparing(GraphPath::getWeight);
    Comparator<GraphPath<SectionSplitLeg, DefaultWeightedEdge>> length = Comparator.comparing(GraphPath::getLength);
    return weight.thenComparing(length);
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

    while (iter.hasNext()) {
      Pair<SectionSplitLeg, SectionSplitLeg> pair = iter.next();

      SectionSplitLeg start = pair.first();
      SectionSplitLeg end = pair.second();

      GraphPath<SectionSplitLeg, DefaultWeightedEdge> path;
      try {
        path = dijkstra.getPath(start, end);
      } catch (NegativeCycleDetectedException e) {
        throw e;
      }

      if (shortest == null || pathComparator().compare(path, shortest) < 0) {
        shortest = path;
      }
    }

    return shortest;
  }
}