package org.mitre.tdp.boogie.alg.resolve;

import java.time.Duration;
import java.util.Optional;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.TurnDirection;

import com.google.common.collect.Range;

/**
 * Simple class for a simple DF leg within the framework. We use these standard implementations
 * for connecting things to individual infrastructure elements in a leg-like way.
 *
 * <p>Elements connected by these are things like:
 * 1) Direct to fixes
 * 2) Direct to LatLon locations
 * 3) Direct to airport
 */
final class FixTerminationLeg implements Leg {

  private final Fix associatedFix;
  private final PathTerminator pathTerminator;

  private FixTerminationLeg(Fix associatedFix, PathTerminator pathTerminator) {
    this.associatedFix = associatedFix;
    this.pathTerminator = pathTerminator;
  }

  public static FixTerminationLeg from(Fix fix, PathTerminator pathTerminator) {
    return new FixTerminationLeg(fix, pathTerminator);
  }

  public static FixTerminationLeg IF(Fix fix) {
    return from(fix, PathTerminator.IF);
  }

  public static FixTerminationLeg DF(Fix fix) {
    return from(fix, PathTerminator.DF);
  }

  @Override
  public Optional<Fix> associatedFix() {
    return Optional.of(associatedFix);
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
  public PathTerminator pathTerminator() {
    return pathTerminator;
  }

  @Override
  public int sequenceNumber() {
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
  public Range<Double> speedConstraint() {
    return Range.all();
  }

  @Override
  public Range<Double> altitudeConstraint() {
    return Range.all();
  }

  @Override
  public Optional<TurnDirection> turnDirection() {
    return Optional.empty();
  }

  @Override
  public boolean isFlyOverFix() {
    return false;
  }

  @Override
  public boolean isPublishedHoldingFix() {
    return false;
  }
}
