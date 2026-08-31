package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestLongitude {

  private static final Longitude parser = new Longitude();

  @Test
  void testLongitudeFiltersEmptyStrings() {
    assertEquals(Optional.empty(), parser.apply("          "));
  }

  @Test
  void testParseValidEasternLongitude() {
    assertEquals(104.7522055, parser.apply("E104450794").orElseThrow(AssertionError::new), 0.000001);
  }

  @Test
  void testParseValidWesternLongitude() {
    assertEquals(-104.7522055, parser.apply("W104450794").orElseThrow(AssertionError::new), 0.000001);
  }

  @Test
  void testExceptionOnInvalidLongitude() {
    assertEquals(Optional.empty(), parser.apply("WA04450794"));
  }

  @Test
  void testRangeParsing() {
    assertAll(
        () -> assertEquals(104.7522055, parseRange("E104450794").orElseThrow(), 0.000001),
        () -> assertEquals(-104.7522055, parseRange("W104450794").orElseThrow(), 0.000001),
        () -> assertEquals(Optional.empty(), parseRange("          ")),
        () -> assertEquals(Optional.empty(), parseRange("WA04450794")),
        () -> assertEquals(-104.7522055, parseRange("X104450794").orElseThrow(), 0.000001),
        () -> assertEquals(
            Double.doubleToRawLongBits(-0.0),
            Double.doubleToRawLongBits(parseRange("W000000000").orElseThrow()),
            "western zero"
        )
    );
  }

  private static Optional<Double> parseRange(String fieldValue) {
    String source = "prefix" + fieldValue + "suffix";
    int startOffset = "prefix".length();
    return parser.apply(source, startOffset, startOffset + fieldValue.length());
  }
}
