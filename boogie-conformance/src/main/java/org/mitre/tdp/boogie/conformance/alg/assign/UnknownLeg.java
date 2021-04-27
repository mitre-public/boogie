package org.mitre.tdp.boogie.conformance.alg.assign;

import java.time.Duration;
import java.util.Optional;

import org.mitre.tdp.boogie.AltitudeLimit;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.SpeedLimit;
import org.mitre.tdp.boogie.TurnDirection;

/**
 * Singleton instance of a leg with no known features. This is useful as a stand in for a state representing the aircraft
 * not being assigned to any state in particular (see {@link ScoreBasedRouteResolver}).
 */
public interface UnknownLeg extends Leg {

  @Override
  default Fix pathTerminator() {
    return null;
  }

  @Override
  default Optional<? extends Fix> recommendedNavaid() {
    return Optional.empty();
  }

  @Override
  default Optional<? extends Fix> centerFix() {
    return Optional.empty();
  }

  @Override
  default PathTerm type() {
    return PathTerm.UNK;
  }

  @Override
  default Integer sequenceNumber() {
    return null;
  }

  @Override
  default Optional<Double> outboundMagneticCourse() {
    return Optional.empty();
  }

  @Override
  default Optional<Double> rho() {
    return Optional.empty();
  }

  @Override
  default Optional<Double> theta() {
    return Optional.empty();
  }

  @Override
  default Optional<Double> rnp() {
    return Optional.empty();
  }

  @Override
  default Optional<Double> routeDistance() {
    return Optional.empty();
  }

  @Override
  default Optional<Duration> holdTime() {
    return Optional.empty();
  }

  @Override
  default Optional<Double> verticalAngle() {
    return Optional.empty();
  }

  @Override
  default Optional<? extends SpeedLimit> speedConstraint() {
    return Optional.empty();
  }

  @Override
  default Optional<? extends AltitudeLimit> altitudeConstraint() {
    return Optional.empty();
  }

  @Override
  default Optional<? extends TurnDirection> turnDirection() {
    return Optional.empty();
  }

  @Override
  default Optional<Boolean> overfly() {
    return Optional.empty();
  }
}
