package org.mitre.tdp.boogie.airspace;

import static java.util.Objects.requireNonNull;

import java.util.Optional;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.AltitudeReference;

/**
 * What a trajectory observation reports vertically, from which its height above each {@link AltitudeReference} is
 * derived. Any of the values may be missing.
 *
 * <p>Sources differ in what they report: a transponder reports pressure altitude, a radar facility may correct it to
 * the local altimeter setting, and a geometric source reports height above the ellipsoid. Callers supply each value
 * they actually have, so a limit is only ever compared with a height in its own reference.
 *
 * @param altitudeFeet the altitude in feet above mean sea level, or null if missing
 * @param pressureAltitudeFeet the pressure altitude in feet (the altitude in the standard atmosphere), or null if missing
 * @param terrainFeet the terrain elevation beneath the observation in feet above mean sea level, or null if missing
 */
public record VerticalPosition(Double altitudeFeet, Double pressureAltitudeFeet, Double terrainFeet) {

  /**
   * Validates the present values.
   *
   * @param altitudeFeet the altitude in feet above mean sea level, or null if missing
   * @param pressureAltitudeFeet the pressure altitude in feet, or null if missing
   * @param terrainFeet the terrain elevation in feet above mean sea level, or null if missing
   * @throws IllegalArgumentException if a present value is not finite
   */
  public VerticalPosition {
    if (Stream.of(altitudeFeet, pressureAltitudeFeet, terrainFeet).anyMatch(value -> value != null && !Double.isFinite(value))) {
      throw new IllegalArgumentException("Present altitudes must be finite");
    }
  }

  /**
   * Creates a position with only an altitude above mean sea level.
   *
   * <p>Enough to evaluate limits measured from mean sea level; limits measured from the terrain or in pressure altitude
   * cannot be evaluated at it.
   *
   * @param altitudeFeet the altitude in feet above mean sea level, or null if missing
   * @return the position
   */
  public static VerticalPosition msl(Double altitudeFeet) {
    return new VerticalPosition(altitudeFeet, null, null);
  }

  /**
   * Tells whether the observation reports any altitude, above mean sea level or in pressure altitude.
   *
   * <p>A leg whose observations both report one has a vertical profile; otherwise only its lateral path is known.
   *
   * @return true if either altitude is present
   */
  public boolean hasAltitude() {
    return altitudeFeet != null || pressureAltitudeFeet != null;
  }

  /**
   * Gets the observation's height in a reference.
   *
   * <ul>
   *   <li>{@link AltitudeReference#MSL}: the altitude above mean sea level.</li>
   *   <li>{@link AltitudeReference#AGL}: the altitude above mean sea level minus the terrain elevation.</li>
   *   <li>{@link AltitudeReference#PRESSURE}: the pressure altitude.</li>
   * </ul>
   *
   * @param reference the reference to measure in
   * @return the height in feet, or empty if a value it needs is missing
   */
  public Optional<Double> heightFeet(AltitudeReference reference) {
    requireNonNull(reference, "reference");
    return switch (reference) {
      case MSL -> Optional.ofNullable(altitudeFeet);
      case AGL -> Optional.ofNullable(altitudeFeet).flatMap(altitude -> Optional.ofNullable(terrainFeet).map(terrain -> altitude - terrain));
      case PRESSURE -> Optional.ofNullable(pressureAltitudeFeet);
    };
  }
}
