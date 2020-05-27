package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.v18.spec.field.Latitude;

public class TestLatitude {

  @Test
  public void testParseValidLatitude() {
    assertEquals(39.86078055, new Latitude().parse("N39513881"), 0.000001);
  }

  @Test
  public void testParseValidSouthernLatitude() {
    assertEquals(-39.86078055, new Latitude().parse("S39513881"), 0.000001);
  }

  @Test
  public void testExceptionOnInvalidLatitude() {
    assertThrows(NumberFormatException.class, () -> new Latitude().parse("SA9513881"));
  }
}
