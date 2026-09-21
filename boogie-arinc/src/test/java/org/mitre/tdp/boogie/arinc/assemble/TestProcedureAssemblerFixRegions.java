package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincHeliport;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestProcedureAssemblerFixRegions {

  @Test
  void heliportFixesUseOwningFacilityRegion() {
    assertFixRegions(SectionCode.H, "K1");
  }

  @Test
  void airportFixesUseOwningFacilityRegion() {
    assertFixRegions(SectionCode.P, "K1");
  }

  @Test
  void enrouteFixesKeepReferencedRegion() {
    assertFixRegions(SectionCode.E, "K2");
  }

  private void assertFixRegions(SectionCode section, String waypointRegion) {
    var converter = converter(section, waypointRegion);
    String subsection = section == SectionCode.E ? "A" : "C";
    ArincProcedureLeg source = new ArincProcedureLeg.Builder()
        .sectionCode(section == SectionCode.H ? SectionCode.H : SectionCode.P)
        .airportIdentifier("TEST")
        .airportIcaoRegion("K1")
        .pathTerm(PathTerminator.RF)
        .sequenceNumber(10)
        .fixIdentifier("ASSOC")
        .fixIcaoRegion("K2")
        .fixSectionCode(section)
        .fixSubSectionCode(subsection)
        .centerFixIdentifier("CNTER")
        .centerFixIcaoRegion("K2")
        .centerFixSectionCode(section)
        .centerFixSubSectionCode(subsection)
        .fileRecordNumber(1)
        .build();

    Leg assembled = converter.apply(source);

    assertAll(
        () -> assertEquals("ASSOC", assembled.associatedFix().orElseThrow().fixIdentifier()),
        () -> assertEquals(40., assembled.associatedFix().orElseThrow().latitude()),
        () -> assertEquals("CNTER", assembled.centerFix().orElseThrow().fixIdentifier()),
        () -> assertEquals(41., assembled.centerFix().orElseThrow().latitude())
    );
  }

  private ProcedureAssembler.Standard.ArincProcedureLegConverter<Procedure, Transition, Leg, Fix> converter(
      SectionCode section, String waypointRegion) {
    ArincAirport airport = new ArincAirport.Builder()
        .recordType(RecordType.S)
        .sectionCode(SectionCode.P)
        .subSectionCode("A")
        .airportIdentifier("TEST")
        .airportIcaoRegion("K1")
        .latitude(39.)
        .longitude(-75.)
        .fileRecordNumber(1)
        .build();
    ArincHeliport heliport = ArincHeliport.builder()
        .recordType(RecordType.S)
        .customerAreaCode(CustomerAreaCode.USA)
        .sectionCode(SectionCode.H)
        .subSectionCode("A")
        .heliportIdentifier("TEST")
        .heliportIcaoRegion("K1")
        .latitude(39.)
        .longitude(-75.)
        .build();
    List<ArincAirport> airports = section == SectionCode.H ? List.of() : List.of(airport);
    List<ArincHeliport> heliports = section == SectionCode.H ? List.of(heliport) : List.of();
    List<ArincWaypoint> waypoints = List.of(
        waypoint(section, waypointRegion, "ASSOC", 40.),
        waypoint(section, waypointRegion, "CNTER", 41.)
    );
    var terminalDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        airports, List.of(), List.of(), List.of(), List.of(), waypoints, List.of(), List.of(), List.of(), heliports);
    var fixDatabase = ArincDatabaseFactory.newFixDatabase(
        List.of(), List.of(), waypoints, airports, List.of(), heliports);
    return new ProcedureAssembler.Standard.ArincProcedureLegConverter<>(
        terminalDatabase, fixDatabase, ProcedureAssemblyStrategy.standard(), FixAssemblyStrategy.standard());
  }

  private ArincWaypoint waypoint(SectionCode section, String region, String identifier, double latitude) {
    boolean enroute = section == SectionCode.E;
    return new ArincWaypoint.Builder()
        .recordType(RecordType.S)
        .sectionCode(section)
        .enrouteSubSectionCode(enroute ? "A" : null)
        .terminalSubSectionCode(enroute ? null : "C")
        .airportIdentifier(enroute ? null : "TEST")
        .airportIcaoRegion(enroute ? null : "K1")
        .waypointIdentifier(identifier)
        .waypointIcaoRegion(region)
        .latitude(latitude)
        .longitude(-75.)
        .fileRecordNumber(1)
        .magneticVariation(0.)
        .lastUpdateCycle("2101")
        .build();
  }
}
