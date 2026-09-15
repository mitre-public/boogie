package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.AirspaceType;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.dafif.model.DafifBoundaryParent;
import org.mitre.tdp.boogie.dafif.model.DafifBoundarySegment;
import org.mitre.tdp.boogie.dafif.model.DafifSuasParent;
import org.mitre.tdp.boogie.dafif.model.DafifSuasSegment;
import org.mitre.tdp.boogie.dafif.model.enums.BoundaryType;
import org.mitre.tdp.boogie.dafif.model.enums.Derivation;
import org.mitre.tdp.boogie.dafif.model.enums.Shape;
import org.mitre.tdp.boogie.dafif.model.enums.SpecialUseAirspaceType;

import com.google.common.collect.Range;

class AirspaceAssemblerTest {

  @Test
  void groupsBoundariesAndRetainsParentMetadata() {
    List<DafifBoundaryParent> parents = List.of(parent("US00001", BoundaryType.FIR), parent("US00002", BoundaryType.UIR), parent("US00003", BoundaryType.CONTROL_AREA));
    List<DafifBoundarySegment> segments = List.of(circle("US00003"), circle("US00001"), circle("US00002"));

    List<Airspace> airspaces = BoundaryAssembler.standard().assemble(parents, segments).toList();

    assertEquals(List.of("US00001", "US00002", "US00003"), airspaces.stream().map(Airspace::identifier).toList());
    assertEquals(List.of(AirspaceType.FIR, AirspaceType.UIR, AirspaceType.CONTROLLED), airspaces.stream().map(Airspace::airspaceType).toList());
    assertEquals("KZ", airspaces.get(0).area());
    assertEquals(Range.closed(1000.0, 18000.0), airspaces.get(0).altitudeLimit());
    assertEquals(LatLong.of(35.0, -100.0), airspaces.get(0).sequences().get(0).centerFix().orElseThrow());
    assertEquals(12.5, airspaces.get(0).sequences().get(0).arcRadius().orElseThrow());
  }

  @Test
  void ordersSegmentsAndRetainsBothEndsOfEveryEdge() {
    List<DafifBoundarySegment> segments = List.of(
        edge(30, Shape.GENERALIZED, 1, 1, 0, 0), edge(10, Shape.GREAT_CIRCLE, 0, 0, 0, 1), edge(20, Shape.RHUMB_LINE, 0, 1, 1, 1));

    Airspace airspace = BoundaryAssembler.standard().assemble(List.of(parent("US00001", BoundaryType.FIR)), segments).findFirst().orElseThrow();

    assertEquals(List.of(LatLong.of(0.0, 0.0), LatLong.of(0.0, 1.0), LatLong.of(1.0, 1.0), LatLong.of(0.0, 0.0)), points(airspace));
    assertEquals(List.of(0, 1, 2, 3), airspace.sequences().stream().map(AirspaceSequence::sequenceNumber).toList());
    assertEquals(List.of(Geometry.GREAT_CIRCLE, Geometry.GREAT_CIRCLE, Geometry.RHUMB_LINE, Geometry.GREAT_CIRCLE),
        airspace.sequences().stream().map(AirspaceSequence::geometry).toList());
  }

  @Test
  void preservesArcDirectionCenterRadiusAndEndingBearing() {
    DafifBoundarySegment clockwise = edge(10, Shape.CLOCKWISE_ARC, 1, 0, 0, 1).toBuilder()
        .latitude0(0.0).longitude0(0.0).radius1(60.0).bearing1(360.0).bearing2(90.0).build();
    DafifBoundarySegment counterclockwise = edge(20, Shape.COUNTERCLOCKWISE_ARC, 0, 1, 1, 0).toBuilder()
        .latitude0(0.0).longitude0(0.0).radius1(60.0).build();

    Airspace airspace = BoundaryAssembler.standard().assemble(List.of(parent("US00001", BoundaryType.FIR)), List.of(counterclockwise, clockwise)).findFirst().orElseThrow();

    AirspaceSequence right = airspace.sequences().get(1);
    AirspaceSequence left = airspace.sequences().get(2);
    assertEquals(Geometry.CLOCKWISE_ARC, right.geometry());
    assertEquals(Geometry.COUNTER_CLOCKWISE_ARC, left.geometry());
    assertEquals(LatLong.of(0.0, 0.0), right.centerFix().orElseThrow());
    assertEquals(LatLong.of(0.0, 1.0), right.associatedFix().orElseThrow());
    assertEquals(60.0, right.arcRadius().orElseThrow());
    assertEquals(90.0, right.arcBearing().orElseThrow());
    assertEquals(360.0, left.arcBearing().orElseThrow(), 1e-9);
  }

  @Test
  void derivesMissingArcEndpointsFromCenterRadiusAndBearings() {
    LatLong center = LatLong.of(0.0, 0.0);
    LatLong start = center.projectOut(0.0, 60.0);
    LatLong end = center.projectOut(90.0, 60.0);
    DafifBoundarySegment arc = DafifBoundarySegment.builder().boundaryIdentification("US00001").segmentNumber(10)
        .shape(Shape.CLOCKWISE_ARC).derivation(Derivation.DISTANCE_AND_BEARING).latitude0(0.0).longitude0(0.0).radius1(60.0).bearing1(0.0).bearing2(90.0).build();
    DafifBoundarySegment closing = edge(20, Shape.GREAT_CIRCLE, end.latitude(), end.longitude(), start.latitude(), start.longitude());

    Airspace airspace = BoundaryAssembler.standard().assemble(List.of(parent("US00001", BoundaryType.FIR)), List.of(arc, closing)).findFirst().orElseThrow();

    assertEquals(List.of(start, end, start), points(airspace));
    assertEquals(Geometry.CLOCKWISE_ARC, airspace.sequences().get(1).geometry());
  }

  @Test
  void bridgesSmallSourceCoordinateGapsWithoutDiscardingEndpoints() {
    List<DafifBoundarySegment> segments = List.of(
        edge(10, Shape.GREAT_CIRCLE, 0, 0, 0, 1), edge(20, Shape.GREAT_CIRCLE, 0.0005, 1, 1, 1), edge(30, Shape.GREAT_CIRCLE, 1, 1, 0.0005, 0));

    Airspace airspace = BoundaryAssembler.standard().assemble(List.of(parent("US00001", BoundaryType.FIR)), segments).findFirst().orElseThrow();

    assertEquals(List.of(LatLong.of(0.0, 0.0), LatLong.of(0.0, 1.0), LatLong.of(0.0005, 1.0), LatLong.of(1.0, 1.0),
        LatLong.of(0.0005, 0.0), LatLong.of(0.0, 0.0)), points(airspace));
  }

  @Test
  void omitsUnsupportedStandardBoundariesIndividually() {
    List<DafifBoundaryParent> parents = List.of(parent("US00001", BoundaryType.FIR), parent("US00002", BoundaryType.FIR), parent("US00003", BoundaryType.FIR));
    List<DafifBoundarySegment> segments = List.of(circle("US00001"), circle("US00002").toBuilder().radius2(5.0).build(),
        edge(10, Shape.GREAT_CIRCLE, 0, 0, 0, 1).toBuilder().boundaryIdentification("US00003").build());

    assertEquals(List.of("US00001"), BoundaryAssembler.standard().assemble(parents, segments).map(Airspace::identifier).toList());
    assertEquals(List.of("US00001"), BoundaryAssembler.usingStrategy(BoundaryAssemblyStrategy.standard())
        .assemble(parents, segments).map(Airspace::identifier).toList());
  }

  @Test
  void doesNotConnectDisjointRingsOrAssembleParentsWithoutSegments() {
    List<DafifBoundarySegment> segments = List.of(
        edge(10, Shape.GREAT_CIRCLE, 0, 0, 0, 1), edge(20, Shape.GREAT_CIRCLE, 0, 1, 0, 0),
        edge(30, Shape.GREAT_CIRCLE, 10, 10, 10, 11), edge(40, Shape.GREAT_CIRCLE, 10, 11, 10, 10));

    assertEquals(0, BoundaryAssembler.standard().assemble(List.of(parent("US00001", BoundaryType.FIR), parent("US00002", BoundaryType.FIR)), segments).count());
  }

  @Test
  void separatesSuasSectorsIncludingTheUnsectoredParent() {
    List<DafifSuasParent> parents = List.of(suasParent(null), suasParent("A"), suasParent("B"));
    List<DafifSuasSegment> segments = List.of(suasCircle("B", 30.0), suasCircle(null, 10.0), suasCircle("A", 20.0));

    List<Airspace> airspaces = SuasAssembler.standard().assemble(parents, segments).toList();

    assertEquals(List.of("R100", "R100/A", "R100/B"), airspaces.stream().map(Airspace::identifier).toList());
    assertEquals(List.of(10.0, 20.0, 30.0), airspaces.stream().map(airspace -> airspace.sequences().get(0).arcRadius().orElseThrow()).toList());
    assertTrue(airspaces.stream().allMatch(airspace -> airspace.airspaceType() == AirspaceType.RESTRICTIVE));
  }

  @Test
  void omitsPointAndAnnularSuasIndividually() {
    List<DafifSuasParent> parents = List.of(suasParent(null), suasParent("A"), suasParent("B"));
    List<DafifSuasSegment> segments = List.of(suasCircle(null, 10.0), suasCircle("A", 0.1).toBuilder().shape(Shape.POINT).build(),
        suasCircle("B", 20.0).toBuilder().radius2(5.0).build());

    assertEquals(List.of("R100"), SuasAssembler.standard().assemble(parents, segments).map(Airspace::identifier).toList());
    assertEquals(List.of("R100"), SuasAssembler.usingStrategy(SuasAssemblyStrategy.standard())
        .assemble(parents, segments).map(Airspace::identifier).toList());
  }

  @Test
  void customStrategyReceivesOrderedSourceSegmentsAndUnsupportedShapes() {
    BoundaryAssemblyStrategy<List<DafifBoundarySegment>, DafifBoundarySegment> strategy = new BoundaryAssemblyStrategy<>() {
      @Override
      public List<DafifBoundarySegment> convertBoundary(DafifBoundaryParent parent, List<DafifBoundarySegment> sequences) {
        return sequences;
      }

      @Override
      public List<DafifBoundarySegment> convertBoundarySequences(List<DafifBoundarySegment> segments) {
        return segments;
      }
    };
    DafifBoundarySegment annulus = circle("US00001").toBuilder().segmentNumber(10).radius2(5.0).build();
    DafifBoundarySegment point = circle("US00001").toBuilder().segmentNumber(20).shape(Shape.POINT).build();

    assertEquals(List.of(annulus, point), BoundaryAssembler.usingStrategy(strategy)
        .assemble(List.of(parent("US00001", BoundaryType.FIR)), List.of(point, annulus)).findFirst().orElseThrow());
  }

  @Test
  void customStrategyExceptionsPropagate() {
    UnsupportedOperationException failure = new UnsupportedOperationException("Client failure");
    SuasAssemblyStrategy<String, String> strategy = new SuasAssemblyStrategy<>() {
      @Override
      public String convertSuas(DafifSuasParent parent, List<String> sequences) {
        throw failure;
      }

      @Override
      public List<String> convertSuasSequences(List<DafifSuasSegment> segments) {
        return List.of();
      }
    };

    assertSame(failure, assertThrows(UnsupportedOperationException.class,
        () -> SuasAssembler.usingStrategy(strategy).assemble(List.of(suasParent(null)), List.of()).toList()));
  }

  private static List<LatLong> points(Airspace airspace) {
    return airspace.sequences().stream().map(sequence -> sequence.associatedFix().orElseThrow()).toList();
  }

  private static DafifBoundaryParent parent(String identifier, BoundaryType type) {
    return DafifBoundaryParent.builder().boundaryIdentification(identifier).boundaryType(type).icaoCode("KZ")
        .lowerAltitude("01000AMSL").upperAltitude("FL180").build();
  }

  private static DafifBoundarySegment circle(String identifier) {
    return DafifBoundarySegment.builder().boundaryIdentification(identifier).segmentNumber(10).shape(Shape.CIRCLE)
        .latitude0(35.0).longitude0(-100.0).radius1(12.5).build();
  }

  private static DafifBoundarySegment edge(int sequence, Shape shape, double latitude1, double longitude1, double latitude2, double longitude2) {
    return DafifBoundarySegment.builder().boundaryIdentification("US00001").segmentNumber(sequence).shape(shape)
        .latitude1(latitude1).longitude1(longitude1).latitude2(latitude2).longitude2(longitude2).build();
  }

  private static DafifSuasParent suasParent(String sector) {
    return DafifSuasParent.builder().suasIdentification("R100").sector(sector).specialUseAirspaceType(SpecialUseAirspaceType.RESTRICTED)
        .icaoCode("KZ").lowerAltitude("SURFACE").upperAltitude("UNLTD").build();
  }

  private static DafifSuasSegment suasCircle(String sector, double radius) {
    return DafifSuasSegment.builder().suasIdentification("R100").sector(sector).segmentNumber(10).shape(Shape.CIRCLE)
        .latitude0(35.0).longitude0(-100.0).radius1(radius).build();
  }
}
