package org.mitre.tdp.boogie.conformance.alg.assemble;

import java.util.Objects;
import java.util.Optional;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;

/**
 * Simple class for use within the {@link ReducedLegGraph} which extracts simple features of legs such that they can be reduced
 * via checks against hashCode and equals.
 */
public class ReducibleLeg {

  private final Leg leg;

  public ReducibleLeg(Leg leg) {
    this.leg = leg;
  }

  public Leg leg() {
    return leg;
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

    Optional<Fix> pathTerminator = Optional.ofNullable(leg.pathTerminator());
    Optional<Fix> thatPathTerminator = Optional.ofNullable(that.leg.pathTerminator());

    return Objects.equals(pathTerminator.map(Fix::identifier).orElse(null), thatPathTerminator.map(Fix::identifier).orElse(null))
        && Objects.equals(pathTerminator.map(Fix::latitude).orElse(null), thatPathTerminator.map(Fix::latitude).orElse(null))
        && Objects.equals(pathTerminator.map(Fix::longitude).orElse(null), thatPathTerminator.map(Fix::longitude).orElse(null))
        && Objects.equals(leg.type(), that.leg.type());
  }

  @Override
  public int hashCode() {
    return Objects.hash(leg.pathTerminator(), leg.type());
  }
}
