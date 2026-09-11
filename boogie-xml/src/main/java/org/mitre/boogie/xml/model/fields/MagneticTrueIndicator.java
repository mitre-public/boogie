package org.mitre.boogie.xml.model.fields;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.CourseReference;

public enum MagneticTrueIndicator {
  BOTH,
  TRUE,
  MAGNETIC;

  public Optional<CourseReference> courseReference() {
    return switch (this) {
      case TRUE -> Optional.of(CourseReference.TRUE);
      case MAGNETIC -> Optional.of(CourseReference.MAGNETIC);
      case BOTH -> Optional.empty();
    };
  }

  public static final Set<String> VALID = Arrays.stream(MagneticTrueIndicator.values()).map(MagneticTrueIndicator::name).collect(Collectors.toSet());
}
