package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Objects;

import org.mitre.tdp.boogie.Leg;

/**
 * Simple class for use within the {@link GraphBasedLegReducer} which extracts simple features of legs such that they can
 * be reduced via checks against hashCode and equals.
 */
public class ReducibleLeg {
  /**
   * The decorated leg.
   */
  private Leg leg;

  public ReducibleLeg(Leg leg) {
    this.leg = leg;
  }

  public Leg leg() {
    return leg;
  }

  public ReducibleLeg setLeg(Leg leg) {
    this.leg = leg;
    return this;
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReducibleLeg that = (ReducibleLeg) o;
    return Objects.equals(leg.pathTerminator(), that.leg.pathTerminator())
        && Objects.equals(leg.type(), that.leg.type());
  }

  @Override
  public int hashCode() {
    return Objects.hash(leg.pathTerminator(), leg.type());
  }
}
