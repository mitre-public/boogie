package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.MinimumAltitude;

public class TestMinimumAltitude {

  @Test
  public void testParseMinimumAltitudeNormal() {
    assertEquals(29000.0f, new MinimumAltitude().parseValue("29000"));
  }

  @Test
  public void testParseMinimumAltitudePreFiltersSpecialCodes() {
    MinimumAltitude spec = new MinimumAltitude();
    spec.specialAltitudeCodes().forEach(code -> assertTrue(spec.filterInput(code)));
  }

  @Test
  public void testParseMinimumAltitudeNegative() {
    assertEquals(-9000.0f, new MinimumAltitude().parseValue("-9000"));
  }

  @Test
  public void testParseMinimumAltitudeFlightLevel() {
    assertEquals(10000.0f, new MinimumAltitude().parseValue("FL100"));
  }

  @Test
  public void testParseMinimumAltitudeThrowsParseExceptionOn_NESTB() {
    assertThrows(FieldSpecParseException.class, () -> new MinimumAltitude().parseValue("NESTB"));
  }

  @Test
  public void testParseMinimumAltitudeThrowsParseExceptionOn_UNKNN() {
    assertThrows(FieldSpecParseException.class, () -> new MinimumAltitude().parseValue("UNKNN"));
  }
}