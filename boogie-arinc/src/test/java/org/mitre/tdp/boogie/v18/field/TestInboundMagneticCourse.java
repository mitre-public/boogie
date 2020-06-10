package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.InboundMagneticCourse;

public class TestInboundMagneticCourse {

  @Test
  public void testIsTrueCourse() {
    assertTrue(new InboundMagneticCourse().isTrueCourse("123T"));
  }

  @Test
  public void testParseValidInboundMagneticCourse() {
    assertEquals(27.6, new InboundMagneticCourse().parseValue("0276"));
  }

  @Test
  public void testThrowsSpecExceptionOnTrueCourse() {
    assertThrows(FieldSpecParseException.class, () -> new InboundMagneticCourse().parseValue("123T"));
  }

  @Test
  public void testParseValidInboundMagneticCourseNoPad() {
    assertEquals(123.7, new InboundMagneticCourse().parseValue("1237"));
  }
}
