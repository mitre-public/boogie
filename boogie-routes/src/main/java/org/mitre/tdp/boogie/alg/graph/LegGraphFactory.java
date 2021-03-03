package org.mitre.tdp.boogie.alg.graph;

import static com.google.common.base.Preconditions.checkNotNull;
import static org.mitre.tdp.boogie.util.Combinatorics.cartesianProduct;

import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

import org.jgrapht.graph.DefaultWeightedEdge;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.alg.resolve.ResolvedRoute;
import org.mitre.tdp.boogie.alg.resolve.ResolvedSection;
import org.mitre.tdp.boogie.util.Iterators;

/**
 * Factory class for generating {@link RouteLegGraph} objects based on an input {@link ResolvedRoute}.
 */
public final class LegGraphFactory {

  /**
   * Converts the resolved route and all of its contained sections into legs with edges between them and inserts them into a
   * {@link RouteLegGraph}.
   */
  public RouteLegGraph newLegGraphFor(ResolvedRoute resolvedRoute) {
    RouteLegGraph graph = new RouteLegGraph();

    Iterators.fastslow2(
        checkNotNull(resolvedRoute).sections(),
        s -> !s.allLegs().isEmpty(),
        (s1, s2, skipped) -> linkSections(s1, s2, graph));

    return graph;
  }

  /**
   * Links legs generated from subsequent portions of the route string together with some default weighting.
   *
   * This method considers the cartesian product of all leg combinations between the two sections, generating edges between all
   * legs from the two sets for which a pathTerminator -> pathTerminator distance can be computed (that distance is taken to be
   * the edge weight).
   *
   * This results in a relatively large - but full linked graph - which later on we can run a shortest path algorithm in order
   * to resolve a unique route (sequence of legs) via {@link RouteLegGraph#shortestPath()}.
   */
  private void linkSections(ResolvedSection s1, ResolvedSection s2, RouteLegGraph graph) {
    List<LinkedLegs> legs1 = s1.allLegs();
    List<LinkedLegs> legs2 = s2.allLegs();

    Iterator<Pair<LinkedLegs, LinkedLegs>> pairs = cartesianProduct(legs1::iterator, legs2::iterator);

    List<Pair<LinkedLegs, LinkedLegs>> lpairs = new ArrayList<>();
    pairs.forEachRemaining(lpairs::add);

    boolean noLocations = lpairs.stream().noneMatch(pair -> pair.second().source().leg().type().isConcrete())
        || lpairs.stream().noneMatch(pair -> pair.first().target().leg().type().isConcrete());

    lpairs.forEach(pair -> {
      LinkedLegs ll1 = pair.first();
      LinkedLegs ll2 = pair.second();

      insert(ll1, graph);
      insert(ll2, graph);

      Leg tgt1 = ll1.target().leg();
      Leg src2 = ll2.source().leg();

      if (tgt1.type().isConcrete() && src2.type().isConcrete() && !tgt1.equals(src2)) {

        LatLong lltgt = tgt1.pathTerminator().latLong();
        LatLong llsrc = src2.pathTerminator().latLong();
        double distance = lltgt.distanceInNM(llsrc);

        setEdgeWeight(distance, ll1.target(), ll2.source(), graph);
      }

      if (noLocations) {
        setEdgeWeight(LinkedLegs.MATCH_WEIGHT, ll1.target(), ll2.source(), graph);
      }
    });
  }

  /**
   * Inserts the converted legs from within the {@link ResolvedSection} into the {@link RouteLegGraph} for reference later.
   */
  private void insert(LinkedLegs linked, RouteLegGraph graph) {
    GraphableLeg ssl1 = linked.source();
    GraphableLeg ssl2 = linked.target();

    graph.addVertex(ssl1);
    graph.addVertex(ssl2);

    if (!ssl1.equals(ssl2)) {
      double weight = linked.linkWeight();
      setEdgeWeight(weight, ssl1, ssl2, graph);
    }
  }

  private void setEdgeWeight(double weight, GraphableLeg source, GraphableLeg target, RouteLegGraph graph) {
    if (graph.containsEdge(source, target)) {
      DefaultWeightedEdge edge = graph.getEdge(source, target);
      setContainedEdgeWeight(weight, edge, graph);
    } else {
      DefaultWeightedEdge edge = graph.addEdge(source, target);
      graph.setEdgeWeight(edge, weight);
    }
  }

  /**
   * If a given edge already exists in the graph we use the lower of the two provided weights.
   */
  private void setContainedEdgeWeight(double weight, DefaultWeightedEdge edge, RouteLegGraph graph) {
    double cw = graph.getEdgeWeight(edge);
    if (weight < cw) {
      graph.setEdgeWeight(edge, weight);
    }
  }
}
