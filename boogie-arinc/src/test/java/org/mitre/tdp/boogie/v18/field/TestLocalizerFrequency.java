package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.LocalizerFrequency;

public class TestLocalizerFrequency {

  @Test
  public void testParseValidFrequency() {
    assertEquals(110.3, new LocalizerFrequency().parse("11030"));
  }

  @Test
  public void testParseExceptionOnInvalidFrequency() {
    assertThrows(FieldSpecParseException.class, () -> new LocalizerFrequency().parse("110AB"));
  }
}
