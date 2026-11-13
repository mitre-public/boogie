package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.airspace.BoundaryArcRadiusIdentifier.Mismatch;

class BoundaryArcRadiusIdentifierTest {
  private final LatLong center = LatLong.of(45.0, 179.9);
  private final BoundaryArcRadiusIdentifier identifier = new BoundaryArcRadiusIdentifier(0.05);

  @ParameterizedTest
  @EnumSource(value = Geometry.class, names = {"CLOCKWISE_ARC", "COUNTER_CLOCKWISE_ARC"})
  void reportsCoordinateDerivedRadiiForBothArcDirectionsWithoutChangingSource(Geometry geometry) {
    List<AirspaceSequence> source = List.of(
        line(1, center.projectOut(270.0, 100.0)),
        arc(2, geometry, center.projectOut(0.0, 10.0), center),
        line(3, center.projectOut(90.0, 12.0)));
    List<AirspaceSequence> original = List.copyOf(source);

    List<Mismatch> issues = identifier.apply(source);
    assertEquals(1, issues.size());
    Mismatch mismatch = issues.get(0);
    assertEquals(2, mismatch.arcSequenceNumber());
    assertEquals(3, mismatch.endingSequenceNumber());
    assertEquals(10.0, mismatch.startRadiusNm(), 1e-6);
    assertEquals(12.0, mismatch.endRadiusNm(), 1e-6);
    assertEquals(2.0, mismatch.differenceNm(), 1e-6);
    assertEquals(original, source);
    assertThrows(UnsupportedOperationException.class, issues::clear);
  }

  @Test
  void comparesTheCurrentAndNextFixEvenWhenThePreviousFixHasAnotherRadius() {
    List<AirspaceSequence> source = List.of(
        line(1, center.projectOut(270.0, 100.0)),
        arc(2, Geometry.CLOCKWISE_ARC, center.projectOut(0.0, 10.0), center),
        line(3, center.projectOut(90.0, 10.0)));
    assertTrue(identifier.apply(source).isEmpty());
  }

  @ParameterizedTest
  @EnumSource(value = Geometry.class, names = {"CLOCKWISE_ARC", "COUNTER_CLOCKWISE_ARC"})
  void checksClosingArcsInTwoSequenceBoundaries(Geometry geometry) {
    List<AirspaceSequence> source = List.of(
        line(10, center.projectOut(0.0, 12.0)),
        arc(20, geometry, center.projectOut(180.0, 10.0), center));
    List<Mismatch> issues = identifier.apply(source);
    assertEquals(1, issues.size());
    assertEquals(20, issues.get(0).arcSequenceNumber());
    assertEquals(10, issues.get(0).endingSequenceNumber());
    assertEquals(2.0, issues.get(0).differenceNm(), 1e-6);
  }

  @Test
  void acceptsRoundedEndpointsAndDoesNotTreatAMismatchAsProofOfACrossing() {
    LatLong origin = LatLong.of(0.0, 0.0);
    List<AirspaceSequence> source = List.of(
        line(10, LatLong.of(1.0, 0.0)),
        arc(20, Geometry.COUNTER_CLOCKWISE_ARC, LatLong.of(-1.0005, 0.0), origin));
    assertTrue(identifier.apply(source).isEmpty());
    assertEquals(1, new BoundaryArcRadiusIdentifier(0.01).apply(source).size());
    assertEquals(1, new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("rounded endpoints", Airspace.builder().sequences(source).build()))).size());
  }

  @Test
  void acceptsDifferencesEqualToTheToleranceAndReportsThoseAboveIt() {
    List<AirspaceSequence> source = List.of(
        arc(10, Geometry.CLOCKWISE_ARC, center.projectOut(0.0, 10.0), center),
        line(20, center.projectOut(90.0, 12.0)));
    double difference = identifier.apply(source).get(0).differenceNm();
    assertTrue(new BoundaryArcRadiusIdentifier(difference).apply(source).isEmpty());
    assertEquals(1, new BoundaryArcRadiusIdentifier(Math.nextDown(difference)).apply(source).size());
  }

  @Test
  void leavesMissingCoordinatesToCompilationValidation() {
    AirspaceSequence complete = arc(10, Geometry.CLOCKWISE_ARC, center.projectOut(0.0, 10.0), center);
    AirspaceSequence next = line(20, center.projectOut(90.0, 12.0));
    assertTrue(identifier.apply(List.of(
        arc(10, Geometry.CLOCKWISE_ARC, complete.associatedFix().orElseThrow(), null), next)).isEmpty());
    assertTrue(identifier.apply(List.of(arc(10, Geometry.CLOCKWISE_ARC, null, center), next)).isEmpty());
    assertTrue(identifier.apply(List.of(complete, line(20, null))).isEmpty());
  }

  @Test
  void ignoresLinesAndCirclesAndHandlesEmptyBoundaries() {
    for (Geometry geometry : List.of(Geometry.GREAT_CIRCLE, Geometry.RHUMB_LINE, Geometry.CIRCLE)) {
      List<AirspaceSequence> source = List.of(
          arc(10, geometry, center.projectOut(0.0, 10.0), center),
          line(20, center.projectOut(90.0, 12.0)));
      assertTrue(identifier.apply(source).isEmpty());
    }
    assertTrue(identifier.apply(List.of()).isEmpty());
    assertTrue(identifier.apply(List.of(AirspaceSequence.builder(Geometry.CIRCLE, 1)
        .centerFix(center).arcRadius(10.0).build())).isEmpty());
  }

  @Test
  void canReportBeforeCompilationWithoutRepairingOrMutatingSource() {
    List<AirspaceSequence> source = List.of(
        line(10, LatLong.of(1.0, 0.0)),
        arc(20, Geometry.COUNTER_CLOCKWISE_ARC, LatLong.of(-1.0005, 0.0), LatLong.of(0.0, 0.0)));
    List<Mismatch> reported = new ArrayList<>();
    IssueResolver<List<AirspaceSequence>, Mismatch> reporting = IssueResolver.of(
        new BoundaryArcRadiusIdentifier(0.01), (sequences, issues) -> {
          reported.addAll(issues);
          return sequences;
        });
    assertSame(source, reporting.apply(source));

    AirspaceIndex<String> index = new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("inspect arcs", Airspace.builder().sequences(source).build())));
    assertEquals(1, index.size());
    assertEquals(1, reported.size());
    assertEquals(source, reporting.apply(source));
  }

  @Test
  void rejectsInvalidTolerancesAndAllowsAnExactComparison() {
    for (double tolerance : new double[] {-1.0, Double.NaN, Double.NEGATIVE_INFINITY, Double.POSITIVE_INFINITY}) {
      assertThrows(IllegalArgumentException.class, () -> new BoundaryArcRadiusIdentifier(tolerance));
    }
    assertTrue(new BoundaryArcRadiusIdentifier(0.0).apply(List.of()).isEmpty());
  }

  private static AirspaceSequence line(int number, LatLong fix) {
    return AirspaceSequence.builder(Geometry.GREAT_CIRCLE, number).associatedFix(fix).build();
  }

  private static AirspaceSequence arc(int number, Geometry geometry, LatLong start, LatLong center) {
    // Projection derives its radius and bearings from coordinates; these published values are ignored.
    return AirspaceSequence.builder(geometry, number).associatedFix(start).centerFix(center)
        .arcRadius(999.0).arcBearing(250.0).build();
  }
}
