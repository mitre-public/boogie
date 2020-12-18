package org.mitre.tdp.boogie.conformance.alg;

import org.mitre.tdp.boogie.Leg;

public final class LegPair {

  private final Leg previous;
  private final Leg current;

  public LegPair(Leg previous, Leg current) {
    this.previous = previous;
    this.current = current;
  }

  public Leg previous() {
    return previous;
  }

  public Leg current() {
    return current;
  }
}
