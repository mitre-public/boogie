package org.mitre.boogie.xml;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.v23_4.generated.AeroPublication;
import org.mitre.boogie.xml.v23_4.generated.Airport;
import org.mitre.boogie.xml.v23_4.generated.ApproachLeg;
import org.mitre.boogie.xml.v23_4.generated.Heliport;
import org.mitre.boogie.xml.v23_4.generated.Waypoint;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ConvertedArincRecords;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class CifpXmlRoutesTest {

  @Test
  void splitsDisconnectedAirwaySegmentsWithoutChangingPublishedIdentifiers() {
    var publication = new AeroPublication();
    var refs = new CifpXmlReferences();
    CifpXmlRoutes.populate(records(List.of(
        airwayLeg(10, "FIRST", "E   "), airwayLeg(20, "END1", "EE  "),
        airwayLeg(30, "NEXT", "E   "), airwayLeg(40, "END2", "EE  ")), List.of()), publication, refs);

    var airways = publication.getAirways().getAirway();
    assertAll(
        () -> assertEquals(2, airways.size()),
        () -> assertEquals(List.of("J1", "J1"), airways.stream().map(airway -> airway.getIdentifier()).toList()),
        () -> assertEquals(List.of("FIRST", "END1"), airways.get(0).getAirwayLeg().stream().map(leg -> leg.getFixIdent()).toList()),
        () -> assertEquals(List.of("NEXT", "END2"), airways.get(1).getAirwayLeg().stream().map(leg -> leg.getFixIdent()).toList()),
        () -> assertNotEquals(airways.get(0).getReferenceId(), airways.get(1).getReferenceId())
    );
  }

  @Test
  void separatesEmbeddedMissedApproachAndRetainsRepeatedLegsAndUnresolvedIdentifiers() {
    var refs = new CifpXmlReferences();
    var airport = new Airport();
    var fix = new Waypoint();
    refs.addPort("KAAA", "K1", airport);
    refs.addFix("E", "A", "KNOWN", "K1", null, fix);
    var finalLeg = procedureLeg(10, "KNOWN", "E  F").altitudeDescription("B").minAltitude1(7000.0).minAltitude2(4000.0).build();
    var missedLeg = procedureLeg(20, "UNKNOWN", "E M ").build();
    CifpXmlRoutes.populate(records(List.of(), List.of(finalLeg, missedLeg, missedLeg)), new AeroPublication(), refs);

    var approach = airport.getTerminalProcedures().getApproach().get(0);
    var encodedFinal = approach.getFinalApproach().getProcedureLeg().get(0);
    var missed = approach.getMissedApproach().get(0).getProcedureLeg();
    assertAll(
        () -> assertEquals(1, approach.getFinalApproach().getProcedureLeg().size()),
        () -> assertEquals(2, missed.size()),
        () -> assertSame(fix, encodedFinal.getFixRef()),
        () -> assertEquals(4000, encodedFinal.getAltitudeConstraint().getAtOrAbove().getAltitude()),
        () -> assertEquals(7000, encodedFinal.getAltitudeConstraint().getAtOrBelow().getAltitude()),
        () -> assertEquals(List.of("UNKNOWN", "UNKNOWN"), missed.stream().map(leg -> leg.getFixIdent()).toList()),
        () -> assertNull(missed.get(0).getFixRef())
    );
  }

  @Test
  void preservesFlightLevelsTrueCoursesAndVerticalAngleReferenceAltitudes() {
    var refs = new CifpXmlReferences();
    var airport = new Airport();
    refs.addPort("KAAA", "K1", airport);
    char[] raw = new char[132];
    Arrays.fill(raw, ' ');
    put(raw, 0, "SUSAP");
    put(raw, 6, "KAAAK1FTEST  R");
    put(raw, 26, "010KNOWNK1EA0");
    put(raw, 47, "TF");
    put(raw, 70, "194T");
    put(raw, 82, "V FL18017500");
    put(raw, 123, "000102101");
    var parsed = ArincRecordParser.standard(ArincVersion.V19.specs()).parse(new String(raw)).orElseThrow();
    refs.addRouteRecord("PF", parsed);
    var source = procedureLeg(10, "KNOWN", "E  F")
        .altitudeDescription("V").minAltitude1(18000.0).minAltitude2(17500.0).verticalAngle(-3.0).build();
    CifpXmlRoutes.populate(records(List.of(), List.of(source)), new AeroPublication(), refs);

    var leg = airport.getTerminalProcedures().getApproach().get(0).getFinalApproach().getProcedureLeg().get(0);
    assertAll(
        () -> assertEquals(18000, leg.getAltitudeConstraint().getAtOrAbove().getAltitude()),
        () -> assertTrue(leg.getAltitudeConstraint().getAtOrAbove().isIsFlightLevel()),
        () -> assertEquals(new BigDecimal("194"), leg.getCourse().getCourseValue()),
        () -> assertTrue(leg.getCourse().isIsTrue()),
        () -> assertEquals(List.of("CIFP altitude2 (vertical angle reference): 17500"), leg.getNotes()),
        () -> assertNull(((ApproachLeg) leg).getGlideSlopeCrossingAltitude())
    );
  }

  @Test
  void keepsAirportAndHeliportProceduresSeparateWhenTheirIdentifiersMatch() {
    var refs = new CifpXmlReferences();
    var airport = new Airport();
    var heliport = new Heliport();
    refs.addPort("KAAA", "K1", airport);
    refs.addPort("KAAA", "K1", heliport);
    var airportLeg = procedureLeg(10, "AIR", "E  F").build();
    var heliportLeg = procedureLeg(10, "HELI", "E  F").sectionCode(SectionCode.H).build();
    CifpXmlRoutes.populate(records(List.of(), List.of(airportLeg, heliportLeg)), new AeroPublication(), refs);

    var airportApproach = airport.getTerminalProcedures().getApproach().get(0);
    var heliportApproach = heliport.getTerminalProcedures().getApproach().get(0);
    assertAll(
        () -> assertEquals("AIR", airportApproach.getFinalApproach().getProcedureLeg().get(0).getFixIdent()),
        () -> assertEquals("HELI", heliportApproach.getFinalApproach().getProcedureLeg().get(0).getFixIdent()),
        () -> assertNotEquals(airportApproach.getReferenceId(), heliportApproach.getReferenceId())
    );
  }

  private static ArincAirwayLeg airwayLeg(int sequence, String fix, String description) {
    return new ArincAirwayLeg.Builder().recordType(RecordType.S).customerAreaCode(CustomerAreaCode.USA)
        .sectionCode(SectionCode.E).subSectionCode("R").routeIdentifier("J1").sequenceNumber(sequence)
        .fixIdentifier(fix).fixIcaoRegion("K1").fixSectionCode(SectionCode.E).fixSubSectionCode("A")
        .waypointDescription(description).routeType("O").fileRecordNumber(sequence).lastUpdateCycle("2101").build();
  }

  private static ArincProcedureLeg.Builder procedureLeg(int sequence, String fix, String description) {
    return new ArincProcedureLeg.Builder().recordType(RecordType.S).customerAreaCode(CustomerAreaCode.USA)
        .sectionCode(SectionCode.P).airportIdentifier("KAAA").airportIcaoRegion("K1").subSectionCode("F")
        .sidStarIdentifier("TEST").routeType("R").sequenceNumber(sequence).pathTerm(PathTerminator.TF)
        .fixIdentifier(fix).fixIcaoRegion("K1").fixSectionCode(SectionCode.E).fixSubSectionCode("A")
        .waypointDescription(description).fileRecordNumber(sequence).lastUpdateCycle("2101");
  }

  private static ConvertedArincRecords records(List<ArincAirwayLeg> airways, List<ArincProcedureLeg> procedures) {
    return new ConvertedArincRecords(List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), List.of(),
        airways, procedures, List.of(), List.of(), List.of(), List.of(), List.of(), List.of(), Optional.empty(), List.of());
  }

  private static void put(char[] target, int offset, String value) {
    value.getChars(0, value.length(), target, offset);
  }
}
