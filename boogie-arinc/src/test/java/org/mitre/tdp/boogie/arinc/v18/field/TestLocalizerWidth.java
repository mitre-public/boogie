package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestLocalizerWidth {

  private static final LocalizerWidth parser = new LocalizerWidth();

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
    assertEquals(Optional.empty(), parser.apply("AC11"));
  }

  @Test
  void testParserReturnsValidDoublesIfPresent() {
    assertAll(
        () -> assertEquals(Optional.of(29.12), parser.apply("2912")),
        () -> assertEquals(Optional.of(.51), parser.apply("0051")),
        () -> assertEquals(Optional.of(6.), parser.apply("0600"))
    );
  }
}
