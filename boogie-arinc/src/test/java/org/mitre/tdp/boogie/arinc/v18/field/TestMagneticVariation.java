package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;
import org.mitre.tdp.boogie.arinc.v18.field.MagneticVariation;

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
  public void testTrueVariationsAreSkipped(){
    assertFalse(new MagneticVariation().parse("T0140").isPresent());
  }

  @Test
  public void testTrueVariationThrowsParseException() {
    assertThrows(FieldSpecParseException.class, () -> new MagneticVariation().parseValue("T0140"));
  }
}
