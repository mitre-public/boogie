package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;
import org.mitre.tdp.boogie.arinc.v18.field.WaypointDescription;

public class TestWaypointDescription {

  @Test
  public void testCorrectParseWithValidCodes() {
    assertEquals("ABCD", new WaypointDescription().parseValue("ABCD"));
  }

  @Test
  public void testParseExceptionWithInvalidColumn1() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointDescription().parseValue("IBCD"));
  }

  @Test
  public void testParseExceptionWithInvalidColumn2() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointDescription().parseValue("AACD"));
  }

  @Test
  public void testParseExceptionWithInvalidColumn3() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointDescription().parseValue("ABFD"));
  }

  @Test
  public void testParseExceptionWithInvalidColumn4() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointDescription().parseValue("ABCZ"));
  }
}
