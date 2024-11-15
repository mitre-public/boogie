package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestIlsGlsMlsCategory {

  private static final IlsMlsGlsCategory parser = new IlsMlsGlsCategory();

  @Test
  void testParserFiltersEmptyInputs() {
    assertEquals(Optional.empty(), parser.apply(""));
  }

  @Test
  void testParserFiltersWhitespaceInputs() {
    assertEquals(Optional.empty(), parser.apply("   "));
  }

  @Test
  void testFiltersUnallowedValues() {
    assertFalse(IlsMlsGlsCategory.allowedCategories.contains("Q"));
    assertEquals(Optional.empty(), parser.apply("Q"));
  }

  @Test
  void testReturnsAllAllowedValues() {
    IlsMlsGlsCategory.allowedCategories.forEach(value -> assertEquals(Optional.of(value), parser.apply(value)));
  }
}
