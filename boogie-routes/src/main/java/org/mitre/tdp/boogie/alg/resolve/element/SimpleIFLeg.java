package org.mitre.tdp.boogie.alg.resolve.element;

import java.util.Optional;

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
final class SimpleIFLeg<F extends Fix> implements Leg<F> {

  private final F path_terminator;

  private SimpleIFLeg(F term) {
    this.path_terminator = term;
  }

  @Override
  public F pathTerminator() {
    return path_terminator;
  }

  @Override
  public Optional<F> recommendedNavaid() {
    return Optional.empty();
  }

  @Override
  public Optional<F> centerFix() {
    return Optional.empty();
  }

  @Override
  public LegType type() {
    return LegType.IF;
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
//
//  @Override
//  public Double targetAltitude() {
//    return Optional.empty();
//  }

  @Override
  public Optional<TurnDirection> turnDirection() {
    return Optional.empty();
  }

  @Override
  public Optional<Boolean> overfly() {
    return Optional.empty();
  }

  public static <F extends Fix> SimpleIFLeg<F> from(F fix) {
    return new SimpleIFLeg<>(fix);
  }
}
