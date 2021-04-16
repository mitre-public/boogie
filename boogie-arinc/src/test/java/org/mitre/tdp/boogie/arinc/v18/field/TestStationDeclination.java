package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;
import org.mitre.tdp.boogie.arinc.v18.field.StationDeclination;

public class TestStationDeclination {

  @Test
  public void testEastValidVariationIsPositive() {
    assertEquals(14.0, new StationDeclination().parseValue("E0140"));
  }

  @Test
  public void testWestValidVariationIsNegative() {
    assertEquals(-14.0, new StationDeclination().parseValue("W0140"));
  }

  @Test
  public void testTrueVariationsAreSkipped() {
    assertFalse(new StationDeclination().parse("T0140").isPresent());
  }

  @Test
  public void testGridVariationsAreSkipped() {
    assertFalse(new StationDeclination().parse("G0140").isPresent());
  }

  @Test
  public void testTrueVariationThrowsParseException() {
    assertThrows(FieldSpecParseException.class, () -> new StationDeclination().parseValue("T0140"));
  }
}
