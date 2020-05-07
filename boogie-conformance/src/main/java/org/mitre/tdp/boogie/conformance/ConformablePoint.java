package org.mitre.tdp.boogie.conformance;

import java.util.Optional;

import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.HasTime;
import org.mitre.tdp.boogie.LegType;

/**
 * The top level interface for points which we can evaluate conformance against across the
 * various {@link LegType}s.
 *
 * <p>Not all features are required to be populated to measure conformance to any particular
 * type of leg, but all conformable leg types will check for the existence of their required
 * fields.
 */
public interface ConformablePoint extends HasPosition, HasTime {
  /**
   * The true course of the aircraft - this is required instead of magnetic as for most
   * users this is easier that deriving the magnetic bearing, at least in the context of
   * the procedure and the package has the ability to re-derive the magnetic bearing per
   * leg anyway.
   */
  Optional<Double> trueCourse();

  /**
   * The pressure altitude of the aircraft in feet.
   */
  Optional<Double> pressureAltitude();

  /**
   * The indicated airspeed of the aircraft in knots.
   */
  Optional<Double> indicatedAirspeed();

  /**
   * The climb rate of the aircraft in feet per minute.
   */
  Optional<Double> climbrate();
}
