package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.AltitudeLimit;
import org.mitre.tdp.boogie.AltitudeLimits;
import org.mitre.tdp.boogie.AltitudeReference;
import org.mitre.tdp.boogie.Geometry;

class ReferencedAltitudeTest {

  /**
   * A transition area: from 700 feet above ground up to 12,000 feet above mean sea level.
   */
  private static final AltitudeBand TRANSITION = AltitudeBand.of(AltitudeLimits.of(AltitudeLimit.agl(700), AltitudeLimit.msl(12_000)));

  @Test
  void heightIsAltitudeAboveTheLimitsReference() {
    VerticalPosition position = new VerticalPosition(5_000.0, null, 1_200.0);
    assertAll(
        () -> assertEquals(Optional.of(5_000.0), position.heightFeet(AltitudeReference.MSL)),
        () -> assertEquals(Optional.of(3_800.0), position.heightFeet(AltitudeReference.AGL)),
        () -> assertEquals(Optional.empty(), VerticalPosition.msl(5_000.0).heightFeet(AltitudeReference.AGL)),
        () -> assertEquals(Optional.empty(), new VerticalPosition(null, null, 1_200.0).heightFeet(AltitudeReference.AGL)),
        () -> assertThrows(IllegalArgumentException.class, () -> new VerticalPosition(Double.NaN, null, null)),
        () -> assertThrows(IllegalArgumentException.class, () -> new VerticalPosition(1_000.0, null, Double.POSITIVE_INFINITY)),
        () -> assertThrows(IllegalArgumentException.class, () -> AltitudeLimit.agl(Double.NaN)));
  }

  /**
   * * Class A: from flight level 180 to flight level 600, both pressure altitudes.
   */
  private static final AltitudeBand CLASS_A = AltitudeBand.of(AltitudeLimits.of(AltitudeLimit.flightLevel(180), AltitudeLimit.flightLevel(600)));

  @Test
  void flightLevelsAreComparedWithPressureAltitude() {
    // A low-pressure day: 17,500 ft above sea level is a pressure altitude of 18,100 ft, inside Class A.
    VerticalPosition lowPressure = new VerticalPosition(17_500.0, 18_100.0, null);
    assertAll(
        () -> assertEquals(Optional.of(18_100.0), lowPressure.heightFeet(AltitudeReference.PRESSURE)),
        () -> assertTrue(CLASS_A.contains(lowPressure)),
        () -> assertFalse(CLASS_A.contains(new VerticalPosition(18_500.0, 17_900.0, null)), "above FL180 by MSL, below it by pressure"),
        () -> assertFalse(CLASS_A.checkable(VerticalPosition.msl(25_000.0)), "an MSL altitude alone cannot place a flight level"),
        () -> assertTrue(CLASS_A.checkable(new VerticalPosition(null, 25_000.0, null))),
        () -> assertTrue(new VerticalPosition(null, 25_000.0, null).hasAltitude()),
        () -> assertFalse(new VerticalPosition(null, null, 600.0).hasAltitude()));
  }

  @Test
  void queryUsesPressureAltitudeForFlightLevels() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>().apply(List.of(
        new AirspaceDefinition<>("class A", box(), CLASS_A),
        new AirspaceDefinition<>("layer", box(), AltitudeBand.closed(10_000, 18_000)))).newQuery(spans::add);
    LatLong west = LatLong.of(0.0, -2.0);
    LatLong east = LatLong.of(0.0, 2.0);

    // 17,500 ft MSL is below the MSL layer's ceiling and, by pressure, inside Class A.
    query.intersect(west, new VerticalPosition(17_500.0, 18_100.0, null), east, new VerticalPosition(17_500.0, 18_100.0, null));
    assertEquals(List.of(new AirspaceSpan<>("class A", 0.25, 0.75, true), new AirspaceSpan<>("layer", 0.25, 0.75, true)), spans);

    // With only MSL altitudes, Class A's limits cannot be evaluated: lateral occupancy, unchecked.
    spans.clear();
    query.intersect(west, 17_500.0, east, 17_500.0);
    assertEquals(List.of(new AirspaceSpan<>("class A", 0.25, 0.75, false), new AirspaceSpan<>("layer", 0.25, 0.75, true)), spans);

    // With only pressure altitudes, the MSL layer cannot be evaluated.
    spans.clear();
    query.intersect(west, new VerticalPosition(null, 25_000.0, null), east, new VerticalPosition(null, 25_000.0, null));
    assertEquals(List.of(new AirspaceSpan<>("class A", 0.25, 0.75, true), new AirspaceSpan<>("layer", 0.25, 0.75, false)), spans);
  }

  @Test
  void eachLimitIsComparedInItsOwnReference() {
    assertAll(
        () -> assertTrue(TRANSITION.contains(new VerticalPosition(5_000.0, null, 600.0))),
        () -> assertFalse(TRANSITION.contains(new VerticalPosition(39_000.0, null, 600.0)), "above the MSL ceiling"),
        () -> assertFalse(TRANSITION.contains(new VerticalPosition(1_000.0, null, 600.0)), "400 ft above ground, below the floor"),
        () -> assertTrue(TRANSITION.contains(new VerticalPosition(1_300.0, null, 600.0)), "on the floor"),
        () -> assertTrue(TRANSITION.contains(new VerticalPosition(1_300.0, null, -100.0)), "terrain below sea level"));
  }

  @Test
  void terrainIsNeededOnlyForLimitsMeasuredFromIt() {
    AltitudeBand mslOnly = AltitudeBand.closed(1_000, 2_000);
    VerticalPosition noTerrain = VerticalPosition.msl(5_000.0);
    assertAll(
        () -> assertTrue(TRANSITION.requiresTerrain()),
        () -> assertFalse(mslOnly.requiresTerrain()),
        () -> assertFalse(TRANSITION.checkable(noTerrain)),
        () -> assertTrue(mslOnly.checkable(noTerrain)),
        () -> assertFalse(TRANSITION.contains(noTerrain), "uncheckable is never definitely inside"),
        () -> assertFalse(TRANSITION.contains(5_000.0), "the double overload has no terrain"),
        () -> assertEquals(Optional.empty(), TRANSITION.clip(5_000.0, 5_000.0)),
        () -> assertFalse(AltitudeBand.unknown().checkable(new VerticalPosition(5_000.0, null, 0.0))));
  }

  @Test
  void levelFlightOverRisingTerrainLeavesAGroundRelativeFloor() {
    // 1,500 ft MSL while the terrain rises from 0 to 1,000 ft: height above ground falls from 1,500 to 500 ft and
    // passes the 700 ft floor 80% of the way along.
    AltitudeBand.Interval interval = TRANSITION.clip(new VerticalPosition(1_500.0, null, 0.0), new VerticalPosition(1_500.0, null, 1_000.0)).orElseThrow();
    assertAll(
        () -> assertEquals(0.0, interval.startFraction(), 1e-12),
        () -> assertEquals(0.8, interval.endFraction(), 1e-12));
  }

  @Test
  void climbIsClippedByTheGroundFloorAndTheSeaLevelCeiling() {
    // Climbing from 0 to 20,000 ft MSL over 500 ft terrain: the floor is passed at 1,200 ft MSL and the ceiling at 12,000.
    AltitudeBand.Interval interval = TRANSITION.clip(new VerticalPosition(0.0, null, 500.0), new VerticalPosition(20_000.0, null, 500.0)).orElseThrow();
    assertAll(
        () -> assertEquals(0.06, interval.startFraction(), 1e-12),
        () -> assertEquals(0.6, interval.endFraction(), 1e-12));
  }

  @Test
  void evaluableLimitsClipWhenOthersCannotBeEvaluated() {
    VerticalPosition low = VerticalPosition.msl(0.0);
    VerticalPosition high = VerticalPosition.msl(20_000.0);
    AltitudeBand.Interval climb = TRANSITION.clipEvaluable(low, high).orElseThrow();
    assertAll(
        () -> assertEquals(Optional.empty(), TRANSITION.clip(low, high), "the floor needs terrain"),
        () -> assertEquals(0.0, climb.startFraction(), 1e-12, "the floor cannot be evaluated"),
        () -> assertEquals(0.6, climb.endFraction(), 1e-12, "the ceiling still clips"),
        () -> assertEquals(Optional.empty(), TRANSITION.clipEvaluable(VerticalPosition.msl(39_000.0), VerticalPosition.msl(39_000.0)),
            "above the ceiling whatever the terrain"),
        () -> assertEquals(Optional.of(new AltitudeBand.Interval(0.0, 1.0)),
            TRANSITION.clipEvaluable(new VerticalPosition(null, null, null), new VerticalPosition(null, null, null)), "nothing to evaluate"),
        () -> assertEquals(Optional.empty(), AltitudeBand.unknown().clipEvaluable(low, high)));
  }

  @Test
  void onlyLimitsInTheSameReferenceAreOrdered() {
    assertAll(
        () -> assertThrows(IllegalArgumentException.class, () -> AltitudeLimits.of(AltitudeLimit.agl(2_000), AltitudeLimit.agl(1_000))),
        () -> assertEquals(AltitudeBand.Status.RESOLVED, AltitudeBand.of(AltitudeLimits.of(AltitudeLimit.agl(2_000), AltitudeLimit.msl(1_000))).status()),
        () -> assertEquals(AltitudeBand.unknown(), AltitudeBand.of(AltitudeLimits.unknown())),
        () -> assertEquals(AltitudeBand.closed(1_000, 2_000), AltitudeBand.of(AltitudeLimits.of(AltitudeLimit.msl(1_000), AltitudeLimit.msl(2_000)))),
        () -> assertNotEquals(AltitudeBand.atLeast(1_000), AltitudeBand.of(AltitudeLimits.of(AltitudeLimit.agl(1_000), AltitudeLimit.none()))),
        () -> assertEquals(AltitudeLimit.agl(700), TRANSITION.limits().lower()));
  }

  @Test
  void queryChecksTerrainReferencedBandsWhenTerrainIsReported() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>().apply(List.of(
        new AirspaceDefinition<>("transition", box(), TRANSITION),
        new AirspaceDefinition<>("layer", box(), AltitudeBand.closed(1_000, 20_000)))).newQuery(spans::add);
    LatLong west = LatLong.of(0.0, -2.0);
    LatLong east = LatLong.of(0.0, 2.0);

    query.intersect(west, new VerticalPosition(5_000.0, null, 600.0), east, new VerticalPosition(5_000.0, null, 600.0));
    assertEquals(List.of(new AirspaceSpan<>("transition", 0.25, 0.75, true), new AirspaceSpan<>("layer", 0.25, 0.75, true)), spans);

    spans.clear();
    query.intersect(west, new VerticalPosition(1_000.0, null, 600.0), east, new VerticalPosition(1_000.0, null, 600.0));
    assertEquals(List.of(new AirspaceSpan<>("layer", 0.25, 0.75, true)), spans, "400 ft above ground is below the floor");

    spans.clear();
    query.intersect(west, new VerticalPosition(39_000.0, null, 600.0), east, new VerticalPosition(39_000.0, null, 600.0));
    assertEquals(List.of(), spans, "above both ceilings");
  }

  @Test
  void queryWithoutTerrainStillAppliesSeaLevelLimits() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>().apply(List.of(
        new AirspaceDefinition<>("transition", box(), TRANSITION),
        new AirspaceDefinition<>("layer", box(), AltitudeBand.closed(1_000, 20_000)))).newQuery(spans::add);
    LatLong west = LatLong.of(0.0, -2.0);
    LatLong east = LatLong.of(0.0, 2.0);

    // The floor needs terrain, so the transition area is only laterally established; the MSL layer is still checked.
    query.intersect(west, 5_000.0, east, 5_000.0);
    assertEquals(List.of(new AirspaceSpan<>("transition", 0.25, 0.75, false), new AirspaceSpan<>("layer", 0.25, 0.75, true)), spans);

    // Above the transition area's MSL ceiling it is ruled out whatever the terrain.
    spans.clear();
    query.intersect(west, 15_000.0, east, 15_000.0);
    assertEquals(List.of(new AirspaceSpan<>("layer", 0.25, 0.75, true)), spans);

    // Terrain at only one end cannot establish a height above ground along the leg.
    spans.clear();
    query.intersect(west, new VerticalPosition(5_000.0, null, 600.0), east, VerticalPosition.msl(5_000.0));
    assertEquals(List.of(new AirspaceSpan<>("transition", 0.25, 0.75, false), new AirspaceSpan<>("layer", 0.25, 0.75, true)), spans);
  }

  @Test
  void queryClipsWhereTerrainRisesUnderLevelFlight() {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>().apply(List.of(new AirspaceDefinition<>("transition", box(), TRANSITION)))
        .newQuery(spans::add);
    // Height above ground falls from 1,500 to 500 ft along the leg, passing 700 ft at 0.8; the box is 0.25 to 0.75.
    query.intersect(LatLong.of(0.0, -2.0), new VerticalPosition(1_500.0, null, 0.0), LatLong.of(0.0, 2.0), new VerticalPosition(1_500.0, null, 1_000.0));
    assertEquals(List.of(new AirspaceSpan<>("transition", 0.25, 0.75, true)), spans);

    // Terrain rising faster puts the floor crossing at 0.5, inside the box.
    spans.clear();
    query.intersect(LatLong.of(0.0, -2.0), new VerticalPosition(1_500.0, null, 0.0), LatLong.of(0.0, 2.0), new VerticalPosition(1_500.0, null, 1_600.0));
    assertEquals(1, spans.size());
    assertAll(
        () -> assertEquals(0.25, spans.get(0).enterFraction(), 1e-10),
        () -> assertEquals(0.5, spans.get(0).exitFraction(), 1e-10),
        () -> assertTrue(spans.get(0).altitudeChecked()));
  }

  @Test
  void indexReadsAnAirspacesTypedLimits() {
    Airspace transition = withLimits(AltitudeLimits.of(AltitudeLimit.agl(700.0), AltitudeLimit.msl(12_000.0)));
    Airspace notamFloor = withLimits(AltitudeLimits.of(AltitudeLimit.byNotam(), AltitudeLimit.msl(18_000.0)));
    Airspace notam = withLimits(AltitudeLimits.unknown());
    Airspace surfaceUp = withLimits(AltitudeLimits.of(AltitudeLimit.none(), AltitudeLimit.none()));
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>().apply(List.of(
        new AirspaceDefinition<>("transition", transition),
        new AirspaceDefinition<>("notam floor", notamFloor),
        new AirspaceDefinition<>("notam", notam),
        new AirspaceDefinition<>("open", surfaceUp))).newQuery(spans::add);
    LatLong west = LatLong.of(0.0, -2.0);
    LatLong east = LatLong.of(0.0, 2.0);

    query.intersect(west, new VerticalPosition(5_000.0, null, 600.0), east, new VerticalPosition(5_000.0, null, 600.0));
    assertEquals(List.of(
        new AirspaceSpan<>("transition", 0.25, 0.75, true),
        new AirspaceSpan<>("notam floor", 0.25, 0.75, false),
        new AirspaceSpan<>("open", 0.25, 0.75, true)), spans, "a floor set by NOTAM is never checked; no limits at all is unknown");

    spans.clear();
    query.intersect(west, new VerticalPosition(39_000.0, null, 600.0), east, new VerticalPosition(39_000.0, null, 600.0));
    assertEquals(List.of(new AirspaceSpan<>("open", 0.25, 0.75, true)), spans, "both published ceilings rule FL390 out");
  }

  @Test
  void bandsFromLimitsRoundTrip() {
    AltitudeLimits notamFloor = AltitudeLimits.of(AltitudeLimit.byNotam(), AltitudeLimit.msl(18_000.0));
    assertAll(
        () -> assertEquals(notamFloor, AltitudeBand.of(notamFloor).limits()),
        () -> assertFalse(AltitudeBand.of(notamFloor).checkable(new VerticalPosition(5_000.0, null, 0.0))),
        () -> assertEquals(AltitudeBand.unknown(), AltitudeBand.of(AltitudeLimits.unknown())),
        () -> assertEquals(AltitudeBand.Status.UNKNOWN, AltitudeBand.of(AltitudeLimits.of(AltitudeLimit.notSpecified(), AltitudeLimit.none())).status(),
            "nothing to evaluate on either side"),
        () -> assertEquals(AltitudeBand.unbounded(), AltitudeBand.of(AltitudeLimits.of(AltitudeLimit.none(), AltitudeLimit.none()))),
        () -> assertEquals(TRANSITION, AltitudeBand.of(TRANSITION.limits())));
  }

  private static Airspace withLimits(AltitudeLimits limits) {
    return Airspace.builder().sequences(box().sequences().stream().map(AirspaceSequence.class::cast).toList())
        .altitudeLimits(limits).build();
  }

  private static Airspace box() {
    List<AirspaceSequence> sequences = new ArrayList<>();
    double[][] points = {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}};
    for (int i = 0; i < points.length; i++) {
      sequences.add(AirspaceSequence.builder(Geometry.GREAT_CIRCLE, i + 1)
          .associatedFix(LatLong.of(points[i][0], points[i][1])).build());
    }
    return Airspace.builder().sequences(sequences).build();
  }
}
