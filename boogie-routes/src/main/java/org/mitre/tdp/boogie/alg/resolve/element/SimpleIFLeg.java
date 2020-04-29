package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Optional;

import org.mitre.tdp.boogie.Constraint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.TurnDirection;

/**
 * Simple class for a simple TF leg within the framework. We use these standard implementations
 * for connecting things to individual infrastructure elements in a leg-like way.
 *
 * Elements connected by these are things like:
 *
 * 1) Direct to fixes
 * 2) Direct to LatLon locations
 * 3) Direct to airport
 */
final class SimpleIFLeg implements Leg {

  private final Fix pathTerminator;

  private SimpleIFLeg(Fix term) {
    this.pathTerminator = term;
  }

  public static SimpleIFLeg from(Fix fix) {
    return new SimpleIFLeg(fix);
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
  public LegType type() {
    return LegType.IF;
  }

  @Override
  public Integer sequenceNumber() {
    return 0;
  }

  @Override
  public Optional<Double> inboundMagneticCourse() {
    return Optional.empty();
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
  public Optional<Double> distance() {
    return Optional.empty();
  }

  @Override
  public Optional<Double> verticalAngle() {
    return Optional.empty();
  }

  @Override
  public Optional<Constraint> speedConstraint() {
    return Optional.empty();
  }

  @Override
  public Optional<Constraint> altitudeConstraint() {
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
