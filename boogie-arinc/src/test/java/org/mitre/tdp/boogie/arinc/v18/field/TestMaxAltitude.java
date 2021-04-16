package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.FieldSpecParseException;
import org.mitre.tdp.boogie.arinc.v18.field.MaxAltitude;

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
