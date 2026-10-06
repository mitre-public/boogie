package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.airspace.fixtures.Lido2609FirUirAirspaces.canariesUir;
import static org.mitre.tdp.boogie.airspace.fixtures.Lido2609FirUirAirspaces.johannesburgOceanic;
import static org.mitre.tdp.boogie.airspace.fixtures.Lido2609FirUirAirspaces.tahiti;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;

/** Regression coverage for the LIDO cycle 2609 polar and rhumb-line projection failures. */
class PolarAndRhumbBoundaryTest {
  @Test
  void johannesburgOceanicRetainsCoverageWhenRhumbBoundaryLeavesSouthPole() {
    AirspaceIndex<String> index = compile(johannesburgOceanic());

    assertInside(index, -40.0, 30.0);
    assertInside(index, -89.5, 30.0);
    assertOutside(index, -89.5, -20.0);
    assertOutside(index, -25.0, 25.0);
  }

  @Test
  void canariesUirClosesRhumbBoundaryWithoutCreatingCrossingAtFirstVertex() {
    AirspaceIndex<String> index = compile(canariesUir());

    assertInside(index, 28.0, -20.0);
    assertInside(index, 29.99, -24.99);
    assertOutside(index, 30.01, -25.01);
    assertOutside(index, 33.0, -20.0);
  }

  @Test
  void tahitiRetainsRhumbCornerWithoutCreatingCrossingAtAdjacentLegJoin() {
    AirspaceIndex<String> index = compile(tahiti());

    assertInside(index, -15.0, -140.0);
    assertInside(index, -29.99, -120.01);
    assertOutside(index, -30.01, -120.01);
    assertOutside(index, -15.0, -119.99);
  }

  private static AirspaceIndex<String> compile(Airspace airspace) {
    return assertDoesNotThrow(() -> new AirspaceIndexFactory<String>().apply(
        List.of(new AirspaceDefinition<>(airspace.identifier(), airspace))));
  }

  private static void assertInside(AirspaceIndex<String> index, double latitude, double longitude) {
    List<AirspaceSpan<String>> spans = query(index, latitude, longitude);
    assertEquals(1, spans.size(), "Expected interior point: " + latitude + ", " + longitude);
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
