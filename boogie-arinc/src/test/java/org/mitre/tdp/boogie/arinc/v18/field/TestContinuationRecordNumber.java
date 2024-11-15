package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestContinuationRecordNumber {

  private static final ContinuationRecordNumber parser = new ContinuationRecordNumber();

  @Test
  void testParserFiltersEmptyInputs() {
    assertEquals(Optional.empty(), parser.apply(""));
  }

  @Test
  void testParserFiltersWhitespaceInputs() {
    assertEquals(Optional.empty(), parser.apply("   "));
  }

  @Test
  void testParserFiltersNonAlphaNumericInput() {
    assertEquals(Optional.empty(), parser.apply("@"));
  }

  @Test
  void testParserReturnsNonEmptyNumericInput() {
    assertEquals(Optional.of("0"), parser.apply("0"));
  }

  @Test
  void testParserReturnsNonEmptyCharacterInput() {

    assertEquals(Optional.of("A"), parser.apply("A"));
  }

  @Test
  void testParserReturnsTrimmedInput() {
    assertEquals(Optional.of("1"), parser.apply("   1   "));
  }
}
