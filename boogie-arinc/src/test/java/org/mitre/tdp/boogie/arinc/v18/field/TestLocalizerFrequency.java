package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestLocalizerFrequency {

  @Test
  public void testParseValidFrequency() {
    assertEquals(110.3, new LocalizerFrequency().parseValue("11030"));
  }

  @Test
  public void testParseExceptionOnInvalidFrequency() {
    assertThrows(FieldSpecParseException.class, () -> new LocalizerFrequency().parseValue("110AB"));
  }
}
