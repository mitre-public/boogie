package org.mitre.tdp.boogie.alg;

import java.util.List;

import org.mitre.tdp.boogie.alg.graph.MultiplyExpandedLegMerger;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;

/**
 * Final output container for the expanded route information.
 *
 * Contains both the input route string targeted for expansion as well as the full leg expansion
 * of the route labeled by section.
 */
public final class ExpandedRoute {

  private static final MultiplyExpandedLegMerger merger = new MultiplyExpandedLegMerger();

  private final String route;
  private final List<GraphableLeg> legs;

  public ExpandedRoute(String route, List<GraphableLeg> legs) {
    this.route = route;
    this.legs = legs;
  }

  public String route() {
    return route;
  }

  public List<GraphableLeg> legs() {
    return legs;
  }

  public List<GraphableLeg> mergedLegs() {
    return merger.mergeLegs(legs());
  }

  public ExpandedRoute replaceLegs(List<GraphableLeg> legs) {
    this.legs.clear();
    this.legs.addAll(legs);
    return this;
  }
}
