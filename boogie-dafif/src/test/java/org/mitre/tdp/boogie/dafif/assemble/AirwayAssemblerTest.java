package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.dafif.TestObjects;
import org.mitre.tdp.boogie.dafif.database.DafifDatabaseFactory;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

public class AirwayAssemblerTest {

  static AirwayAssembler<Airway> assembler;

  @BeforeAll
  static void setUp() {
    Collection<DafifWaypoint> waypoints = Set.of(
        TestObjects.wptAdrivAA, TestObjects.wptCa400AA, TestObjects.wptMidvuAA,
        TestObjects.wptAndopAS, TestObjects.wptCodieAS, TestObjects.wptTvorAA);
    Collection<DafifNavaid> navaids = Set.of(TestObjects.navTVOR);
    DafifFixDatabase fdb = DafifDatabaseFactory.newFixDatabase(waypoints, navaids);

    assembler = AirwayAssembler.standard(fdb, FixAssemblyStrategy.standard());
  }

  @Test
  void testAssembleJ100ForwardOnly() {
    List<Airway> airways = assembler.assemble(TestObjects.atsJ100ForwardSegments).collect(Collectors.toList());

    assertEquals(1, airways.size(), "J100 forward-only segments should produce one airway");
    Airway j100 = airways.get(0);
    assertAll(
        () -> assertEquals("J100", j100.airwayIdentifier()),
        () -> assertEquals(4, j100.legs().size(), "3 segments should produce 4 legs (N+1)"),
        () -> assertEquals("ADRIV|CA400|MIDVU|ANDOP", fixSequence(j100))
    );
  }

  @Test
  void testAssembleJ100BothDirections() {
    List<Airway> airways = assembler.assemble(TestObjects.atsJ100AllSegments).collect(Collectors.toList());

    assertEquals(2, airways.size(), "Both directions of J100 should produce two separate airways");
    List<String> identifiers = airways.stream().map(Airway::airwayIdentifier).sorted().collect(Collectors.toList());
    assertEquals(List.of("J100", "J100"), identifiers);
  }

  @Test
  void testForwardDirectionLegTypes() {
    List<Airway> airways = assembler.assemble(TestObjects.atsJ100ForwardSegments).collect(Collectors.toList());
    Airway j100 = airways.get(0);

    assertAll(
        () -> assertEquals(PathTerminator.IF, j100.legs().get(0).pathTerminator(), "Start leg should be IF"),
        () -> assertEquals(PathTerminator.TF, j100.legs().get(1).pathTerminator(), "Subsequent legs should be TF"),
        () -> assertEquals(0, j100.legs().get(0).sequenceNumber(), "Start leg seq should be 0"),
        () -> assertEquals(10, j100.legs().get(1).sequenceNumber(), "First segment leg seq should be 10")
    );
  }

  @Test
  void testAssembleV200() {
    List<Airway> airways = assembler.assemble(TestObjects.atsV200Segments).collect(Collectors.toList());

    assertEquals(1, airways.size(), "V200 single-direction segments should produce one airway");
    Airway v200 = airways.get(0);
    assertAll(
        () -> assertEquals("V200", v200.airwayIdentifier()),
        () -> assertEquals(3, v200.legs().size(), "2 segments should produce 3 legs (N+1)"),
        () -> assertEquals("ANDOP|CODIE|ANDOP", fixSequence(v200))
    );
  }

  @Test
  void testAssembleMultipleAirways() {
    List<DafifAirTrafficSegment> allSegments = new ArrayList<>();
    allSegments.addAll(TestObjects.atsJ100ForwardSegments);
    allSegments.addAll(TestObjects.atsV200Segments);

    List<Airway> airways = assembler.assemble(allSegments).collect(Collectors.toList());

    assertEquals(2, airways.size(), "Should produce two separate airways");
    List<String> identifiers = airways.stream().map(Airway::airwayIdentifier).sorted().collect(Collectors.toList());
    assertEquals(List.of("J100", "V200"), identifiers);
  }

  @Test
  void testLegRouteDistance() {
    List<Airway> airways = assembler.assemble(TestObjects.atsJ100ForwardSegments).collect(Collectors.toList());
    Airway j100 = airways.get(0);

    assertAll(
        () -> assertEquals(Optional.empty(), j100.legs().get(0).routeDistance(), "Start leg should have no distance"),
        () -> assertEquals(Optional.of(15.2), j100.legs().get(1).routeDistance()),
        () -> assertEquals(Optional.of(9.1), j100.legs().get(2).routeDistance()),
        () -> assertEquals(Optional.of(120.5), j100.legs().get(3).routeDistance())
    );
  }

  @Test
  void testStartLegHasFix() {
    List<Airway> airways = assembler.assemble(TestObjects.atsJ100ForwardSegments).collect(Collectors.toList());
    Airway j100 = airways.get(0);

    assertTrue(j100.legs().get(0).associatedFix().isPresent(), "Start leg should have an associated fix");
    assertEquals("ADRIV", j100.legs().get(0).associatedFix().get().fixIdentifier());
  }

  @Test
  void testNavaidFixResolution() {
    List<Airway> airways = assembler.assemble(TestObjects.atsJ300Segments).collect(Collectors.toList());

    assertEquals(1, airways.size());
    Airway j300 = airways.get(0);
    assertAll(
        () -> assertEquals("J300", j300.airwayIdentifier()),
        () -> assertEquals(2, j300.legs().size(), "1 segment should produce 2 legs (N+1)"),
        () -> assertEquals("ADRIV", j300.legs().get(0).associatedFix().get().fixIdentifier(),
            "Start fix should be waypoint ADRIV (desc E)"),
        () -> assertEquals("TVOR", j300.legs().get(1).associatedFix().get().fixIdentifier(),
            "End fix should be navaid TVOR resolved via WPT->NAV (desc V)")
    );
  }

  @Test
  void splitsDisconnectedSectionsWithoutDroppingTheirStartingFixes() {
    List<Airway> airways = assembler.assemble(List.of(TestObjects.atsJ100Seg3, TestObjects.atsJ100Seg1)).toList();

    assertAll(
        () -> assertEquals(List.of("ADRIV|CA400", "MIDVU|ANDOP"), airways.stream().map(this::fixSequence).toList()),
        () -> assertEquals(List.of(15.2, 120.5), airways.stream()
            .map(airway -> airway.legs().get(1).routeDistance().orElseThrow()).toList()),
        () -> assertTrue(airways.stream().allMatch(airway -> airway.legs().get(0).pathTerminator() == PathTerminator.IF))
    );
  }

  @ParameterizedTest
  @ValueSource(booleans = {false, true})
  void splitsAtPublishedEndMarkersOnEitherSideOfASharedFix(boolean markerOnPreviousEnd) {
    var first = segment(10, "ADRIV", "CA400")
        .waypoint2AtsWaypointDescriptionCode2(markerOnPreviousEnd ? "E" : null).build();
    var second = segment(20, "CA400", "MIDVU")
        .waypoint1AtsWaypointDescriptionCode2(markerOnPreviousEnd ? null : "E").build();
    List<Airway> airways = assembler.assemble(List.of(second, first)).toList();

    assertAll(
        () -> assertEquals(List.of("ADRIV|CA400", "CA400|MIDVU"), airways.stream().map(this::fixSequence).toList()),
        () -> assertTrue(airways.stream().allMatch(airway -> airway.legs().get(0).sequenceNumber() == 0)),
        () -> assertTrue(airways.stream().allMatch(airway -> airway.legs().get(0).pathTerminator() == PathTerminator.IF))
    );
  }

  @Test
  void doesNotJoinSameNamedFixesFromDifferentCountries() {
    var otherCa400 = DafifWaypoint.builder().waypointIdentifier("CA400").countryCode("AS")
        .waypointPointNavaidFlag(false).degreesLatitude(-30.0).degreesLongitude(130.0).cycleDate(202404).build();
    var database = DafifDatabaseFactory.newFixDatabase(
        List.of(TestObjects.wptAdrivAA, TestObjects.wptCa400AA, TestObjects.wptMidvuAA, otherCa400), List.of());
    var localAssembler = AirwayAssembler.standard(database, FixAssemblyStrategy.standard());
    var first = segment(10, "ADRIV", "CA400").build();
    var second = segment(20, "CA400", "MIDVU").waypoint1CountryCode("AS").build();
    List<Airway> airways = localAssembler.assemble(List.of(first, second)).toList();

    assertAll(
        () -> assertEquals(List.of("ADRIV|CA400", "CA400|MIDVU"), airways.stream().map(this::fixSequence).toList()),
        () -> assertEquals(-30.0, airways.get(1).legs().get(0).associatedFix().orElseThrow().latitude())
    );
  }

  @Test
  void preservesDirectionGroupingWhenInputOrderChanges() {
    var segments = new ArrayList<>(TestObjects.atsJ100AllSegments);
    List<String> expected = assembler.assemble(segments).map(this::fixSequence).toList();
    Collections.reverse(segments);
    List<String> reversed = assembler.assemble(segments).map(this::fixSequence).toList();

    assertAll(
        () -> assertEquals(2, reversed.size(), "Opposite directions remain separate"),
        () -> assertEquals(expected, reversed)
    );
  }

  @Test
  void convertsTrueOutboundCourseUsingTheDepartureFixVariation() {
    var source = segment(10, "ADRIV", "CA400").atsRouteOutboundMagneticCourse("180.T").build();
    var airway = assembler.assemble(List.of(source)).findFirst().orElseThrow();
    var departure = airway.legs().get(0).associatedFix().orElseThrow();
    var arrival = airway.legs().get(1).associatedFix().orElseThrow();
    double departureVariation = departure.magneticVariation().orElseThrow().angle().inDegrees();
    double arrivalVariation = arrival.magneticVariation().orElseThrow().angle().inDegrees();

    assertAll(
        () -> assertNotEquals(departureVariation, arrivalVariation, "The fixture distinguishes the course's reference point"),
        () -> assertEquals(180.0 - departureVariation, airway.legs().get(1).outboundMagneticCourse().orElseThrow(), 1e-9)
    );
  }

  private static DafifAirTrafficSegment.Builder segment(int sequence, String from, String to) {
    return new DafifAirTrafficSegment.Builder()
        .atsIdentifier("J100").atsRouteDirection("E").atsRouteSequenceNumber(sequence)
        .waypoint1WaypointIdentifierWptIdent(from).waypoint1CountryCode("AA").waypoint1AtsWaypointDescriptionCode1("E")
        .waypoint2WaypointIdentifierWptIdent(to).waypoint2CountryCode("AA").waypoint2AtsWaypointDescriptionCode1("E");
  }

  private String fixSequence(Airway airway) {
    return airway.legs().stream()
        .map(Leg::associatedFix)
        .filter(Optional::isPresent)
        .map(Optional::get)
        .map(Fix::fixIdentifier)
        .collect(Collectors.joining("|"));
  }
}
