package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.v18.spec.field.Longitude;

public class TestLongitude {

  @Test
  public void testParseValidLongitude() {
    assertEquals(104.7522055, new Longitude().parseValue("E104450794"), 0.000001);
  }

  @Test
  public void testParseValidSouthernLongitude() {
    assertEquals(-104.7522055, new Longitude().parseValue("W104450794"), 0.000001);
  }

  @Test
  public void testExceptionOnInvalidLongitude() {
    assertThrows(NumberFormatException.class, () -> new Longitude().parseValue("WA04450794"));
  }
}
