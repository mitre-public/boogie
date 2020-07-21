package org.mitre.tdp.boogie;

import java.time.Duration;
import java.util.Optional;

/**
 * Defines the core fieldset of a procedure leg. Combinations of these features are used
 * when determining how to fly legs of a particular type. In general though for most leg
 * types the majority of these will be null, and are hence optional in the signature.
 *
 * The exceptions to this are the pathTerminator since it is widely used (although it may
 * also be nullable for certain leg types) and the legType which is required.
 */
public interface Leg {

  /**
   * The path terminator for the leg if said leg ends in a concrete fix.
   *
   * e.g. for a TF (track-to-fix) leg this will be a navaid or a waypoint
   * e.g. for a CA (course-to-altitude) leg this will be null since the termination
   * point is at a (altitude) constraint rather than a position.
   */
  Fix pathTerminator();

  /**
   * If the leg requires a navaid to be flown correctly this field will contain
   * the pertinent information about that navaid.
   *
   * e.g. for a FA (fix to altitude) this will be the fix the outbound course will
   * be with reference to for climbing to the target altitude.
   */
  Optional<? extends Fix> recommendedNavaid();

  /**
   * For leg types which define arcs as a constant radius turn between fixes this
   * will contain the information about the center of that turn.
   */
  Optional<? extends Fix> centerFix();

  /**
   * The {@link PathTerm} of the leg. It indicates how the leg is to be flown.
   */
  PathTerm type();

  /**
   * The sequence number of the leg. This represents the position of the leg in the
   * procedure/airway which references it.
   *
   * Sorting legs by sequence number within an airway or procedure should produce the
   * full path as intended by the procedure designer.
   */
  Integer sequenceNumber();

  /**
   * The magnetic course to fly on the current leg - when considering triples of legs this
   * is referred to as the outbound magnetic course. The inbound magnetic course is taken to
   * be the outbound magnetic course of the previous leg.
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
   * The along route distance of flight if required by the leg.
   */
  Optional<Double> routeDistance();

  /**
   * If the {@link #type()} indicates a hold this is the duration of that hold.
   */
  Optional<Duration> holdTime();

  /**
   * The vertical angle identifies the vertical navigation path for the procedure.
   */
  Optional<Double> verticalAngle();

  /**
   * The speed constraint at the {@link Leg#pathTerminator()}.
   */
  Optional<? extends SpeedLimit> speedConstraint();


  /**
   * The altitude constraint at the {@link Leg#pathTerminator()}.
   */
  Optional<? extends AltitudeLimit> altitudeConstraint();

  /**
   * If the leg contains a turn this indicates the direction of the turn or both if
   * either is viable.
   */
  Optional<? extends TurnDirection> turnDirection();

  /**
   * Indicates whether the leg should be overflown at the path terminator.
   */
  Optional<Boolean> overfly();
}
