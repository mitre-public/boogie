package org.mitre.tdp.boogie.airspace;

import static java.util.Objects.requireNonNull;

import java.time.Duration;
import java.util.List;

/**
 * A continuous visit assembled from consecutive trajectory legs. Crossing positions and times are
 * estimates along those legs. The accumulator supplies an immutable list of the original events
 * covered by the visit; a crossing between two outside observations can have no events in that list.
 *
 * @param <K> caller's airspace key type
 * @param <E> caller's original event type
 * @param airspaceKey identity of the visited airspace
 * @param entry first covered point, interpolated or an original observation
 * @param exit last covered point, interpolated or an original observation
 * @param events original observations inside the visit, in trajectory order
 * @param entryCrossing whether an entry transition was established within a supplied leg
 * @param exitCrossing whether an exit transition was established within a supplied leg
 * @param altitudeChecked whether every contributing span had altitude checked
 */
public record AirspaceVisit<K, E>(K airspaceKey, AirspacePoint entry, AirspacePoint exit, List<E> events, boolean entryCrossing, boolean exitCrossing, boolean altitudeChecked) {
  /**
   * Holds visit data. When a crossing flag is false, that endpoint marks available coverage rather
   * than an established transition. Unchecked altitude anywhere makes the entire visit unchecked.
   *
   * @param airspaceKey nonnull airspace identity
   * @param entry nonnull first covered point
   * @param exit nonnull last covered point
   * @param events nonnull original events, in trajectory order
   * @param entryCrossing entry transition established within a leg
   * @param exitCrossing exit transition established within a leg
   * @param altitudeChecked all contributing spans had altitude checked
   */
  public AirspaceVisit {
    requireNonNull(airspaceKey, "airspaceKey");
    requireNonNull(entry, "entry");
    requireNonNull(exit, "exit");
    requireNonNull(events, "events");
  }

  /**
   * Returns the duration covered by this visit, which can be truncated by the available observations.
   *
   * @return elapsed time from entry to exit
   */
  public Duration duration() {
    return Duration.between(entry.time(), exit.time());
  }
}
