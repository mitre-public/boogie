package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestNameFormat {

  private static final NameFormat parser = new NameFormat();

  @Test
  void testParserFiltersWhitespaceInput() {
    assertEquals(Optional.of("   "), parser.apply("   "));
  }

  @Test
  public void testParseValidNameFormatColumn1() {
    assertAll(
        () -> assertEquals(Optional.of("A  "), parser.apply("A  ")),
        () -> assertEquals(Optional.of("   "), parser.apply("Z  "), "Invalid value should be returned as empty")
    );
  }

  @Test
  public void testParseValidNameFormatColumn2() {
    assertAll(
        () -> assertEquals(Optional.of(" O "), parser.apply(" O ")),
        () -> assertEquals(Optional.of("   "), parser.apply(" Q "), "Invalid value should be returned as empty")
    );
  }

  @Test
  public void testParseValidNameFormatColumn3() {
    assertAll(
        () -> assertEquals(Optional.of("   "), parser.apply("   ")),
        () -> assertEquals(Optional.of("   "), parser.apply("  Z"), "Invalid value should be returned as empty")
    );
  }
}
