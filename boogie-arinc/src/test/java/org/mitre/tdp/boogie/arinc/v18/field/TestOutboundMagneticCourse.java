package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestOutboundMagneticCourse {

  @Test
  public void testFiltersTrimmableInput() {
    assertTrue(new OutboundMagneticCourse().filterInput("  "));
  }

  @Test
  public void testIsTrueCourse() {
    assertTrue(new OutboundMagneticCourse().isTrueCourse("123T"));
  }

  @Test
  public void testParseValidInboundMagneticCourse() {
    assertEquals(27.6, new OutboundMagneticCourse().parseValue("0276"));
  }

  @Test
  public void testThrowsSpecExceptionOnTrueCourse() {
    assertThrows(FieldSpecParseException.class, () -> new OutboundMagneticCourse().parseValue("123T"));
  }

  @Test
  public void testParseValidInboundMagneticCourseNoPad() {
    assertEquals(123.7, new OutboundMagneticCourse().parseValue("1237"));
  }
}
