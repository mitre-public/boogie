package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.EuIndicator;

public class TestEuIndicator {

  @Test
  public void testAllowsBlankInputs() {
    assertFalse(new EuIndicator().filterInput(" "));
  }

  @Test
  public void testTrueWhenRestrictionRecordExists() {
    assertTrue(new EuIndicator().parseValue("Y"));
  }

  @Test
  public void testFalseWhenEmpty() {
    assertFalse(new EuIndicator().parseValue(" "));
  }

  @Test
  public void testParseExceptionInvalidValue() {
    assertThrows(FieldSpecParseException.class, () -> new EuIndicator().parseValue("G"));
  }
}
