package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestLongestRunwaySurfaceCode {

  @Test
  void testParserFiltersEmptyInputs() {
    assertEquals(Optional.empty(), LongestRunwaySurfaceCode.SPEC.apply(""));
  }

  @Test
  void testParserFiltersWhitespaceInputs() {
    assertEquals(Optional.empty(), LongestRunwaySurfaceCode.SPEC.apply("   "));
  }

  @Test
  void testParserFiltersInvalidLongestRunwaySurfaceCodeValues() {
    assertAll(
        () -> assertFalse(LongestRunwaySurfaceCode.VALID.contains("1")),
        () -> assertEquals(Optional.empty(), LongestRunwaySurfaceCode.SPEC.apply("1")),
        () -> assertFalse(LongestRunwaySurfaceCode.VALID.contains("A")),
        () -> assertEquals(Optional.empty(), LongestRunwaySurfaceCode.SPEC.apply("A"))
    );
  }

  @Test
  void testParserReturnsAllValidLongestRunwaySurfaceCodes() {
    LongestRunwaySurfaceCode.VALID.forEach(val -> assertEquals(Optional.of(LongestRunwaySurfaceCode.valueOf(val)), LongestRunwaySurfaceCode.SPEC.apply(val)));
  }
}
