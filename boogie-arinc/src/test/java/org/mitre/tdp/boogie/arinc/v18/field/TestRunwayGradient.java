package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestRunwayGradient {

  @Test
  public void testParseValidRunwayGradient() {
    assertEquals(0.45, new RunwayGradient().parseValue("+0450"));
  }

  @Test
  public void testParseNegativeRunwayGradient() {
    assertEquals(-0.3, new RunwayGradient().parseValue("-0300"));
  }

  @Test
  public void testParseExceptionOnInvalidRunwayGradient() {
    assertThrows(FieldSpecParseException.class, () -> new RunwayGradient().parseValue("110AB"));
  }
}