package org.mitre.tdp.boogie.conformance.alg.evaluate;

import java.util.Objects;

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

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LegPair legPair = (LegPair) o;
    return Objects.equals(previous, legPair.previous) &&
        Objects.equals(current, legPair.current);
  }

  @Override
  public int hashCode() {
    return Objects.hash(previous, current);
  }

  @Override
  public String toString() {
    return "LegPair{" +
        "previous=" + previous +
        ", current=" + current +
        '}';
  }
}
