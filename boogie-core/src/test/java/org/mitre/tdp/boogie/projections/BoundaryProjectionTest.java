package org.mitre.tdp.boogie.projections;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Geometry;

class BoundaryProjectionTest {
  @Test
  void greatCircleUsesNauticalMileStepsAndExcludesEnd() {
    LatLong start = LatLong.of(0.0, 170.0);
    LatLong end = start.projectOut(90.0, 35.0);
    List<LatLong> points = GreatCircle.project(start, end, 12.0);
    assertEquals(3, points.size());
    assertAll(
        () -> assertSame(start, points.get(0)),
        () -> assertEquals(12.0, start.distanceInNM(points.get(1)), 1e-6),
        () -> assertEquals(24.0, start.distanceInNM(points.get(2)), 1e-6),
        () -> assertFalse(points.contains(end)),
        () -> assertEquals(List.of(start), GreatCircle.project(start, end, 40.0)),
        () -> assertEquals(List.of(start), GreatCircle.project(start, start, 12.0))
    );
  }

  @Test
  void circleUsesDegreeStepsWithoutRepeatingFirstVertex() {
    LatLong center = LatLong.of(75.0, 179.0);
    List<LatLong> points = Circle.project(150.0, center, 45.0);
    assertEquals(8, points.size());
    assertAll(
        () -> assertEquals(points.size(), points.stream().distinct().count()),
        () -> assertFalse(points.get(0).equals(points.get(points.size() - 1))),
        () -> assertEquals(16, Circle.project(150.0, center, 22.5).size())
    );
    for (int i = 0; i < points.size(); i++) {
      assertEquals(0.0, center.projectOut(i * 45.0, 150.0).distanceInNM(points.get(i)), 1e-6);
    }
  }

  @Test
  void arcsFollowTheirDirectionAcrossNorthAndExcludeEnd() {
    LatLong center = LatLong.of(45.0, 179.0);
    LatLong westOfNorth = center.projectOut(350.0, 120.0);
    LatLong eastOfNorth = center.projectOut(10.0, 120.0);
    List<LatLong> clockwise = ClockwiseArc.project(westOfNorth, center, eastOfNorth, 7.0);
    List<LatLong> counterclockwise = CounterClockwiseArc.project(eastOfNorth, center, westOfNorth, 7.0);
    assertAll(
        () -> assertEquals(3, clockwise.size()),
        () -> assertEquals(3, counterclockwise.size())
    );
    assertAll(
        () -> assertSame(westOfNorth, clockwise.get(0)),
        () -> assertSame(eastOfNorth, counterclockwise.get(0)),
        () -> assertEquals(0.0, center.projectOut(357.0, 120.0).distanceInNM(clockwise.get(1)), 1e-6),
        () -> assertEquals(0.0, center.projectOut(4.0, 120.0).distanceInNM(clockwise.get(2)), 1e-6),
        () -> assertEquals(0.0, center.projectOut(3.0, 120.0).distanceInNM(counterclockwise.get(1)), 1e-6),
        () -> assertEquals(0.0, center.projectOut(356.0, 120.0).distanceInNM(counterclockwise.get(2)), 1e-6),
        () -> assertFalse(clockwise.contains(eastOfNorth)),
        () -> assertFalse(counterclockwise.contains(westOfNorth)),
        () -> assertEquals(4, CounterClockwiseArc.project(westOfNorth, center, eastOfNorth, 90.0).size()),
        () -> assertEquals(4, ClockwiseArc.project(eastOfNorth, center, westOfNorth, 90.0).size())
    );
  }

  @Test
  void rhumbLineUsesNauticalMileStepsAcrossDateLine() {
    LatLong start = LatLong.of(80.0, 170.0);
    LatLong end = LatLong.of(80.0, -170.0);
    List<LatLong> points = RhumbLine.project(start, end, 50.0);
    assertEquals(5, points.size());
    assertAll(
        () -> assertSame(start, points.get(0)),
        () -> assertFalse(points.contains(end)),
        () -> assertTrue(points.stream().allMatch(point -> Math.abs(point.longitude()) >= 170.0)),
        () -> assertTrue(points.stream().anyMatch(point -> point.longitude() < 0.0)),
        () -> assertEquals(List.of(start), RhumbLine.project(start, end, 250.0)),
        () -> assertEquals(List.of(start), RhumbLine.project(start, start, 50.0))
    );
    for (LatLong point : points) {
      assertEquals(80.0, point.latitude(), 1e-9);
    }
  }

  @Test
  void dispatcherUsesCurrentGeometryAndSeparateLineAndArcSteps() {
    LatLong center = LatLong.of(20.0, 30.0);
    LatLong start = center.projectOut(0.0, 100.0);
    LatLong end = center.projectOut(90.0, 100.0);
    AirspaceSequence next = sequence(Geometry.RHUMB_LINE, end);
    AirspaceSequence clockwise = AirspaceSequence.builder(Geometry.CLOCKWISE_ARC, 0)
        .associatedFix(start).centerFix(center).arcRadius(999.0).arcBearing(200.0).build();
    AirspaceSequence counterclockwise = AirspaceSequence.builder(Geometry.COUNTER_CLOCKWISE_ARC, 0)
        .associatedFix(start).centerFix(center).build();
    AirspaceSequence circle = AirspaceSequence.builder(Geometry.CIRCLE, 0).centerFix(center).arcRadius(100.0).build();
    assertAll(
        () -> assertEquals(GreatCircle.project(start, end, 12.0),
            BoundaryProjection.project(sequence(Geometry.GREAT_CIRCLE, start), next, 12.0, 7.0)),
        () -> assertEquals(RhumbLine.project(start, end, 12.0),
            BoundaryProjection.project(sequence(Geometry.RHUMB_LINE, start), next, 12.0, 7.0)),
        () -> assertEquals(ClockwiseArc.project(start, center, end, 7.0),
            BoundaryProjection.project(clockwise, next, 12.0, 7.0)),
        () -> assertEquals(CounterClockwiseArc.project(start, center, end, 7.0),
            BoundaryProjection.project(counterclockwise, next, 12.0, 7.0)),
        () -> assertEquals(Circle.project(100.0, center, 7.0),
            BoundaryProjection.project(circle, null, 12.0, 7.0))
    );
  }

  @Test
  void tenUnitMethodsMatchExplicitSteps() {
    LatLong center = LatLong.of(20.0, 30.0);
    LatLong start = center.projectOut(350.0, 100.0);
    LatLong end = center.projectOut(15.0, 100.0);
    assertAll(
        () -> assertEquals(Circle.project(100.0, center, 10.0), Circle.project10Deg(100.0, center)),
        () -> assertEquals(ClockwiseArc.project(start, center, end, 10.0), ClockwiseArc.project10Deg(start, center, end)),
        () -> assertEquals(CounterClockwiseArc.project(start, center, end, 10.0), CounterClockwiseArc.project10Deg(start, center, end)),
        () -> assertEquals(GreatCircle.project(start, end, 10.0), GreatCircle.project10NM(start, end)),
        () -> assertEquals(RhumbLine.project(start, end, 10.0), RhumbLine.project10NM(start, end))
    );
  }

  @Test
  void rejectsNonpositiveAndNonfiniteSteps() {
    LatLong center = LatLong.of(20.0, 30.0);
    LatLong start = center.projectOut(0.0, 100.0);
    LatLong end = center.projectOut(90.0, 100.0);
    for (double step : new double[] {0.0, -1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
      assertAll("Step " + step,
          () -> assertThrows(IllegalArgumentException.class, () -> Circle.project(100.0, center, step)),
          () -> assertThrows(IllegalArgumentException.class, () -> ClockwiseArc.project(start, center, end, step)),
          () -> assertThrows(IllegalArgumentException.class, () -> CounterClockwiseArc.project(start, center, end, step)),
          () -> assertThrows(IllegalArgumentException.class, () -> GreatCircle.project(start, end, step)),
          () -> assertThrows(IllegalArgumentException.class, () -> RhumbLine.project(start, end, step))
      );
    }
  }

  private static AirspaceSequence sequence(Geometry geometry, LatLong fix) {
    return AirspaceSequence.builder(geometry, 0).associatedFix(fix).build();
  }
}
