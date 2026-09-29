package org.mitre.tdp.boogie.airspace;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.arinc.assemble.ControlledAirspaceAssembler;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.model.ArincControlledAirspaceLeg;
import org.mitre.tdp.boogie.arinc.v18.field.AirspaceType;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryVia;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.dafif.assemble.BoundaryAssembler;
import org.mitre.tdp.boogie.dafif.model.DafifBoundaryParent;
import org.mitre.tdp.boogie.dafif.model.DafifBoundarySegment;
import org.mitre.tdp.boogie.dafif.model.enums.BoundaryType;
import org.mitre.tdp.boogie.dafif.model.enums.Shape;

/** Source adapters must agree on which outgoing edge carries each geometry. */
class AirspaceAssemblyTest {

  @Test
  void arincClosingArcProducesTheEasternHalfOfTheCircle() {
    ArincControlledAirspaceLeg diameter = arincLeg(10, BoundaryVia.G, 1, 0).build();
    ArincControlledAirspaceLeg closingArc = arincLeg(20, BoundaryVia.LE, -1, 0)
        .arcOriginLatitude(0.0).arcOriginLongitude(0.0)
        // Published radius is rounded; the explicit endpoint coordinates define the arc.
        .arcDistance(60.0).arcBearing(180.0).build();

    Airspace airspace = ControlledAirspaceAssembler.standard(ArincDatabaseFactory.emptyFixDatabase())
        .assemble(List.of(closingArc, diameter)).findFirst().orElseThrow();

    assertEasternHalfCircle(airspace);
  }

  @Test
  void dafifClosingArcProducesTheSameEasternHalfOfTheCircle() {
    DafifBoundarySegment diameter = dafifEdge(10, Shape.GREAT_CIRCLE, 1, 0, -1, 0).build();
    DafifBoundarySegment closingArc = dafifEdge(20, Shape.COUNTERCLOCKWISE_ARC, -1, 0, 1, 0)
        .latitude0(0.0).longitude0(0.0).radius1(60.0).bearing1(180.0).bearing2(0.0).build();

    Airspace airspace = assembleDafif(List.of(closingArc, diameter));

    assertAll(
        () -> assertEquals(2, airspace.sequences().size(), "The final arc closes directly to the first starting fix"),
        () -> assertEasternHalfCircle(airspace));
  }

  @Test
  void independentlyPublishedArcEndpointsMayHaveSlightlyDifferentRadii() {
    // The two source endpoints differ in radius by about 0.03 NM. Intermediate arc samples
    // use the starting radius; the next sequence supplies the exact ending fix.
    DafifBoundarySegment diameter = dafifEdge(10, Shape.GREAT_CIRCLE, 1, 0, -1.0005, 0).build();
    DafifBoundarySegment closingArc = dafifEdge(20, Shape.COUNTERCLOCKWISE_ARC, -1.0005, 0, 1, 0)
        .latitude0(0.0).longitude0(0.0).radius1(60.0).bearing1(180.0).bearing2(0.0).build();

    AirspaceSpan<String> crossing = assertEasternHalfCircle(assembleDafif(List.of(diameter, closingArc)));

    assertEquals((2.0 + 1.0005) / 4.0, crossing.exitFraction(), 0.00005);
  }

  @Test
  void dafifSourceCoordinateGapsRemainQueryableAcrossTheClosingEdge() {
    Airspace airspace = assembleDafif(List.of(
        dafifEdge(10, Shape.RHUMB_LINE, -1, -1, -1, 1).build(),
        dafifEdge(20, Shape.GREAT_CIRCLE, -0.9995, 1, 1, 1).build(),
        dafifEdge(30, Shape.RHUMB_LINE, 1, 1, 1, -1).build(),
        dafifEdge(40, Shape.GREAT_CIRCLE, 1, -1, -0.9995, -1).build()));

    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("DAFIF", airspace))).newQuery(spans::add);
    query.intersect(LatLong.of(0.0, -2.0), LatLong.of(0.0, 2.0));

    assertEquals(1, spans.size());
    assertAll(
        () -> assertEquals(6, airspace.sequences().size(), "Four source edges and two short gap connections"),
        () -> assertEquals(0.25, spans.get(0).enterFraction(), 1e-9),
        () -> assertEquals(0.75, spans.get(0).exitFraction(), 1e-9));
  }

  private static AirspaceSpan<String> assertEasternHalfCircle(Airspace airspace) {
    List<AirspaceSpan<String>> spans = new ArrayList<>();
    var query = new AirspaceIndexFactory<String>()
        .apply(List.of(new AirspaceDefinition<>("source", airspace))).newQuery(spans::add);
    query.intersect(LatLong.of(0.0, -2.0), LatLong.of(0.0, 2.0));
    assertEquals(1, spans.size());
    AirspaceSpan<String> crossing = spans.get(0);
    assertAll(
        () -> assertEquals(0.5, crossing.enterFraction(), 1e-9),
        () -> assertEquals(0.75, crossing.exitFraction(), 0.001));

    spans.clear();
    query.intersect(LatLong.of(0.0, 0.75), LatLong.of(0.0, 0.75));
    assertEquals(List.of(new AirspaceSpan<>("source", 0, 1, false)), spans);
    spans.clear();
    query.intersect(LatLong.of(0.0, -0.75), LatLong.of(0.0, -0.75));
    assertTrue(spans.isEmpty(), "Reversing the arc's starting and ending fixes would select the western half");
    return crossing;
  }

  private static ArincControlledAirspaceLeg.Builder arincLeg(int sequence, BoundaryVia via, double latitude, double longitude) {
    return new ArincControlledAirspaceLeg.Builder()
        .recordType(RecordType.S).customerAreaCode(CustomerAreaCode.USA)
        .sectionCode(SectionCode.U).subSectionCode("C").icaoCode("K1")
        .airspaceType(AirspaceType.C).airspaceCenter("TEST").airspaceClassification("C").multipleCode("A")
        .sequenceNumber(sequence).continuationRecordNumber("0").boundaryVia(via)
        .latitude(latitude).longitude(longitude).controlledAirspaceName("SYNTHETIC EAST HALF");
  }

  private static Airspace assembleDafif(List<DafifBoundarySegment> segments) {
    DafifBoundaryParent parent = DafifBoundaryParent.builder()
        .boundaryIdentification("TEST").boundaryType(BoundaryType.CONTROL_AREA).icaoCode("K1").build();
    return BoundaryAssembler.standard().assemble(List.of(parent), segments).findFirst().orElseThrow();
  }

  private static DafifBoundarySegment.Builder dafifEdge(int sequence, Shape shape,
      double latitude1, double longitude1, double latitude2, double longitude2) {
    return DafifBoundarySegment.builder().boundaryIdentification("TEST").segmentNumber(sequence).shape(shape)
        .latitude1(latitude1).longitude1(longitude1).latitude2(latitude2).longitude2(longitude2);
  }
}
