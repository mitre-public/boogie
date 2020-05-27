package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.VorNdbFrequency;

public class TestVorNdbFrequency {

  @Test
  public void testParseValidFrequency() {
    assertEquals(123.4f, new VorNdbFrequency().parse("1234"));
  }

  @Test
  public void testParseInvalidAlphanumericFrequency() {
    assertThrows(FieldSpecParseException.class, () -> new VorNdbFrequency().parse("123A"));
  }
}
