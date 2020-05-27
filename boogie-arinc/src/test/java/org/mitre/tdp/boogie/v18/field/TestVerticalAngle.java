package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.VerticalAngle;

public class TestVerticalAngle {

  @Test
  public void testParseValidAngle() {
    assertEquals(-9.69f, new VerticalAngle().parse("-969"));
  }

  @Test
  public void testParseExceptionAngleTooHigh() {
    assertThrows(FieldSpecParseException.class, () -> new VerticalAngle().parse("-1045"));
  }

  @Test
  public void testParseExceptionBadAngle() {
    assertThrows(FieldSpecParseException.class, () -> new VerticalAngle().parse("-12A"));
  }

  @Test
  public void testParseExceptionNonNegativeAngle() {
    assertThrows(FieldSpecParseException.class, () -> new VerticalAngle().parse("905"));
  }
}
