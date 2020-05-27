package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.TurnDirection;

public class TestTurnDirection {

  @Test
  public void testTurnDirectionValidValue() {
    assertEquals(TurnDirection.R, TurnDirection.SPEC.parse("R"));
  }

  @Test
  public void testInvalidTurnDirectionThrowsException() {
    assertThrows(FieldSpecParseException.class, () -> TurnDirection.SPEC.parse("D"));
  }
}
