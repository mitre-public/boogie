package org.mitre.tdp.boogie.models;

import java.util.List;

import org.mitre.caasd.commons.TimeWindow;

public class TraversedRoute {

  private final TimeWindow departureDuration;
  private final List<TraversedLeg> traversedLegs;
  private final TimeWindow approachDuration;

  public TraversedRoute(TimeWindow d, List<TraversedLeg> legs, TimeWindow a) {
    this.departureDuration = d;
    this.traversedLegs = legs;
    this.approachDuration = a;
  }

  public TimeWindow departureDuration() {
    return departureDuration;
  }

  public List<TraversedLeg> traversedLegs() {
    return traversedLegs;
  }

  public TimeWindow approachDuration() {
    return approachDuration;

  }
}
