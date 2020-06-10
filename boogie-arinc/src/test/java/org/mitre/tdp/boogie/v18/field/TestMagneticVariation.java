package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.MagneticVariation;

public class TestMagneticVariation {

  @Test
  public void testEastValidVariationIsPositive() {
    assertEquals(14.0, new MagneticVariation().parseValue("E0140"));
  }

  @Test
  public void testWestValidVariationIsNegative() {
    assertEquals(-14.0, new MagneticVariation().parseValue("W0140"));
  }

  @Test
  public void testTrueVariationThrowsParseException() {
    assertThrows(FieldSpecParseException.class, () -> new MagneticVariation().parseValue("T0140"));
  }
}
