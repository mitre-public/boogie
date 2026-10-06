package org.mitre.tdp.boogie.projections;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

class RhumbLineParallelTest {
  @Test
  void projectsSharedScizAndXx01BoundaryWithoutLongitudeJump() {
    List<LatLong> samples = assertParallelSamples(-15.0, -120.0, -90.0, 30.0, 10.0);
    assertEquals(174, samples.size());
    assertEquals(-90.14953679817594, samples.get(samples.size() - 1).longitude(), 1e-12);
  }

  @Test
  void projectsSouthernParallelWestbound() {
    assertParallelSamples(-15.0, -90.0, -120.0, -30.0, 10.0);
  }

  @Test
  void projectsNorthernParallelsInBothDirections() {
    assertParallelSamples(15.0, -120.0, -90.0, 30.0, 10.0);
    assertParallelSamples(15.0, -90.0, -120.0, -30.0, 10.0);
    assertParallelSamples(75.0, -120.0, -90.0, 30.0, 10.0);
    assertParallelSamples(75.0, -90.0, -120.0, -30.0, 10.0);
  }

  @Test
  void projectsEquatorInBothDirectionsAndExcludesExactStepEndpoint() {
    List<LatLong> east = assertParallelSamples(0.0, 0.0, 1.0, 1.0, 10.0);
    List<LatLong> west = assertParallelSamples(0.0, 1.0, 0.0, -1.0, 10.0);
    assertEquals(6, east.size());
    assertEquals(6, west.size());
  }

  @Test
  void crossesAntimeridianAlongShorterEastboundPath() {
    assertParallelSamples(-15.0, 170.0, -170.0, 20.0, 10.0);
  }

  @Test
  void crossesAntimeridianAlongShorterWestboundPath() {
    assertParallelSamples(-15.0, -170.0, 170.0, -20.0, 10.0);
  }

  @Test
  void preservesSignedHalfTurnDirection() {
    assertParallelSamples(-15.0, -150.0, 30.0, 180.0, 10.0);
    assertParallelSamples(-15.0, 30.0, -150.0, -180.0, 10.0);
  }

  @Test
  void handlesEndpointsOnEitherAntimeridianRepresentation() {
    assertParallelSamples(-15.0, 180.0, -170.0, 10.0, 10.0);
    assertParallelSamples(-15.0, -180.0, 170.0, -10.0, 10.0);
    assertParallelSamples(-15.0, 170.0, -180.0, 10.0, 10.0);
    assertParallelSamples(-15.0, -170.0, 180.0, -10.0, 10.0);
  }

  @Test
  void respectsCustomSamplingDistance() {
    assertParallelSamples(-15.0, -120.0, -90.0, 30.0, 7.5);
  }

  @Test
  void keepsOnlyStartForCoincidentOrShortParallel() {
    LatLong start = LatLong.of(-15.0, 180.0);
    assertEquals(List.of(start), RhumbLine.project10NM(start, LatLong.of(-15.0, -180.0)));
    assertEquals(List.of(start), RhumbLine.project10NM(start, start));
    assertParallelSamples(-15.0, -120.0, -119.99, 0.01, 10.0);
  }

  private static List<LatLong> assertParallelSamples(double latitude, double startLongitude,
      double endLongitude, double signedLongitudeSpan, double stepNm) {
    LatLong start = LatLong.of(latitude, startLongitude);
    LatLong end = LatLong.of(latitude, endLongitude);
    List<LatLong> samples = RhumbLine.project(start, end, stepNm);
    // A degree of longitude along a parallel spans 60 * cos(latitude) nautical miles
    // under the angular-distance convention used by the rhumb projection API.
    double nmPerLongitudeDegree = 60.0 * Math.cos(Math.toRadians(latitude));
    double totalNm = Math.abs(signedLongitudeSpan) * nmPerLongitudeDegree;
    double expectedLongitudeStep = Math.copySign(stepNm / nmPerLongitudeDegree, signedLongitudeSpan);
    assertEquals((int) Math.ceil(totalNm / stepNm - 1e-12), samples.size());
    assertSame(start, samples.get(0));
    double traveledLongitude = 0.0;
    for (int i = 0; i < samples.size(); i++) {
      LatLong sample = samples.get(i);
      String description = "latitude=" + latitude + ", start=" + startLongitude
          + ", end=" + endLongitude + ", sample=" + i;
      assertEquals(latitude, sample.latitude(), 0.0, description);
      assertTrue(Double.isFinite(sample.longitude()), description);
      assertTrue(sample.longitude() >= -180.0 && sample.longitude() <= 180.0, description);
      if (i > 0) {
        double longitudeStep = Math.IEEEremainder(sample.longitude() - samples.get(i - 1).longitude(), 360.0);
        assertTrue(longitudeStep * signedLongitudeSpan > 0.0, description);
        assertEquals(expectedLongitudeStep, longitudeStep, 1e-10, description);
        traveledLongitude += longitudeStep;
      }
      assertEquals(i * stepNm, Math.abs(traveledLongitude) * nmPerLongitudeDegree, 1e-8, description);
      assertTrue(Math.abs(traveledLongitude) < Math.abs(signedLongitudeSpan), description);
    }
    assertTrue(totalNm - (samples.size() - 1) * stepNm <= stepNm + 1e-8);
    return samples;
  }
}
