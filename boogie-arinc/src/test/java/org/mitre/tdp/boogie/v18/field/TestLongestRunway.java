package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.LongestRunway;

public class TestLongestRunway {

  @Test
  public void testParseValidLength() {
    assertEquals(40 * 100, new LongestRunway().parse("040"));
  }

  @Test
  public void testParseExceptionInvalidLength() {
    assertThrows(FieldSpecParseException.class, () -> new LongestRunway().parse("04A"));
  }
}
