package org.mitre.tdp.boogie.projections;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

class CircleTest {
  static LatLong center = LatLong.of(37.3383, -52.5083);
  static double radius = 143.1;

  @Test
  void project10Deg() {
    List<LatLong> points = Circle.project10Deg(radius, center);
    List<Double> deltas = points.stream()
        .map(i -> i.distanceInNM(center))
        .map(i -> Math.abs(143.1 - i))
        .filter(i -> i < .1)
        .collect(Collectors.toList());
    assertAll(
        () -> assertEquals(36, points.size()),
        () -> assertEquals(36, deltas.size())
    );
  }
}