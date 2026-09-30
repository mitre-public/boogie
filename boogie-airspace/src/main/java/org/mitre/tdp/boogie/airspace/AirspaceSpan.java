package org.mitre.tdp.boogie.airspace;

import static java.util.Objects.requireNonNull;

/**
 * A positive-length portion of a trajectory leg occupied by one airspace. Fractions are in [0, 1],
 * measured along the original shortest great-circle leg. For a stationary lateral position they
 * instead describe progress in time/altitude. A span starting at zero need not represent an entry;
 * the aircraft may already have been inside at the start of the observation.
 * When {@link #altitudeChecked()} is false, the span describes lateral occupancy only.
 *
 * @param <K> caller's airspace key type
 * @param airspaceKey caller identity, including layer/source identity when appropriate
 * @param enterFraction first occupied fraction
 * @param exitFraction last occupied fraction
 * @param altitudeChecked whether the span was clipped against the altitude band using both endpoint altitudes
 */
public record AirspaceSpan<K>(K airspaceKey, double enterFraction, double exitFraction, boolean altitudeChecked) {
  /**
   * Creates a span with positive length along the observation pair.
   *
   * @param airspaceKey nonnull caller identity for the airspace
   * @param enterFraction first occupied fraction within {@code [0, 1)}
   * @param exitFraction last occupied fraction, greater than entry and at most one
   * @param altitudeChecked true for vertical clipping, false for lateral occupancy only
   * @throws IllegalArgumentException if fractions are nonfinite, outside {@code [0, 1]}, or not strictly increasing
   */
  public AirspaceSpan {
    requireNonNull(airspaceKey, "airspaceKey");
    if (!Double.isFinite(enterFraction) || !Double.isFinite(exitFraction) || enterFraction < 0.0 || exitFraction > 1.0 || enterFraction >= exitFraction) {
      throw new IllegalArgumentException("Expected 0 <= enterFraction < exitFraction <= 1");
    }
  }
}
