package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.StationDeclination;

public class TestStationDeclination {

  @Test
  public void testParseValidStartCodes() {
    assertEquals("E1000", new StationDeclination().parseValue("E1000"));
  }

  @Test
  public void testParseExceptionOnBadStartCode() {
    assertThrows(FieldSpecParseException.class, () -> new StationDeclination().parseValue("Q"));
  }

  @Test
  public void testParseExceptionOfBadNumericValue() {
    assertThrows(FieldSpecParseException.class, () -> new StationDeclination().parseValue("W0A2"));
  }
}
