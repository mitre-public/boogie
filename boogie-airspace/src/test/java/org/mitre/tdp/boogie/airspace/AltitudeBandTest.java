package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;

class AltitudeBandTest {

  @Test
  void unknownAndExplicitlyUnlimitedHaveDifferentMembership() {
    AltitudeBand unknown = AltitudeBand.unknown();
    AltitudeBand unlimited = AltitudeBand.unbounded();

    assertAll(
        () -> assertEquals(AltitudeBand.Status.UNKNOWN, unknown.status()),
        () -> assertEquals(AltitudeBand.Status.UNBOUNDED, unlimited.status()),
        () -> assertNotEquals(unknown, unlimited),
        () -> assertFalse(unknown.contains(1000.0)),
        () -> assertTrue(unknown.clip(1000.0, 2000.0).isEmpty()),
        () -> assertTrue(unlimited.contains(1000.0)),
        () -> assertInterval(unlimited, 1000.0, 2000.0, 0.0, 1.0));
  }

  @Test
  void nullableLimitsLeaveOneMissingSideUnlimited() {
    AltitudeBand lowerOnly = AltitudeBand.of(1000.0, null);
    assertAll(
        () -> assertEquals(AltitudeBand.atLeast(1000.0), lowerOnly),
        () -> assertTrue(lowerOnly.contains(100000.0)),
        () -> assertFalse(lowerOnly.contains(999.0)),
        () -> assertInterval(lowerOnly, 0.0, 4000.0, 0.25, 1.0));

    AltitudeBand upperOnly = AltitudeBand.of(null, 1000.0);
    assertAll(
        () -> assertEquals(AltitudeBand.atMost(1000.0), upperOnly),
        () -> assertTrue(upperOnly.contains(-1000.0)),
        () -> assertFalse(upperOnly.contains(1001.0)),
        () -> assertInterval(upperOnly, 4000.0, 0.0, 0.75, 1.0));
  }

  @Test
  void nullableLimitsResolveBothPresentAndLeaveBothMissingUnknown() {
    AltitudeBand bounded = AltitudeBand.of(1000.0, 2000.0);
    assertAll(
        () -> assertEquals(AltitudeBand.closed(1000.0, 2000.0), bounded),
        () -> assertInterval(bounded, 0.0, 4000.0, 0.25, 0.5));

    AltitudeBand missing = AltitudeBand.of(null, null);
    assertAll(
        () -> assertEquals(AltitudeBand.unknown(), missing),
        () -> assertNotEquals(AltitudeBand.unbounded(), missing),
        () -> assertFalse(missing.contains(1000.0)),
        () -> assertTrue(missing.clip(0.0, 4000.0).isEmpty()));
  }

  @Test
  void clipsClimbAndDescentInTrajectoryOrder() {
    AltitudeBand band = AltitudeBand.closed(1000.0, 2000.0);
    assertAll(
        () -> assertInterval(band, 0.0, 4000.0, 0.25, 0.5),
        () -> assertInterval(band, 4000.0, 0.0, 0.5, 0.75),
        () -> assertTrue(band.clip(2100.0, 3000.0).isEmpty()),
        () -> assertTrue(band.clip(900.0, 0.0).isEmpty()),
        () -> assertInterval(band, 1200.0, 1800.0, 0.0, 1.0));
  }

  @Test
  void clipsOneSidedLimits() {
    AltitudeBand lowerBounded = AltitudeBand.atLeast(1000.0);
    AltitudeBand upperBounded = AltitudeBand.atMost(1000.0);
    assertAll(
        () -> assertInterval(lowerBounded, 0.0, 4000.0, 0.25, 1.0),
        () -> assertInterval(upperBounded, 4000.0, 0.0, 0.75, 1.0),
        () -> assertEquals(AltitudeBand.Status.RESOLVED, lowerBounded.status()),
        () -> assertEquals(1000.0, lowerBounded.lowerFeet().orElseThrow()),
        () -> assertTrue(lowerBounded.upperFeet().isEmpty()),
        () -> assertTrue(upperBounded.lowerFeet().isEmpty()),
        () -> assertEquals(1000.0, upperBounded.upperFeet().orElseThrow()));
  }

  @Test
  void includesBoundaryTouchesAndConstantAltitude() {
    AltitudeBand band = AltitudeBand.closed(1000.0, 2000.0);
    assertAll(
        () -> assertTrue(band.contains(1000.0)),
        () -> assertTrue(band.contains(2000.0)),
        () -> assertInterval(band, 1000.0, 1000.0, 0.0, 1.0),
        () -> assertInterval(band, 2000.0, 2000.0, 0.0, 1.0),
        () -> assertInterval(band, 0.0, 1000.0, 1.0, 1.0),
        () -> assertInterval(band, 1000.0, 0.0, 0.0, 0.0),
        () -> assertInterval(AltitudeBand.closed(1500.0, 1500.0), 0.0, 3000.0, 0.5, 0.5),
        () -> assertTrue(band.clip(999.0, 999.0).isEmpty()));
  }

  @Test
  void supportsNegativeAltitudesAndFiniteExtremes() {
    assertAll(
        () -> assertInterval(AltitudeBand.closed(-1000.0, 0.0), -2000.0, 2000.0, 0.25, 0.5),
        () -> assertInterval(AltitudeBand.atLeast(0.0), -Double.MAX_VALUE, Double.MAX_VALUE, 0.5, 1.0),
        () -> assertInterval(AltitudeBand.unbounded(), Double.MAX_VALUE, -Double.MAX_VALUE, 0.0, 1.0));
  }

  @Test
  void rejectsUnresolvedNumbersAndReversedBounds() {
    assertAll(
        () -> assertThrows(IllegalArgumentException.class, () -> AltitudeBand.closed(2000.0, 1000.0)),
        () -> assertThrows(IllegalArgumentException.class, () -> AltitudeBand.of(2000.0, 1000.0)));
    for (double invalid : new double[] {Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY}) {
      assertAll("Invalid altitude: " + invalid,
          () -> assertThrows(IllegalArgumentException.class, () -> AltitudeBand.closed(invalid, 2000.0)),
          () -> assertThrows(IllegalArgumentException.class, () -> AltitudeBand.closed(1000.0, invalid)),
          () -> assertThrows(IllegalArgumentException.class, () -> AltitudeBand.atLeast(invalid)),
          () -> assertThrows(IllegalArgumentException.class, () -> AltitudeBand.atMost(invalid)),
          () -> assertThrows(IllegalArgumentException.class, () -> AltitudeBand.of(invalid, null)),
          () -> assertThrows(IllegalArgumentException.class, () -> AltitudeBand.of(null, invalid)),
          () -> assertThrows(IllegalArgumentException.class, () -> AltitudeBand.unbounded().contains(invalid)),
          () -> assertThrows(IllegalArgumentException.class, () -> AltitudeBand.unknown().clip(invalid, 1000.0)),
          () -> assertThrows(IllegalArgumentException.class, () -> AltitudeBand.unbounded().clip(1000.0, invalid)));
    }
  }

  private static void assertInterval(AltitudeBand band, double startFeet, double endFeet, double startFraction, double endFraction) {
    AltitudeBand.Interval interval = band.clip(startFeet, endFeet).orElseThrow();
    assertAll(
        () -> assertEquals(startFraction, interval.startFraction(), 1e-12),
        () -> assertEquals(endFraction, interval.endFraction(), 1e-12));
  }
}
