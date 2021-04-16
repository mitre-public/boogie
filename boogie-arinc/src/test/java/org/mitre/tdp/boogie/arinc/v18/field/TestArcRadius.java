package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;
import org.mitre.tdp.boogie.arinc.v18.field.ArcRadius;

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