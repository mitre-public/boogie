package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    assertAll(
        () -> assertEquals(Optional.empty(), parser.apply("ABCD")),
        () -> assertEquals(Optional.empty(), parser.apply("١٢٣٤"))
    );
  }

  @Test
  void testParserFiltersIncorrectLengthNumericInputs() {
    assertEquals(Optional.empty(), parser.apply("12345"));
  }

  @Test
  void testParserReturnsValidCycleInput() {
    assertAll(
        () -> assertEquals(Optional.of("2001"), parser.apply("2001")),
        () -> assertEquals(Optional.of("1701"), parser.apply("1701")),
        () -> assertEquals(Optional.of("2001"), parser.apply(" 2001\t"))
    );
  }

  @Test
  void testRangeParsingMatchesStringParsing() {
    assertAll(
        () -> assertRangeEquals(""),
        () -> assertRangeEquals("    "),
        () -> assertRangeEquals("2001"),
        () -> assertRangeEquals(" 2001\t"),
        () -> assertRangeEquals("ABCD"),
        () -> assertRangeEquals("123"),
        () -> assertRangeEquals("12345"),
        () -> assertRangeEquals("١٢٣٤"),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> parser.parse("2001", -1, 1)),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> parser.parse("2001", 0, 5)),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> parser.parse("2001", 2, 1)),
        () -> assertThrows(NullPointerException.class, () -> parser.apply(null)),
        () -> assertThrows(NullPointerException.class, () -> parser.parse(null, 0, 0))
    );
  }

  @Test
  void testParserCachesAsciiCycles() {
    Optional<String> direct = parser.apply("2609");
    Optional<String> ranged = parseRange("2609");

    assertAll(
        () -> assertSame(direct, ranged),
        () -> assertSame(direct.orElseThrow(), ranged.orElseThrow())
    );
  }

  private static void assertRangeEquals(String fieldValue) {
    assertEquals(parser.apply(fieldValue), parseRange(fieldValue), () -> "Range result for [" + fieldValue + "]");
  }

  private static Optional<String> parseRange(String fieldValue) {
    String source = "prefix" + fieldValue + "suffix";
    int startOffset = "prefix".length();
    return parser.parse(source, startOffset, startOffset + fieldValue.length());
  }
}
