package org.mitre.tdp.boogie;

import static java.util.Objects.requireNonNull;

import java.io.Serializable;
import java.util.Objects;
import java.util.function.Supplier;

/**
 * A course in degrees together with the north reference used to express it.
 *
 * <p>This type allows callers to preserve whether a published course is magnetic or true without attaching a magnetic
 * variation to the course itself. A variation is supplied only when a magnetic course must be resolved to true degrees.
 */
public final class ReferencedCourse implements Serializable {

  private final double degrees;

  private final CourseReference reference;

  private ReferencedCourse(double degrees, CourseReference reference) {
    this.degrees = degrees;
    this.reference = requireNonNull(reference, "Course reference cannot be null.");
  }

  public static ReferencedCourse magnetic(double degrees) {
    return new ReferencedCourse(degrees, CourseReference.MAGNETIC);
  }

  public static ReferencedCourse trueCourse(double degrees) {
    return new ReferencedCourse(degrees, CourseReference.TRUE);
  }

  public double degrees() {
    return degrees;
  }

  public CourseReference reference() {
    return reference;
  }

  /**
   * Resolves this course to true degrees.
   * <p>
   * The magnetic variation supplier is evaluated only for magnetic courses. This allows true courses to be resolved when no
   * applicable magnetic variation exists.
   */
  public double trueDegrees(Supplier<MagneticVariation> magneticVariationSupplier) {
    requireNonNull(magneticVariationSupplier, "Magnetic variation supplier cannot be null.");

    return switch (reference) {
      case TRUE -> degrees;
      case MAGNETIC -> requireNonNull(
          magneticVariationSupplier.get(),
          "Magnetic variation supplier cannot return null."
      ).magneticToTrue(degrees);
    };
  }

  @Override
  public boolean equals(Object o) {
    if (this == o) {
      return true;
    }
    if (o == null || getClass() != o.getClass()) {
      return false;
    }
    ReferencedCourse that = (ReferencedCourse) o;
    return Double.compare(degrees, that.degrees) == 0 && reference == that.reference;
  }

  @Override
  public int hashCode() {
    return Objects.hash(degrees, reference);
  }

  @Override
  public String toString() {
    return "ReferencedCourse{" +
        "degrees=" + degrees +
        ", reference=" + reference +
        '}';
  }
}
