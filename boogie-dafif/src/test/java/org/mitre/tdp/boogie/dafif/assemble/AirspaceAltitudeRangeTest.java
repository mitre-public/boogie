package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;

import com.google.common.collect.Range;

class AirspaceAltitudeRangeTest {

  @Test
  void convertsMslAndFlightLevelLimits() {
    assertEquals(Range.closed(195.0, 18000.0), AirspaceAltitudeRange.INSTANCE.apply("00195AMSL", "FL180"));
    assertEquals(Range.closed(18000.0, 60000.0), AirspaceAltitudeRange.INSTANCE.apply("FL180", "060000AMSL"));
    assertEquals(Range.atLeast(24000.0), AirspaceAltitudeRange.INSTANCE.apply("FL240", "UNLTD"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"GND", "SURFACE", "00500AGL", "U", "BY NOTAM"})
  void doesNotTreatTerrainRelativeOrUnknownLowerLimitsAsMsl(String lower) {
    assertEquals(Range.atMost(18000.0), AirspaceAltitudeRange.INSTANCE.apply(lower, "FL180"));
  }

  @ParameterizedTest
  @ValueSource(strings = {"000500AGL", "UNLTD", "U", "BY NOTAM"})
  void leavesUnconvertibleUpperLimitsUnbounded(String upper) {
    assertEquals(Range.atLeast(1000.0), AirspaceAltitudeRange.INSTANCE.apply("01000AMSL", upper));
    assertEquals(Range.all(), AirspaceAltitudeRange.INSTANCE.apply("SURFACE", upper));
  }

  @Test
  void rejectsMalformedAndReversedLimits() {
    assertThrows(IllegalArgumentException.class, () -> AirspaceAltitudeRange.INSTANCE.apply("bad altitude", "FL180"));
    assertThrows(IllegalArgumentException.class, () -> AirspaceAltitudeRange.INSTANCE.apply("FL240", "FL180"));
  }
}
