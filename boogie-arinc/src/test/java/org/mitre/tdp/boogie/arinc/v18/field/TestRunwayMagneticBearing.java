package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;
import org.mitre.tdp.boogie.arinc.v18.field.RunwayMagneticBearing;

public class TestRunwayMagneticBearing {

  @Test
  public void testParseValidBearing() {
    assertEquals(123.5f, new RunwayMagneticBearing().parseValue("1235"));
  }

  @Test
  public void testParseExceptionInvalidBearing() {
    assertThrows(FieldSpecParseException.class, () -> new RunwayMagneticBearing().parseValue("123A"));
  }

  @Test
  public void testParseTrueCourseBearingException() {
    assertThrows(FieldSpecParseException.class, () -> new RunwayMagneticBearing().parseValue("123T"));
  }
}
