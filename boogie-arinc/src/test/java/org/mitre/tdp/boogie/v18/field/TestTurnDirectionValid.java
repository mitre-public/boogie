package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.TurnDirectionValid;

public class TestTurnDirectionValid {

  @Test
  public void testParseValidTurnDirection() {
    assertEquals(TurnDirectionValid.Y, TurnDirectionValid.SPEC.parse("Y"));
  }

  @Test
  public void testParseFalseIfBlank() {
    assertEquals(TurnDirectionValid.N, TurnDirectionValid.SPEC.parse(" "));
  }

  @Test
  public void testParseExceptionWhenInvalidDirection() {
    assertThrows(FieldSpecParseException.class, () -> TurnDirectionValid.SPEC.parse("G"));
  }
}
