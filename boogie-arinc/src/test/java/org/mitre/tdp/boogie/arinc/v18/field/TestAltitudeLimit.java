package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestAltitudeLimit {

  @Test
  public void testFiltersTrimmableInputs() {
    assertTrue(new AltitudeLimit().filterInput("   "));
  }

  @Test
  public void testFiltersTrimmableInput() {
    assertTrue(new AltitudeLimit().filterInput("   "));
  }

  @Test
  public void testParseValidLimit() {
    assertEquals(Pair.of(18000.0d, 60000.0d), new AltitudeLimit().parseValue("180600"));
  }

  @Test
  public void testParseExceptionInvalidLimit() {
    assertThrows(FieldSpecParseException.class, () -> new AltitudeLimit().parseValue("18A60A"));
  }
}
