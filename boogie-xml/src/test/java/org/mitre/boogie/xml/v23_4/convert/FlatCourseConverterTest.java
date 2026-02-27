package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.*;

import java.math.BigDecimal;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.Course;

class FlatCourseConverterTest {
  private final FlatCourseConverter converter = FlatCourseConverter.INSTANCE;

  @Test
  void nullReturnsEmpty() {
    Optional<FlatCourse> result = converter.apply(null);
    assertTrue(result.isEmpty());
  }

  @Test
  void convertsValidCourse() {
    Course course = new Course();
    course.setCourseValue(BigDecimal.valueOf(270.5));
    course.setIsTrue(true);

    FlatCourse result = converter.apply(course).orElseThrow();
    assertAll(
        () -> assertEquals(270.5, result.value(), 0.001),
        () -> assertEquals(true, result.isTrue())
    );
  }

  @Test
  void nullFieldsInsideCourse() {
    Course course = new Course();

    FlatCourse result = converter.apply(course).orElseThrow();
    assertAll(
        () -> assertNull(result.value()),
        () -> assertNull(result.isTrue())
    );
  }
}
