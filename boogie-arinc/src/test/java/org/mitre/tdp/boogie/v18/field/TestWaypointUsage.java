package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.WaypointUsage;

public class TestWaypointUsage {

  @Test
  public void testPassOnValidColumn12() {
    assertEquals("RB", new WaypointUsage().parse("RB"));
  }

  @Test
  public void testPassOnBlankColumn1() {
    assertEquals(" B", new WaypointUsage().parse(" B"));
  }

  @Test
  public void testPassOnBlankColumn2() {
    assertEquals("R ", new WaypointUsage().parse("R "));
  }

  @Test
  public void testParseExceptionOnNonRColumn1() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointUsage().parse("QB"));
  }

  @Test
  public void testParseExceptionOnInvalidColumn2() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointUsage().parse("RA"));
  }
}
