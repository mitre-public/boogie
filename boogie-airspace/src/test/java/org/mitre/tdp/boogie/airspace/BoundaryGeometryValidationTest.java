package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.EnumSource;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Geometry;

class BoundaryGeometryValidationTest {
  private static final LatLong CENTER = LatLong.of(0.0, 0.0);
  private static final LatLong FIX = LatLong.of(1.0, 0.0);
  private static final String KEY = "invalid source geometry";
  // Coarse steps ensure geometry checks also run when an arc would emit only its starting fix.
  private final AirspaceIndexFactory<String> factory = new AirspaceIndexFactory<>(1000.0, 179.0);

  @ParameterizedTest
  @EnumSource(value = Geometry.class, mode = EnumSource.Mode.EXCLUDE, names = "CIRCLE")
  void requiresBothEndpointFixes(Geometry geometry) {
    assertInvalid(boundary(geometry, null, CENTER, FIX), "Missing starting fix");
    assertInvalid(boundary(geometry, FIX, CENTER, null), "Missing ending fix");
  }

  @ParameterizedTest
  @EnumSource(value = Geometry.class, mode = EnumSource.Mode.EXCLUDE, names = "CIRCLE")
  void validatesSourceEndpointCoordinatesBeforeProjection(Geometry geometry) {
    for (LatLong invalid : invalidCoordinates()) {
      assertInvalid(boundary(geometry, invalid, CENTER, FIX), "Expected finite latitude/longitude");
      assertInvalid(boundary(geometry, FIX, CENTER, invalid), "Expected finite latitude/longitude");
    }
  }

  @ParameterizedTest
  @EnumSource(value = Geometry.class, names = {"CLOCKWISE_ARC", "COUNTER_CLOCKWISE_ARC", "CIRCLE"})
  void requiresACenterFix(Geometry geometry) {
    assertInvalid(centeredBoundary(geometry, null), "Missing center fix");
  }

  @ParameterizedTest
  @EnumSource(value = Geometry.class, names = {"CLOCKWISE_ARC", "COUNTER_CLOCKWISE_ARC", "CIRCLE"})
  void validatesCenterCoordinatesEvenThoughTheCenterIsNotAPolygonVertex(Geometry geometry) {
    for (LatLong invalid : invalidCoordinates()) {
      assertInvalid(centeredBoundary(geometry, invalid), "Expected finite latitude/longitude");
    }
  }

  @ParameterizedTest
  @EnumSource(value = Geometry.class, names = {"CLOCKWISE_ARC", "COUNTER_CLOCKWISE_ARC"})
  void rejectsUndefinedArcBearingsAtEitherEndpoint(Geometry geometry) {
    for (LatLong endpoint : List.of(CENTER, LatLong.of(0.0, 180.0), LatLong.of(0.0, 180.0 - 1e-12))) {
      assertInvalid(boundary(geometry, endpoint, CENTER, FIX), "Arc starting fix");
      assertInvalid(boundary(geometry, FIX, CENTER, endpoint), "Arc ending fix");
    }
    // Pole longitude labels identify the same position and cannot define an arc bearing.
    assertInvalid(boundary(geometry, LatLong.of(90.0, 120.0), LatLong.of(90.0, 0.0), FIX), "Arc starting fix");
    assertInvalid(boundary(geometry, FIX, LatLong.of(90.0, 0.0), LatLong.of(-90.0, 120.0)), "Arc ending fix");
  }

  @ParameterizedTest
  @EnumSource(value = Geometry.class, names = {"CLOCKWISE_ARC", "COUNTER_CLOCKWISE_ARC"})
  void acceptsUnequalCoordinateDerivedRadiiAndIgnoresPublishedArcParameters(Geometry geometry) {
    Airspace source = Airspace.builder().sequences(List.of(
        AirspaceSequence.builder(geometry, 1).associatedFix(LatLong.of(-1.0005, 0.0))
            .centerFix(CENTER).arcRadius(Double.NaN).arcBearing(Double.NaN).build(),
        line(2, Geometry.GREAT_CIRCLE, FIX))).build();
    // Use normal sampling so this semicircle has enough vertices for polygon validation.
    assertDoesNotThrow(() -> new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("rounded arc", source))));
  }

  @Test
  void keepsRhumbPoleMeridiansExplicitAndAcceptsEquivalentDateLineLongitudes() {
    assertInvalid(List.of(
        line(1, Geometry.RHUMB_LINE, LatLong.of(90.0, 0.0)),
        line(2, Geometry.GREAT_CIRCLE, LatLong.of(-90.0, 10.0)),
        line(3, Geometry.GREAT_CIRCLE, LatLong.of(0.0, 90.0))), "Opposite poles need a common longitude");

    Airspace source = Airspace.builder().sequences(List.of(
        line(1, Geometry.RHUMB_LINE, LatLong.of(90.0, 180.0)),
        line(2, Geometry.GREAT_CIRCLE, LatLong.of(-90.0, -180.0)),
        line(3, Geometry.GREAT_CIRCLE, LatLong.of(0.0, 90.0)))).build();
    assertDoesNotThrow(() -> factory.apply(List.of(new AirspaceDefinition<>("polar meridian", source))));
  }

  @Test
  void requiresAFinitePositiveCircleRadiusSmallerThanAHemisphere() {
    for (Double radius : new Double[] {null, 0.0, -1.0, Double.NaN, Double.POSITIVE_INFINITY,
        Math.PI * Spherical.EARTH_RADIUS_NM / 2.0}) {
      assertInvalid(List.of(AirspaceSequence.builder(Geometry.CIRCLE, 1)
          .centerFix(CENTER).arcRadius(radius).build()), "Circle radius must be finite, positive");
    }
  }

  private void assertInvalid(List<AirspaceSequence> sequences, String message) {
    var error = assertThrows(IllegalArgumentException.class, () -> factory.apply(List.of(
        new AirspaceDefinition<>(KEY, Airspace.builder().sequences(sequences).build()))));
    assertTrue(error.getMessage().contains("Cannot compile airspace " + KEY), error::getMessage);
    assertTrue(error.getMessage().contains(message), error::getMessage);
  }

  private static List<AirspaceSequence> centeredBoundary(Geometry geometry, LatLong center) {
    return geometry == Geometry.CIRCLE
        ? List.of(AirspaceSequence.builder(geometry, 1).centerFix(center).arcRadius(60.0).build())
        : boundary(geometry, FIX, center, LatLong.of(0.0, 1.0));
  }

  private static List<AirspaceSequence> boundary(Geometry geometry, LatLong start, LatLong center, LatLong end) {
    return List.of(
        AirspaceSequence.builder(geometry, 1).associatedFix(start).centerFix(center).build(),
        line(2, Geometry.GREAT_CIRCLE, end),
        line(3, Geometry.GREAT_CIRCLE, LatLong.of(2.0, 2.0)));
  }

  private static AirspaceSequence line(int number, Geometry geometry, LatLong fix) {
    return AirspaceSequence.builder(geometry, number).associatedFix(fix).build();
  }

  private static List<LatLong> invalidCoordinates() {
    // LatLong constructors reject invalid coordinates; test custom/deserialized source values.
    return List.of(invalidCoordinate(Double.NaN, 0.0), invalidCoordinate(Double.POSITIVE_INFINITY, 0.0),
        invalidCoordinate(91.0, 0.0), invalidCoordinate(0.0, Double.NaN),
        invalidCoordinate(0.0, Double.NEGATIVE_INFINITY), invalidCoordinate(0.0, 181.0));
  }

  private static LatLong invalidCoordinate(double latitude, double longitude) {
    LatLong position = mock(LatLong.class);
    when(position.latitude()).thenReturn(latitude);
    when(position.longitude()).thenReturn(longitude);
    return position;
  }
}
