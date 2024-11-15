package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;

class TestAltitudeLimit {

  private static final AltitudeLimit parser = new AltitudeLimit();

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
    assertEquals(Optional.empty(), parser.apply("AAABBB"));
  }

  @Test
  void testParserFiltersNumericInputsWithoutEnoughCharacters() {
    assertEquals(Optional.empty(), parser.apply("123   "));
  }

  @Test
  void testParserReturnsAppropriateAltitudeValues() {
    assertEquals(Optional.of(Pair.of(30000.0, 0.0)), parser.apply("300000"));
  }
}
