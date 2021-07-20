package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestCenterFix {

  private static final CenterFix parser = new CenterFix();

  @Test
  void testParserFiltersEmptyInputs() {
    assertEquals(Optional.empty(), parser.apply(""));
  }

  @Test
  void testParserFiltersWhitespaceInputs() {
    assertEquals(Optional.empty(), parser.apply("   "));
  }

  @Test
  void testParserReturnsNonEmptyInput() {
    assertEquals(Optional.of("HI"), parser.apply("HI"));
  }

  @Test
  void testParserReturnsTrimmedInput() {
    assertEquals(Optional.of("HI"), parser.apply("   HI   "));
  }
}
