package org.mitre.tdp.boogie.arinc;

import static java.util.Objects.requireNonNull;

import java.util.Optional;

import org.mitre.tdp.boogie.CourseReference;
import org.mitre.tdp.boogie.ReferencedCourse;

/**
 * Reads course references and applies parent declarations using ARINC coding rules.
 */
public final class ArincCourses {

  private ArincCourses() {
    throw new IllegalStateException("Cannot instantiate utility class.");
  }

  /**
   * Reads a numeric course field, preserving an explicit {@code T} suffix. Unmarked courses initially use magnetic north.
   * Missing, blank, or invalid fields return an empty result, as in {@link ArincRecord#optionalField(String)}.
   */
  public static Optional<ReferencedCourse> parse(ArincRecord record, String fieldName) {
    return record.<Double>optionalField(fieldName)
        .map(degrees -> ReferencedCourse.of(degrees, record.rawFieldEndsWith(fieldName, 'T') ? CourseReference.TRUE : CourseReference.MAGNETIC));
  }

  /**
   * Applies a parent declaration to an unmarked ARINC course. An explicit true course always keeps its reference; a magnetic
   * course represents an unmarked field and inherits the supplied default. This ARINC rule changes the reference without
   * converting the published degrees. Returns the original instance when its reference is unchanged.
   */
  public static ReferencedCourse withDefaultReference(ReferencedCourse course, CourseReference defaultReference) {
    requireNonNull(course, "Course cannot be null.");
    requireNonNull(defaultReference, "Default course reference cannot be null.");
    return course.reference() == CourseReference.TRUE || course.reference() == defaultReference
        ? course : ReferencedCourse.of(course.degrees(), defaultReference);
  }
}
