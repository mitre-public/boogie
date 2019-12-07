package org.mitre.tdp.boogie.models;

import java.util.List;

import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;

/**
 * Final output container for the expanded route information.
 *
 * Contains both the input route string targeted for expansion as well as the full leg expansion
 * of the route labeled by section.
 */
public final class ExpandedRoute {

  private final String route;
  private final List<SectionSplitLeg> legs;

  public ExpandedRoute(String route, List<SectionSplitLeg> legs) {
    this.route = route;
    this.legs = legs;
  }

  public String route() {
    return route;
  }

  public List<SectionSplitLeg> legs() {
    return legs;
  }
}
