package org.mitre.tdp.boogie.contract.routes;

import java.time.Duration;
import java.util.Optional;

import org.immutables.value.Value;
import org.mitre.tdp.boogie.PathTerminator;

import com.fasterxml.jackson.databind.annotation.JsonDeserialize;
import com.fasterxml.jackson.databind.annotation.JsonSerialize;
import com.google.common.collect.Range;

@Value.Immutable
@JsonSerialize(as = ImmutableLeg.class)
@JsonDeserialize(as = ImmutableLeg.class)
public interface Leg {
  /**
   * The associated fix for the leg if one was specified in the leg definition.
   * <br>
   * For example:
   * <br>
   * 1. TF (track-to-fix) -> this will be a navaid or a waypoint as a location is required to specify the end of the 2D path
   * segment.
   * 2. CA (course-to-altitude) -> this will be empty as the leg doesn't end in a concrete location - instead it ends when the
   * flight reaches some altitude while flying the specified heading.
   */
  Optional<Fix> associatedFix();

  /**
   * If the leg requires a navaid to be flown correctly this field will contain the pertinent information about that navaid.
   * <br>
   * e.g. for a FA (fix to altitude) this will be the fix the outbound course will be with reference to for climbing to the
   * target altitude.
   */
  Optional<Fix> recommendedNavaid();

  /**
   * For leg types which define arcs as a constant radius turn between fixes this will contain the information about the center
   * of that turn.
   */
  Optional<Fix> centerFix();

  /**
   * The {@link PathTerminator} of the leg, indicating how the leg is to be flown.
   */
  PathTerminator pathTerminator();

  /**
   * The sequence number of the leg. This represents the position of the leg in the procedure/airway which references it.
   * <br>
   * Sorting legs by sequence number within an airway or procedure should produce the full path as intended by the procedure
   * designer.
   */
  int sequenceNumber();

  /**
   * The magnetic course to fly on the current leg - when considering triples of legs this is referred to as the outbound
   * magnetic course.
   * <br>
   * For non-airway legs the inbound magnetic course is taken to be the outbound magnetic course of the previous leg.
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
   * <br>
   * This represents the maximum distance (in NM) the aircraft should be allowed off the leg if
   */
  Optional<Double> rnp();

  /**
   * The along route distance of flight if required by the leg.
   * <br>
   * This should be the expected flown distance in NM (Nautical Miles) along the leg (if specified).
   */
  Optional<Double> routeDistance();

  /**
   * If the {@link #pathTerminator()} indicates a hold this is the duration of that hold.
   */
  Optional<Duration> holdTime();

  /**
   * The vertical angle identifies the vertical navigation path for the procedure.
   * <br>
   * This should be the angle in degrees of climb or descent in the range (-10., 10.).
   */
  Optional<Double> verticalAngle();

  /**
   * The speed constraint at the {@link org.mitre.tdp.boogie.Leg#associatedFix()}.
   * <br>
   * The interpretation of this field should be as follows:
   * <br>
   * If {@link Range#hasLowerBound()} == {@link Range#hasUpperBound()}...
   * <br>
   * 1. Both bounds exist and are equal -> <i>AT</i>
   * 2. Both bounds exist and are not equal -> <i>BETWEEN</i>
   * 2. Neither bound exists -> <i>UNCONSTRAINED</i>
   * <br>
   * If !{@link Range#hasLowerBound()} && {@link Range#hasUpperBound()} -> <i>AT OR BELOW</i> the upper bound.
   * <br>
   * If {@link Range#hasLowerBound()} && !{@link Range#hasUpperBound()} -> <i>AT OR ABOVE</i> the lower bound.
   */
  Range<Double> speedConstraint();

  /**
   * The altitude constraint at the {@link org.mitre.tdp.boogie.Leg#associatedFix()}.
   * <br>
   * Interpretation of constraint should be as in {@link #speedConstraint()}.
   */
  Range<Double> altitudeConstraint();

  /**
   * If the leg contains a turn this indicates the direction of the turn or both if either is viable.
   */
  Optional<TurnDirection> turnDirection();

  /**
   * Indicates whether the terminal fix of the leg is a 'FlyOver' fix..
   * <br>
   * Typically this can be inferred from the waypoint description information in the concrete leg definition (e.g. from ARINC).
   */
  boolean isFlyOverFix();

  /**
   * Indicates whether the terminal fix of the leg is a published holding fix. These are typically encountered at the end of
   * missed approach procedures.
   * <br>
   * Typically this can be inferred from the waypoint description information in the concrete leg definition (e.g. from ARINC).
   */
  boolean isPublishedHoldingFix();
}
