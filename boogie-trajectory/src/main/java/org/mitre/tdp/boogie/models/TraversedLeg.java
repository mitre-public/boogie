package org.mitre.tdp.boogie.models;

import java.util.List;

import org.mitre.caasd.commons.TimeWindow;
import org.mitre.tdp.boogie.Leg;

/**
 * Class containing a terminal concrete leg, any non-concrete legs traversed between the terminal
 * leg and the previous concrete leg, and the modeled traversal time.
 */
public class TraversedLeg {
  /**
   * The terminal leg of the segment.
   */
  private final Leg terminalLeg;
  /**
   * The ordered list of legs traversed between the current terminal leg and the previous.
   */
  private final List<Leg> traversedLegs;
  /**
   * the amount of time it took to traverse the leg.
   */
  private final TimeWindow traversalTime;

  private TraversedLeg(Builder bldr) {
    this.terminalLeg = bldr.terminalLeg;
    this.traversedLegs = bldr.traversedLegs;
    this.traversalTime = bldr.traversalTime;
  }

  public Leg terminalLeg() {
    return terminalLeg;
  }

  public List<Leg> traversedLegs() {
    return traversedLegs;
  }

  public TimeWindow traversalTime() {
    return traversalTime;
  }

  public Builder builder() {
    return new Builder()
        .setTerminalLeg(terminalLeg)
        .setTraversedLegs(traversedLegs)
        .setTraversalTime(traversalTime);
  }

  public static class Builder {
    private Leg terminalLeg;
    private List<Leg> traversedLegs;
    private TimeWindow traversalTime;

    public Builder setTerminalLeg(Leg leg) {
      this.terminalLeg = leg;
      return this;
    }

    public Builder setTraversedLegs(List<Leg> legs) {
      this.traversedLegs = legs;
      return this;
    }

    public Builder setTraversalTime(TimeWindow time) {
      this.traversalTime = time;
      return this;
    }

    public TraversedLeg build() {
      return new TraversedLeg(this);
    }
  }
}
