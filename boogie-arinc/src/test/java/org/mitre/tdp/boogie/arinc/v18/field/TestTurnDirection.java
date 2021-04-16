package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;
import org.mitre.tdp.boogie.arinc.v18.field.TurnDirection;

public class TestTurnDirection {

  @Test
  public void testTurnDirectionValidValue() {
    assertEquals(TurnDirection.R, TurnDirection.SPEC.parseValue("R"));
  }

  @Test
  public void testInvalidTurnDirectionThrowsException() {
    assertThrows(FieldSpecParseException.class, () -> TurnDirection.SPEC.parseValue("D"));
  }
}
