package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;
import org.mitre.tdp.boogie.arinc.v18.field.LongestRunway;

public class TestLongestRunway {

  @Test
  public void testParseValidLength() {
    assertEquals(40 * 100, new LongestRunway().parseValue("040"));
  }

  @Test
  public void testParseExceptionInvalidLength() {
    assertThrows(FieldSpecParseException.class, () -> new LongestRunway().parseValue("04A"));
  }
}
