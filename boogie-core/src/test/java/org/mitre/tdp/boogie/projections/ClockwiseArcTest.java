package org.mitre.tdp.boogie.projections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.IOException;
import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

class ClockwiseArcTest {
  static LatLong start = LatLong.of(37.337500000000006000, -49.516666666666666000);
  static LatLong center = LatLong.of(37.338333333333340000, -52.508333333333330000);
  static LatLong end = LatLong.of(37.337500000000006000, -55.500000000000000000);
  static Double startAngle = center.courseInDegrees(start);
  static Double endAngle = center.courseInDegrees(end);
  static Double diff = endAngle - startAngle;

  static LatLong pointOnArc = LatLong.of(36.925220453464220000, -49.570170127695405000);

  @Test
  void endIsSmaller() {
    List<LatLong> points = ClockwiseArc.project10Deg(pointOnArc, center, start);
    List<Double> deltas = points.stream()
        .map(i -> i.distanceInNM(center))
        .map(i -> Math.abs(142.7 - i))
        .filter(i -> i < .1)
        .collect(Collectors.toList());
    assertAll(
        () -> assertEquals(36, points.size()),
        () -> assertEquals(36, deltas.size())
    );
  }

  @Test
  void project10Deg() throws IOException {
    List<LatLong> points = ClockwiseArc.project10Deg(start, center, end);
     assertAll(
          () -> assertEquals(19, points.size(), "just enough error in coordinate to create a point short of the next leg")
     );
  }
}