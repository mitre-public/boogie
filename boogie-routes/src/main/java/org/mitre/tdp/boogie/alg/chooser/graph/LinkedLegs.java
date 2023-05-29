package org.mitre.tdp.boogie.alg.chooser.graph;

import java.util.Objects;

import org.mitre.tdp.boogie.Leg;

public final class LinkedLegs {

  /**
   * Global match weight which should be used for all in-element-produced linked leg weights.
   */
  public static final double SAME_ELEMENT_MATCH_WEIGHT = 0.00001;

  private final Leg source;
  private final Leg target;
  private final double linkWeight;

  public LinkedLegs(Leg s, Leg t, double linkWeight) {
    this.source = s;
    this.target = t;
    this.linkWeight = linkWeight;
  }

  public Leg source() {
    return source;
  }

  public Leg target() {
    return target;
  }

  public double linkWeight() {
    return linkWeight;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    LinkedLegs that = (LinkedLegs) o;
    return Double.compare(that.linkWeight, linkWeight) == 0 &&
        Objects.equals(source, that.source) &&
        Objects.equals(target, that.target);
  }

  @Override
  public int hashCode() {
    return Objects.hash(source, target, linkWeight);
  }

  @Override
  public String toString() {
    return "LinkedLegs{" +
        "source=" + source +
        ", target=" + target +
        ", linkWeight=" + linkWeight +
        '}';
  }
}

