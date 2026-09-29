package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.time.Duration;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.function.Function;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Geometry;

class AirspaceVisitAccumulatorTest {
  private static final Instant START = Instant.parse("2025-01-01T00:00:00Z");
  private static final LatLong CENTER = LatLong.of(39.0, -77.0);

  @Test
  void oneTrackCrossesOneAirspaceAndRetainsItsOriginalPoints() {
    Track track = track(1500.0, 0);
    List<AirspaceVisit<String, AirspacePoint>> visits = visits(track, List.of(
        new AirspaceDefinition<>("inner", circle(1.0), AltitudeBand.closed(0, 4000))));

    assertEquals(1, visits.size());
    AirspaceVisit<String, AirspacePoint> visit = visits.get(0);
    assertEquals(track.points().subList(61, 121), visit.events());
    assertAll(
        () -> assertEquals("inner", visit.airspaceKey()),
        () -> assertSame(track.points().get(61), visit.events().get(0)),
        () -> assertSame(track.points().get(120), visit.events().get(59)),
        () -> assertThrows(UnsupportedOperationException.class, () -> visit.events().add(track.points().get(0))),
        () -> assertTime(30.3, visit.entry().time()),
        () -> assertTime(60.3, visit.exit().time()),
        () -> assertEquals(30.0, visit.duration().toNanos() / 1e9, 1e-3),
        () -> assertEquals(0.0, CENTER.projectOut(270.0, 1.0).distanceInNM(visit.entry().position()), 1e-6),
        () -> assertEquals(0.0, CENTER.projectOut(90.0, 1.0).distanceInNM(visit.exit().position()), 1e-6),
        () -> assertTrue(visit.entryCrossing()),
        () -> assertTrue(visit.exitCrossing()),
        () -> assertTrue(visit.altitudeChecked()));
  }

  @Test
  void oneTrackCrossesTwoAirspacesWithMissingAltitudeDuringTheOuterEntry() {
    Track track = track(1500.0, 45);
    List<AirspaceVisit<String, AirspacePoint>> visits = visits(track, List.of(
        new AirspaceDefinition<>("outer shelf", circle(2.0), AltitudeBand.closed(1000, 4000)),
        new AirspaceDefinition<>("inner", circle(1.0), AltitudeBand.closed(0, 4000))));

    assertEquals(2, visits.size());
    AirspaceVisit<String, AirspacePoint> outer = visits.stream().filter(visit -> visit.airspaceKey().equals("outer shelf")).findFirst().orElseThrow();
    AirspaceVisit<String, AirspacePoint> inner = visits.stream().filter(visit -> visit.airspaceKey().equals("inner")).findFirst().orElseThrow();
    assertAll(
        () -> assertEquals(track.points().subList(31, 151), outer.events()),
        () -> assertEquals(track.points().subList(61, 121), inner.events()),
        () -> assertTime(15.3, outer.entry().time()),
        () -> assertTime(75.3, outer.exit().time()),
        () -> assertTime(30.3, inner.entry().time()),
        () -> assertTime(60.3, inner.exit().time()),
        () -> assertEquals(60.0, outer.duration().toNanos() / 1e9, 1e-3),
        () -> assertEquals(30.0, inner.duration().toNanos() / 1e9, 1e-3),
        () -> assertNull(outer.entry().altitudeFeet()),
        () -> assertEquals(1500.0, outer.exit().altitudeFeet()),
        () -> assertEquals(1500.0, inner.entry().altitudeFeet()),
        () -> assertFalse(outer.altitudeChecked()),
        () -> assertTrue(inner.altitudeChecked()),
        () -> assertTrue(outer.entryCrossing()),
        () -> assertTrue(outer.exitCrossing()),
        () -> assertTrue(inner.entryCrossing()),
        () -> assertTrue(inner.exitCrossing()));
  }

  @Test
  void laterAltitudeBelowTheShelfDoesNotConfirmTheEarlierOuterVisit() {
    Track track = track(500.0, 45);
    List<AirspaceVisit<String, AirspacePoint>> visits = visits(track, List.of(
        new AirspaceDefinition<>("outer shelf", circle(2.0), AltitudeBand.closed(1000, 4000)),
        new AirspaceDefinition<>("inner", circle(1.0), AltitudeBand.closed(0, 4000))));

    assertEquals(2, visits.size());
    AirspaceVisit<String, AirspacePoint> outer = visits.stream().filter(visit -> visit.airspaceKey().equals("outer shelf")).findFirst().orElseThrow();
    AirspaceVisit<String, AirspacePoint> inner = visits.stream().filter(visit -> visit.airspaceKey().equals("inner")).findFirst().orElseThrow();
    assertAll(
        () -> assertEquals(track.points().subList(31, 46), outer.events()),
        () -> assertEquals(track.points().subList(61, 121), inner.events()),
        () -> assertTime(15.3, outer.entry().time()),
        () -> assertSame(track.points().get(45), outer.exit()),
        () -> assertTime(22.5, outer.exit().time()),
        () -> assertFalse(outer.exitCrossing()),
        () -> assertFalse(outer.altitudeChecked()),
        () -> assertTime(30.3, inner.entry().time()),
        () -> assertTime(60.3, inner.exit().time()),
        () -> assertTrue(inner.altitudeChecked()));
  }

  @Test
  void leavingAndReturningProducesTwoVisitsForOneTrack() {
    Track outbound = track(1500.0, 0);
    List<AirspacePoint> points = new ArrayList<>(outbound.points());
    for (int i = outbound.points().size() - 2; i >= 0; i--) {
      AirspacePoint original = outbound.points().get(i);
      points.add(new AirspacePoint(START.plusMillis(points.size() * 500L), original.position(), original.altitudeFeet()));
    }
    Track track = new Track(points);
    List<AirspaceVisit<String, AirspacePoint>> visits = visits(track, List.of(
        new AirspaceDefinition<>("inner", circle(1.0), AltitudeBand.closed(0, 4000))));

    assertEquals(2, visits.size());
    AirspaceVisit<String, AirspacePoint> outboundVisit = visits.get(0);
    AirspaceVisit<String, AirspacePoint> inboundVisit = visits.get(1);
    assertAll(
        () -> assertEquals("inner", outboundVisit.airspaceKey()),
        () -> assertEquals("inner", inboundVisit.airspaceKey()),
        () -> assertEquals(track.points().subList(61, 121), outboundVisit.events()),
        () -> assertEquals(track.points().subList(240, 300), inboundVisit.events()),
        () -> assertTime(30.3, outboundVisit.entry().time()),
        () -> assertTime(60.3, outboundVisit.exit().time()),
        () -> assertTime(119.7, inboundVisit.entry().time()),
        () -> assertTime(149.7, inboundVisit.exit().time()),
        () -> assertTrue(outboundVisit.altitudeChecked()),
        () -> assertTrue(inboundVisit.altitudeChecked()));
  }

  private static List<AirspaceVisit<String, AirspacePoint>> visits(Track track, List<AirspaceDefinition<String>> airspaces) {
    AirspaceIndex<String> index = new AirspaceIndexFactory<String>().apply(airspaces);
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    AirspaceQuery<String> query = index.newQuery(spans::add);
    List<AirspaceVisit<String, AirspacePoint>> visits = new ArrayList<>();
    AirspaceVisitAccumulator<String, AirspacePoint> accumulator = new AirspaceVisitAccumulator<>(Function.identity(), visits::add);
    for (int i = 1; i < track.points().size(); i++) {
      AirspacePoint start = track.points().get(i - 1);
      AirspacePoint end = track.points().get(i);
      spans.clear();
      query.intersect(start.position(), start.altitudeFeet(), end.position(), end.altitudeFeet());
      accumulator.accept(start, end, spans);
    }
    accumulator.finish();
    return visits;
  }

  private static Track track(double altitudeFeet, int missingAltitudePoints) {
    // 90 seconds at 240 knots, sampled every 0.5 seconds, west to east through both circles.
    // The 3.02 NM starting offset places crossings between observations.
    List<AirspacePoint> points = new ArrayList<>();
    for (int i = 0; i <= 180; i++) {
      double offsetNm = -3.02 + i / 30.0;
      LatLong position = CENTER.projectOut(offsetNm < 0.0 ? 270.0 : 90.0, Math.abs(offsetNm));
      points.add(new AirspacePoint(START.plusMillis(i * 500L), position, i < missingAltitudePoints ? null : altitudeFeet));
    }
    return new Track(points);
  }

  private static Airspace circle(double radiusNm) {
    return Airspace.builder().sequences(List.of(AirspaceSequence.builder(Geometry.CIRCLE, 1)
        .centerFix(CENTER).arcRadius(radiusNm).build())).build();
  }

  private static void assertTime(double seconds, Instant actual) {
    assertEquals(seconds, Duration.between(START, actual).toNanos() / 1e9, 1e-3);
  }

  private record Track(List<AirspacePoint> points) {}
}
