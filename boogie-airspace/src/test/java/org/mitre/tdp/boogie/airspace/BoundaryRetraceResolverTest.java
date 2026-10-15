package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.airspace.BoundaryRetraceIdentifier.Retrace;
import org.mitre.tdp.boogie.airspace.fixtures.BoiseClassCAirspace;

class BoundaryRetraceResolverTest {
  private final BoundaryRetraceResolver resolver = new BoundaryRetraceResolver(1.0);

  @Test
  void inspectionReportsSequenceNumbersAndLengthWithoutChangingSource() {
    List<AirspaceSequence> source = boxWithSpur(Geometry.GREAT_CIRCLE);
    List<AirspaceSequence> original = List.copyOf(source);
    List<Retrace> issues = resolver.identify(source);

    assertEquals(List.of(new Retrace(2, 3, 4,
        source.get(1).associatedFix().orElseThrow().distanceInNM(source.get(2).associatedFix().orElseThrow()))), issues);
    assertEquals(original, source);
    assertThrows(UnsupportedOperationException.class, issues::clear);

    List<AirspaceSequence> repaired = resolver.resolve(source, issues);
    assertEquals(List.of(1, 4, 5, 6), numbers(repaired));
    assertEquals(original, source);
    assertThrows(UnsupportedOperationException.class, repaired::clear);
  }

  @Test
  void standaloneIdentifierCanBeCombinedWithAReportingPolicy() {
    List<AirspaceSequence> source = boxWithSpur(Geometry.GREAT_CIRCLE);
    List<Retrace> reported = new ArrayList<>();
    IssueResolver<List<AirspaceSequence>, Retrace> reporting = IssueResolver.of(
        new BoundaryRetraceIdentifier(), (sequences, issues) -> {
          reported.addAll(issues);
          return sequences;
        });

    assertSame(source, reporting.apply(source));
    assertEquals(resolver.identify(source), reported);
    assertEquals(6, source.size());
  }

  @Test
  void retainsReturningRecordsIncludingOutgoingArcParametersAndSourceMetadata() {
    List<AirspaceSequence> source = new ArrayList<>(boxWithSpur(Geometry.GREAT_CIRCLE));
    AirspaceSequence resumed = AirspaceSequence.record("published return", AirspaceSequence
        .builder(Geometry.CLOCKWISE_ARC, 4).associatedFix(LatLong.of(-1.0, 1.0))
        .centerFix(LatLong.of(0.0, 1.0)).arcRadius(60.0).arcBearing(180.0).build());
    source.set(3, resumed);

    List<AirspaceSequence> repaired = resolver.apply(source);

    assertEquals(List.of(1, 4, 5, 6), numbers(repaired));
    assertSame(resumed, repaired.get(1));
    assertSame(resumed, source.get(3));
  }

  @ParameterizedTest
  @EnumSource(value = Geometry.class, names = {"GREAT_CIRCLE", "RHUMB_LINE"})
  void repairsRetracesAtEveryRingPositionAndPreservesQueryableCoverage(Geometry geometry) {
    List<AirspaceSequence> original = boxWithSpur(geometry);
    for (int rotation = 0; rotation < original.size(); rotation++) {
      List<AirspaceSequence> ordered = rotate(original, rotation);
      List<AirspaceSequence> source = new ArrayList<>(ordered);
      Collections.reverse(source); // Preprocessing uses traversal order while leaving source records unchanged.
      var definition = new AirspaceDefinition<>("spur", Airspace.builder().sequences(source).build());
      var strict = new AirspaceIndexFactory<String>();
      assertThrows(IllegalArgumentException.class, () -> strict.apply(List.of(definition)));

      List<AirspaceSequence> repaired = resolver.apply(ordered);
      AirspaceIndex<String> index = strict.apply(List.of(new AirspaceDefinition<>("spur",
          Airspace.builder().sequences(repaired).build())));
      assertEquals(ordered.size() - 2, repaired.size());
      assertBoxCoverage(index);
      assertEquals(source, definition.airspace().sequences());
      assertThrows(IllegalArgumentException.class, () -> strict.apply(List.of(definition)));
    }
  }

  @Test
  void repairsNestedAndOverlappingRetracesUntilStable() {
    List<List<AirspaceSequence>> sources = List.of(
        boundary(new double[][] {{-1, -1}, {-1, 1}, {-0.99999, 1}, {-0.99998, 1},
            {-0.99999, 1}, {-1, 1}, {1, 1}, {1, -1}}),
        boundary(new double[][] {{-1, -1}, {-1, 1}, {-0.99999, 1}, {-1, 1},
            {-0.99999, 1}, {-1, 1}, {1, 1}, {1, -1}}));
    for (List<AirspaceSequence> source : sources) {
      for (int rotation = 0; rotation < source.size(); rotation++) {
        List<AirspaceSequence> ordered = rotate(source, rotation);
        List<AirspaceSequence> repaired = resolver.apply(ordered);
        assertEquals(4, repaired.size());
        assertTrue(resolver.identify(repaired).isEmpty());
        assertSame(repaired, resolver.apply(repaired));
        assertBoxCoverage(new AirspaceIndexFactory<String>()
            .apply(List.of(new AirspaceDefinition<>("spur", Airspace.builder().sequences(repaired).build()))));
      }
    }
  }

  @Test
  void explicitResolutionRepairsOneRetraceAndApplyResolvesTheRemainingRetraces() {
    List<AirspaceSequence> source = boundary(new double[][] {
        {-1, -1}, {-1, 1}, {-0.99999, 1}, {-1, 1},
        {1, 1}, {1, -1}, {1.00001, -1}, {1, -1}});
    List<Retrace> issues = resolver.identify(source);
    assertEquals(2, issues.size());

    List<AirspaceSequence> onePass = resolver.resolve(source, issues);
    assertEquals(List.of(1, 4, 5, 6, 7, 8), numbers(onePass));
    assertEquals(List.of(issues.get(1)), resolver.identify(onePass));

    List<AirspaceSequence> repaired = resolver.apply(source);
    assertEquals(List.of(1, 4, 5, 8), numbers(repaired));
    assertSame(repaired, resolver.apply(repaired));
    assertBoxCoverage(new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("spur", Airspace.builder().sequences(repaired).build()))));
  }

  @Test
  void overlappingRetracesPreserveTheReturningRecordsContinuingBoundaryEdge() {
    List<AirspaceSequence> source = boundary(new double[][] {
        {-1, -1}, {-1, 1}, {-0.99999, 1}, {-1, 1}, {-0.99999, 1}, {1, 1}, {1, -1}});
    List<Retrace> issues = resolver.identify(source);
    assertEquals(2, issues.size());

    List<AirspaceSequence> repaired = resolver.apply(source);
    assertEquals(List.of(1, 4, 5, 6, 7), numbers(repaired));
    assertSame(source.get(3), repaired.get(1));
    assertSame(source.get(4), repaired.get(2));
    assertTrue(resolver.identify(repaired).isEmpty());
    assertEquals(repaired, resolver.resolve(source, issues));
    assertBoxCoverage(new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("spur", Airspace.builder().sequences(repaired).build()))));
  }

  @Test
  void reportsLongRetracesButLeavesThemForCompilationValidation() {
    List<AirspaceSequence> source = boundary(new double[][] {
        {-1, -1}, {-1, 1}, {0, 1}, {-1, 1}, {1, 1}, {1, -1}});

    assertEquals(1, resolver.identify(source).size());
    assertTrue(resolver.identify(source).get(0).lengthNm() > 1.0);
    assertSame(source, resolver.apply(source));
    var error = assertThrows(IllegalArgumentException.class, () -> new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("long spur", Airspace.builder().sequences(resolver.apply(source)).build()))));
    assertTrue(error.getMessage().contains("Cannot compile airspace long spur"));
  }

  @ParameterizedTest
  @EnumSource(value = Geometry.class, names = {"GREAT_CIRCLE", "RHUMB_LINE"})
  void treatsTheLengthLimitAsInclusive(Geometry geometry) {
    List<AirspaceSequence> source = boxWithSpur(geometry);
    double length = resolver.identify(source).get(0).lengthNm();
    assertEquals(4, new BoundaryRetraceResolver(length).apply(source).size());
    assertSame(source, new BoundaryRetraceResolver(Math.nextDown(length)).apply(source));
  }

  @Test
  void explicitResolutionUsesOnlySuppliedFindingsAndRechecksTheirGeometry() {
    List<AirspaceSequence> source = boxWithSpur(Geometry.GREAT_CIRCLE);
    List<Retrace> issues = resolver.identify(source);
    assertSame(source, resolver.resolve(source, List.of()));
    List<AirspaceSequence> changed = new ArrayList<>(source);
    changed.set(3, sequence(4, Geometry.GREAT_CIRCLE, -0.999999, 1));
    assertSame(changed, resolver.resolve(changed, issues));
  }

  @Test
  void leavesCleanRingsClosingDuplicatesAndCirclesAlone() {
    List<AirspaceSequence> box = boundary(new double[][] {{-1, -1}, {-1, 1}, {1, 1}, {1, -1}});
    List<AirspaceSequence> closingDuplicate = new ArrayList<>(box);
    closingDuplicate.add(sequence(5, Geometry.GREAT_CIRCLE, -1, -1));
    List<AirspaceSequence> circle = List.of(AirspaceSequence.builder(Geometry.CIRCLE, 1)
        .centerFix(LatLong.of(0.0, 0.0)).arcRadius(1.0).build());
    for (List<AirspaceSequence> source : List.<List<AirspaceSequence>>of(box, closingDuplicate, circle,
        List.copyOf(BoiseClassCAirspace.outerShelf().sequences()))) {
      assertTrue(resolver.identify(source).isEmpty());
      assertSame(source, resolver.apply(source));
    }
  }

  @Test
  void leavesBoundariesWithoutThreeSequencesForCompilationValidation() {
    List<AirspaceSequence> source = boxWithSpur(Geometry.GREAT_CIRCLE);
    for (int size = 0; size < 3; size++) {
      List<AirspaceSequence> boundary = source.subList(0, size);
      assertTrue(resolver.identify(boundary).isEmpty());
      assertSame(boundary, resolver.apply(boundary));
    }
  }

  @Test
  void repeatedFixesDoNotEstablishARetraceForDifferentPathsOrNearReturns() {
    for (Geometry geometry : List.of(Geometry.RHUMB_LINE, Geometry.CLOCKWISE_ARC, Geometry.COUNTER_CLOCKWISE_ARC)) {
      List<AirspaceSequence> source = new ArrayList<>(boxWithSpur(Geometry.GREAT_CIRCLE));
      source.set(2, sequence(3, geometry, -0.99999, 1));
      assertTrue(resolver.identify(source).isEmpty());
      assertSame(source, resolver.apply(source));
    }
    List<AirspaceSequence> nearReturn = new ArrayList<>(boxWithSpur(Geometry.GREAT_CIRCLE));
    nearReturn.set(3, sequence(4, Geometry.GREAT_CIRCLE, -0.999999, 1));
    assertTrue(resolver.identify(nearReturn).isEmpty());
    assertSame(nearReturn, resolver.apply(nearReturn));
  }

  @Test
  void leavesAntipodalGreatCirclesAndMissingFixesForValidation() {
    List<AirspaceSequence> antipodal = boundary(new double[][] {{0, 0}, {0, 180}, {0, 0}, {1, 1}, {1, -1}});
    assertTrue(resolver.identify(antipodal).isEmpty());
    assertSame(antipodal, new BoundaryRetraceResolver(20_000).apply(antipodal));

    List<AirspaceSequence> missing = new ArrayList<>(boxWithSpur(Geometry.GREAT_CIRCLE));
    missing.set(2, AirspaceSequence.builder(Geometry.GREAT_CIRCLE, 3).build());
    assertTrue(resolver.identify(missing).isEmpty());
    assertSame(missing, resolver.apply(missing));
  }

  @Test
  void measuresRhumbRetracesAcrossTheDateLineAlongTheShortPath() {
    List<AirspaceSequence> source = List.of(
        sequence(1, Geometry.RHUMB_LINE, 50.0, 179.99999),
        sequence(2, Geometry.RHUMB_LINE, 50.0, -179.99999),
        sequence(3, Geometry.GREAT_CIRCLE, 50.0, 179.99999),
        sequence(4, Geometry.GREAT_CIRCLE, 51.0, 179.0),
        sequence(5, Geometry.GREAT_CIRCLE, 49.0, 179.0));
    // Rhumb projection measures one degree of angular distance as 60 nautical miles.
    double expectedLengthNm = 60.0 * Math.cos(Math.toRadians(50.0)) * 0.00002;

    assertEquals(1, resolver.identify(source).size());
    assertEquals(expectedLengthNm, resolver.identify(source).get(0).lengthNm(), 1e-9);
    assertEquals(List.of(3, 4, 5), numbers(resolver.apply(source)));
  }

  @Test
  void leavesAmbiguousOppositePoleRhumbEdgesForValidationEvenWithALargeLimit() {
    List<AirspaceSequence> source = List.of(
        sequence(1, Geometry.RHUMB_LINE, -90.0, 0.0),
        sequence(2, Geometry.RHUMB_LINE, 90.0, 10.0),
        sequence(3, Geometry.GREAT_CIRCLE, -90.0, 0.0),
        sequence(4, Geometry.GREAT_CIRCLE, -80.0, 20.0),
        sequence(5, Geometry.GREAT_CIRCLE, -80.0, -20.0));
    var largeLimit = new BoundaryRetraceResolver(20_000.0);
    assertTrue(largeLimit.identify(source).isEmpty());
    assertSame(source, largeLimit.apply(source));
    var error = assertThrows(IllegalArgumentException.class, () -> new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("ambiguous meridian", Airspace.builder().sequences(largeLimit.apply(source)).build()))));
    assertTrue(error.getMessage().contains("Opposite poles need a common longitude"));
  }

  @Test
  void rejectsInvalidRepairLimits() {
    for (double limit : new double[] {0.0, -1.0, Double.NaN, Double.POSITIVE_INFINITY, Double.NEGATIVE_INFINITY}) {
      assertThrows(IllegalArgumentException.class, () -> new BoundaryRetraceResolver(limit));
    }
  }

  private static List<AirspaceSequence> boxWithSpur(Geometry geometry) {
    return List.of(sequence(1, Geometry.GREAT_CIRCLE, -1, -1),
        sequence(2, geometry, -1, 1), sequence(3, geometry, -0.99999, 1),
        sequence(4, Geometry.RHUMB_LINE, -1, 1), sequence(5, Geometry.GREAT_CIRCLE, 1, 1),
        sequence(6, Geometry.GREAT_CIRCLE, 1, -1));
  }

  private static List<AirspaceSequence> boundary(double[][] points) {
    return IntStream.range(0, points.length)
        .mapToObj(i -> sequence(i + 1, Geometry.GREAT_CIRCLE, points[i][0], points[i][1])).toList();
  }

  private static List<AirspaceSequence> rotate(List<AirspaceSequence> sequences, int offset) {
    return IntStream.range(0, sequences.size()).mapToObj(i -> {
      AirspaceSequence source = sequences.get((i + offset) % sequences.size());
      return (AirspaceSequence) AirspaceSequence.builder(source.geometry(), i + 1)
          .associatedFix(source.associatedFix().orElse(null)).build();
    }).toList();
  }

  private static AirspaceSequence sequence(int number, Geometry geometry, double latitude, double longitude) {
    return AirspaceSequence.builder(geometry, number).associatedFix(LatLong.of(latitude, longitude)).build();
  }

  private static List<Integer> numbers(List<AirspaceSequence> sequences) {
    return sequences.stream().map(AirspaceSequence::sequenceNumber).toList();
  }

  private static void assertBoxCoverage(AirspaceIndex<String> index) {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    index.newQuery(spans::add).intersect(LatLong.of(0.0, -2.0), LatLong.of(0.0, 2.0));
    assertEquals(1, spans.size());
    assertEquals("spur", spans.get(0).airspaceKey());
    assertEquals(0.25, spans.get(0).enterFraction(), 1e-12);
    assertEquals(0.75, spans.get(0).exitFraction(), 1e-12);
  }
}
