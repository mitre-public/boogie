package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestLevel {

  @Test
  public void testLevelSuccessOnValidString() {
    assertEquals(Level.B, Level.SPEC.parseValue("B"));
  }

  @Test
  public void testThrowsExceptionWhenUnknownCode() {
    assertThrows(FieldSpecParseException.class, () -> Level.SPEC.parseValue("K"));
  }
}
