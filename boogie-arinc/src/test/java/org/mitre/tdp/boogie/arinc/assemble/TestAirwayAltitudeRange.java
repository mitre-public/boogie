package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.Test;

import com.google.common.collect.Range;

class TestAirwayAltitudeRange {

  @Test
  void testAltitudeRanges() {
    assertAll(
        () -> assertEquals(Range.closed(0., 100.), AirwayAltitudeRange.INSTANCE.apply(null, 0., 100.), "null, 0., 100."),
        () -> assertEquals(Range.closed(0., 100.), AirwayAltitudeRange.INSTANCE.apply(0., null, 100.), "0., null, 100."),
        () -> assertEquals(Range.atLeast(100.), AirwayAltitudeRange.INSTANCE.apply(100., null, null), "100., null, null"),
        () -> assertEquals(Range.atLeast(100.), AirwayAltitudeRange.INSTANCE.apply(null, 100., null), "null, 100., null"),
        () -> assertEquals(Range.atMost(100.), AirwayAltitudeRange.INSTANCE.apply(null, null, 100.), "null, null, 100."),
        () -> assertEquals(Range.all(), AirwayAltitudeRange.INSTANCE.apply(null, null, null), "null, null, null"),
        () -> assertEquals(Range.closed(100., 300.), AirwayAltitudeRange.INSTANCE.apply(100., 200., 300.), "100., 200., 300."),
        () -> assertEquals(Range.all(), AirwayAltitudeRange.INSTANCE.apply(200., 300., 100.), "200., 300., 100.")
    );
  }
}
