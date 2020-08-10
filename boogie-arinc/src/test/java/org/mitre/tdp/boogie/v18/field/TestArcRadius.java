package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.ArcRadius;

class TestArcRadius {

  @Test
  public void testParseValidArcRadius() {
    assertEquals(246.868, new ArcRadius().parseValue("246868"));
  }

  @Test
  public void testParseExceptionOnInvalidArcRadius() {
    assertThrows(FieldSpecParseException.class, () -> new ArcRadius().parseValue("110AB"));
  }

}