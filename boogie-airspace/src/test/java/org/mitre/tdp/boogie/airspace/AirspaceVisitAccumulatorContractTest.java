package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;

class AirspaceVisitAccumulatorContractTest {
  private static final Instant START = Instant.parse("2025-01-01T00:00:00Z");

  @Test
  void mergesTouchingSpansAndPreservesSeparateReentriesWithinOneLeg() {
    List<AirspaceVisit<String, AirspacePoint>> visits = new ArrayList<>();
    var accumulator = new AirspaceVisitAccumulator<String, AirspacePoint>(Function.identity(), visits::add);
    accumulator.accept(point(0, -2), point(100, 2), List.of(
        new AirspaceSpan<>("concave", 0.1, 0.2, true),
        new AirspaceSpan<>("concave", 0.2, 0.3, true),
        new AirspaceSpan<>("concave", 0.6, 0.9, true)));
    accumulator.finish();

    assertEquals(2, visits.size());
    assertAll(
        () -> assertTime(10, visits.get(0).entry().time()),
        () -> assertTime(30, visits.get(0).exit().time()),
        () -> assertTime(60, visits.get(1).entry().time()),
        () -> assertTime(90, visits.get(1).exit().time()),
        () -> assertTrue(visits.get(0).events().isEmpty()),
        () -> assertTrue(visits.get(1).events().isEmpty()),
        () -> assertTrue(visits.get(0).entryCrossing()),
        () -> assertTrue(visits.get(0).exitCrossing()),
        () -> assertTrue(visits.get(1).entryCrossing()),
        () -> assertTrue(visits.get(1).exitCrossing()));
  }

  @Test
  void finishClosesAtTheTrackEndAndResetsForAnotherTrajectory() {
    List<AirspaceVisit<String, AirspacePoint>> visits = new ArrayList<>();
    var accumulator = new AirspaceVisitAccumulator<String, AirspacePoint>(Function.identity(), visits::add);
    var start = point(0, 0);
    var end = point(10, 0.5);
    accumulator.accept(start, end, List.of(new AirspaceSpan<>("box", 0, 1, true)));
    accumulator.finish();
    accumulator.finish();
    assertEquals(1, visits.size());
    var visit = visits.get(0);
    assertAll(
        () -> assertEquals(start, visit.entry()),
        () -> assertEquals(end, visit.exit()),
        () -> assertEquals(List.of(start, end), visit.events()),
        () -> assertEquals(Duration.ofSeconds(10), visit.duration()),
        () -> assertFalse(visit.entryCrossing()),
        () -> assertFalse(visit.exitCrossing()));

    accumulator.accept(start, end, List.of(new AirspaceSpan<>("box", 0, 1, true)));
    accumulator.finish();
    assertEquals(List.of(visit, visit), visits);
  }

  @Test
  void emptyAndDisconnectedLegsPreventBridgingAcrossUncoveredTrack() {
    List<AirspaceVisit<String, AirspacePoint>> visits = new ArrayList<>();
    var accumulator = new AirspaceVisitAccumulator<String, AirspacePoint>(Function.identity(), visits::add);
    var first = point(0, 0);
    var second = point(10, 0.1);
    var third = point(20, 0.2);
    var fourth = point(30, 0.3);
    var fifth = point(40, 0.4);
    var sixth = point(50, 0.5);
    var fullSpan = List.of(new AirspaceSpan<>("box", 0, 1, true));
    accumulator.accept(first, second, fullSpan);
    accumulator.accept(second, third, List.of());
    accumulator.accept(third, fourth, fullSpan);
    accumulator.accept(fifth, sixth, fullSpan);
    accumulator.finish();

    assertEquals(3, visits.size());
    assertAll(
        () -> assertEquals(List.of(first, second), visits.get(0).events()),
        () -> assertEquals(List.of(third, fourth), visits.get(1).events()),
        () -> assertEquals(List.of(fifth, sixth), visits.get(2).events()),
        () -> assertEquals(second, visits.get(0).exit()),
        () -> assertEquals(fourth, visits.get(1).exit()),
        () -> assertFalse(visits.get(0).exitCrossing()),
        () -> assertFalse(visits.get(1).exitCrossing()),
        () -> assertFalse(visits.get(2).exitCrossing()));
  }

  @Test
  void rejectsReversedTimestampsBeforeAccumulatingTheLeg() {
    List<AirspaceVisit<String, AirspacePoint>> visits = new ArrayList<>();
    var accumulator = new AirspaceVisitAccumulator<String, AirspacePoint>(Function.identity(), visits::add);
    var start = point(0, 0);
    var end = point(10, 0.5);
    var spans = List.of(new AirspaceSpan<>("box", 0, 1, true));
    assertThrows(IllegalArgumentException.class, () -> accumulator.accept(end, start, spans));
    accumulator.finish();
    assertTrue(visits.isEmpty());
    accumulator.accept(start, end, spans);
    accumulator.finish();
    assertEquals(1, visits.size());
    assertEquals(Duration.ofSeconds(10), visits.get(0).duration());
  }

  @Test
  void rejectsRecursiveCallbacksAndClearsAllPendingVisitsAfterFailure() {
    List<AirspaceVisit<String, AirspacePoint>> visits = new ArrayList<>();
    AtomicReference<AirspaceVisitAccumulator<String, AirspacePoint>> reference = new AtomicReference<>();
    AtomicBoolean recurse = new AtomicBoolean(true);
    var accumulator = new AirspaceVisitAccumulator<String, AirspacePoint>(Function.identity(), visit -> {
      if (recurse.getAndSet(false)) {
        reference.get().finish();
      }
      visits.add(visit);
    });
    reference.set(accumulator);
    accumulator.accept(point(10, 0), point(20, 0.5), List.of(
        new AirspaceSpan<>("first", 0, 1, true),
        new AirspaceSpan<>("second", 0, 1, true)));
    assertThrows(IllegalStateException.class, accumulator::finish);
    assertTrue(visits.isEmpty());

    var start = point(0, 0);
    var end = point(5, 0.5);
    accumulator.accept(start, end, List.of(new AirspaceSpan<>("fresh", 0, 1, true)));
    accumulator.finish();
    assertEquals(1, visits.size());
    assertAll(
        () -> assertEquals("fresh", visits.get(0).airspaceKey()),
        () -> assertEquals(List.of(start, end), visits.get(0).events()),
        () -> assertEquals(Duration.ofSeconds(5), visits.get(0).duration()));
  }

  @Test
  void rejectsOverlappingAndOutOfOrderSpansAndDiscardsPartialTrajectory() {
    for (var invalidSpans : List.of(
        List.of(new AirspaceSpan<>("box", 0, 0.75, true), new AirspaceSpan<>("box", 0.5, 1, true)),
        List.of(new AirspaceSpan<>("box", 0.6, 0.9, true), new AirspaceSpan<>("box", 0.1, 0.3, true)))) {
      List<AirspaceVisit<String, AirspacePoint>> visits = new ArrayList<>();
      var accumulator = new AirspaceVisitAccumulator<String, AirspacePoint>(Function.identity(), visits::add);
      var previousEnd = point(20, 0.5);
      accumulator.accept(point(10, 0), previousEnd, List.of(new AirspaceSpan<>("pending", 0, 1, true)));
      assertThrows(IllegalArgumentException.class, () -> accumulator.accept(previousEnd, point(30, 1), invalidSpans));
      assertTrue(visits.isEmpty());

      var start = point(0, 0);
      var end = point(5, 0.5);
      accumulator.accept(start, end, List.of(new AirspaceSpan<>("fresh", 0, 1, true)));
      accumulator.finish();
      assertEquals(1, visits.size());
      assertAll(
          () -> assertEquals("fresh", visits.get(0).airspaceKey()),
          () -> assertEquals(List.of(start, end), visits.get(0).events()));
    }
  }

  @Test
  void rejectsLegsStartingBeforeThePreviousEndAndClearsTrajectoryState() {
    List<AirspaceVisit<String, AirspacePoint>> visits = new ArrayList<>();
    var accumulator = new AirspaceVisitAccumulator<String, AirspacePoint>(Function.identity(), visits::add);
    var spans = List.of(new AirspaceSpan<>("box", 0, 1, true));
    accumulator.accept(point(10, 0), point(20, 0.5), spans);
    assertThrows(IllegalArgumentException.class, () -> accumulator.accept(point(15, 0.25), point(25, 0.75), spans));
    assertTrue(visits.isEmpty());

    var start = point(0, 0);
    var end = point(5, 0.5);
    accumulator.accept(start, end, spans);
    accumulator.finish();
    assertEquals(1, visits.size());
    assertAll(
        () -> assertEquals(start, visits.get(0).entry()),
        () -> assertEquals(end, visits.get(0).exit()),
        () -> assertEquals(List.of(start, end), visits.get(0).events()));
  }

  private static AirspacePoint point(long seconds, double longitude) {
    return new AirspacePoint(START.plusSeconds(seconds), LatLong.of(0.0, longitude), 1500.0);
  }

  private static void assertTime(long seconds, Instant actual) {
    assertEquals((double) seconds, Duration.between(START, actual).toNanos() / 1e9, 1e-6);
  }
}
