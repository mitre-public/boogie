package org.mitre.tdp.boogie;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;

class ReferencedCourseTest {

  @Test
  void resolvesTheSamePublishedDegreesUsingTheirReference() {
    ReferencedCourse magnetic = ReferencedCourse.of(125.0, CourseReference.MAGNETIC);
    ReferencedCourse truth = ReferencedCourse.of(125.0, CourseReference.TRUE);

    assertAll(
        () -> assertEquals(ReferencedCourse.magnetic(125.0), magnetic),
        () -> assertEquals(ReferencedCourse.trueCourse(125.0), truth),
        () -> assertEquals(125.0, magnetic.degrees()),
        () -> assertEquals(125.0, truth.degrees()),
        () -> assertEquals(137.0, magnetic.trueDegrees(() -> MagneticVariation.ofDegrees(12.0))),
        () -> assertEquals(125.0, truth.trueDegrees(() -> {
          throw new AssertionError("A true course must not request magnetic variation.");
        }))
    );
  }

  @Test
  void rejectsAnUnspecifiedReferenceOrMissingRequiredVariation() {
    assertAll(
        () -> assertThrows(NullPointerException.class, () -> ReferencedCourse.of(125.0, null)),
        () -> assertThrows(NullPointerException.class,
            () -> ReferencedCourse.of(125.0, CourseReference.MAGNETIC).trueDegrees(() -> null))
    );
  }
}
