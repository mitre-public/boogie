package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

public class TestSectionCode {

  @Test
  public void testValidSectionCode() {
    assertEquals(SectionCode.A, SectionCode.SPEC.parseValue("A"));
  }

  @Test
  public void testParseExceptionOnInvalidCode() {
    assertThrows(FieldSpecParseException.class, () -> SectionCode.SPEC.parseValue("Q"));
  }
}