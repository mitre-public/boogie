package org.mitre.tdp.boogie.projections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mitre.caasd.commons.LatLong;

class RhumbLineBoundaryTest {
  private static final double COORDINATE_TOLERANCE = 1e-12;

  @ParameterizedTest
  @CsvSource({
      "-90.0, 75.0, -89.0, -10.0, -89.66666666666667, -89.33333333333333",
      "-89.0, -10.0, -90.0, 75.0, -89.33333333333333, -89.66666666666667",
      "90.0, 75.0, 89.0, -10.0, 89.66666666666667, 89.33333333333333",
      "89.0, -10.0, 90.0, 75.0, 89.33333333333333, 89.66666666666667"
  })
  void polarMeridianUsesNonpolarLongitudeAndKeepsPublishedStart(double startLatitude, double startLongitude, double endLatitude, double endLongitude, double firstLatitude, double secondLatitude) {
    LatLong start = LatLong.of(startLatitude, startLongitude);
    LatLong end = LatLong.of(endLatitude, endLongitude);

    List<LatLong> points = RhumbLine.project(start, end, 20.0);

    assertEquals(3, points.size());
    assertSame(start, points.get(0));
    assertPosition(points.get(1), firstLatitude, -10.0);
    assertPosition(points.get(2), secondLatitude, -10.0);
    assertFalse(points.contains(end));
  }

  @ParameterizedTest
  @CsvSource({"-90.0", "90.0"})
  void differentLongitudeLabelsAtSamePoleContributeOnlyThePublishedStart(double latitude) {
    LatLong start = LatLong.of(latitude, 75.0);
    LatLong end = LatLong.of(latitude, -10.0);

    List<LatLong> points = RhumbLine.project(start, end, 10.0);

    assertEquals(List.of(start), points);
    assertSame(start, points.get(0));
  }

  @Test
  void zeroLengthNonpolarLegContributesOnlyThePublishedStart() {
    LatLong start = LatLong.of(24.0, -25.0);
    assertEquals(List.of(start), RhumbLine.project(start, start, 10.0));
  }

  @ParameterizedTest
  @CsvSource({"-180.0, 180.0", "180.0, -180.0"})
  void dateLineLongitudeLabelsDefineTheSameMeridian(double startLongitude, double endLongitude) {
    LatLong start = LatLong.of(24.0, startLongitude);
    List<LatLong> points = RhumbLine.project(start, LatLong.of(30.0, endLongitude), 60.0);

    assertEquals(6, points.size());
    assertSame(start, points.get(0));
    for (int i = 0; i < points.size(); i++) {
      assertPosition(points.get(i), 24.0 + i, startLongitude);
    }
  }

  @Test
  void oppositePolesOnOneMeridianHaveFiniteInteriorSamples() {
    LatLong start = LatLong.of(-90.0, 75.0);
    List<LatLong> points = RhumbLine.project(start, LatLong.of(90.0, 75.0), 3600.0);

    assertEquals(3, points.size());
    assertSame(start, points.get(0));
    assertPosition(points.get(1), -30.0, 75.0);
    assertPosition(points.get(2), 30.0, 75.0);
  }

  @ParameterizedTest
  @CsvSource({"-90.0, 90.0", "90.0, -90.0"})
  void oppositePolesWithDifferentMeridiansAreAmbiguous(double startLatitude, double endLatitude) {
    assertThrows(IllegalArgumentException.class, () -> RhumbLine.project(
        LatLong.of(startLatitude, 75.0), LatLong.of(endLatitude, -10.0), 3600.0));
  }

  @ParameterizedTest
  @CsvSource({
      "24.0, 30.0, -25.0, 36, 29.833333333333332",
      "3.5, -30.0, -120.0, 201, -29.833333333333332"
  })
  void publishedGcccAndNtttMeridiansExcludeTheirEndpoints(double startLatitude, double endLatitude,
      double longitude, int expectedCount, double lastLatitude) {
    LatLong start = LatLong.of(startLatitude, longitude);
    LatLong end = LatLong.of(endLatitude, longitude);
    List<LatLong> points = RhumbLine.project10NM(start, end);

    assertEquals(expectedCount, points.size());
    assertSame(start, points.get(0));
    assertPosition(points.get(points.size() - 1), lastLatitude, longitude);
    for (int i = 1; i < points.size(); i++) {
      assertTrue(points.get(i).latitude() > Math.min(startLatitude, endLatitude));
      assertTrue(points.get(i).latitude() < Math.max(startLatitude, endLatitude));
      assertEquals(longitude, points.get(i).longitude());
    }
  }

  @Test
  void fractionalStepsExcludeEndpointDespiteLatitudeSubtractionRoundoff() {
    LatLong start = LatLong.of(12.0, 20.0);
    LatLong end = LatLong.of(12.05, 20.0);
    List<LatLong> points = RhumbLine.project(start, end, 0.1);

    assertEquals(30, points.size());
    assertSame(start, points.get(0));
    for (int i = 0; i < points.size(); i++) {
      assertPosition(points.get(i), 12.0 + i / 600.0, 20.0);
      assertTrue(points.get(i).latitude() < end.latitude());
    }
  }

  @Test
  void retainsGenuineInteriorSampleVeryCloseToEndpoint() {
    LatLong start = LatLong.of(0.0, 20.0);
    LatLong end = LatLong.of(1.0 / 6.0 + 1e-8, 20.0);
    List<LatLong> points = RhumbLine.project(start, end, 10.0);

    assertEquals(2, points.size());
    assertSame(start, points.get(0));
    assertPosition(points.get(1), 1.0 / 6.0, 20.0);
    assertTrue(points.get(1).latitude() < end.latitude());
  }

  @Test
  void equatorialRhumbCrossesDateLineAlongShortRoute() {
    LatLong start = LatLong.of(0.0, 170.0);
    List<LatLong> points = RhumbLine.project(start, LatLong.of(0.0, -170.0), 60.0);

    assertEquals(20, points.size());
    assertSame(start, points.get(0));
    for (int i = 0; i < points.size(); i++) {
      assertPosition(points.get(i), 0.0, 170.0 + i);
    }
  }

  @Test
  void ordinaryObliqueRhumbStillMovesAlongBothCoordinates() {
    LatLong start = LatLong.of(10.0, 20.0);
    List<LatLong> points = RhumbLine.project(start, LatLong.of(11.0, 21.0), 20.0);

    assertEquals(5, points.size());
    assertSame(start, points.get(0));
    for (int i = 1; i < points.size(); i++) {
      assertTrue(points.get(i).latitude() > points.get(i - 1).latitude());
      assertTrue(points.get(i).longitude() > points.get(i - 1).longitude());
      assertTrue(points.get(i).latitude() < 11.0);
      assertTrue(points.get(i).longitude() < 21.0);
    }
  }

  private static void assertPosition(LatLong point, double latitude, double longitude) {
    assertEquals(latitude, point.latitude(), COORDINATE_TOLERANCE);
    assertEquals(0.0, Math.IEEEremainder(point.longitude() - longitude, 360.0), COORDINATE_TOLERANCE);
  }
}
