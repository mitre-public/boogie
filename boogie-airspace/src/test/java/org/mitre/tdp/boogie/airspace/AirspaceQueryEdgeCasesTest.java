package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;
import java.util.concurrent.Callable;
import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;
import java.util.concurrent.Future;
import java.util.concurrent.TimeUnit;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Geometry;

class AirspaceQueryEdgeCasesTest {

  @Test
  void preservesSeparateVisitsToAConcaveAirspaceInBothDirections() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    AirspaceQuery<String> query = new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("concave", concave()))).newQuery(spans::add);
    query.intersect(point(0, -4), point(0, 4));
    List<AirspaceSpan<String>> forward = List.copyOf(spans);
    assertEquals(2, forward.size());
    assertAll(
        () -> assertFractions(forward.get(0), 0.125, 0.375),
        () -> assertFractions(forward.get(1), 0.625, 0.875));

    spans.clear();
    query.intersect(point(0, 4), point(0, -4));
    List<AirspaceSpan<String>> backward = spans;
    assertEquals(2, backward.size());
    for (int i = 0; i < forward.size(); i++) {
      AirspaceSpan<String> reversed = backward.get(backward.size() - i - 1);
      assertFractions(reversed, 1.0 - forward.get(i).exitFraction(), 1.0 - forward.get(i).enterFraction());
    }
  }

  @Test
  void clipsAltitudeAcrossSeparateConcaveVisits() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    AirspaceQuery<String> query = new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("concave", concave(), AltitudeBand.closed(2000.0, 7000.0))))
        .newQuery(spans::add);
    query.intersect(point(0, -4), 0.0, point(0, 4), 8000.0);
    assertEquals(2, spans.size());
    assertAll(
        () -> assertFractions(spans.get(0), 0.25, 0.375),
        () -> assertFractions(spans.get(1), 0.625, 0.875));
  }

  @Test
  void crossesDateLineOnTheShortPathAndCanonicalizesEquivalentPositions() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    AirspaceQuery<String> query = new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("date-line", dateLine()))).newQuery(spans::add);
    query.intersect(point(0, 178), point(0, -178));
    assertEquals(1, spans.size());
    assertFractions(spans.get(0), 0.25, 0.75);

    spans.clear();
    query.intersect(point(0, 180), point(0, -180));
    assertEquals(1, spans.size());
    assertFractions(spans.get(0), 0.0, 1.0);
    spans.clear();
    query.intersect(point(0, -1), point(0, 1));
    assertTrue(spans.isEmpty());
  }

  @Test
  void acceptsThePoleWithAnyLongitude() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    AirspaceQuery<String> query = new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("polar", polar()))).newQuery(spans::add);
    query.intersect(point(90, -120), point(90, 120));
    assertEquals(1, spans.size());
    assertFractions(spans.get(0), 0.0, 1.0);
    spans.clear();
    query.intersect(point(89, 0), point(90, 0));
    List<AirspaceSpan<String>> expected = List.copyOf(spans);
    spans.clear();
    query.intersect(point(89, 0), point(90, 180));
    assertEquals(expected, spans);
    spans.clear();
    query.intersect(point(-90, 0), point(-90, 120));
    assertTrue(spans.isEmpty());
  }

  @Test
  void omitsIsolatedVertexTouches() {
    Airspace triangle = polygon(new double[][] {{0, 0}, {1, 1}, {1, -1}});
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    AirspaceQuery<String> query = new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("triangle", triangle))).newQuery(spans::add);
    query.intersect(point(0, -2), point(0, 2));
    assertTrue(spans.isEmpty());
    query.intersect(point(0, -2), point(0, 0));
    assertTrue(spans.isEmpty());
    query.intersect(point(0, 0), point(0, -2));
    assertTrue(spans.isEmpty());
  }

  @Test
  void spatialIndexFindsTheSameSpansAsQueryingEveryAirspace() {
    List<Airspace> airspaces = new ArrayList<>(List.of(concave(), dateLine(), polar(), rectangle(0, 0, 20)));
    for (int latitude = -60; latitude <= 60; latitude += 30) {
      for (int longitude = -150; longitude <= 150; longitude += 30) {
        airspaces.add(rectangle(latitude, longitude, 3));
      }
    }
    List<AirspaceDefinition<Integer>> definitions = new ArrayList<>();
    AirspaceIndexFactory<Integer> factory = new AirspaceIndexFactory<>();
    List<AirspaceQuery<Integer>> individualQueries = new ArrayList<>();
    List<AirspaceSpan<Integer>> expected = new ArrayList<>();
    for (int i = 0; i < airspaces.size(); i++) {
      AirspaceDefinition<Integer> definition = new AirspaceDefinition<>(i, airspaces.get(i));
      definitions.add(definition);
      individualQueries.add(factory.apply(List.of(definition)).newQuery(expected::add));
    }
    List<AirspaceSpan<Integer>> actual = new ArrayList<>();
    AirspaceQuery<Integer> combined = factory.apply(definitions).newQuery(actual::add);
    List<Leg> legs = new ArrayList<>(List.of(
        new Leg(point(0, -80), point(0, 80)),
        new Leg(point(0, 178), point(0, -178)),
        new Leg(point(75, 0), point(75, 180)),
        new Leg(point(0, 0), point(0, 0)),
        new Leg(point(0, -4), point(0, 4))));
    Random random = new Random(7241L);
    for (int i = 0; i < 40; i++) {
      legs.add(new Leg(point(random.nextDouble() * 160 - 80, random.nextDouble() * 360 - 180),
          point(random.nextDouble() * 160 - 80, random.nextDouble() * 360 - 180)));
    }
    for (Leg leg : legs) {
      expected.clear();
      actual.clear();
      individualQueries.forEach(query -> query.intersect(leg.start(), leg.end()));
      combined.intersect(leg.start(), leg.end());
      assertEquals(expected, actual, leg.toString());
    }
  }

  @Test
  void sharesCompiledGeometryAcrossIndependentWorkerQueries() throws Exception {
    AirspaceIndex<String> index = new AirspaceIndexFactory<String>().apply(List.of(
        new AirspaceDefinition<>("concave", concave()),
        new AirspaceDefinition<>("date-line", dateLine()),
        new AirspaceDefinition<>("polar", polar())));
    List<Leg> legs = List.of(new Leg(point(0, -4), point(0, 4)),
        new Leg(point(0, 178), point(0, -178)), new Leg(point(75, 0), point(75, 180)));
    List<List<AirspaceSpan<String>>> expected = legs.stream()
        .map(leg -> {
          List<AirspaceSpan<String>> spans = new ArrayList<>();
          index.newQuery(spans::add).intersect(leg.start(), leg.end());
          return List.copyOf(spans);
        }).toList();
    ExecutorService workers = Executors.newFixedThreadPool(4);
    try {
      List<Callable<Void>> tasks = new ArrayList<>();
      for (int task = 0; task < 16; task++) {
        tasks.add(() -> {
          List<AirspaceSpan<String>> spans = new ArrayList<>();
          AirspaceQuery<String> query = index.newQuery(spans::add);
          for (int repetition = 0; repetition < 25; repetition++) {
            for (int i = 0; i < legs.size(); i++) {
              Leg leg = legs.get(i);
              spans.clear();
              query.intersect(leg.start(), leg.end());
              assertEquals(expected.get(i), spans);
            }
          }
          return null;
        });
      }
      List<Future<Void>> results = workers.invokeAll(tasks, 30, TimeUnit.SECONDS);
      for (Future<Void> result : results) {
        result.get();
      }
    } finally {
      workers.shutdownNow();
    }
  }

  private static Airspace concave() {
    return polygon(new double[][] {{-2, -3}, {-2, 3}, {2, 3}, {2, 1}, {-1, 1}, {-1, -1}, {2, -1}, {2, -3}});
  }

  private static Airspace dateLine() {
    return polygon(new double[][] {{-2, 179}, {-2, -179}, {2, -179}, {2, 179}});
  }

  private static Airspace polar() {
    return polygon(new double[][] {{80, -120}, {80, 0}, {80, 120}});
  }

  private static Airspace rectangle(double latitude, double longitude, double halfSize) {
    return polygon(new double[][] {{latitude - halfSize, longitude - halfSize},
        {latitude - halfSize, longitude + halfSize}, {latitude + halfSize, longitude + halfSize},
        {latitude + halfSize, longitude - halfSize}});
  }

  private static Airspace polygon(double[][] coordinates) {
    List<AirspaceSequence> sequences = new ArrayList<>();
    for (int i = 0; i < coordinates.length; i++) {
      sequences.add(AirspaceSequence.builder(Geometry.GREAT_CIRCLE, i)
          .associatedFix(point(coordinates[i][0], coordinates[i][1])).build());
    }
    return Airspace.builder().sequences(sequences).build();
  }

  private static LatLong point(double latitude, double longitude) {
    return LatLong.of(latitude, longitude);
  }

  private static void assertFractions(AirspaceSpan<?> span, double start, double end) {
    assertAll(
        () -> assertEquals(start, span.enterFraction(), 1e-10),
        () -> assertEquals(end, span.exitFraction(), 1e-10));
  }

  private record Leg(LatLong start, LatLong end) {}
}
