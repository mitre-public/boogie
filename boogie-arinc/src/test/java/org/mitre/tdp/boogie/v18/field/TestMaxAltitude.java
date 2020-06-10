package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.MaxAltitude;

public class TestMaxAltitude {

  @Test
  public void testParseExceptionSpecialAltitude() {
    assertThrows(FieldSpecParseException.class, () -> new MaxAltitude().parseValue("UNLTD"));
  }

  @Test
  public void testPreFiltersSpecialAltitudeCodes() {
    MaxAltitude spec = new MaxAltitude();
    spec.specialAltitudeCodes().forEach(code -> assertTrue(spec.filterInput(code)));
  }
}
