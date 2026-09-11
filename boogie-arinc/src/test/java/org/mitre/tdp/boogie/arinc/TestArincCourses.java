package org.mitre.tdp.boogie.arinc;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotSame;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.CourseReference;
import org.mitre.tdp.boogie.ReferencedCourse;
import org.mitre.tdp.boogie.arinc.v18.field.OutboundMagneticCourse;

class TestArincCourses {

  @Test
  void parsesPublishedDegreesAndReferences() {
    ArincRecord record = new ArincRecord("125T1250    BADT", ArincRecord.FieldLayout.from(List.of(
        new RecordField<>("trueCourse", new OutboundMagneticCourse()),
        new RecordField<>("magneticCourse", new OutboundMagneticCourse()),
        new RecordField<>("blankCourse", new OutboundMagneticCourse()),
        new RecordField<>("invalidCourse", new OutboundMagneticCourse())
    )));

    assertAll(
        () -> assertEquals(ReferencedCourse.trueCourse(125), ArincCourses.parse(record, "trueCourse").orElseThrow()),
        () -> assertEquals(ReferencedCourse.magnetic(125), ArincCourses.parse(record, "magneticCourse").orElseThrow()),
        () -> assertTrue(ArincCourses.parse(record, "blankCourse").isEmpty()),
        () -> assertTrue(ArincCourses.parse(record, "invalidCourse").isEmpty()),
        () -> assertTrue(ArincCourses.parse(record, "missingCourse").isEmpty())
    );
  }

  @Test
  void parentDeclarationChangesOnlyUnmarkedReference() {
    ReferencedCourse unmarked = ReferencedCourse.magnetic(125);
    ReferencedCourse explicitTrue = ReferencedCourse.trueCourse(126);
    ReferencedCourse inheritedTrue = ArincCourses.withDefaultReference(unmarked, CourseReference.TRUE);

    assertAll(
        () -> assertEquals(ReferencedCourse.trueCourse(125), inheritedTrue),
        () -> assertNotSame(unmarked, inheritedTrue),
        () -> assertEquals(CourseReference.MAGNETIC, unmarked.reference()),
        () -> assertSame(unmarked, ArincCourses.withDefaultReference(unmarked, CourseReference.MAGNETIC)),
        () -> assertSame(explicitTrue, ArincCourses.withDefaultReference(explicitTrue, CourseReference.MAGNETIC)),
        () -> assertSame(explicitTrue, ArincCourses.withDefaultReference(explicitTrue, CourseReference.TRUE))
    );
  }
}
