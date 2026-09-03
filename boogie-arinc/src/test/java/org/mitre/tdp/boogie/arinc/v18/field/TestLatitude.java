package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestLatitude {

  private static final Latitude parser = new Latitude();

  @Test
  void testParseValidNorthernLatitude() {
    assertEquals(39.86078055, parser.apply("N39513881").orElseThrow(AssertionError::new), 0.000001);
  }

  @Test
  void testParseValidSouthernLatitude() {
    assertEquals(-39.86078055, parser.apply("S39513881").orElseThrow(AssertionError::new), 0.000001);
  }

  @Test
  void testExceptionOnInvalidLatitude() {
    assertEquals(Optional.empty(), parser.apply("SA9513881"));
  }

  @Test
  void testRangeParsing() {
    assertAll(
        () -> assertEquals(39.86078055, parseRange("N39513881").orElseThrow(), 0.000001),
        () -> assertEquals(-39.86078055, parseRange("S39513881").orElseThrow(), 0.000001),
        () -> assertEquals(Optional.empty(), parseRange("         ")),
        () -> assertEquals(Optional.empty(), parseRange("SA9513881")),
        () -> assertEquals(Optional.empty(), parseRange("X39513881")),
        () -> assertEquals(
            Double.doubleToRawLongBits(-0.0),
            Double.doubleToRawLongBits(parseRange("S00000000").orElseThrow()),
            "southern zero"
        )
    );
  }

  private static Optional<Double> parseRange(String fieldValue) {
    String source = "prefix" + fieldValue + "suffix";
    int startOffset = "prefix".length();
    return parser.parse(source, startOffset, startOffset + fieldValue.length());
  }
}
