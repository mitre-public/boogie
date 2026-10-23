package org.mitre.boogie.xml.assemble;

import java.util.Optional;
import javax.annotation.Nullable;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;

/** Converts XML courses to the magnetic reference used by core leg consumers. */
final class AssemblyCourses {

  private AssemblyCourses() {}

  static Optional<Double> magnetic(Optional<Double> course, boolean isTrue,
      @Nullable Fix associatedFix, @Nullable Fix recommendedNavaid) {
    if (!isTrue) {
      return course;
    }
    // Match the variation precedence used by core consumers. Without a variation,
    // retain the true course only in the source record rather than mislabeling it.
    Optional<MagneticVariation> variation = Optional.ofNullable(recommendedNavaid).flatMap(Fix::magneticVariation)
        .or(() -> Optional.ofNullable(associatedFix).flatMap(Fix::magneticVariation));
    return course.flatMap(value -> variation.map(v -> ((v.trueToMagnetic(value) % 360.0) + 360.0) % 360.0));
  }
}
