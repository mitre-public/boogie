package org.mitre.tdp.boogie.alg.resolve.element;

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
  public F recommendedNavaid() {
    return null;
  }

  @Override
  public F centerFix() {
    return null;
  }

  @Override
  public LegType type() {
    return LegType.IF;
  }

  @Override
  public Double inboundMagneticCourse() {
    return null;
  }

  @Override
  public Double outboundMagneticCourse() {
    return null;
  }

  @Override
  public Double rho() {
    return null;
  }

  @Override
  public Double theta() {
    return null;
  }

  @Override
  public Double rnp() {
    return null;
  }

  @Override
  public Double distance() {
    return null;
  }

  @Override
  public Double verticalAngle() {
    return null;
  }
//
//  @Override
//  public Double targetAltitude() {
//    return null;
//  }

  @Override
  public TurnDirection turnDirection() {
    return null;
  }

  @Override
  public Boolean overfly() {
    return false;
  }

  public static <F extends Fix> SimpleIFLeg<F> from(F fix) {
    return new SimpleIFLeg<>(fix);
  }
}
