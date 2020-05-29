package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.Rnp;

public class TestRnp {

  @Test
  public void testParseValidRnp() {
    assertEquals(10.0f, new Rnp().parseValue("100"));
  }

  @Test
  public void testParseValidRnpWithNegativeExponent() {
    assertEquals(0.001, new Rnp().parseValue("013"));
  }

  @Test
  public void testParseExceptionInvalidValue() {
    assertThrows(FieldSpecParseException.class, () -> new Rnp().parseValue("A01"));
  }
}
