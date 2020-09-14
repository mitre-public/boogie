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
public class UnknownLeg implements Leg {

  private static final UnknownLeg INSTANCE = new UnknownLeg();

  private UnknownLeg() {
  }

  /**
   * Returns the singleton instance of the unknown leg.
   */
  public static UnknownLeg getInstance() {
    return INSTANCE;
  }

  @Override
  public Fix pathTerminator() {
    return null;
  }

  @Override
  public Optional<? extends Fix> recommendedNavaid() {
    return Optional.empty();
  }

  @Override
  public Optional<? extends Fix> centerFix() {
    return Optional.empty();
  }

  @Override
  public PathTerm type() {
    return PathTerm.UNK;
  }

  @Override
  public Integer sequenceNumber() {
    return null;
  }

  @Override
  public Optional<Double> outboundMagneticCourse() {
    return Optional.empty();
  }

  @Override
  public Optional<Double> rho() {
    return Optional.empty();
  }

  @Override
  public Optional<Double> theta() {
    return Optional.empty();
  }

  @Override
  public Optional<Double> rnp() {
    return Optional.empty();
  }

  @Override
  public Optional<Double> routeDistance() {
    return Optional.empty();
  }

  @Override
  public Optional<Duration> holdTime() {
    return Optional.empty();
  }

  @Override
  public Optional<Double> verticalAngle() {
    return Optional.empty();
  }

  @Override
  public Optional<? extends SpeedLimit> speedConstraint() {
    return Optional.empty();
  }

  @Override
  public Optional<? extends AltitudeLimit> altitudeConstraint() {
    return Optional.empty();
  }

  @Override
  public Optional<? extends TurnDirection> turnDirection() {
    return Optional.empty();
  }

  @Override
  public Optional<Boolean> overfly() {
    return Optional.empty();
  }
}
