package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestRunwayMagneticBearing {

  private static final RunwayMagneticBearing parser = new RunwayMagneticBearing();

  @Test
  void testParseValidBearing() {
    assertEquals(Optional.of(123.5), parser.apply("1235"));
  }

  @Test
  void testParseTrueBearing() {
    assertEquals(Optional.empty(), parser.apply("123T"));
  }

  @Test
  void testParseInvalidBearing() {
    assertEquals(Optional.empty(), parser.apply("123A"));
  }
}
