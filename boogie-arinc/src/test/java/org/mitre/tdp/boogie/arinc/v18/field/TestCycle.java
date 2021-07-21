package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestCycle {

  private static final Cycle parser = new Cycle();

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
    assertEquals(Optional.empty(), parser.apply("ABCD"));
  }

  @Test
  void testParserFiltersIncorrectLengthNumericInputs() {
    assertEquals(Optional.empty(), parser.apply("12345"));
  }

  @Test
  void testParserReturnsValidCycleInput() {
    assertAll(
        () -> assertEquals(Optional.of("2001"), parser.apply("2001")),
        () -> assertEquals(Optional.of("1701"), parser.apply("1701"))
    );
  }
}
