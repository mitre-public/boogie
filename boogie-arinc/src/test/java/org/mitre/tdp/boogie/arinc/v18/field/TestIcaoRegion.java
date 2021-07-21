package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

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
}
