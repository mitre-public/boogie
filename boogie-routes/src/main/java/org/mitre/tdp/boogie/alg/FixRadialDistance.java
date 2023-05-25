package org.mitre.tdp.boogie.alg;

import static java.util.Objects.requireNonNull;

import java.util.Objects;

import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;

/**
 * Representation of a fix-radial-distance (FRD) as a simple POJO class.
 */
public final class FixRadialDistance {

  private final Fix fix;

  private final Course radial;

  private final Distance distance;

  private FixRadialDistance(Fix fix, Course radial, Distance distance) {
    this.fix = requireNonNull(fix);
    this.radial = requireNonNull(radial);
    this.distance = requireNonNull(distance);
  }

  public static FixRadialDistance create(Fix fix, Course radial, Distance distance) {
    return new FixRadialDistance(fix, radial, distance);
  }

  public Fix fix() {
    return fix;
  }

  public Course radial() {
    return radial;
  }

  public Distance distance() {
    return distance;
  }

  public LatLong projectedLocation() {
    return fix.latLong().project(fix.magneticVariation().magneticToTrue(radial), distance);
  }

  /**
   * Returns a formatted identifier in the form {@code [FIX][courseDegrees][distanceInNm]}.
   */
  public String formattedIdentifier() {
    return String.format("%s%03d%03d",
        fix.fixIdentifier(),
        Double.valueOf(radial.inDegrees()).intValue(),
        Double.valueOf(distance.inNauticalMiles()).intValue()
    );
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    FixRadialDistance that = (FixRadialDistance) o;
    return Objects.equals(fix, that.fix)
        && Objects.equals(radial, that.radial)
        && Objects.equals(distance, that.distance);
  }

  @Override
  public int hashCode() {
    return Objects.hash(fix, radial, distance);
  }

  @Override
  public String toString() {
    return "FixRadialDistance{" +
        "fix=" + fix +
        ", radial=" + radial +
        ", distance=" + distance +
        '}';
  }
}
