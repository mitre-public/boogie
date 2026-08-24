package org.mitre.tdp.boogie.arinc.v18.field;

import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Optional;

import org.junit.jupiter.api.Test;

class TestStationDeclination {

  private static final StationDeclination parser = new StationDeclination();

  @Test
  void testEastValidVariationIsPositive() {
    assertEquals(Optional.of(14.), parser.apply("E0140"));
  }

  @Test
  void testWestValidVariationIsNegative() {
    assertEquals(Optional.of(-14.), parser.apply("W0140"));
  }

  @Test
  void testNonZeroTrueVariationsAreSkipped() {
    assertEquals(Optional.empty(), parser.apply("T0140"));
  }

  @Test
  void testTrueNorthReferenceIsRetainedAsZero() {
    assertEquals(Optional.of(0.0), parser.apply("T0000"));
  }

  @Test
  void testGridVariationsAreSkipped() {
    assertEquals(Optional.empty(), parser.apply("G0140"));
  }
}
