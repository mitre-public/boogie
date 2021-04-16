package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;
import org.mitre.tdp.boogie.arinc.v18.field.VorNdbFrequency;

public class TestVorNdbFrequency {

  @Test
  public void testParseValidFrequency() {
    assertEquals(123.4, new VorNdbFrequency().parseValue("1234"), 0.00000001);
  }

  @Test
  public void testParseInvalidAlphanumericFrequency() {
    assertThrows(FieldSpecParseException.class, () -> new VorNdbFrequency().parseValue("123A"));
  }
}
