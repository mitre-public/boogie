package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestDirectionRestriction {

  @Test
  public void testNoFilterOnEmptyInputs() {
    Assertions.assertFalse(new DirectionRestriction().filterInput(" "));
  }

  @Test
  public void testParsesAllValidInputStrings() {
    DirectionRestriction spec = new DirectionRestriction();
    spec.allowedValues().forEach(value -> assertEquals(value, spec.parseValue(value), "Error parsing allowed value: " + value));
  }

  @Test
  public void testParseExceptionWhenInputBadDirection() {
    assertThrows(FieldSpecParseException.class, () -> new DirectionRestriction().parseValue("Y"));
  }
}
