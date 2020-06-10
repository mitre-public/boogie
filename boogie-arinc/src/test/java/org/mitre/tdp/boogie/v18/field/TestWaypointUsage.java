package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.WaypointUsage;

public class TestWaypointUsage {

  @Test
  public void testPassOnValidColumn12() {
    assertEquals("RB", new WaypointUsage().parseValue("RB"));
  }

  @Test
  public void testPassOnBlankColumn1() {
    assertEquals(" B", new WaypointUsage().parseValue(" B"));
  }

  @Test
  public void testPassOnBlankColumn2() {
    assertEquals("R ", new WaypointUsage().parseValue("R "));
  }

  @Test
  public void testParseExceptionOnNonRColumn1() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointUsage().parseValue("QB"));
  }

  @Test
  public void testParseExceptionOnInvalidColumn2() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointUsage().parseValue("RA"));
  }
}
