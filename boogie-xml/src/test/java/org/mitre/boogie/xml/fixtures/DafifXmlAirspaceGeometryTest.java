package org.mitre.boogie.xml.fixtures;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.AirspaceSegment;
import org.mitre.boogie.xml.v23_4.generated.BoundaryVia;
import org.mitre.boogie.xml.v23_4.generated.EastWest;
import org.mitre.boogie.xml.v23_4.generated.Location;
import org.mitre.boogie.xml.v23_4.generated.NorthSouth;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.dafif.model.DafifBoundarySegment;
import org.mitre.tdp.boogie.dafif.model.DafifSuasSegment;
import org.mitre.tdp.boogie.dafif.model.enums.Shape;

class DafifXmlAirspaceGeometryTest {

  @Test
  void sortsEdgesAndPlacesArcGeometryAtTheStartingEndpoint() {
    var arc = boundary(10, Shape.CLOCKWISE_ARC, 0, 1, 1, 0)
        .latitude0(0.0).longitude0(0.0).radius1(60.0).bearing1(90.0).bearing2(0.0).build();
    var back = boundary(20, Shape.RHUMB_LINE, 1, 0, 0, 1).build();

    var result = DafifXmlAirspaceGeometry.boundary(List.of(back, arc));

    assertEquals(List.of(1L, 2L, 3L), result.stream().map(AirspaceSegment::getSequenceNumber).toList());
    assertEquals(List.of(BoundaryVia.CLOCKWISE_ARC, BoundaryVia.RHUMB_LINE, BoundaryVia.GREAT_CIRCLE),
        result.stream().map(AirspaceSegment::getBoundaryVia).toList());
    assertLocation(result.get(0).getLocation(), 0, 1);
    assertLocation(result.get(1).getLocation(), 1, 0);
    assertLocation(result.get(2).getLocation(), 0, 1);
    assertLocation(result.get(0).getArcOriginLocation(), 0, 0);
    assertEquals(BigDecimal.valueOf(90.0), result.get(0).getArcBearing());
    assertEquals(BigDecimal.valueOf(60.0), result.get(0).getArcDistance());
    assertNull(result.get(1).getArcBearing());
    assertFalse(Boolean.TRUE.equals(result.get(0).isIsEndOfDescription()));
    assertTrue(result.get(2).isIsEndOfDescription());
    assertEquals("2601", result.get(0).getCycleDate());
  }

  @Test
  void projectsMissingArcEndpointsAndRetainsGeneralizedApproximation() {
    LatLong center = LatLong.of(35.0, -90.0);
    LatLong start = center.projectOut(90.0, 10.0);
    LatLong end = center.projectOut(0.0, 10.0);
    var arc = DafifBoundarySegment.builder().boundaryIdentification("TEST").segmentNumber(10)
        .shape(Shape.COUNTERCLOCKWISE_ARC).latitude0(35.0).longitude0(-90.0)
        .radius1(10.0).bearing1(90.0).bearing2(0.0).cycleDate(202601).build();
    var back = boundary(20, Shape.GENERALIZED, end.latitude(), end.longitude(), start.latitude(), start.longitude()).build();

    var result = DafifXmlAirspaceGeometry.boundary(List.of(arc, back));

    assertEquals(3, result.size());
    assertEquals(BoundaryVia.COUNTER_CLOCKWISE_ARC, result.get(0).getBoundaryVia());
    assertLocation(result.get(0).getLocation(), start.latitude(), start.longitude());
    assertLocation(result.get(1).getLocation(), end.latitude(), end.longitude());
    assertEquals(BoundaryVia.GREAT_CIRCLE, result.get(1).getBoundaryVia());
  }

  @Test
  void derivesMissingArcRadiusAndStartBearingFromCoordinates() {
    var arc = boundary(10, Shape.COUNTERCLOCKWISE_ARC, 0, 1, 1, 0)
        .latitude0(0.0).longitude0(0.0).build();
    var back = boundary(20, Shape.GREAT_CIRCLE, 1, 0, 0, 1).build();

    var result = DafifXmlAirspaceGeometry.boundary(List.of(arc, back));

    assertEquals(90.0, result.get(0).getArcBearing().doubleValue(), 0.000001);
    assertEquals(LatLong.of(0.0, 0.0).distanceInNM(LatLong.of(0.0, 1.0)), result.get(0).getArcDistance().doubleValue(), 0.000001);
  }

  @Test
  void preservesBothEndpointsAcrossSmallSourceCoordinateGaps() {
    var first = boundary(10, Shape.GREAT_CIRCLE, 0, 0, 0, 1).build();
    var second = boundary(20, Shape.GREAT_CIRCLE, 0, 1.0001, 0, 0.0001).build();

    var result = DafifXmlAirspaceGeometry.boundary(List.of(first, second));

    assertEquals(5, result.size());
    assertLocation(result.get(1).getLocation(), 0, 1);
    assertLocation(result.get(2).getLocation(), 0, 1.0001);
    assertLocation(result.get(3).getLocation(), 0, 0.0001);
    assertLocation(result.get(4).getLocation(), 0, 0);
    assertTrue(result.get(4).isIsEndOfDescription());
  }

  @Test
  void preservesCircleCenterAndRadiusWithoutRequiringEndpoints() {
    var circle = DafifSuasSegment.builder().suasIdentification("TEST").segmentNumber(10)
        .shape(Shape.CIRCLE).latitude0(35.0).longitude0(-90.0).radius1(5.0).cycleDate(202601).build();

    var result = DafifXmlAirspaceGeometry.suas(List.of(circle));

    assertEquals(1, result.size());
    assertEquals(BoundaryVia.CIRCLE, result.get(0).getBoundaryVia());
    assertLocation(result.get(0).getArcOriginLocation(), 35, -90);
    assertEquals(BigDecimal.valueOf(5.0), result.get(0).getArcDistance());
    assertNull(result.get(0).getLocation());
    assertTrue(result.get(0).isIsEndOfDescription());
  }

  @Test
  void omitsCompleteUnsupportedAirspacesWithoutInventingConnectionsOrFillingHoles() {
    var circle = DafifSuasSegment.builder().suasIdentification("TEST").segmentNumber(10)
        .shape(Shape.CIRCLE).latitude0(35.0).longitude0(-90.0).radius1(5.0).build();
    var point = circle.toBuilder().shape(Shape.POINT).build();
    assertTrue(DafifXmlAirspaceGeometry.suas(List.of(point)).isEmpty());
    assertTrue(DafifXmlAirspaceGeometry.suas(List.of(circle.toBuilder().radius2(2.0).build())).isEmpty());
    assertTrue(DafifXmlAirspaceGeometry.suas(List.of(circle, circle.toBuilder().segmentNumber(20).build())).isEmpty());
    assertTrue(DafifXmlAirspaceGeometry.boundary(List.of(boundary(10, Shape.GREAT_CIRCLE, 0, 0, 0, 1).build())).isEmpty());
    assertTrue(DafifXmlAirspaceGeometry.boundary(List.of(
        boundary(10, Shape.GREAT_CIRCLE, 0, 0, 0, 1).build(),
        boundary(20, Shape.GREAT_CIRCLE, 0, 2, 0, 0).build())).isEmpty());
  }

  private static DafifBoundarySegment.Builder boundary(int number, Shape shape, double lat1, double lon1, double lat2, double lon2) {
    return DafifBoundarySegment.builder().boundaryIdentification("TEST").segmentNumber(number).shape(shape)
        .latitude1(lat1).longitude1(lon1).latitude2(lat2).longitude2(lon2).cycleDate(202601);
  }

  private static void assertLocation(Location location, double latitude, double longitude) {
    var lat = location.getLatitude();
    double actualLatitude = lat.getDeg() + lat.getMin() / 60.0 + lat.getSec() / 3600.0 + lat.getHSec() / 360000.0;
    if (lat.getNorthSouth() == NorthSouth.SOUTH) actualLatitude = -actualLatitude;
    var lon = location.getLongitude();
    double actualLongitude = lon.getDeg() + lon.getMin() / 60.0 + lon.getSec() / 3600.0 + lon.getHSec() / 360000.0;
    if (lon.getEastWest() == EastWest.WEST) actualLongitude = -actualLongitude;
    assertEquals(latitude, actualLatitude, 0.000002);
    assertEquals(longitude, actualLongitude, 0.000002);
  }
}
