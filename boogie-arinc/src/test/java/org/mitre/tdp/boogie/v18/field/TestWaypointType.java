package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.WaypointType;

public class TestWaypointType {

  @Test
  public void testParseDoesntTrimBlankInput() {
    assertTrue(new WaypointType().filterInput("   "));
  }

  @Test
  public void testParseWithValidCodes() {
    assertEquals("R  ", new WaypointType().parseValue("R  "));
    assertEquals(" A ", new WaypointType().parseValue(" A "));
    assertEquals("  Z", new WaypointType().parseValue("  Z"));
  }

  @Test
  public void testFailWithInvalidColumn1() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointType().parseValue("B  "));
  }

  @Test
  public void testFailWithInvalidColumn2() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointType().parseValue(" Z "));
  }

  @Test
  public void testFailWithInvalidColumn3() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointType().parseValue("  A"));
  }
}
