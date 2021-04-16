package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;
import org.mitre.tdp.boogie.arinc.v18.field.LocalizerBearing;

public class TestLocalizerBearing {

  @Test
  public void testParseValidBearing() {
    assertEquals(257.0, new LocalizerBearing().parseValue("2570"));
  }

  @Test
  public void testExceptionOnTrueBearing() {
    assertThrows(FieldSpecParseException.class, () -> new LocalizerBearing().parseValue("347T"));
  }
}
