package org.mitre.boogie.xml.assemble;

import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.model.fields.MagneticTrueIndicator;
import org.mitre.tdp.boogie.CourseReference;

/** Resolves XML course references before constructing a course value. */
final class CourseReferences {

  private CourseReferences() {
  }

  static CourseReference fromTrueFlag(boolean isTrue) {
    return isTrue ? CourseReference.TRUE : CourseReference.MAGNETIC;
  }

  static CourseReference forPort(ArincPortInfo portInfo) {
    return portInfo.magneticTrueIndicator()
        .flatMap(MagneticTrueIndicator::courseReference)
        .orElse(CourseReference.MAGNETIC);
  }
}
