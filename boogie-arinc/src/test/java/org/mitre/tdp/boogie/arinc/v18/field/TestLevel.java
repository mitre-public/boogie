package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestLevel {

  @Test
  void testParserFiltersEmptyInputs() {
    assertEquals(Optional.empty(), Level.SPEC.apply(""));
  }

  @Test
  void testParserFiltersWhitespaceInputs() {
    assertEquals(Optional.empty(), Level.SPEC.apply("   "));
  }

  @Test
  void testParserFiltersInvalidLevelValues() {
    assertAll(
        () -> assertFalse(Level.enumValues.contains("1")),
        () -> assertEquals(Optional.empty(), Level.SPEC.apply("1")),
        () -> assertFalse(Level.enumValues.contains("A")),
        () -> assertEquals(Optional.empty(), Level.SPEC.apply("A"))
    );
  }

  @Test
  void testParserReturnsAllValidLevels() {
    Level.enumValues.forEach(val -> assertEquals(Optional.of(Level.valueOf(val)), Level.SPEC.apply(val)));
  }
}
