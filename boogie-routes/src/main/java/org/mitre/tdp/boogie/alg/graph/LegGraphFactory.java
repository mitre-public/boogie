package org.mitre.tdp.boogie.alg.graph;

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
import org.mitre.tdp.boogie.models.LinkedLegs;
import org.mitre.tdp.boogie.utils.Iterators;

public class LegGraphFactory {

  private LegGraphFactory() {
    throw new IllegalStateException("Cannot access static utility class.");
  }

  /**
   * Converts the resolved route and all of its contained sections into legs with edges between them and inserts
   * them into an {@link RouteLegGraph}.
   */
  public static RouteLegGraph build(ResolvedRoute route) {
    RouteLegGraph graph = new RouteLegGraph();

    Iterators.fastslow2(
        route.sections(),
        s -> !s.allLegs().isEmpty(),
        (s1, s2, skipped) -> linkSections(s1, s2, graph));

    return graph;
  }

  /**
   * Generates linking edges between the subsequent resolved sections so there are valid paths between them.
   */
  private static void linkSections(ResolvedSection s1, ResolvedSection s2, RouteLegGraph graph) {
    List<LinkedLegs> legs1 = s1.allLegs();
    List<LinkedLegs> legs2 = s2.allLegs();

    Iterator<Pair<LinkedLegs, LinkedLegs>> pairs = cartesianProduct(legs1, legs2);
    List<Pair<LinkedLegs, LinkedLegs>> lpairs = new ArrayList<>();
    pairs.forEachRemaining(lpairs::add);
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
    });
  }

  /**
   * Inserts the converted legs from within the {@link ResolvedSection} into the {@link RouteLegGraph} for reference later.
   */
  private static void insert(LinkedLegs linked, RouteLegGraph graph) {
    GraphableLeg ssl1 = linked.source();
    GraphableLeg ssl2 = linked.target();

    graph.addVertex(ssl1);
    graph.addVertex(ssl2);

    if (!ssl1.equals(ssl2)) {
      double weight = linked.linkWeight();
      setEdgeWeight(weight, ssl1, ssl2, graph);
    }
  }

  private static void setEdgeWeight(double weight, GraphableLeg source, GraphableLeg target, RouteLegGraph graph) {
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
  private static void setContainedEdgeWeight(double weight, DefaultWeightedEdge edge, RouteLegGraph graph) {
    double cw = graph.getEdgeWeight(edge);
    if (weight < cw) {
      graph.setEdgeWeight(edge, weight);
    }
  }
}
