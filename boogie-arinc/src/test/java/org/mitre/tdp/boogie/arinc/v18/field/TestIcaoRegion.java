package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestIcaoRegion {

  private static final IcaoRegion parser = new IcaoRegion();

  @Test
  void testParserFiltersEmptyInputs() {
    assertEquals(Optional.empty(), parser.apply(""));
  }

  @Test
  void testParserFiltersWhitespaceInputs() {
    assertEquals(Optional.empty(), parser.apply("   "));
  }

  @Test
  void testParserFiltersInputsOver2CharactersInputs() {
    assertEquals(Optional.empty(), parser.apply("ABCD"));
  }

  @Test
  void testParserReturnsValidIcaoRegionInput() {
    assertAll(
        () -> assertEquals(Optional.of("EG"), parser.apply("EG")),
        () -> assertEquals(Optional.of("K2"), parser.apply("K2"))
    );
  }

  /**
   * One of the best ways to blow up the heap is to maintain a million copies of the same strings - this is one of the commonly
   * re-used string fields.
   */
  @Test
  void testParserInternsEncounteredRegionCodes() {
    String ab = parser.apply("AB").orElse(null);
    String cd = parser.apply("CD").orElse(null);

    assertAll(
        () -> assertTrue(ab == parser.apply("AB").orElse(null), "Identity equals should be true on AB"),
        () -> assertTrue(cd == parser.apply("CD").orElse(null), "Identity equals should be true on CD")
    );
  }
}
