package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.stream.IntStream;

import org.junit.jupiter.api.TestReporter;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.airspace.fixtures.BoiseClassCAirspace;
import org.mitre.tdp.boogie.projections.BoundaryProjection;

import com.google.common.geometry.S2Error;
import com.google.common.geometry.S2Loop;
import com.google.common.geometry.S2Point;
import com.google.common.geometry.S2Polygon;

class BoundaryCompilerTest {

  @ParameterizedTest(name = "Boise Class C: {0} NM / {1} degrees, {2} samples with one closing duplicate")
  @CsvSource({"10.0, 10.0, 22", "1.0, 1.0, 185"})
  void realBoundaryOnlyRepeatsItsPublishedClosingFix(double lineStepNm, double arcStepDegrees, int expectedProjectedPoints, TestReporter reporter) {
    Airspace airspace = BoiseClassCAirspace.outerShelf();
    List<? extends AirspaceSequence> sequences = airspace.sequences();
    List<LatLong> sourceFixes = sequences.stream().map(sequence -> sequence.associatedFix().orElseThrow()).toList();

    assertEquals(List.of(10, 20, 30, 40, 50, 60), sequences.stream().map(AirspaceSequence::sequenceNumber).toList());
    assertDuplicates("Source fixes", sourceFixes, List.of(new Duplicate(0, 5)), reporter);

    List<LatLong> projected = new ArrayList<>();
    for (int i = 0; i < sequences.size(); i++) {
      AirspaceSequence current = sequences.get(i);
      AirspaceSequence next = sequences.get((i + 1) % sequences.size());
      List<LatLong> samples = BoundaryProjection.project(current, next, lineStepNm, arcStepDegrees);
      String stage = "Projected sequence " + current.sequenceNumber() + " (" + current.geometry() + ")";

      assertDuplicates(stage, samples, List.of(), reporter);
      assertEquals(current.associatedFix().orElseThrow(), samples.get(0), stage + " must retain the published starting fix");
      if (i == sequences.size() - 1) {
        // The source's GE record explicitly repeats sequence 10; its outgoing edge has zero length.
        assertEquals(List.of(sourceFixes.get(0)), samples, "The closing record contributes only its published fix");
      } else {
        assertFalse(samples.contains(next.associatedFix().orElseThrow()), stage + " must leave the endpoint to the next sequence");
      }
      projected.addAll(samples);
    }

    assertEquals(expectedProjectedPoints, projected.size(), "Total samples from all six source sequences");
    List<Duplicate> closingFixOnly = List.of(new Duplicate(0, projected.size() - 1));
    assertDuplicates("Concatenated projections", projected, closingFixOnly, reporter);

    List<S2Point> spherical = projected.stream().map(BoundaryCompiler::point).toList();
    assertDuplicates("S2 conversion", spherical, closingFixOnly, reporter);

    S2Polygon polygon = new BoundaryCompiler(lineStepNm, arcStepDegrees).apply(airspace);
    assertEquals(1, polygon.numLoops());
    S2Loop loop = polygon.loop(0);
    List<S2Point> compiled = IntStream.range(0, loop.numVertices()).mapToObj(loop::vertex).toList();
    assertDuplicates("Compiled polygon", compiled, List.of(), reporter);

    S2Error error = new S2Error();
    assertAll(
        () -> assertEquals(spherical.size() - 1, compiled.size(), "Compilation removes exactly the source's closing duplicate"),
        // Normalizing the loop may reverse its orientation, but must preserve every distinct sample.
        () -> assertEquals(new HashSet<>(spherical), new HashSet<>(compiled), "Compilation must preserve the projected boundary"),
        () -> assertFalse(loop.findValidationError(error), error::text),
        () -> assertTrue(polygon.getArea() > 0.0));
  }

  private static <T> void assertDuplicates(String stage, List<T> points, List<Duplicate> expected, TestReporter reporter) {
    Map<T, Integer> firstIndex = new HashMap<>();
    List<Duplicate> duplicates = new ArrayList<>();
    for (int i = 0; i < points.size(); i++) {
      Integer previous = firstIndex.putIfAbsent(points.get(i), i);
      if (previous != null) {
        duplicates.add(new Duplicate(previous, i));
      }
    }
    String counts = "points=" + points.size() + ", unique=" + firstIndex.size() + ", duplicates=" + duplicates;
    reporter.publishEntry(stage, counts);
    assertEquals(expected, duplicates, stage + ": " + counts);
  }

  private record Duplicate(int firstIndex, int repeatedIndex) {}
}
