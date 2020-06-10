package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.LocalizerBearing;

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
