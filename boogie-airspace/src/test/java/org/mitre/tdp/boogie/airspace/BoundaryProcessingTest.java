package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;
import java.util.stream.IntStream;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Geometry;

class BoundaryProcessingTest {
  @Test
  void repairsSourceSequencesBeforeCompilationWithoutChangingTheOriginalAirspace() {
    Airspace.Standard source = boxWithSpur();
    List<? extends AirspaceSequence> original = List.copyOf(source.sequences());
    List<AirspaceSequence> ordered = List.copyOf(source.sequences().stream()
        .sorted(Comparator.comparingInt(AirspaceSequence::sequenceNumber)).toList());
    List<AirspaceSequence> sequences = new BoundaryRetraceResolver(1.0)
        .andThen(new BoundaryArcRadiusResolver(0.05)).apply(ordered);
    Airspace repaired = source.toBuilder().sequences(sequences).build();
    var factory = new AirspaceIndexFactory<String>();
    var originalDefinition = new AirspaceDefinition<>("spur", source);

    assertThrows(IllegalArgumentException.class, () -> factory.apply(List.of(originalDefinition)));
    AirspaceIndex<String> index = factory.apply(List.of(new AirspaceDefinition<>("spur", repaired)));
    assertEquals(List.of(1, 4, 5, 6), sequences.stream().map(AirspaceSequence::sequenceNumber).toList());
    assertEquals(original, source.sequences(), "Repairs must leave source records available for inspection");
    assertBoxCoverage(index);
    assertThrows(IllegalArgumentException.class, () -> factory.apply(List.of(originalDefinition)),
        "Index construction must not automatically repair the original source");
  }

  @Test
  void finalValidationRejectsUnresolvedCrossingsWithTheCallerKey() {
    Airspace.Standard source = polygon(new double[][] {{-1, -1}, {1, 1}, {-1, 1}, {1, -1}});
    IssueResolver<List<AirspaceSequence>, String> unresolved = IssueResolver.of(
        sequences -> List.of("unresolved crossing"), (sequences, issues) -> sequences);
    Airspace repaired = source.toBuilder().sequences(unresolved.apply(List.copyOf(source.sequences()))).build();

    var error = assertThrows(IllegalArgumentException.class, () -> new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("bowtie", repaired))));
    assertTrue(error.getMessage().contains("Cannot compile airspace bowtie"));
    assertTrue(error.getMessage().contains("Invalid airspace boundary"));
  }

  @Test
  void validatesReplacementSequencesBeforeProjection() {
    Airspace.Standard source = boxWithSpur();
    IssueResolver<List<AirspaceSequence>, String> duplicates = IssueResolver.of(
        sequences -> List.of("duplicate sequences"),
        (sequences, issues) -> List.of(sequences.get(0), sequences.get(0), sequences.get(1)));
    Airspace repaired = source.toBuilder().sequences(duplicates.apply(List.copyOf(source.sequences()))).build();

    var error = assertThrows(IllegalArgumentException.class, () -> new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("duplicate replacement", repaired))));
    assertTrue(error.getMessage().contains("duplicate replacement"));
    assertTrue(error.getMessage().contains("Duplicate boundary sequence number"));
  }

  private static Airspace.Standard boxWithSpur() {
    List<AirspaceSequence> sequences = new ArrayList<>(List.of(
        sequence(1, Geometry.GREAT_CIRCLE, -1, -1),
        sequence(2, Geometry.GREAT_CIRCLE, -1, 1),
        sequence(3, Geometry.GREAT_CIRCLE, -0.99999, 1),
        sequence(4, Geometry.RHUMB_LINE, -1, 1),
        sequence(5, Geometry.GREAT_CIRCLE, 1, 1),
        sequence(6, Geometry.GREAT_CIRCLE, 1, -1)));
    Collections.reverse(sequences);
    return Airspace.builder().sequences(sequences).build();
  }

  private static Airspace.Standard polygon(double[][] points) {
    return Airspace.builder().sequences(IntStream.range(0, points.length)
        .mapToObj(i -> sequence(i + 1, Geometry.GREAT_CIRCLE, points[i][0], points[i][1])).toList()).build();
  }

  private static AirspaceSequence sequence(int number, Geometry geometry, double latitude, double longitude) {
    return AirspaceSequence.builder(geometry, number).associatedFix(LatLong.of(latitude, longitude)).build();
  }

  private static void assertBoxCoverage(AirspaceIndex<String> index) {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    index.newQuery(spans::add).intersect(LatLong.of(0.0, -2.0), LatLong.of(0.0, 2.0));
    assertEquals(1, spans.size());
    assertEquals("spur", spans.get(0).airspaceKey());
    assertEquals(0.25, spans.get(0).enterFraction(), 1e-12);
    assertEquals(0.75, spans.get(0).exitFraction(), 1e-12);
    assertFalse(spans.get(0).altitudeChecked());
  }
}
