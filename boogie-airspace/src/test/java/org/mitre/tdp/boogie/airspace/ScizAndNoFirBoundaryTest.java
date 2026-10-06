package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.Geometry;

import com.google.common.collect.Range;

/** Regression boundaries from the LIDO cycle 2609 SCIZ/XX01 assignment failure. */
class ScizAndNoFirBoundaryTest {
  @Test
  void islaDePascuaRetainsSharedRhumbBoundaryAndSouthPoleCoverage() {
    AirspaceIndex<String> index = compile(islaDePascua());

    assertInside(index, "SCIZ", -20.0, -105.0);
    assertInside(index, "SCIZ", -15.01, -105.0);
    assertOutside(index, -14.99, -105.0);
    assertInside(index, "SCIZ", -89.5, -110.0);
    assertOutside(index, -89.5, -140.0);
    assertOutside(index, -89.5, -80.0);
    assertInside(index, "SCIZ", -30.01, -125.0);
    assertOutside(index, -29.99, -125.0);
  }

  @Test
  void noFirRetainsCoverageNorthOfSharedRhumbBoundary() {
    AirspaceIndex<String> index = compile(noFir());

    assertInside(index, "XX01", -5.0, -110.0);
    assertInside(index, "XX01", -14.99, -105.0);
    assertOutside(index, -15.01, -105.0);
    assertOutside(index, -5.0, -80.0);
    assertOutside(index, -5.0, -125.0);
  }

  @Test
  void sharedBoundarySeparatesAirspacesThroughoutThirtyDegreesOfLongitude() {
    assertSharedBoundaryCoverage(compile(islaDePascua(), noFir()));
  }

  @Test
  void reversedBoundariesRetainCoverageWithWestboundSharedRhumbLine() {
    AirspaceIndex<String> index = compile(reverse(islaDePascua()), reverse(noFir()));

    assertSharedBoundaryCoverage(index);
    assertInside(index, "SCIZ", -89.5, -110.0);
    assertOutside(index, -89.5, -140.0);
    assertOutside(index, -89.5, -80.0);
  }

  private static void assertSharedBoundaryCoverage(AirspaceIndex<String> index) {
    // These offsets exceed the small great-circle bow between adjacent 10 NM samples.
    for (double longitude : new double[] {-119.99, -115.0, -105.0, -95.0, -90.01}) {
      assertInside(index, "SCIZ", -15.01, longitude);
      assertInside(index, "XX01", -14.99, longitude);
    }
  }

  private static Airspace islaDePascua() {
    return Airspace.builder()
        .area("SAM")
        .identifier("SCIZ")
        .airspaceType(AirspaceType.FIR)
        .altitudeLimit(Range.all())
        .sequences(List.of(
            sequence(1, Geometry.RHUMB_LINE, -15.0, -120.0),
            sequence(2, Geometry.RHUMB_LINE, -15.0, -90.0),
            sequence(3, Geometry.RHUMB_LINE, -18.35, -90.0),
            sequence(4, Geometry.RHUMB_LINE, -28.5, -90.0),
            sequence(5, Geometry.RHUMB_LINE, -38.5, -90.0),
            sequence(6, Geometry.RHUMB_LINE, -47.0, -90.0),
            sequence(7, Geometry.RHUMB_LINE, -90.0, -90.0),
            sequence(8, Geometry.RHUMB_LINE, -90.0, -131.0),
            sequence(9, Geometry.RHUMB_LINE, -60.0, -131.0),
            sequence(10, Geometry.RHUMB_LINE, -30.0, -131.0),
            sequence(11, Geometry.RHUMB_LINE, -30.0, -120.0)))
        .build();
  }

  private static Airspace noFir() {
    return Airspace.builder()
        .area("SPA")
        .identifier("XX01")
        .airspaceType(AirspaceType.FIR)
        .altitudeLimit(Range.all())
        .sequences(List.of(
            sequence(1, Geometry.GREAT_CIRCLE, 10.0, -104.5),
            sequence(2, Geometry.RHUMB_LINE, 5.0, -120.0),
            sequence(3, Geometry.RHUMB_LINE, 3.5, -120.0),
            sequence(4, Geometry.RHUMB_LINE, 0.08333333333333333, -120.0),
            sequence(5, Geometry.RHUMB_LINE, -15.0, -120.0),
            sequence(6, Geometry.RHUMB_LINE, -15.0, -90.0),
            sequence(7, Geometry.RHUMB_LINE, -3.4, -90.0),
            sequence(8, Geometry.RHUMB_LINE, -3.4, -92.0),
            sequence(9, Geometry.GREAT_CIRCLE, 1.4166666666666667, -92.0)))
        .build();
  }

  private static AirspaceSequence sequence(int number, Geometry geometry, double latitude, double longitude) {
    return AirspaceSequence.builder(geometry, number)
        .associatedFix(LatLong.of(latitude, longitude))
        .build();
  }

  private static Airspace reverse(Airspace airspace) {
    List<? extends AirspaceSequence> original = airspace.sequences();
    List<AirspaceSequence> reversed = new ArrayList<>();
    for (int i = original.size() - 1; i >= 0; i--) {
      // The preceding source leg becomes this vertex's outgoing leg in the reverse traversal.
      Geometry geometry = original.get((i + original.size() - 1) % original.size()).geometry();
      reversed.add(AirspaceSequence.builder(geometry, reversed.size() + 1)
          .associatedFix(original.get(i).associatedFix().orElseThrow())
          .build());
    }
    return Airspace.builder()
        .area(airspace.area())
        .identifier(airspace.identifier())
        .airspaceType(airspace.airspaceType())
        .altitudeLimit(airspace.altitudeLimit())
        .sequences(reversed)
        .build();
  }

  private static AirspaceIndex<String> compile(Airspace... airspaces) {
    return assertDoesNotThrow(() -> new AirspaceIndexFactory<String>().apply(
        Arrays.stream(airspaces)
            .map(airspace -> new AirspaceDefinition<>(airspace.identifier(), airspace))
            .toList()));
  }

  private static void assertInside(AirspaceIndex<String> index, String identifier, double latitude, double longitude) {
    List<AirspaceSpan<String>> spans = query(index, latitude, longitude);
    assertEquals(1, spans.size(), "Expected only " + identifier + " at " + latitude + ", " + longitude);
    assertEquals(identifier, spans.get(0).airspaceKey());
    assertEquals(0.0, spans.get(0).enterFraction(), 1e-12);
    assertEquals(1.0, spans.get(0).exitFraction(), 1e-12);
    assertFalse(spans.get(0).altitudeChecked());
  }

  private static void assertOutside(AirspaceIndex<String> index, double latitude, double longitude) {
    assertTrue(query(index, latitude, longitude).isEmpty(),
        "Expected exterior point: " + latitude + ", " + longitude);
  }

  private static List<AirspaceSpan<String>> query(AirspaceIndex<String> index, double latitude, double longitude) {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    LatLong position = LatLong.of(latitude, longitude);
    index.newQuery(spans::add).intersect(position, position);
    return spans;
  }
}
