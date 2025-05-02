package org.mitre.tdp.boogie;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.Declinations.declination;

import java.io.Serializable;
import java.time.Instant;
import java.util.Objects;
import javax.annotation.Nullable;

import org.mitre.caasd.commons.Course;

/**
 * Value object representing the magnetic variation at some location with a few convenience methods for working with it.
 *
 * <p>Canonically the magnetic variation is represented as a positive or negative angle on the unit circle between [-180, 180]
 * degrees.
 *
 * <p>Typically this is either the published magnetic variation at some location (if provided, e.g. on a navaid) or the modeled
 * variation which can be derived for an arbitrary position based on {@link Declinations}.
 */
public final class MagneticVariation implements Serializable {

  private static final Course MAX = Course.ofDegrees(180);

  public static final MagneticVariation ZERO = new MagneticVariation(Course.ZERO);

  /**
   * This class wraps a {@link Course} object internally for safety around units and ease-of-use.
   */
  private final Course angle;

  private MagneticVariation() {
    /*
     * This constructor supports Avro's reflection-based object instantiation. This constructor
     * is private to prevent "standard users" from seeing it.
     *
     * Note, tools that use reflection (e.g. Avro) are the only users who will benefit from this
     * constructor. Those tools use reflection magic to build the object, then they use more
     * reflection magic to mutate the values inside the "Immutable object".
     */
    this.angle = new Course(0., Course.Unit.DEGREES);
  }

  private MagneticVariation(Course angle) {
    this.angle = requireNonNull(angle);
    checkArgument(angle.abs().isLessThan(MAX), "Variation should be [-180 < {} < 180].", angle);
  }

  public static MagneticVariation ofDegrees(double angle) {
    return new MagneticVariation(Course.ofDegrees(angle));
  }

  public static MagneticVariation ofRadians(double angle) {
    return new MagneticVariation(Course.ofRadians(angle));
  }

  public static MagneticVariation from(double lat, double lon, Instant tau) {
    return MagneticVariation.ofDegrees(declination(lat, lon, tau));
  }

  public static MagneticVariation from(double lat, double lon, @Nullable Double elev, Instant tau) {
    return MagneticVariation.ofDegrees(declination(lat, lon, elev, tau));
  }
  /**
   * Return the angle of this magnetic variation (wrt true north) as a course (note this may be negative).
   */
  public Course angle() {
    return angle;
  }

  public Double magneticToTrue(double courseInDegrees) {
    return magneticToTrue(Course.ofDegrees(courseInDegrees)).inDegrees();
  }

  /**
   * Convert the provided course from magnetic to true by apply the local variation's offset.
   */
  public Course magneticToTrue(Course course) {
    return course.plus(angle);
  }

  public Double trueToMagnetic(double courseInDegrees) {
    return trueToMagnetic(Course.ofDegrees(courseInDegrees)).inDegrees();
  }

  /**
   * Convert the provided course from true to magnetic by apply the local variation's offset.
   */
  public Course trueToMagnetic(Course course) {
    return course.minus(angle);
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    MagneticVariation that = (MagneticVariation) o;
    return Objects.equals(angle, that.angle);
  }

  @Override
  public int hashCode() {
    return Objects.hash(angle);
  }

  @Override
  public String toString() {
    return "MagneticVariation{" +
        "angle=" + angle +
        '}';
  }
}
