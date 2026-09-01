package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

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
    assertEquals(Optional.empty(), parser.apply("  "));
  }

  @Test
  void testParserReturnsValidIcaoRegionInput() {
    assertAll(
        () -> assertEquals(Optional.of("EG"), parser.apply("EG")),
        () -> assertEquals(Optional.of("K2"), parser.apply("K2")),
        () -> assertEquals(Optional.of("K"), parser.apply("K")),
        () -> assertEquals(Optional.of("K"), parser.apply("K "))
    );
  }

  @Test
  void testParserRejectsNonArincCodes() {
    assertAll(
        () -> assertEquals(Optional.empty(), parser.apply("K\t")),
        () -> assertEquals(Optional.empty(), parser.apply("K\u2003")),
        () -> assertEquals(Optional.empty(), parser.apply("É٢")),
        () -> assertEquals(Optional.empty(), parser.apply("k2")),
        () -> assertEquals(Optional.empty(), parser.apply("K2X"))
    );
  }

  @Test
  void testRangeParsingMatchesStringParsing() {
    assertAll(
        () -> assertRangeEquals(""),
        () -> assertRangeEquals("  "),
        () -> assertRangeEquals("EG"),
        () -> assertRangeEquals("K2"),
        () -> assertRangeEquals("K"),
        () -> assertRangeEquals("K "),
        () -> assertRangeEquals("K\t"),
        () -> assertRangeEquals("K\u2003"),
        () -> assertRangeEquals("É٢"),
        () -> assertRangeEquals(" K"),
        () -> assertRangeEquals("K!"),
        () -> assertRangeEquals("\ud801\udc00"),
        () -> assertRangeEquals("K2 "),
        () -> assertRangeEquals("K2X")
    );
  }

  @Test
  void testRangeBoundsAndMalformedInputs() {
    assertAll(
        () -> assertEquals(Optional.empty(), parser.apply("!")),
        () -> assertEquals(Optional.empty(), parseRange("!")),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> parser.parse("K2", -1, 1)),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> parser.parse("K2", 0, 3)),
        () -> assertThrows(IndexOutOfBoundsException.class, () -> parser.parse("K2", 2, 1)),
        () -> assertThrows(NullPointerException.class, () -> parser.apply(null)),
        () -> assertThrows(NullPointerException.class, () -> parser.parse(null, 0, 0))
    );
  }

  /**
   * One of the best ways to blow up the heap is to maintain a million copies of the same strings - this is one of the commonly
   * re-used string fields.
   */
  @Test
  void testParserReusesCanonicalRegionCodes() {
    String ab = parser.apply("AB").orElse(null);
    String cd = parseRange("CD").orElse(null);
    Optional<String> single = parser.apply("K");

    assertAll(
        () -> assertSame(ab, parser.apply("AB").orElse(null)),
        () -> assertSame(ab, parseRange("AB").orElse(null)),
        () -> assertSame(cd, parser.apply("CD").orElse(null)),
        () -> assertSame(cd, parseRange("CD").orElse(null)),
        () -> assertSame(single, parseRange("K")),
        () -> assertSame(single, parser.apply("K ")),
        () -> assertSame(single.orElseThrow(), parser.apply("K ").orElseThrow())
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
