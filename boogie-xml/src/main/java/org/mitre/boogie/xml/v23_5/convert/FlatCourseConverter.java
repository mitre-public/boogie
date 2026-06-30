package org.mitre.boogie.xml.v23_5.convert;

import java.math.BigDecimal;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.boogie.xml.v23_5.generated.Course;

final class FlatCourseConverter implements Function<Course, Optional<FlatCourse>> {
  static final FlatCourseConverter INSTANCE = new FlatCourseConverter();

  private FlatCourseConverter() {
  }

  @Override
  public Optional<FlatCourse> apply(Course course) {
    return Optional.ofNullable(course)
        .map(c -> new FlatCourse(
            Optional.ofNullable(c.getCourseValue()).map(BigDecimal::doubleValue).orElse(null),
            c.isIsTrue()));
  }
}
