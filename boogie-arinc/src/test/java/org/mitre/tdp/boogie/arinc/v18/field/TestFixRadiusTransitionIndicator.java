package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;

public class TestFixRadiusTransitionIndicator {

  @Test
  public void testParseValidFixRadius() {
    assertEquals(96.9d, new FixedRadiusTransitionIndicator().parseValue("969"));
  }

  @Test
  public void testParseExceptionInValidFixRadius() {
    assertThrows(FieldSpecParseException.class, () -> new FixedRadiusTransitionIndicator().parseValue("9A5"));
  }
}
