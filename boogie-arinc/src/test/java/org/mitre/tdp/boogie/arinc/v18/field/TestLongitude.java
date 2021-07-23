package org.mitre.tdp.boogie.arinc.v18.field;

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
}
