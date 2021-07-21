package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestVorNdbFrequency {

  private static final VorNdbFrequency parser = new VorNdbFrequency();

  @Test
  void testParserFiltersEmptyInputs() {
    assertEquals(Optional.empty(), parser.apply(""));
  }

  @Test
  void testParserFiltersWhitespaceInputs() {
    assertEquals(Optional.empty(), parser.apply("     "));
  }

  @Test
  void testParserFiltersNonNumericInputs() {
    assertEquals(Optional.empty(), parser.apply("A11AB"));
  }

  @Test
  void testParserReturnsValidDoublesIfPresent() {
    assertAll(
        () -> assertEquals(Optional.of(9.1), parser.apply("00091")),
        () -> assertEquals(Optional.of(.1), parser.apply("00001")),
        () -> assertEquals(Optional.of(1106.), parser.apply("11060"))
    );
  }
}
