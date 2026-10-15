package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertInstanceOf;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.junit.jupiter.params.provider.ValueSource;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.airspace.BoundaryArcRadiusIdentifier.Mismatch;
import org.mitre.tdp.boogie.projections.BoundaryProjection;

class BoundaryArcRadiusResolverTest {
  private final BoundaryArcRadiusResolver resolver = new BoundaryArcRadiusResolver(0.05);

  @ParameterizedTest
  @EnumSource(value = Geometry.class, names = {"CLOCKWISE_ARC", "COUNTER_CLOCKWISE_ARC"})
  void repairsBothDirectionsAcrossTheDateLineAndNearThePoles(Geometry geometry) {
    for (LatLong originalCenter : List.of(LatLong.of(45.0, 179.9), LatLong.of(89.95, 90.0), LatLong.of(-89.95, -90.0))) {
      AirspaceSequence arc = AirspaceSequence.record("published arc", arc(10, geometry,
          originalCenter.projectOut(0.0, 10.0), originalCenter));
      AirspaceSequence end = line(20, originalCenter.projectOut(90.0, 12.0));
      List<AirspaceSequence> source = List.of(arc, end);
      Mismatch issue = resolver.identify(source).get(0);

      List<AirspaceSequence> repaired = resolver.apply(source);
      AirspaceSequence replacement = repaired.get(0);
      LatLong center = replacement.centerFix().orElseThrow();
      double radiusNm = (issue.startRadiusNm() + issue.endRadiusNm()) / 2.0;
      assertEquals(radiusNm, center.distanceInNM(arc.associatedFix().orElseThrow()), 1e-6);
      assertEquals(radiusNm, center.distanceInNM(end.associatedFix().orElseThrow()), 1e-6);
      assertNotEquals(originalCenter, center);
      assertSame(arc.associatedFix().orElseThrow(), replacement.associatedFix().orElseThrow());
      assertEquals(arc.geometry(), replacement.geometry());
      assertEquals(arc.sequenceNumber(), replacement.sequenceNumber());
      assertEquals(arc.arcRadius(), replacement.arcRadius());
      assertEquals(arc.arcBearing(), replacement.arcBearing());
      assertEquals("published arc", assertInstanceOf(AirspaceSequence.Record.class, replacement).datum());
      assertSame(end, repaired.get(1));
      assertSame(originalCenter, arc.centerFix().orElseThrow());
      assertSame(arc, source.get(0));
      assertTrue(resolver.identify(repaired).isEmpty());
      assertSame(repaired, resolver.apply(repaired));
      assertThrows(UnsupportedOperationException.class, repaired::clear);

      List<LatLong> samples = BoundaryProjection.project(replacement, end, 10.0, 10.0);
      samples.forEach(sample -> assertEquals(radiusNm, center.distanceInNM(sample), 1e-6));
      assertTrue(geometry == Geometry.CLOCKWISE_ARC ? samples.size() < 19 : samples.size() > 19,
          "The existing projector must preserve the short clockwise and long counterclockwise sweeps");
    }
  }

  @ParameterizedTest
  @ValueSource(doubles = {-0.5, 0.5})
  void choosesTheIntersectionClosestToTheOriginalCenter(double latitude) {
    LatLong originalCenter = LatLong.of(latitude, 0.25);
    List<AirspaceSequence> source = List.of(
        arc(10, Geometry.CLOCKWISE_ARC, LatLong.of(0.0, -1.0), originalCenter),
        line(20, LatLong.of(0.0, 1.0)));
    Mismatch issue = resolver.identify(source).get(0);

    LatLong center = resolver.apply(source).get(0).centerFix().orElseThrow();
    LatLong otherCenter = LatLong.of(-center.latitude(), center.longitude());
    double radiusNm = (issue.startRadiusNm() + issue.endRadiusNm()) / 2.0;

    assertEquals(0.0, center.longitude(), 1e-10);
    assertEquals(Math.signum(latitude), Math.signum(center.latitude()));
    assertTrue(originalCenter.distanceInNM(center) < originalCenter.distanceInNM(otherCenter));
    for (AirspaceSequence sequence : source) {
      assertEquals(radiusNm, center.distanceInNM(sequence.associatedFix().orElseThrow()), 1e-6);
      assertEquals(radiusNm, otherCenter.distanceInNM(sequence.associatedFix().orElseThrow()), 1e-6);
    }
  }

  @Test
  void repairsAClosingSemicircleDespiteThePublishedRadiusBeingTooSmall() {
    LatLong originalCenter = LatLong.of(0.0, 0.0);
    AirspaceSequence diameter = line(10, LatLong.of(1.0, 0.0));
    AirspaceSequence arc = AirspaceSequence.builder(Geometry.COUNTER_CLOCKWISE_ARC, 20)
        .associatedFix(LatLong.of(-1.0005, 0.0)).centerFix(originalCenter).arcRadius(60.0).arcBearing(180.0).build();
    List<AirspaceSequence> source = List.of(diameter, arc);
    BoundaryArcRadiusResolver precise = new BoundaryArcRadiusResolver(0.01);
    Mismatch issue = precise.identify(source).get(0);

    List<AirspaceSequence> repaired = precise.apply(source);
    LatLong center = repaired.get(1).centerFix().orElseThrow();
    assertSame(diameter, repaired.get(0));
    assertEquals(-0.00025, center.latitude(), 1e-9);
    assertEquals(0.0, center.longitude(), 1e-5);
    double radiusNm = (issue.startRadiusNm() + issue.endRadiusNm()) / 2.0;
    assertEquals(radiusNm, center.distanceInNM(arc.associatedFix().orElseThrow()), 1e-6);
    assertEquals(radiusNm, center.distanceInNM(diameter.associatedFix().orElseThrow()), 1e-6);
    assertTrue(radiusNm > arc.arcRadius().orElseThrow());
    assertTrue(precise.identify(repaired).isEmpty());
  }

  @Test
  void canRepairAnArcWhoseOriginalCenterIsItsStartingFix() {
    LatLong start = LatLong.of(0.0, 0.0);
    AirspaceSequence arc = arc(10, Geometry.CLOCKWISE_ARC, start, start);
    AirspaceSequence end = line(20, LatLong.of(0.0, 1.0));
    List<AirspaceSequence> repaired = resolver.apply(List.of(arc, end));
    LatLong center = repaired.get(0).centerFix().orElseThrow();
    assertEquals(0.0, center.latitude(), 1e-5);
    assertEquals(0.5, center.longitude(), 1e-9);
    assertEquals(center.distanceInNM(start), center.distanceInNM(end.associatedFix().orElseThrow()), 1e-6);
    assertTrue(resolver.identify(repaired).isEmpty());
  }

  @Test
  void repairsAdjacentArcsIndependentlyAndOnlyUsesSelectedCurrentFindings() {
    LatLong center = LatLong.of(40.0, -75.0);
    List<AirspaceSequence> source = List.of(
        arc(10, Geometry.CLOCKWISE_ARC, center.projectOut(0.0, 10.0), center),
        arc(20, Geometry.COUNTER_CLOCKWISE_ARC, center.projectOut(90.0, 12.0), center),
        line(30, center.projectOut(180.0, 14.0)));
    List<Mismatch> issues = resolver.identify(source);
    assertEquals(2, issues.size());
    assertSame(source, resolver.resolve(source, List.of()));

    List<AirspaceSequence> oneRepair = resolver.resolve(source, List.of(issues.get(0)));
    assertSame(source.get(1), oneRepair.get(1));
    assertEquals(List.of(issues.get(1)), resolver.identify(oneRepair));

    List<AirspaceSequence> repaired = resolver.resolve(source, issues);
    assertTrue(resolver.identify(repaired).isEmpty());
    assertSame(source.get(2), repaired.get(2));
    assertEquals(repaired, resolver.apply(source));
    List<AirspaceSequence> changed = new ArrayList<>(source);
    changed.set(0, source.get(0).withCenterFix(center.projectOut(270.0, 3.0)));
    assertSame(changed, resolver.resolve(changed, List.of(issues.get(0))));
  }

  @Test
  void leavesAcceptableDifferencesLinesCirclesAndMissingCoordinatesAlone() {
    LatLong center = LatLong.of(0.0, 0.0);
    List<AirspaceSequence> source = List.of(
        arc(10, Geometry.CLOCKWISE_ARC, center.projectOut(0.0, 10.0), center),
        line(20, center.projectOut(90.0, 10.02)));
    List<AirspaceSequence> circle = List.of(AirspaceSequence.builder(Geometry.CIRCLE, 1)
        .centerFix(center).arcRadius(10.0).build());
    List<AirspaceSequence> missing = List.of(
        arc(10, Geometry.CLOCKWISE_ARC, LatLong.of(1.0, 0.0), null), line(20, LatLong.of(0.0, 2.0)));
    for (List<AirspaceSequence> boundary : List.of(source, circle, missing, List.<AirspaceSequence>of(),
        List.of(line(10, LatLong.of(0.0, 0.0)), line(20, LatLong.of(1.0, 1.0))))) {
      assertSame(boundary, resolver.apply(boundary));
    }
  }

  @Test
  void leavesAntipodalAndNearlyAntipodalEndpointChoicesUnchanged() {
    for (double longitude : new double[] {180.0, 180.0 - 1e-12}) {
      List<AirspaceSequence> source = List.of(
          arc(10, Geometry.CLOCKWISE_ARC, LatLong.of(0.0, 0.0), LatLong.of(0.0, 30.0)),
          line(20, LatLong.of(0.0, longitude)));
      assertEquals(1, resolver.identify(source).size());
      assertSame(source, resolver.apply(source));
    }
  }

  @Test
  void repairsACrossingCausedByTheOffCenterArcBeforeCompilation() {
    List<AirspaceSequence> source = List.of(
        arc(10, Geometry.CLOCKWISE_ARC, LatLong.of(0.0, -1.0), LatLong.of(0.0, 0.0)),
        line(20, LatLong.of(0.0, 2.0)),
        line(30, LatLong.of(0.25, 1.5)),
        line(40, LatLong.of(0.5, 0.0)));
    Airspace.Standard airspace = Airspace.builder().sequences(source).build();
    var definition = new AirspaceDefinition<>("off-center arc", airspace);
    var strict = new AirspaceIndexFactory<String>();
    assertThrows(IllegalArgumentException.class, () -> strict.apply(List.of(definition)));

    List<AirspaceSpan<String>> spans = new ArrayList<>();
    Airspace repaired = airspace.toBuilder().sequences(resolver.apply(source)).build();
    var query = strict.apply(List.of(new AirspaceDefinition<>(definition.key(), repaired))).newQuery(spans::add);
    LatLong inside = LatLong.of(0.8, 0.5);
    query.intersect(inside, inside);
    assertEquals(List.of(new AirspaceSpan<>("off-center arc", 0.0, 1.0, false)), spans);
    spans.clear();
    LatLong outside = LatLong.of(2.0, 0.5);
    query.intersect(outside, outside);
    assertTrue(spans.isEmpty());
    assertSame(source.get(0).centerFix().orElseThrow(), airspace.sequences().get(0).centerFix().orElseThrow());
    assertThrows(IllegalArgumentException.class, () -> strict.apply(List.of(definition)));
  }

  @Test
  void composesRetraceAndArcRadiusRepairsBeforeCompilation() {
    List<AirspaceSequence> source = List.of(
        arc(10, Geometry.CLOCKWISE_ARC, LatLong.of(0.0, -1.0), LatLong.of(0.0, 0.0)),
        line(20, LatLong.of(0.0, 2.0)),
        line(21, LatLong.of(0.00001, 2.0)),
        line(22, LatLong.of(0.0, 2.0)),
        line(30, LatLong.of(0.25, 1.5)),
        line(40, LatLong.of(0.5, 0.0)));
    Airspace.Standard airspace = Airspace.builder().sequences(source).build();
    var definition = new AirspaceDefinition<>("retrace and off-center arc", airspace);
    var strict = new AirspaceIndexFactory<String>();
    assertThrows(IllegalArgumentException.class, () -> strict.apply(List.of(definition)));

    List<AirspaceSpan<String>> spans = new ArrayList<>();
    List<AirspaceSequence> sequences = new BoundaryRetraceResolver(1.0).andThen(resolver).apply(source);
    Airspace repaired = airspace.toBuilder().sequences(sequences).build();
    var query = strict.apply(List.of(new AirspaceDefinition<>(definition.key(), repaired))).newQuery(spans::add);
    LatLong inside = LatLong.of(0.8, 0.5);
    query.intersect(inside, inside);
    assertEquals(List.of(new AirspaceSpan<>("retrace and off-center arc", 0.0, 1.0, false)), spans);
    assertEquals(source, airspace.sequences());
  }

  @Test
  void rejectsInvalidTolerances() {
    for (double tolerance : new double[] {-1.0, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY}) {
      assertThrows(IllegalArgumentException.class, () -> new BoundaryArcRadiusResolver(tolerance));
    }
    assertTrue(new BoundaryArcRadiusResolver(0.0).identify(List.of()).isEmpty());
  }

  private static AirspaceSequence line(int number, LatLong fix) {
    return AirspaceSequence.builder(Geometry.GREAT_CIRCLE, number).associatedFix(fix).build();
  }

  private static AirspaceSequence arc(int number, Geometry geometry, LatLong start, LatLong center) {
    return AirspaceSequence.builder(geometry, number).associatedFix(start).centerFix(center)
        .arcRadius(999.0).arcBearing(250.0).build();
  }
}
