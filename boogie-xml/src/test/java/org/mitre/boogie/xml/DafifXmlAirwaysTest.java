package org.mitre.boogie.xml;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.mitre.boogie.xml.v23_4.generated.Airway;
import org.mitre.boogie.xml.v23_4.generated.AirwayLeg;
import org.mitre.boogie.xml.v23_4.generated.EnrouteAirwayDirectionalRestriction;
import org.mitre.boogie.xml.v23_4.generated.EnrouteAirwayRouteType;
import org.mitre.boogie.xml.v23_4.generated.Vor;
import org.mitre.boogie.xml.v23_4.generated.Waypoint;
import org.mitre.tdp.boogie.dafif.model.DafifAirTrafficSegment;

class DafifXmlAirwaysTest {

  @Test
  void placesSegmentValuesAtTheCorrectEndpointAndStoresFlightLevelsInFeet() {
    var first = segment(10, "A", "B")
        .atsRouteDistance(12.3).atsRouteOutboundMagneticCourse("89.T").atsRouteInboundMagneticCourse("90.1")
        .minimumAltitude("FL180").lowerLimit("05000").maxAuthorizedAltitude("FL450").upperLimit("UNLTD")
        .requiredNavPerformance(5).build();
    var second = segment(20, "B", "C")
        .atsRouteDistance(45.6).atsRouteOutboundMagneticCourse("100.2").atsRouteInboundMagneticCourse("101.T")
        .lowerLimit("05000").upperLimit("UNLTD")
        .build();
    var refs = references("A", "B", "C");
    var airways = convert(List.of(second, first), refs);

    assertEquals(1, airways.size());
    var legs = airways.get(0).getAirwayLeg();
    assertEquals(List.of("A", "B", "C"), legs.stream().map(AirwayLeg::getFixIdent).toList());
    assertSame(refs.waypoint("B", "US"), legs.get(1).getFixRef());
    assertEquals(new BigDecimal("12.3"), legs.get(0).getRouteDistanceFrom());
    assertEquals(new BigDecimal("45.6"), legs.get(1).getRouteDistanceFrom());
    assertNull(legs.get(2).getRouteDistanceFrom());
    assertEquals(new BigDecimal("89"), legs.get(0).getOutboundCourse().getCourseValue());
    assertTrue(legs.get(0).getOutboundCourse().isIsTrue());
    assertEquals(new BigDecimal("90.1"), legs.get(1).getInboundCourse().getCourseValue());
    assertFalse(legs.get(1).getInboundCourse().isIsTrue());
    assertEquals(new BigDecimal("100.2"), legs.get(1).getOutboundCourse().getCourseValue());
    assertTrue(legs.get(2).getInboundCourse().isIsTrue());
    assertEquals(18000, legs.get(0).getMinimumAltitudes().get(0).getAltitude());
    assertTrue(legs.get(0).getMinimumAltitudes().get(0).isIsFlightLevel());
    assertEquals(1, legs.get(0).getMinimumAltitudes().size());
    assertEquals(5000, legs.get(1).getMinimumAltitudes().get(0).getAltitude());
    assertEquals(45000, legs.get(0).getMaximumAltitudes().get(0).getAltitude());
    assertEquals(1, legs.get(0).getMaximumAltitudes().size());
    assertTrue(legs.get(1).getMaximumAltitudes().get(0).isIsUnlimited());
    assertEquals(BigDecimal.valueOf(5), legs.get(0).getRnp());
    assertEquals("2601", legs.get(0).getCycleDate());
  }

  @Test
  void retainsPublishedEndMarkersWithoutBreakingConnectedEdgesAndDoesNotConnectGaps() {
    var segments = List.of(
        segment(10, "A", "B").waypoint2AtsWaypointDescriptionCode2("E").build(),
        segment(20, "B", "C").build(),
        segment(30, "D", "E").build());
    var airways = convert(segments, references("A", "B", "C", "D", "E"));

    assertEquals(List.of(List.of("A", "B", "C"), List.of("D", "E")),
        airways.stream().map(airway -> airway.getAirwayLeg().stream().map(AirwayLeg::getFixIdent).toList()).toList());
    assertEquals(3, airways.stream().mapToInt(airway -> airway.getAirwayLeg().size() - 1).sum());
    assertEquals(2, airways.stream().map(Airway::getReferenceId).distinct().count());
    assertTrue(airways.get(0).getAirwayLeg().get(1).getNotes().stream().anyMatch(note -> note.contains("description=EE")));
  }

  @Test
  void mergesEndpointIdentityDespiteDifferentDescriptionsAndCoordinatesAndRetainsBothMetadata() {
    var segments = List.of(
        segment(10, "A", "B").waypoint2AtsWaypointDescriptionCode3("C").waypoint2DegreesLatitude(41.0).build(),
        segment(20, "B", "C").waypoint1AtsWaypointDescriptionCode1("R").build());
    var airways = convert(segments, references("A", "B", "C"));

    assertEquals(1, airways.size());
    assertEquals(List.of("A", "B", "C"), fixes(airways.get(0)));
    var shared = airways.get(0).getAirwayLeg().get(1);
    assertTrue(shared.getNotes().stream().anyMatch(note -> note.contains("description=E C")));
    assertTrue(shared.getNotes().stream().anyMatch(note -> note.contains("description=R")));
    assertTrue(shared.getNotes().stream().anyMatch(note -> note.contains("latitude=41.0")));
    assertTrue(shared.getNotes().stream().anyMatch(note -> note.contains("latitude=40.0")));
    assertNull(shared.getWaypointDescription().isIsEssential());
    assertNull(shared.getWaypointDescription().isIsNonEssential());
  }

  @Test
  void resolvesNavaidWaypointAliasesWhileMergingOppositeDirections() {
    var refs = references("A", "B");
    var vor = new Vor();
    refs.addWaypointNavaid("A", "US", vor);
    var east = segment(10, "A", "B").waypoint1AtsWaypointDescriptionCode1("V").waypoint1NavaidType(1).build();
    var west = segment(10, "B", "A").atsRouteDirection("W").waypoint2AtsWaypointDescriptionCode1("V")
        .waypoint2NavaidType(1).build();
    var airways = convert(List.of(east, west), refs);

    assertEquals(1, airways.size());
    assertEquals(List.of("A", "B"), fixes(airways.get(0)));
    assertSame(vor, airways.get(0).getAirwayLeg().get(0).getFixRef());
    assertNull(airways.get(0).getAirwayLeg().get(0).getLegDirectionRestriction());
  }

  @Test
  void mergesOppositeChainsAndDeduplicatesIdenticalDirectionalAltitudes() {
    var eastAB = segment(10, "A", "B").minimumAltitude("FL180").maxAuthorizedAltitude("FL450").build();
    var eastBC = segment(20, "B", "C").minimumAltitude("FL200").upperLimit("UNLTD").build();
    var westCB = segment(10, "C", "B").atsRouteDirection("W").minimumAltitude("FL200").upperLimit("UNLTD").build();
    var westBA = segment(20, "B", "A").atsRouteDirection("W").minimumAltitude("FL180").maxAuthorizedAltitude("FL450").build();
    var airways = convert(List.of(eastAB, westCB, eastBC, westBA), references("A", "B", "C"));

    assertEquals(1, airways.size());
    var airway = airways.get(0);
    assertEquals(List.of("A", "B", "C"), fixes(airway));
    for (var leg : airway.getAirwayLeg().subList(0, 2)) {
      assertNull(leg.getLegDirectionRestriction());
      assertEquals(1, leg.getMinimumAltitudes().size());
      assertEquals(1, leg.getMaximumAltitudes().size());
      assertNull(leg.getMinimumAltitudes().get(0).getAltitudeDirectionRestriction());
      assertNull(leg.getMaximumAltitudes().get(0).getAltitudeDirectionRestriction());
    }
    assertEquals(18000, airway.getAirwayLeg().get(0).getMinimumAltitudes().get(0).getAltitude());
    assertEquals(20000, airway.getAirwayLeg().get(1).getMinimumAltitudes().get(0).getAltitude());
    assertTrue(airway.getAirwayLeg().get(1).getMaximumAltitudes().get(0).isIsUnlimited());
  }

  @Test
  void honorsAnExplicitBidirectionalRowWithoutRequiringADuplicateReverseRecord() {
    var source = segment(10, "B", "A").atsRouteDirection("W").biDirectional("Y")
        .minimumAltitude("FL180").maxAuthorizedAltitude("FL450").build();
    var airway = convert(List.of(source), references("A", "B")).get(0);

    assertEquals(List.of("A", "B"), fixes(airway));
    var leg = airway.getAirwayLeg().get(0);
    assertNull(leg.getLegDirectionRestriction());
    assertEquals(1, leg.getMinimumAltitudes().size());
    assertEquals(1, leg.getMaximumAltitudes().size());
    assertNull(leg.getMinimumAltitudes().get(0).getAltitudeDirectionRestriction());
    assertNull(leg.getMaximumAltitudes().get(0).getAltitudeDirectionRestriction());
  }

  @Test
  void preservesDifferentForwardAndBackwardAltitudeLimitsOnOneSharedEdge() {
    var east = segment(10, "A", "B").minimumAltitude("FL180").maxAuthorizedAltitude("FL450").build();
    var west = segment(10, "B", "A").atsRouteDirection("W").minimumAltitude("FL200").maxAuthorizedAltitude("FL400").build();
    var airways = convert(List.of(west, east), references("A", "B"));

    assertEquals(1, airways.size());
    var leg = airways.get(0).getAirwayLeg().get(0);
    assertNull(leg.getLegDirectionRestriction());
    assertEquals(Map.of(EnrouteAirwayDirectionalRestriction.ONE_WAY_FORWARD, 18000,
            EnrouteAirwayDirectionalRestriction.ONE_WAY_BACKWARD, 20000),
        leg.getMinimumAltitudes().stream().collect(Collectors.toMap(value -> value.getAltitudeDirectionRestriction(), value -> value.getAltitude())));
    assertEquals(Map.of(EnrouteAirwayDirectionalRestriction.ONE_WAY_FORWARD, 45000,
            EnrouteAirwayDirectionalRestriction.ONE_WAY_BACKWARD, 40000),
        leg.getMaximumAltitudes().stream().collect(Collectors.toMap(value -> value.getAltitudeDirectionRestriction(), value -> value.getAltitude())));
  }

  @Test
  void mergesPartialOverlapsAndKeepsDirectionalTailsOnTheirActualEdges() {
    var segments = List.of(
        segment(10, "A", "B").build(), segment(20, "B", "C").build(), segment(30, "C", "D").build(),
        segment(10, "E", "D").atsRouteDirection("W").build(),
        segment(20, "D", "C").atsRouteDirection("W").build(),
        segment(30, "C", "B").atsRouteDirection("W").build());
    var airways = convert(segments, references("A", "B", "C", "D", "E"));

    assertEquals(1, airways.size());
    assertEquals(List.of("A", "B", "C", "D", "E"), fixes(airways.get(0)));
    var legs = airways.get(0).getAirwayLeg();
    assertEquals(EnrouteAirwayDirectionalRestriction.ONE_WAY_FORWARD, legs.get(0).getLegDirectionRestriction());
    assertNull(legs.get(1).getLegDirectionRestriction());
    assertNull(legs.get(2).getLegDirectionRestriction());
    assertEquals(EnrouteAirwayDirectionalRestriction.ONE_WAY_BACKWARD, legs.get(3).getLegDirectionRestriction());
    assertNull(legs.get(4).getLegDirectionRestriction());
  }

  @Test
  void reversesCoursesAtTheCorrectEndpointsWhenTheOnlySourceRunsBackward() {
    var source = segment(10, "B", "A").atsRouteDirection("W").atsRouteDistance(12.3)
        .atsRouteOutboundMagneticCourse("270.2").atsRouteInboundMagneticCourse("271.T")
        .minimumAltitude("FL180").maxAuthorizedAltitude("FL450").build();
    var airway = convert(List.of(source), references("A", "B")).get(0);

    assertEquals(List.of("A", "B"), fixes(airway));
    var from = airway.getAirwayLeg().get(0);
    var to = airway.getAirwayLeg().get(1);
    assertEquals(EnrouteAirwayDirectionalRestriction.ONE_WAY_BACKWARD, from.getLegDirectionRestriction());
    assertEquals(12.3, from.getRouteDistanceFrom().doubleValue(), 1e-9);
    assertEquals(91.0, from.getOutboundCourse().getCourseValue().doubleValue(), 1e-9);
    assertTrue(from.getOutboundCourse().isIsTrue());
    assertEquals(90.2, to.getInboundCourse().getCourseValue().doubleValue(), 1e-9);
    assertFalse(to.getInboundCourse().isIsTrue());
    assertNull(from.getInboundCourse());
    assertNull(to.getOutboundCourse());
    assertNull(to.getRouteDistanceFrom());
    assertEquals(EnrouteAirwayDirectionalRestriction.ONE_WAY_BACKWARD,
        from.getMinimumAltitudes().get(0).getAltitudeDirectionRestriction());
    assertEquals(EnrouteAirwayDirectionalRestriction.ONE_WAY_BACKWARD,
        from.getMaximumAltitudes().get(0).getAltitudeDirectionRestriction());
  }

  @Test
  void doesNotConnectHomonymousWaypointsFromDifferentCountries() {
    var refs = references("A", "B");
    var canadianB = waypoint("B", "CA", refs);
    waypoint("C", "CA", refs);
    var us = segment(10, "A", "B").build();
    var canada = segment(20, "B", "C").waypoint1CountryCode("CA").waypoint2CountryCode("CA").build();
    var airways = convert(List.of(us, canada), refs);

    assertEquals(2, airways.size());
    assertEquals(List.of(List.of("A", "B"), List.of("B", "C")), airways.stream().map(DafifXmlAirwaysTest::fixes).toList());
    assertSame(refs.waypoint("B", "US"), airways.get(0).getAirwayLeg().get(1).getFixRef());
    assertSame(canadianB, airways.get(1).getAirwayLeg().get(0).getFixRef());
    assertNotEquals(airways.get(0).getReferenceId(), airways.get(1).getReferenceId());
  }

  @Test
  void keepsDisconnectedSectionsAsSeparateAirwaysWithTheSamePublishedIdentifier() {
    var segments = List.of(segment(10, "A", "B").build(), segment(20, "C", "D").build());
    var airways = convert(segments, references("A", "B", "C", "D"));

    assertEquals(2, airways.size());
    assertEquals(List.of("J1", "J1"), airways.stream().map(Airway::getIdentifier).toList());
    assertEquals(List.of(List.of("A", "B"), List.of("C", "D")), airways.stream().map(DafifXmlAirwaysTest::fixes).toList());
    assertNotEquals(airways.get(0).getReferenceId(), airways.get(1).getReferenceId());
    assertEquals(Set.of("A-B", "C-D"), edges(airways));
    assertEquals(2, edgeCount(airways));
  }

  @Test
  void splitsBranchesIntoMaximalContinuousPathsWithoutInventingConnections() {
    var segments = List.of(segment(10, "A", "B").build(), segment(20, "B", "C").build(),
        segment(30, "B", "D").build());
    var airways = convert(segments, references("A", "B", "C", "D"));

    assertEquals(3, airways.size());
    assertEquals(Set.of(List.of("A", "B"), List.of("B", "C"), List.of("B", "D")),
        airways.stream().map(DafifXmlAirwaysTest::fixes).collect(Collectors.toSet()));
    assertEquals(Set.of("A-B", "B-C", "B-D"), edges(airways));
    assertEquals(3, edgeCount(airways));
    assertEquals(3, airways.stream().map(Airway::getReferenceId).distinct().count());
    assertTrue(airways.stream().allMatch(airway -> "J1".equals(airway.getIdentifier())));
  }

  @Test
  void retainsTheClosingEdgeOfACycleExactlyOnce() {
    var segments = List.of(segment(10, "A", "B").build(), segment(20, "B", "C").build(),
        segment(30, "C", "A").build());
    var airways = convert(segments, references("A", "B", "C"));

    assertEquals(1, airways.size());
    assertEquals(List.of("A", "B", "C", "A"), fixes(airways.get(0)));
    assertEquals(Set.of("A-B", "B-C", "A-C"), edges(airways));
    assertEquals(3, edgeCount(airways));
    var legs = airways.get(0).getAirwayLeg();
    assertSame(legs.get(0).getFixRef(), legs.get(3).getFixRef());
    assertNull(legs.get(3).getRouteDistanceFrom());
  }

  @Test
  void preservesIncompatibleDirectionalRnpProfilesAsSeparateOneWayVariants() {
    var forward = segment(10, "A", "B").biDirectional("Y").requiredNavPerformance(1)
        .minimumAltitude("FL180").build();
    var backward = segment(10, "B", "A").atsRouteDirection("W").biDirectional("Y").requiredNavPerformance(5)
        .minimumAltitude("FL200").build();
    var airways = convert(List.of(backward, forward), references("A", "B"));

    assertEquals(2, airways.size());
    assertTrue(airways.stream().allMatch(airway -> List.of("A", "B").equals(fixes(airway))));
    assertTrue(airways.stream().allMatch(airway -> "J1".equals(airway.getIdentifier())));
    assertNotEquals(airways.get(0).getReferenceId(), airways.get(1).getReferenceId());
    var legs = airways.stream().map(airway -> airway.getAirwayLeg().get(0)).toList();
    assertEquals(Map.of(BigDecimal.ONE, EnrouteAirwayDirectionalRestriction.ONE_WAY_FORWARD,
            BigDecimal.valueOf(5), EnrouteAirwayDirectionalRestriction.ONE_WAY_BACKWARD),
        legs.stream().collect(Collectors.toMap(AirwayLeg::getRnp, AirwayLeg::getLegDirectionRestriction)));
    for (var leg : legs) {
      assertEquals(1, leg.getMinimumAltitudes().size());
      assertEquals(leg.getLegDirectionRestriction(), leg.getMinimumAltitudes().get(0).getAltitudeDirectionRestriction());
    }
  }

  @Test
  void retainsFlightLevelAndMslReferencesEvenWhenTheirNumericFeetAreEqual() {
    var forward = segment(10, "A", "B").minimumAltitude("FL180").build();
    var backward = segment(10, "B", "A").atsRouteDirection("W").minimumAltitude("18000").build();
    var airways = convert(List.of(forward, backward), references("A", "B"));

    assertEquals(1, airways.size());
    var leg = airways.get(0).getAirwayLeg().get(0);
    assertNull(leg.getLegDirectionRestriction());
    assertEquals(2, leg.getMinimumAltitudes().size());
    var minima = leg.getMinimumAltitudes().stream()
        .collect(Collectors.toMap(value -> value.getAltitudeDirectionRestriction(), value -> value));
    var flightLevel = minima.get(EnrouteAirwayDirectionalRestriction.ONE_WAY_FORWARD);
    var msl = minima.get(EnrouteAirwayDirectionalRestriction.ONE_WAY_BACKWARD);
    assertEquals(18000, flightLevel.getAltitude());
    assertEquals(18000, msl.getAltitude());
    assertTrue(flightLevel.isIsFlightLevel());
    assertTrue(msl.isIsMsl());
    assertFalse(Boolean.TRUE.equals(flightLevel.isIsMsl()));
    assertFalse(Boolean.TRUE.equals(msl.isIsFlightLevel()));
  }

  @Test
  void keepsIdentifiersAndSerializedGraphStableWhenInputRowsAreReordered() throws Exception {
    var segments = List.of(
        segment(10, "A", "B").minimumAltitude("FL180").build(),
        segment(20, "B", "C").minimumAltitude("FL200").build(),
        segment(10, "C", "B").atsRouteDirection("W").minimumAltitude("FL220").build(),
        segment(20, "B", "A").atsRouteDirection("W").minimumAltitude("FL180").build(),
        segment(50, "D", "E").build());
    var reversed = new ArrayList<>(segments);
    Collections.reverse(reversed);
    var refs = references("A", "B", "C", "D", "E");

    assertEquals(xml(segments, refs), xml(reversed, refs));
  }

  @Test
  void keepsDafifRouteClassificationsAndGridCoursesWithoutInventingArincMeanings() {
    var source = segment(10, "A", "B").atsRouteType("A")
        .atsRouteOutboundMagneticCourse("89.G").atsRouteInboundMagneticCourse("90.G").build();
    var airway = convert(List.of(source), references("A", "B")).get(0);

    assertNull(airway.getAirwayRouteType());
    assertNull(airway.getAirwayLeg().get(0).getOutboundCourse());
    assertNull(airway.getAirwayLeg().get(1).getInboundCourse());
    assertTrue(airway.getAirwayLeg().get(0).getNotes().stream().anyMatch(note -> note.contains(";type=A;")));
    assertTrue(airway.getAirwayLeg().get(0).getNotes().stream().anyMatch(note -> note.contains("outboundCourse=89.G")));
    assertTrue(airway.getAirwayLeg().get(1).getNotes().contains("DAFIF inbound grid course=90.G"));

    var rnav = convert(List.of(segment(10, "A", "B").atsRouteType("R").build()), references("A", "B")).get(0);
    assertEquals(EnrouteAirwayRouteType.RNAV_RNP, rnav.getAirwayRouteType());
  }

  @Test
  void deduplicatesEquivalentDirectedEdgesAndKeepsTheirSourceRecords() {
    var first = segment(10, "A", "B").minimumAltitude("05000").build();
    var duplicate = segment(20, "A", "B").minimumAltitude("5000").build();
    var airway = convert(List.of(first, duplicate), references("A", "B")).get(0);

    assertEquals(List.of("A", "B"), fixes(airway));
    assertEquals(5000, airway.getAirwayLeg().get(0).getMinimumAltitudes().get(0).getAltitude());
    assertEquals(2, airway.getAirwayLeg().get(0).getNotes().stream().filter(note -> note.startsWith("DAFIF ATS:")).count());
  }

  @Test
  void rejectsConflictingAltitudeLimitsForTheSameDirection() {
    var first = segment(10, "A", "B").minimumAltitude("FL180").build();
    var conflict = segment(20, "A", "B").minimumAltitude("FL200").build();

    var failure = assertThrows(IllegalArgumentException.class,
        () -> convert(List.of(first, conflict), references("A", "B")));
    assertTrue(failure.getMessage().contains("Conflicting DAFIF airway J1"));
    assertTrue(failure.getMessage().contains("altitude limits"));
  }

  private static List<Airway> convert(List<DafifAirTrafficSegment> segments, DafifXmlReferences refs) {
    var publication = new AeroPublication();
    DafifXmlAirways.populate(segments, publication, refs);
    return publication.getAirways().getAirway();
  }

  private static List<String> fixes(Airway airway) {
    return airway.getAirwayLeg().stream().map(AirwayLeg::getFixIdent).toList();
  }

  private static int edgeCount(List<Airway> airways) {
    return airways.stream().mapToInt(airway -> airway.getAirwayLeg().size() - 1).sum();
  }

  private static Set<String> edges(List<Airway> airways) {
    var edges = new java.util.HashSet<String>();
    for (var airway : airways) {
      var identifiers = fixes(airway);
      for (int i = 1; i < identifiers.size(); i++) {
        var from = identifiers.get(i - 1);
        var to = identifiers.get(i);
        if (from.compareTo(to) < 0) edges.add(from + "-" + to);
        else edges.add(to + "-" + from);
      }
    }
    return edges;
  }

  private static String xml(List<DafifAirTrafficSegment> segments, DafifXmlReferences refs) throws Exception {
    var publication = new AeroPublication();
    DafifXmlAirways.populate(segments, publication, refs);
    var output = new java.io.StringWriter();
    jakarta.xml.bind.JAXBContext.newInstance(AeroPublication.class).createMarshaller().marshal(publication, output);
    return output.toString();
  }

  private static DafifXmlReferences references(String... identifiers) {
    var refs = new DafifXmlReferences();
    for (String identifier : identifiers) {
      waypoint(identifier, "US", refs);
    }
    return refs;
  }

  private static Waypoint waypoint(String identifier, String country, DafifXmlReferences refs) {
    var waypoint = new Waypoint();
    waypoint.setIdentifier(identifier);
    waypoint.setReferenceId(DafifXmlReferences.id("waypoint", identifier, country));
    refs.addWaypoint(identifier, country, waypoint);
    return waypoint;
  }

  private static DafifAirTrafficSegment.Builder segment(int sequence, String from, String to) {
    return new DafifAirTrafficSegment.Builder().atsIdentifier("J1").atsRouteDirection("E").atsRouteSequenceNumber(sequence)
        .atsRouteType("W").icaoCode("K1").level("B").frequencyClass("H").atsRouteStatus("A").cycleDate(202601)
        .waypoint1WaypointIdentifierWptIdent(from).waypoint1CountryCode("US").waypoint1IcaoCode("K1")
        .waypoint1AtsWaypointDescriptionCode1("E").waypoint1DegreesLatitude(40.0).waypoint1DegreesLongitude(-70.0)
        .waypoint2WaypointIdentifierWptIdent(to).waypoint2CountryCode("US").waypoint2IcaoCode("K1")
        .waypoint2AtsWaypointDescriptionCode1("E").waypoint2DegreesLatitude(40.0).waypoint2DegreesLongitude(-70.0);
  }
}
