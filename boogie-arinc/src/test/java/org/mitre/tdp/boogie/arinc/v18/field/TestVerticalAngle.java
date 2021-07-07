package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestVerticalAngle {

  @Test
  public void testParseValidAngle() {
    assertEquals(-9.69, new VerticalAngle().parseValue("-969"));
  }

  @Test
  public void testParsesZeroVerticalAngle() {
    assertEquals(0.0d, new VerticalAngle().parseValue(" 000"));
  }

  @Test
  public void testParseExceptionAngleTooHigh() {
    assertThrows(FieldSpecParseException.class, () -> new VerticalAngle().parseValue("-1045"));
  }

  @Test
  public void testParseExceptionBadAngle() {
    assertThrows(FieldSpecParseException.class, () -> new VerticalAngle().parseValue("-12A"));
  }
}
