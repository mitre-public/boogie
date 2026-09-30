package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;
import java.util.concurrent.atomic.AtomicBoolean;
import java.util.concurrent.atomic.AtomicReference;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Geometry;

import com.google.common.collect.Range;

class AirspaceQueryTest {
  @Test
  void findsCompleteVisitBetweenOutsideObservations() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>().apply(List.of(new AirspaceDefinition<>("box", box()))).newQuery(spans::add);
    query.intersect(LatLong.of(0.0, -2.0), LatLong.of(0.0, 2.0));
    assertEquals(1, spans.size());
    assertSpan(spans.get(0), "box", 0.25, 0.75);

    spans.clear();
    query.intersect(LatLong.of(0.0, 2.0), LatLong.of(0.0, -2.0));
    assertSpan(spans.get(0), "box", 0.25, 0.75);
  }

  @Test
  void distinguishesAlreadyInsideFromCrossingAndSupportsReuse() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>().apply(List.of(new AirspaceDefinition<>("box", box()))).newQuery(spans::add);
    query.intersect(LatLong.of(0.0, 0.0), LatLong.of(0.0, 2.0));
    assertSpan(spans.get(0), "box", 0.0, 0.5);
    spans.clear();
    query.intersect(LatLong.of(10.0, 10.0), LatLong.of(10.0, 11.0));
    assertTrue(spans.isEmpty());
    query.intersect(LatLong.of(0.0, -0.5), LatLong.of(0.0, 0.5));
    assertSpan(spans.get(0), "box", 0.0, 1.0);
  }

  @Test
  void clipsVerticalEntryWhileLateralPositionIsStationary() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("layer", box(), AltitudeBand.closed(1000, 2000)))).newQuery(spans::add);
    query.intersect(LatLong.of(0.0, 0.0), 0.0, LatLong.of(0.0, 0.0), 4000.0);
    assertSpan(spans.get(0), "layer", 0.25, 0.5);
    spans.clear();
    query.intersect(LatLong.of(0.0, 0.0), 4000.0, LatLong.of(0.0, 0.0), 0.0);
    assertSpan(spans.get(0), "layer", 0.5, 0.75);
    spans.clear();
    query.intersect(LatLong.of(10.0, 0.0), 0.0, LatLong.of(10.0, 0.0), 4000.0);
    assertTrue(spans.isEmpty());
  }

  @Test
  void intersectsLateralAndVerticalIntervalsAndRetainsOverlappingKeys() {
    var index = new AirspaceIndexFactory<String>().apply(List.of(
        new AirspaceDefinition<>("FIR", box(), AltitudeBand.atMost(2000)),
        new AirspaceDefinition<>("UIR", box(), AltitudeBand.atLeast(2000))));
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    index.newQuery(spans::add).intersect(LatLong.of(0.0, -2.0), 0.0, LatLong.of(0.0, 2.0), 4000.0);
    assertEquals(2, spans.size());
    assertAll(
        () -> assertSpan(spans.get(0), "FIR", 0.25, 0.5),
        () -> assertSpan(spans.get(1), "UIR", 0.5, 0.75));
  }

  @Test
  void unknownAltitudeIsUsableLaterallyButCannotYieldDefinite3dAssignment() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>().apply(List.of(
        new AirspaceDefinition<>("unknown", box()),
        new AirspaceDefinition<>("unlimited", box(), AltitudeBand.unbounded()))).newQuery(spans::add);
    query.intersect(LatLong.of(0.0, 0.0), LatLong.of(0.0, 0.0));
    assertEquals(2, spans.size());
    spans.clear();
    query.intersect(LatLong.of(0.0, 0.0), 12000.0, LatLong.of(0.0, 0.0), 12000.0);
    assertEquals(List.of(new AirspaceSpan<>("unlimited", 0, 1, true)), spans);
  }

  @Test
  void missingEndpointAltitudeRetainsLateralSpansWithoutInferringHeights() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("layer", box(), AltitudeBand.closed(1000, 2000)))).newQuery(spans::add);
    var start = LatLong.of(0.0, -2.0);
    var end = LatLong.of(0.0, 2.0);
    Double outside = 4000.0;
    for (var altitudes : List.of(
        new Double[] {null, outside},
        new Double[] {outside, null},
        new Double[] {null, null})) {
      spans.clear();
      query.intersect(start, altitudes[0], end, altitudes[1]);
      assertEquals(1, spans.size());
      assertAll(
          () -> assertSpan(spans.get(0), "layer", 0.25, 0.75),
          () -> assertFalse(spans.get(0).altitudeChecked()));
    }

    spans.clear();
    query.intersect(start, 0.0, end, outside);
    assertEquals(1, spans.size());
    assertAll(
        () -> assertSpan(spans.get(0), "layer", 0.25, 0.5),
        () -> assertTrue(spans.get(0).altitudeChecked()));
    spans.clear();
    query.intersect(start, outside, end, outside);
    assertTrue(spans.isEmpty());
  }

  @Test
  void stationaryLegWithMissingAltitudeIncludesUnknownAirspaceBandLaterally() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>().apply(List.of(
        new AirspaceDefinition<>("layer", box(), AltitudeBand.closed(1000, 2000)),
        new AirspaceDefinition<>("unknown", box()))).newQuery(spans::add);
    var point = LatLong.of(0.0, 0.0);
    query.intersect(point, null, point, 1500.0);
    assertEquals(List.of(new AirspaceSpan<>("layer", 0, 1, false),
        new AirspaceSpan<>("unknown", 0, 1, false)), spans);
    spans.clear();
    query.intersect(point, 1500.0, point, 1500.0);
    assertEquals(List.of(new AirspaceSpan<>("layer", 0, 1, true)), spans);
  }

  @Test
  void laterKnownAltitudeDoesNotConfirmEarlierCandidatesInNestedCircles() {
    var center = LatLong.of(0.0, 0.0);
    Airspace inner = Airspace.builder().sequences(List.of(AirspaceSequence.builder(Geometry.CIRCLE, 1)
        .centerFix(center).arcRadius(5.0).build())).build();
    Airspace outer = Airspace.builder().sequences(List.of(AirspaceSequence.builder(Geometry.CIRCLE, 1)
        .centerFix(center).arcRadius(10.0).build())).build();
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>().apply(List.of(
        new AirspaceDefinition<>("inner", inner, AltitudeBand.closed(0.0, 4000.0)),
        new AirspaceDefinition<>("outer shelf", outer, AltitudeBand.closed(1200.0, 4000.0)))).newQuery(spans::add);
    var first = LatLong.of(0.0, 0.0100);
    var second = LatLong.of(0.0, 0.0105);
    var third = LatLong.of(0.0, 0.0110);

    query.intersect(first, null, second, 500.0);
    assertEquals(2, spans.size());
    var earlierCandidates = List.copyOf(spans);
    spans.clear();
    query.intersect(second, 500.0, third, 500.0);
    assertAll(
        () -> assertEquals(List.of(new AirspaceSpan<>("inner", 0, 1, true)), spans),
        () -> assertEquals(List.of(new AirspaceSpan<>("inner", 0, 1, false),
            new AirspaceSpan<>("outer shelf", 0, 1, false)), earlierCandidates));
  }

  @Test
  void rejectsNonfinitePresentAltitudeEvenWhenOtherEndpointAltitudeIsMissing() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>().apply(List.of(new AirspaceDefinition<>("box", box()))).newQuery(spans::add);
    var point = LatLong.of(0.0, 0.0);
    for (double invalid : new double[] {Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
      assertAll(
          () -> assertThrows(IllegalArgumentException.class, () -> query.intersect(point, invalid, point, null)),
          () -> assertThrows(IllegalArgumentException.class, () -> query.intersect(point, null, point, invalid)));
    }
    assertTrue(spans.isEmpty());
  }

  @Test
  void defaultIndexUsesAvailableAltitudeBoundsAndFlagsBothMissingAsUnknown() {
    var index = new AirspaceIndexFactory<String>().apply(List.of(
        new AirspaceDefinition<>("ceiling", Airspace.builder().sequences(new ArrayList<>(box().sequences())).altitudeLimit(Range.atMost(2000.0)).build()),
        new AirspaceDefinition<>("floor", Airspace.builder().sequences(new ArrayList<>(box().sequences())).altitudeLimit(Range.atLeast(2000.0)).build()),
        new AirspaceDefinition<>("both", Airspace.builder().sequences(new ArrayList<>(box().sequences())).altitudeLimit(Range.closed(1000.0, 3000.0)).build()),
        new AirspaceDefinition<>("neither", Airspace.builder().sequences(new ArrayList<>(box().sequences())).altitudeLimit(Range.all()).build())));
    assertEquals(Set.of("neither"), index.unknownAltitudeKeys());
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = index.newQuery(spans::add);
    query.intersect(LatLong.of(0.0, 0.0), 0.0, LatLong.of(0.0, 0.0), 4000.0);
    assertEquals(3, spans.size());
    assertAll(
        () -> assertSpan(spans.get(0), "ceiling", 0.0, 0.5),
        () -> assertSpan(spans.get(1), "floor", 0.5, 1.0),
        () -> assertSpan(spans.get(2), "both", 0.25, 0.75));
  }

  @Test
  void compilesCirclesWithConfiguredSteps() {
    Airspace circle = Airspace.builder().sequences(List.of(AirspaceSequence.builder(Geometry.CIRCLE, 1)
        .centerFix(LatLong.of(0.0, 0.0)).arcRadius(60.0).build())).build();
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    new AirspaceIndexFactory<String>(5.0, 1.0).apply(List.of(new AirspaceDefinition<>("circle", circle))).newQuery(spans::add)
        .intersect(LatLong.of(0.0, -2.0), LatLong.of(0.0, 2.0));
    assertEquals(1, spans.size());
    assertAll(
        () -> assertEquals(0.25, spans.get(0).enterFraction(), 0.002),
        () -> assertEquals(0.75, spans.get(0).exitFraction(), 0.002));
  }

  @Test
  void acceptsUnorderedSequencesAndExplicitClosure() {
    var sequences = new ArrayList<AirspaceSequence>(box().sequences());
    sequences.add(AirspaceSequence.builder(Geometry.GREAT_CIRCLE, 5).associatedFix(LatLong.of(-1.0, -1.0)).build());
    java.util.Collections.reverse(sequences);
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("box", Airspace.builder().sequences(sequences).build()))).newQuery(spans::add);
    query.intersect(LatLong.of(0.0, -2.0), LatLong.of(0.0, 2.0));
    assertSpan(spans.get(0), "box", 0.25, 0.75);
  }

  @Test
  void validatesKeysGeometryAndAmbiguousLegs() {
    var factory = new AirspaceIndexFactory<String>();
    var source = new AirspaceDefinition<>("box", box());
    var invalidLineStep = new AirspaceIndexFactory<String>(Double.NaN, 10.0);
    var zeroLineStep = new AirspaceIndexFactory<String>(0.0, 10.0);
    var zeroArcStep = new AirspaceIndexFactory<String>(10.0, 0.0);
    var invalidArcStep = new AirspaceIndexFactory<String>(10.0, 180.0);
    Airspace negativeRadius = Airspace.builder().sequences(List.of(AirspaceSequence.builder(Geometry.CIRCLE, 1)
        .centerFix(LatLong.of(0.0, 0.0)).arcRadius(-60.0).build())).build();
    assertAll(
        () -> assertThrows(IllegalArgumentException.class, () -> factory.apply(List.of(source, source))),
        () -> assertThrows(IllegalArgumentException.class, () -> invalidLineStep.apply(List.of(source))),
        () -> assertThrows(IllegalArgumentException.class, () -> zeroLineStep.apply(List.of(source))),
        () -> assertThrows(IllegalArgumentException.class, () -> zeroArcStep.apply(List.of(source))),
        () -> assertThrows(IllegalArgumentException.class, () -> invalidArcStep.apply(List.of(source))),
        () -> assertThrows(IllegalArgumentException.class,
            () -> factory.apply(List.of(new AirspaceDefinition<>("empty", Airspace.builder().sequences(List.of()).build())))),
        () -> assertThrows(IllegalArgumentException.class,
            () -> factory.apply(List.of(new AirspaceDefinition<>("negative radius", negativeRadius)))),
        () -> assertThrows(NullPointerException.class, () -> new AirspaceDefinition<>(null, box())),
        () -> assertThrows(NullPointerException.class, () -> new AirspaceDefinition<>("missing", null)));
    Airspace crossing = polygon(new double[][] {{-1, -1}, {1, 1}, {-1, 1}, {1, -1}});
    var error = assertThrows(IllegalArgumentException.class,
        () -> factory.apply(List.of(new AirspaceDefinition<>("bowtie", crossing))));
    assertTrue(error.getMessage().contains("bowtie"));
    var index = factory.apply(List.of(source));
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = index.newQuery(spans::add);
    assertAll(
        () -> assertThrows(NullPointerException.class, () -> index.newQuery(null)),
        () -> assertThrows(IllegalArgumentException.class, () -> query.intersect(LatLong.of(0.0, 0.0), LatLong.of(0.0, 180.0))),
        () -> assertThrows(IllegalArgumentException.class, () -> query.intersect(LatLong.of(0.0, 0.0), Double.NaN, LatLong.of(1.0, 1.0), 0.0)));
    query.intersect(LatLong.of(0.0, 0.0), LatLong.of(0.0, 0.0));
    assertEquals(List.of(new AirspaceSpan<>("box", 0, 1, false)), spans);
  }

  @Test
  void reusesFactoryAndCreatesIndependentSnapshots() {
    var factory = new AirspaceIndexFactory<String>();
    var sources = new ArrayList<>(List.of(new AirspaceDefinition<>("first", box())));
    var original = factory.apply(sources);
    sources.add(new AirspaceDefinition<>("second", box()));
    var expanded = factory.apply(sources);
    sources.clear();
    var empty = factory.apply(sources);
    assertAll(
        () -> assertEquals(1, original.size()),
        () -> assertEquals(2, expanded.size()),
        () -> assertEquals(0, empty.size()));
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    original.newQuery(spans::add).intersect(LatLong.of(0.0, 0.0), LatLong.of(0.0, 0.0));
    assertEquals(List.of(new AirspaceSpan<>("first", 0, 1, false)), spans);
    spans.clear();
    empty.newQuery(spans::add).intersect(LatLong.of(0.0, 0.0), LatLong.of(0.0, 1.0));
    assertTrue(spans.isEmpty());
  }

  @Test
  void rejectsRecursiveUseAndRecoversFromCallbackExceptions() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    AtomicReference<AirspaceQuery<String>> queryReference = new AtomicReference<>();
    AtomicBoolean recurse = new AtomicBoolean(true);
    var query = new AirspaceIndexFactory<String>().apply(List.of(new AirspaceDefinition<>("box", box()))).newQuery(span -> {
      if (recurse.getAndSet(false)) {
        queryReference.get().intersect(LatLong.of(0.0, 0.0), LatLong.of(0.0, 0.0));
      }
      spans.add(span);
    });
    queryReference.set(query);

    assertThrows(IllegalStateException.class,
        () -> query.intersect(LatLong.of(0.0, 0.0), LatLong.of(0.0, 0.0)));
    assertTrue(spans.isEmpty());
    query.intersect(LatLong.of(0.0, 0.0), LatLong.of(0.0, 0.0));
    assertEquals(List.of(new AirspaceSpan<>("box", 0, 1, false)), spans);
  }

  private static Airspace box() {
    return polygon(new double[][] {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}});
  }

  private static Airspace polygon(double[][] points) {
    List<AirspaceSequence> sequences = new ArrayList<>();
    for (int i = 0; i < points.length; i++) {
      sequences.add(AirspaceSequence.builder(Geometry.GREAT_CIRCLE, i + 1)
          .associatedFix(LatLong.of(points[i][0], points[i][1])).build());
    }
    return Airspace.builder().sequences(sequences).build();
  }

  private static void assertSpan(AirspaceSpan<String> span, String key, double enter, double exit) {
    assertAll(
        () -> assertEquals(key, span.airspaceKey()),
        () -> assertEquals(enter, span.enterFraction(), 1e-10),
        () -> assertEquals(exit, span.exitFraction(), 1e-10));
  }
}
