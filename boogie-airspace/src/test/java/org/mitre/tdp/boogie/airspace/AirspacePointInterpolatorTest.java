package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Instant;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

class AirspacePointInterpolatorTest {
  private static final Instant TIME = Instant.parse("2026-09-28T12:00:00Z");
  private static final AirspacePointInterpolator INTERPOLATOR = AirspacePointInterpolator.INSTANCE;

  @Test
  void followsTheShortestGreatCircleAcrossTheDateLine() {
    AirspacePoint start = point(TIME, 60.0, 170.0, 1000.0);
    AirspacePoint end = point(TIME.plusSeconds(120), 60.0, -170.0, 3000.0);

    AirspacePoint midpoint = INTERPOLATOR.apply(start, end, 0.5);

    assertAll(
        () -> assertEquals(180.0, Math.abs(midpoint.position().longitude()), 1e-10),
        () -> assertTrue(midpoint.position().latitude() > 60.0, "The great-circle leg curves toward the pole"),
        () -> assertEquals(start.position().distanceInNM(end.position()) / 2.0,
            start.position().distanceInNM(midpoint.position()), 1e-9),
        () -> assertEquals(TIME.plusSeconds(60), midpoint.time()),
        () -> assertEquals(2000.0, midpoint.altitudeFeet()));
  }

  @Test
  void exactEndpointsRetainTheirOriginalObservationsAndKnownAltitudes() {
    AirspacePoint start = point(TIME, 0.0, 0.0, 1000.0);
    AirspacePoint missingEnd = point(TIME.plusSeconds(10), 0.0, 1.0, null);
    AirspacePoint missingStart = point(TIME, 0.0, 0.0, null);
    AirspacePoint end = point(TIME.plusSeconds(10), 0.0, 1.0, 3000.0);

    assertAll(
        () -> assertSame(start, INTERPOLATOR.apply(start, missingEnd, 0.0)),
        () -> assertSame(missingEnd, INTERPOLATOR.apply(start, missingEnd, 1.0)),
        () -> assertSame(missingStart, INTERPOLATOR.apply(missingStart, end, 0.0)),
        () -> assertSame(end, INTERPOLATOR.apply(missingStart, end, 1.0)));
  }

  @Test
  void interpolatesTimeAndAltitudeAtAStationaryPosition() {
    AirspacePoint start = point(TIME, 40.0, -75.0, 1000.0);
    AirspacePoint end = point(TIME.plusSeconds(9), 40.0, -75.0, 3000.0);

    AirspacePoint quarter = INTERPOLATOR.apply(start, end, 0.25);

    assertAll(
        () -> assertSame(start.position(), quarter.position()),
        () -> assertEquals(TIME.plusMillis(2250), quarter.time()),
        () -> assertEquals(1500.0, quarter.altitudeFeet()));
  }

  @Test
  void leavesInteriorAltitudeUnknownWhenEitherObservationLacksAltitude() {
    AirspacePoint start = point(TIME, 0.0, 0.0, 1000.0);
    AirspacePoint missingStart = point(TIME, 0.0, 0.0, null);
    AirspacePoint end = point(TIME.plusSeconds(10), 0.0, 1.0, 3000.0);
    AirspacePoint missingEnd = point(TIME.plusSeconds(10), 0.0, 1.0, null);

    assertAll(
        () -> assertNull(INTERPOLATOR.apply(start, missingEnd, 0.5).altitudeFeet()),
        () -> assertNull(INTERPOLATOR.apply(missingStart, end, 0.5).altitudeFeet()),
        () -> assertNull(INTERPOLATOR.apply(missingStart, missingEnd, 0.5).altitudeFeet()));
  }

  @Test
  void interpolatesFiniteAltitudesWithoutOverflowingTheirDifference() {
    AirspacePoint start = point(TIME, 0.0, 0.0, -Double.MAX_VALUE);
    AirspacePoint end = point(TIME.plusSeconds(10), 0.0, 1.0, Double.MAX_VALUE);

    assertAll(
        () -> assertEquals(0.0, INTERPOLATOR.apply(start, end, 0.5).altitudeFeet()),
        () -> assertEquals(0.0, INTERPOLATOR.apply(end, start, 0.5).altitudeFeet()));
  }

  @Test
  void rejectsAntipodalEndpointsAndInvalidFractions() {
    AirspacePoint start = point(TIME, 0.0, 0.0, null);
    AirspacePoint end = point(TIME.plusSeconds(10), 0.0, 1.0, null);
    AirspacePoint antipode = point(TIME.plusSeconds(10), 0.0, 180.0, null);

    assertAll(
        () -> assertThrows(IllegalArgumentException.class, () -> INTERPOLATOR.apply(start, antipode, 0.5)),
        () -> assertThrows(IllegalArgumentException.class, () -> INTERPOLATOR.apply(start, antipode, 0.0)),
        () -> assertThrows(IllegalArgumentException.class, () -> INTERPOLATOR.apply(start, end, -0.1)),
        () -> assertThrows(IllegalArgumentException.class, () -> INTERPOLATOR.apply(start, end, 1.1)),
        () -> assertThrows(IllegalArgumentException.class, () -> INTERPOLATOR.apply(start, end, Double.NaN)),
        () -> assertThrows(IllegalArgumentException.class, () -> INTERPOLATOR.apply(start, end, Double.POSITIVE_INFINITY)),
        () -> assertThrows(IllegalArgumentException.class, () -> INTERPOLATOR.apply(start, end, Double.NEGATIVE_INFINITY)));
  }

  @Test
  void rejectsNonfiniteAltitudeEvenWhenTheOtherObservationLacksAltitude() {
    AirspacePoint missing = point(TIME, 0.0, 0.0, null);
    for (double altitude : new double[] {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
      AirspacePoint invalid = point(TIME.plusSeconds(10), 0.0, 1.0, altitude);
      assertAll("Invalid altitude: " + altitude,
          () -> assertThrows(IllegalArgumentException.class, () -> INTERPOLATOR.apply(missing, invalid, 0.5)),
          () -> assertThrows(IllegalArgumentException.class, () -> INTERPOLATOR.apply(invalid, missing, 0.5)),
          () -> assertThrows(IllegalArgumentException.class, () -> INTERPOLATOR.apply(missing, invalid, 0.0)),
          () -> assertThrows(IllegalArgumentException.class, () -> INTERPOLATOR.apply(invalid, missing, 1.0)));
    }
  }

  private static AirspacePoint point(Instant time, double latitude, double longitude, Double altitude) {
    return new AirspacePoint(time, LatLong.of(latitude, longitude), altitude);
  }
}
