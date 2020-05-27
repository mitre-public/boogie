package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.WaypointDescriptionCode;

public class TestWaypointDescriptionCode {

  @Test
  public void testCorrectParseWithValidCodes() {
    assertEquals("ABCD", new WaypointDescriptionCode().parse("ABCD"));
  }

  @Test
  public void testParseExceptionWithInvalidColumn1() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointDescriptionCode().parse("IBCD"));
  }

  @Test
  public void testParseExceptionWithInvalidColumn2() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointDescriptionCode().parse("AACD"));
  }

  @Test
  public void testParseExceptionWithInvalidColumn3() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointDescriptionCode().parse("ABFD"));
  }

  @Test
  public void testParseExceptionWithInvalidColumn4() {
    assertThrows(FieldSpecParseException.class, () -> new WaypointDescriptionCode().parse("ABCZ"));
  }
}
