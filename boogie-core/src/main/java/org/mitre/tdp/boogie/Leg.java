package org.mitre.tdp.boogie;

import java.util.Optional;

/**
 * Defines the core fieldset of a procedure leg. Combinations of these features are used
 * when determining how to fly legs of a particular type. In general though for most leg
 * types the majority of these will be null, and are hence optional in the signature.
 *
 * The exceptions to this are the pathTerminator since it is widely used (although it may
 * also be nullable for certain leg types) and the legType which is required.
 */
public interface Leg<F extends Fix> {

  /**
   * The path terminator for the leg if said leg ends in a concrete fix.
   *
   * e.g. for a TF (track-to-fix) leg this will be a navaid or a waypoint
   * e.g. for a CA (course-to-altitude) leg this will be null since the termination
   * point is at a (altitude) constraint rather than a position.
   */
  F pathTerminator();

  /**
   * If the leg requires a navaid to be flown correctly this field will contain
   * the pertinent information about that navaid.
   *
   * e.g. for a FA (fix to altitude) this will be the fix the outbound course will
   * be with reference to for climbing to the target altitude.
   */
  Optional<F> recommendedNavaid();

  /**
   * For leg types which define arcs as a constant radius turn between fixes this
   * will contain the information about the center of that turn.
   */
  Optional<F> centerFix();

  /**
   * The {@link LegType} of the leg. It indicates how the leg is to be flown.
   */
  LegType type();

  /**
   * The inbound magnetic course to one of the listed fixes in the leg description.
   */
  Optional<Double> inboundMagneticCourse();

  /**
   * The outbound magnetic course to one of the listed fixes in the leg description.
   */
  Optional<Double> outboundMagneticCourse();

  /**
   * For legs with a recommended navaid this is the distance in nm to that navaid.
   */
  Optional<Double> rho();

  /**
   * For legs with a recommended navaid this is the magnetic bearing to that navaid.
   */
  Optional<Double> theta();

  /**
   * The Required Navigational Performance (RNP) needed to fly the leg.
   */
  Optional<Double> rnp();

  /**
   * The required distance of flight if required by the leg.
   */
  Optional<Double> distance();

  /**
   * The vertical angle identifies the vertical navigation path for the procedure.
   */
  Optional<Double> verticalAngle();

//  /**
//   * If the {@link LegType} requires a specified target altitude (e.g. {@link LegType#CA}
//   * this should return that value.
//   */
//  Double targetAltitude();
//
//  Double targetAltitude2();
//
//
//
//  Double targetSpeed();
//


  /**
   * If the leg contains a turn this indicates the direction of the turn or both if
   * either is viable.
   */
  Optional<TurnDirection> turnDirection();

  /**
   * Indicates whether the leg should be overflown at the path terminator.
   */
  Optional<Boolean> overfly();
}
