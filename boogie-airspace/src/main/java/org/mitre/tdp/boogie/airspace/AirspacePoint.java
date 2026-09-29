package org.mitre.tdp.boogie.airspace;

import static java.util.Objects.requireNonNull;

import java.time.Instant;

import org.mitre.caasd.commons.LatLong;

/**
 * A trajectory observation or an interpolated point on a trajectory leg.
 *
 * @param time observation time
 * @param position geographic position
 * @param altitudeFeet altitude in the airspace bands' reference, or null if missing
 */
public record AirspacePoint(Instant time, LatLong position, Double altitudeFeet) {
  /**
   * Holds a point without interpreting its coordinates or altitude.
   *
   * @param time nonnull observation time
   * @param position nonnull geographic position
   * @param altitudeFeet altitude in feet, or null if missing
   */
  public AirspacePoint {
    requireNonNull(time, "time");
    requireNonNull(position, "position");
  }
}
