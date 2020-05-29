package org.mitre.tdp.boogie.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.FieldSpecParseException;
import org.mitre.tdp.boogie.v18.spec.field.IlsMlsGlsCategory;

public class TestIlsGlsMlsCategory {

  @Test
  public void testFiltersTrimmableInput() {
    assertTrue(new IlsMlsGlsCategory().filterInput("  "));
  }

  @Test
  public void testParseValidCategories() {
    IlsMlsGlsCategory spec = new IlsMlsGlsCategory();
    spec.allowedCategories().forEach(cat -> assertEquals(cat, spec.parseValue(cat), "Bad parse for valid cat value: " + cat));
  }

  @Test
  public void testParseExceptionInvalidCategories() {
    assertThrows(FieldSpecParseException.class, () -> new IlsMlsGlsCategory().parseValue("Q"));
  }
}
