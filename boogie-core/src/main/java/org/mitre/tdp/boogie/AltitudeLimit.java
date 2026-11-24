package org.mitre.tdp.boogie;

import com.google.common.base.Preconditions;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

/**
 * One side of an airspace's vertical extent, as published: a {@link Height} measured from a reference surface, or a
 * {@link Special} value saying the side has no limit or that the source does not say where it is.
 *
 * <p>The surface ({@code GND}) is a height of zero feet above ground level, and mean sea level ({@code MSL}) a height of
 * zero feet above mean sea level. A flight level is a pressure altitude in hundreds of feet (see
 * {@link AltitudeReference#PRESSURE}).
 */
public sealed interface AltitudeLimit permits AltitudeLimit.Height, AltitudeLimit.Special {

  /**
   * A limit at a height above a reference surface or datum.
   *
   * @param feet the height in feet above the reference
   * @param reference the surface the height is measured from
   */
  record Height(double feet, AltitudeReference reference) implements AltitudeLimit {

    /**
     * Validates the height.
     *
     * @param feet the height in feet above the reference
     * @param reference the surface or datum the height is measured from
     * @throws IllegalArgumentException if the height is not finite
     * @throws NullPointerException if the reference is null
     */
    public Height {
      Preconditions.checkArgument(Double.isFinite(feet), "Altitude must be finite");
      requireNonNull(reference, "reference");
    }
  }

  /**
   * A side without a height: it has no limit, or the source does not say where the limit is.
   */
  enum Special implements AltitudeLimit {
    /**
     * The side has no limit, as for an unlimited ceiling ({@code UNLTD}).
     */
    NONE,
    /**
     * The source does not say where the limit is ({@code NOTSP}, unknown, or missing).
     */
    NOT_SPECIFIED,
    /**
     * The limit is set by NOTAM.
     */
    BY_NOTAM
  }

  /**
   * Creates a limit measured from mean sea level.
   *
   * @param feet the height in feet above mean sea level
   * @return the limit
   */
  static AltitudeLimit msl(double feet) {
    return new Height(feet, AltitudeReference.MSL);
  }

  /**
   * Creates a limit measured from the terrain.
   *
   * @param feet the height in feet above ground level
   * @return the limit
   */
  static AltitudeLimit agl(double feet) {
    return new Height(feet, AltitudeReference.AGL);
  }

  /**
   * Creates a limit at a flight level: a pressure altitude in hundreds of feet.
   *
   * @param flightLevel the flight level, such as 180 for FL180
   * @return the limit, at {@code flightLevel * 100} feet of pressure altitude
   */
  static AltitudeLimit flightLevel(double flightLevel) {
    return new Height(flightLevel * 100.0, AltitudeReference.PRESSURE);
  }

  /**
   * The surface: zero feet above ground level.
   *
   * @return the limit
   */
  static AltitudeLimit ground() {
    return agl(0.0);
  }

  /**
   * No limit on this side.
   *
   * @return the limit
   */
  static AltitudeLimit none() {
    return Special.NONE;
  }

  /**
   * A limit the source does not place.
   *
   * @return the limit
   */
  static AltitudeLimit notSpecified() {
    return Special.NOT_SPECIFIED;
  }

  /**
   * A limit set by NOTAM.
   *
   * @return the limit
   */
  static AltitudeLimit byNotam() {
    return Special.BY_NOTAM;
  }

  /**
   * Gets the height, for limits that have one.
   *
   * @return the height, or empty for a {@link Special} limit
   */
  default Optional<Height> height() {
    return this instanceof Height height ? Optional.of(height) : Optional.empty();
  }

  /**
   * Tells whether the source says where this side's limit is: a height, or no limit at all.
   *
   * @return false for a limit not specified or set by NOTAM
   */
  default boolean known() {
    return this != Special.NOT_SPECIFIED && this != Special.BY_NOTAM;
  }
}
