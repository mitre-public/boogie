package org.mitre.tdp.boogie.alg.resolve.element;

import java.time.Duration;
import java.util.Optional;

import org.mitre.tdp.boogie.AltitudeLimit;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.SpeedLimit;
import org.mitre.tdp.boogie.TurnDirection;

/**
 * Simple class for a simple DF leg within the framework. We use these standard implementations
 * for connecting things to individual infrastructure elements in a leg-like way.
 *
 * <p>Elements connected by these are things like:
 * 1) Direct to fixes
 * 2) Direct to LatLon locations
 * 3) Direct to airport
 */
final class SimpleDFLeg implements Leg {

  private final Fix pathTerminator;

  private SimpleDFLeg(Fix term) {
    this.pathTerminator = term;
  }

  public static SimpleDFLeg from(Fix fix) {
    return new SimpleDFLeg(fix);
  }

  @Override
  public Fix pathTerminator() {
    return pathTerminator;
  }

  @Override
  public Optional<Fix> recommendedNavaid() {
    return Optional.empty();
  }

  @Override
  public Optional<Fix> centerFix() {
    return Optional.empty();
  }

  @Override
  public PathTerm type() {
    return PathTerm.DF;
  }

  @Override
  public Integer sequenceNumber() {
    return 0;
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
  public Optional<SpeedLimit> speedConstraint() {
    return Optional.empty();
  }

  @Override
  public Optional<AltitudeLimit> altitudeConstraint() {
    return Optional.empty();
  }

  @Override
  public Optional<TurnDirection> turnDirection() {
    return Optional.empty();
  }

  @Override
  public Optional<Boolean> overfly() {
    return Optional.empty();
  }
}
