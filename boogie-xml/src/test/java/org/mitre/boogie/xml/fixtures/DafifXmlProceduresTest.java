package org.mitre.boogie.xml.fixtures;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.Airport;
import org.mitre.boogie.xml.v23_4.generated.ApproachLeg;
import org.mitre.boogie.xml.v23_4.generated.Dme;
import org.mitre.boogie.xml.v23_4.generated.LocalizerGlideslope;
import org.mitre.boogie.xml.v23_4.generated.Vor;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalParent;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalSegment;

class DafifXmlProceduresTest {

  @Test
  void expandsStarSpeedsAndKeepsHoldingAndConditionalRestrictionsLocal() {
    var refs = new DafifXmlReferences();
    var airport = new Airport();
    refs.addAirport("US00001", airport);
    var parent = parent(1, "TEST1  TEST ARRIVAL").build();
    DafifXmlProcedures.populate(List.of(parent), List.of(
        segment(1, parent.terminalIdentifier(), 10, "5").speedLimit1(210.0).build(),
        segment(1, parent.terminalIdentifier(), 20, "5").build(),
        segment(1, parent.terminalIdentifier(), 30, "5").trackDescriptionCode("HF").speedLimit1(160.0).build(),
        segment(1, parent.terminalIdentifier(), 40, "5").build(),
        segment(1, parent.terminalIdentifier(), 50, "5").speedLimit1(180.0).speedLimitAircraftType1("J")
            .speedLimitAltitude1("FL100").build(),
        segment(1, parent.terminalIdentifier(), 60, "5").build()), refs);
    var legs = airport.getTerminalProcedures().getStar().get(0).getStarCommonRoute().getProcedureLeg();
    assertAll(
        () -> assertEquals(210L, legs.get(1).getSpeedLimit().getAtOrBelow()),
        () -> assertEquals(160L, legs.get(2).getSpeedLimit().getAtOrBelow()),
        () -> assertEquals(210L, legs.get(3).getSpeedLimit().getAtOrBelow()),
        () -> assertNull(legs.get(5).getSpeedLimit()),
        () -> assertTrue(legs.get(5).getNotes().contains("DAFIF speed limit 1: 180 knots; aircraft=J; below altitude=FL100"))
    );
  }

  @Test
  void expandsSidSpeedsBackwardAndUsesAnUnqualifiedSecondLimit() {
    var refs = new DafifXmlReferences();
    var airport = new Airport();
    refs.addAirport("US00001", airport);
    var parent = parent(2, "TEST1  TEST DEPARTURE").build();
    DafifXmlProcedures.populate(List.of(parent), List.of(
        segment(2, parent.terminalIdentifier(), 10, "5").build(),
        segment(2, parent.terminalIdentifier(), 20, "5").speedLimit1(220.0).speedLimitAircraftType1("J")
            .speedLimit2(200.0).speedLimitAircraftType2("A").build(),
        segment(2, parent.terminalIdentifier(), 30, "5").build()), refs);
    var legs = airport.getTerminalProcedures().getSid().get(0).getSidCommonRoute().getProcedureLeg();
    assertAll(
        () -> assertEquals(200L, legs.get(0).getSpeedLimit().getAtOrBelow()),
        () -> assertTrue(legs.get(0).getNotes().contains("DAFIF speed limit 1: 220 knots; aircraft=J; below altitude=unspecified")),
        () -> assertNull(legs.get(2).getSpeedLimit())
    );
  }

  @Test
  void distinguishesDafifStarAndSidTransitionCodesAndPreservesNames() {
    var refs = new DafifXmlReferences();
    var airport = new Airport();
    refs.addAirport("US00001", airport);
    var star = parent(1, "TEST1  TEST ARRIVAL").build();
    var sid = parent(2, "TEST2  TEST DEPARTURE").build();
    DafifXmlProcedures.populate(List.of(star, sid), List.of(
        segment(1, star.terminalIdentifier(), 10, "1").transitionIdentifier("ENTRY").build(),
        segment(2, sid.terminalIdentifier(), 10, "1").transitionIdentifier("RW09").build()), refs);

    var procedures = airport.getTerminalProcedures();
    assertAll(
        () -> assertEquals("TEST1", procedures.getStar().get(0).getIdentifier()),
        () -> assertEquals("TEST ARRIVAL", procedures.getStar().get(0).getProcedureName()),
        () -> assertEquals("ENTRY", procedures.getStar().get(0).getStarEnrouteTransition().get(0).getIdentifier()),
        () -> assertEquals("RW09", procedures.getSid().get(0).getSidRunwayTransition().get(0).getIdentifier()),
        () -> assertEquals(1, procedures.getStar().get(0).getStarEnrouteTransition().get(0).getProcedureLeg().size()),
        () -> assertEquals(1, procedures.getSid().get(0).getSidRunwayTransition().get(0).getProcedureLeg().size())
    );
  }

  @Test
  void splitsMissedApproachesWithoutDroppingDuplicateLegsAndPreservesFlightLevelUnits() {
    var refs = new DafifXmlReferences();
    var airport = new Airport();
    var navaid = new Vor();
    refs.addAirport("US00001", airport);
    refs.addWaypointNavaid("FIX", "US", navaid);
    var parent = parent(3, "G09    RNAV RW09").transitionLevel(18000).build();
    var finalLeg = segment(3, parent.terminalIdentifier(), 10, "G")
        .termSegWaypointIdentifier("FIX").waypointCountryCode("US").terminalWaypointDescriptionCode1("V")
        .terminalWaypointDescriptionCode4("F").terminalMagneticCourse("233.T")
        .altitudeDescription("B").altitude1("FL180").altitude2("15000").verticalNavigationVnav(3.0).build();
    var missedLeg = segment(3, parent.terminalIdentifier(), 20, "G")
        .termSegWaypointIdentifier("MISSING").waypointCountryCode("US").terminalWaypointDescriptionCode1("E")
        .terminalWaypointDescriptionCode3("M").build();
    DafifXmlProcedures.populate(List.of(parent), List.of(finalLeg, missedLeg, missedLeg), refs);

    var approach = airport.getTerminalProcedures().getApproach().get(0);
    var encoded = (ApproachLeg) approach.getFinalApproach().getProcedureLeg().get(0);
    var missed = approach.getMissedApproach().get(0).getProcedureLeg();
    assertAll(
        () -> assertEquals(1, approach.getFinalApproach().getProcedureLeg().size()),
        () -> assertEquals(2, missed.size()),
        () -> assertSame(navaid, encoded.getFixRef()),
        () -> assertEquals(18000, encoded.getAltitudeConstraint().getAtOrBelow().getAltitude()),
        () -> assertTrue(encoded.getAltitudeConstraint().getAtOrBelow().isIsFlightLevel()),
        () -> assertEquals(15000, encoded.getAltitudeConstraint().getAtOrAbove().getAltitude()),
        () -> assertEquals(18000, approach.getFinalApproach().getTransitionAltitudeOrLevel().getAltitude()),
        () -> assertEquals(new BigDecimal("233"), encoded.getCourse().getCourseValue()),
        () -> assertTrue(encoded.getCourse().isIsTrue()),
        () -> assertEquals(new BigDecimal("3.0"), encoded.getVerticalAngle()),
        () -> assertEquals("MISSING", missed.get(0).getFixIdent()),
        () -> assertNull(missed.get(0).getFixRef())
    );
  }

  @Test
  void keepsConditionalSpeedsInNotesAndResolvesAirportFixesByTheirPublishedIdentifier() {
    var refs = new DafifXmlReferences();
    var airport = new Airport();
    var referencedAirport = new Airport();
    refs.addAirport("US00001", airport);
    refs.addAirportAlias("KOTHER", referencedAirport);
    var parent = parent(1, "TEST1  TEST ARRIVAL").build();
    var segment = segment(1, parent.terminalIdentifier(), 10, "2")
        .termSegWaypointIdentifier("KOTHER").terminalWaypointDescriptionCode1("A")
        .speedLimit1(210.0).speedLimitAircraftType1("J").speedLimitAltitude1("FL100").build();
    DafifXmlProcedures.populate(List.of(parent), List.of(segment), refs);

    var leg = airport.getTerminalProcedures().getStar().get(0).getStarCommonRoute().getProcedureLeg().get(0);
    assertAll(
        () -> assertSame(referencedAirport, leg.getFixRef()),
        () -> assertNull(leg.getSpeedLimit(), "A conditional jet restriction must not become an unconditional speed restriction"),
        () -> assertTrue(leg.getNotes().contains("DAFIF speed limit 1: 210 knots; aircraft=J; below altitude=FL100"))
    );
  }

  @Test
  void rejectsSegmentsWithoutAParentInsteadOfSilentlyDroppingThem() {
    var orphan = segment(3, "G09    RNAV RW09", 10, "G").build();
    assertThrows(IllegalArgumentException.class, () -> DafifXmlProcedures.populate(List.of(), List.of(orphan), new DafifXmlReferences()));
  }

  @Test
  void resolvesIlsDmeAndLocalizerReferencesToTheirDistinctComponents() {
    var refs = new DafifXmlReferences();
    var airport = new Airport();
    var localizer = new LocalizerGlideslope();
    var dme = new Dme();
    refs.addAirport("US00001", airport);
    refs.addLocalizer("US00001", "ITST", localizer);
    refs.addIlsDme("US00001", "D", "ITST", dme);
    var parent = parent(3, "I09    ILS RW09").build();
    var locLeg = segment(3, parent.terminalIdentifier(), 10, "I")
        .navaid1Identifier("ITST").navaid1Type("Z").build();
    var dmeLeg = segment(3, parent.terminalIdentifier(), 20, "I")
        .navaid1Identifier("ITST").navaid1Type("D").build();
    DafifXmlProcedures.populate(List.of(parent), List.of(locLeg, dmeLeg), refs);

    var legs = airport.getTerminalProcedures().getApproach().get(0).getFinalApproach().getProcedureLeg();
    assertAll(
        () -> assertSame(localizer, legs.get(0).getRecNavaidRef()),
        () -> assertSame(dme, legs.get(1).getRecNavaidRef()),
        () -> assertEquals("ITST", legs.get(0).getRecNavaidIdent()),
        () -> assertEquals("ITST", legs.get(1).getRecNavaidIdent())
    );
  }

  private static DafifTerminalParent.Builder parent(int type, String name) {
    return DafifTerminalParent.builder().airportIdentification("US00001").terminalProcedureType(type)
        .terminalIdentifier(name).icaoCode("KTEST").cycleDate(202601)
        .levelOfService1("N").levelOfService2("N").levelOfService3("N");
  }

  private static DafifTerminalSegment.Builder segment(int type, String name, int sequence, String routeType) {
    return DafifTerminalSegment.builder().airportIdentification("US00001").terminalProcedureType(type)
        .terminalIdentifier(name).terminalSequenceNumber(sequence).terminalApproachType(routeType)
        .icaoCode("KTEST").trackDescriptionCode("TF").cycleDate(202601);
  }
}
