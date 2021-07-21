package org.mitre.tdp.boogie.arinc.v18.field;

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
}
