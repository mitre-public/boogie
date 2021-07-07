package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestTurnDirectionValid {

  @Test
  public void testParseTurnDirectionAllowsBlanks() {
    assertFalse(new TurnDirectionValid().filterInput(" "));
  }

  @Test
  public void testParseValidTurnDirection() {
    assertEquals(true, new TurnDirectionValid().parseValue("Y"));
  }

  @Test
  public void testParseFalseIfBlank() {
    assertEquals(false, new TurnDirectionValid().parseValue(" "));
  }

  @Test
  public void testParseExceptionWhenInvalidDirection() {
    assertThrows(FieldSpecParseException.class, () -> new TurnDirectionValid().parseValue("G"));
  }
}
