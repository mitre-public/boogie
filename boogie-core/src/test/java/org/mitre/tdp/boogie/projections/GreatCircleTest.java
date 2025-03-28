package org.mitre.tdp.boogie.projections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

class GreatCircleTest {

  static LatLong start = LatLong.of(37.337500000000006000, -49.516666666666666000);
  static LatLong end = LatLong.of(37.337500000000006000, -55.500000000000000000);
  static Double course = start.courseInDegrees(end);
  static Double expectedPoints = start.distanceInNM(end) / 10;

  @Test
  void project10NM() {
    List<LatLong> points = GreatCircle.project10NM(start, end);
    List<Double> deltas = points.stream()
        .skip(1)
        .map(i -> start.courseInDegrees(i))
        .map(i -> Math.abs(course - i))
        .filter(i -> i < .00001)
        .collect(Collectors.toList());
    assertAll(
        () -> assertEquals(expectedPoints.intValue() + 1, points.size(), "the number of points plus the fix itself"),
        () -> assertEquals(28, deltas.size(), "don't measure the first one")
    );
  }
}