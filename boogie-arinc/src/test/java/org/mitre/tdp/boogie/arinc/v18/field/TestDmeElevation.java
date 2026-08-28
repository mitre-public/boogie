package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestDmeElevation {

  private static final DmeElevation parser = new DmeElevation();

  @Test
  void testParserFiltersEmptyInputs() {
    assertEquals(Optional.empty(), parser.apply(""));
  }

  @Test
  void testParserFiltersWhitespaceInputs() {
    assertEquals(Optional.empty(), parser.apply("   "));
  }

  @Test
  void testParserFiltersNonNumericInputs() {
    assertEquals(Optional.empty(), parser.apply("HI"));
  }

  @Test
  void testParserReturnsValidDoubleIfPresent() {
    assertAll(
        () -> assertEquals(Optional.of(-150.0), parser.apply("-150")),
        () -> assertEquals(Optional.of(150.0), parser.apply(" 150")),
        () -> assertEquals(Optional.of(150.0), parser.apply("+150")),
        () -> assertEquals(Optional.of(-150.0), parser.apply("xx -150 yy", 2, 7))
    );
  }

  @Test
  void testRangeAndSignEdgeCases() {
    assertEquals(Optional.of(-0.0), parser.apply("-0"));
    assertEquals(Optional.empty(), parser.apply("+"));
    assertThrows(IndexOutOfBoundsException.class, () -> parser.apply("123", 2, 1));
  }
}
