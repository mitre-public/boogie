package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestMinorAxisBearing {

  private static final MinorAxisBearing parser = new MinorAxisBearing();

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
    assertEquals(Optional.empty(), parser.apply("A1"));
  }

  @Test
  void testParserReturnsValidDoublesIfPresent() {
    assertAll(
        () -> assertEquals(Optional.of(129.1), parser.apply("1291")),
        () -> assertEquals(Optional.of(.1), parser.apply("0001")),
        () -> assertEquals(Optional.of(6.), parser.apply("0060"))
    );
  }
}
