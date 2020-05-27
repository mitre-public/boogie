package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.Level;

public class TestLevel {

  @Test
  public void testLevelSuccessOnValidString() {
    assertEquals(Level.B, Level.SPEC.parse("B"));
  }

  @Test
  public void testThrowsExceptionWhenUnknownCode() {
    assertThrows(FieldSpecParseException.class, () -> Level.SPEC.parse("K"));
  }
}
