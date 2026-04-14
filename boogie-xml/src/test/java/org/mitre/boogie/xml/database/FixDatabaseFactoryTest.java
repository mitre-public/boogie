package org.mitre.boogie.xml.database;

import static org.junit.jupiter.api.Assertions.*;

import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincAirportGate;
import org.mitre.boogie.xml.model.ArincGnssLandingSystem;
import org.mitre.boogie.xml.model.ArincHelipad;
import org.mitre.boogie.xml.model.ArincLocalizerGlideSlope;
import org.mitre.boogie.xml.model.ArincLocalizerGlideslopeMarker;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.ArincRunway;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordType;
import org.mitre.boogie.xml.model.fields.ArincWaypointType;
import org.mitre.boogie.xml.model.fields.ArincWaypointUsage;

class FixDatabaseFactoryTest {

  private static final LatLong POSITION = LatLong.of(42.2124, -83.3534);
  private static final MagneticVariation MAG_VAR = MagneticVariation.ofDegrees(-5.0);
  private static final String CYCLE = "2504";

  @Test
  void standard_indexesWaypointByReferenceId() {
    ArincRecords records = ArincRecords.standard();
    records.addWaypoint(testWaypoint("WPT1", POSITION, MAG_VAR));

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertAll(
        () -> assertTrue(db.fix("WPT1").isPresent()),
        () -> assertEquals("WPT1", db.fix("WPT1").orElseThrow().fixIdentifier())
    );
  }

  @Test
  void standard_indexesNdbNavaidByReferenceId() {
    ArincRecords records = ArincRecords.standard();
    records.addNdbNavaid(testNdb("NDB1", POSITION, MAG_VAR));

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertAll(
        () -> assertTrue(db.fix("NDB1").isPresent()),
        () -> assertEquals("NDB1", db.fix("NDB1").orElseThrow().fixIdentifier())
    );
  }

  @Test
  void standard_indexesVhfNavaidByReferenceId() {
    ArincRecords records = ArincRecords.standard();
    records.addVhfNavaid(testVhf("VHF1", POSITION, MAG_VAR));

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertAll(
        () -> assertTrue(db.fix("VHF1").isPresent()),
        () -> assertEquals("VHF1", db.fix("VHF1").orElseThrow().fixIdentifier())
    );
  }

  @Test
  void standard_indexesAirportByReferenceId() {
    ArincRecords records = ArincRecords.standard();
    records.addAirport(testAirport("KATL", POSITION, MAG_VAR));

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertAll(
        () -> assertTrue(db.fix("KATL").isPresent()),
        () -> assertEquals("KATL", db.fix("KATL").orElseThrow().fixIdentifier())
    );
  }

  @Test
  void standard_indexesRunwayByReferenceId() {
    ArincRunway runway = ArincRunway.builder()
        .pointInfo(testPointInfo("RW09L", POSITION, MAG_VAR))
        .recordInfo(testRecordInfo())
        .build();

    ArincAirport airport = testAirportBuilder("KATL", POSITION, MAG_VAR)
        .runways(List.of(runway))
        .build();

    ArincRecords records = ArincRecords.standard();
    records.addAirport(airport);

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertAll(
        () -> assertTrue(db.fix("RW09L").isPresent()),
        () -> assertEquals("RW09L", db.fix("RW09L").orElseThrow().fixIdentifier())
    );
  }

  @Test
  void standard_indexesGateByReferenceId() {
    ArincAirportGate gate = ArincAirportGate.builder()
        .pointInfo(testPointInfo("GATE_A1", POSITION, MAG_VAR))
        .recordInfo(testRecordInfo())
        .build();

    ArincAirport airport = testAirportBuilder("KATL", POSITION, MAG_VAR)
        .airportGates(List.of(gate))
        .build();

    ArincRecords records = ArincRecords.standard();
    records.addAirport(airport);

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertAll(
        () -> assertTrue(db.fix("GATE_A1").isPresent()),
        () -> assertEquals("GATE_A1", db.fix("GATE_A1").orElseThrow().fixIdentifier())
    );
  }

  @Test
  void standard_indexesTerminalWaypointByReferenceId() {
    ArincWaypoint termWp = testWaypoint("TWPT1", POSITION, MAG_VAR);

    ArincAirport airport = testAirportWithPortInfo("KATL", POSITION, MAG_VAR,
        ArincPortInfo.builder()
            .pointInfo(testPointInfo("KATL", POSITION, MAG_VAR))
            .recordInfo(testRecordInfo())
            .terminalWaypoints(List.of(termWp))
            .build());

    ArincRecords records = ArincRecords.standard();
    records.addAirport(airport);

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertAll(
        () -> assertTrue(db.fix("TWPT1").isPresent()),
        () -> assertEquals("TWPT1", db.fix("TWPT1").orElseThrow().fixIdentifier())
    );
  }

  @Test
  void standard_indexesTerminalNdbByReferenceId() {
    ArincNdbNavaid termNdb = testNdb("TNDB1", POSITION, MAG_VAR);

    ArincAirport airport = testAirportWithPortInfo("KATL", POSITION, MAG_VAR,
        ArincPortInfo.builder()
            .pointInfo(testPointInfo("KATL", POSITION, MAG_VAR))
            .recordInfo(testRecordInfo())
            .ndbNavaid(List.of(termNdb))
            .build());

    ArincRecords records = ArincRecords.standard();
    records.addAirport(airport);

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertAll(
        () -> assertTrue(db.fix("TNDB1").isPresent()),
        () -> assertEquals("TNDB1", db.fix("TNDB1").orElseThrow().fixIdentifier())
    );
  }

  @Test
  void standard_indexesHelipadByReferenceId() {
    ArincHelipad helipad = ArincHelipad.builder()
        .pointInfo(testPointInfo("HP01", POSITION, MAG_VAR))
        .recordInfo(testRecordInfo())
        .build();

    ArincAirport airport = testAirportWithPortInfo("KATL", POSITION, MAG_VAR,
        ArincPortInfo.builder()
            .pointInfo(testPointInfo("KATL", POSITION, MAG_VAR))
            .recordInfo(testRecordInfo())
            .helipads(List.of(helipad))
            .build());

    ArincRecords records = ArincRecords.standard();
    records.addAirport(airport);

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertAll(
        () -> assertTrue(db.fix("HP01").isPresent()),
        () -> assertEquals("HP01", db.fix("HP01").orElseThrow().fixIdentifier())
    );
  }

  @Test
  void standard_indexesLocalizerGlideSlopeByReferenceId() {
    ArincLocalizerGlideSlope loc = ArincLocalizerGlideSlope.builder()
        .pointInfo(testPointInfo("ILS09L", POSITION, MAG_VAR))
        .recordInfo(testRecordInfo())
        .build();

    ArincAirport airport = testAirportWithPortInfo("KATL", POSITION, MAG_VAR,
        ArincPortInfo.builder()
            .pointInfo(testPointInfo("KATL", POSITION, MAG_VAR))
            .recordInfo(testRecordInfo())
            .localizerGlideSlopes(List.of(loc))
            .build());

    ArincRecords records = ArincRecords.standard();
    records.addAirport(airport);

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertAll(
        () -> assertTrue(db.fix("ILS09L").isPresent()),
        () -> assertEquals("ILS09L", db.fix("ILS09L").orElseThrow().fixIdentifier())
    );
  }

  @Test
  void standard_indexesMarkerByReferenceId() {
    ArincLocalizerGlideslopeMarker marker = ArincLocalizerGlideslopeMarker.builder()
        .pointInfo(testPointInfo("OM09L", POSITION, MAG_VAR))
        .recordInfo(testRecordInfo())
        .build();

    ArincAirport airport = testAirportWithPortInfo("KATL", POSITION, MAG_VAR,
        ArincPortInfo.builder()
            .pointInfo(testPointInfo("KATL", POSITION, MAG_VAR))
            .recordInfo(testRecordInfo())
            .markers(List.of(marker))
            .build());

    ArincRecords records = ArincRecords.standard();
    records.addAirport(airport);

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertAll(
        () -> assertTrue(db.fix("OM09L").isPresent()),
        () -> assertEquals("OM09L", db.fix("OM09L").orElseThrow().fixIdentifier())
    );
  }

  @Test
  void standard_indexesGnssLandingSystemByReferenceId() {
    ArincGnssLandingSystem gls = ArincGnssLandingSystem.builder()
        .pointInfo(testPointInfo("GLS09L", POSITION, MAG_VAR))
        .recordInfo(testRecordInfo())
        .build();

    ArincAirport airport = testAirportWithPortInfo("KATL", POSITION, MAG_VAR,
        ArincPortInfo.builder()
            .pointInfo(testPointInfo("KATL", POSITION, MAG_VAR))
            .recordInfo(testRecordInfo())
            .gnssLandingSystems(List.of(gls))
            .build());

    ArincRecords records = ArincRecords.standard();
    records.addAirport(airport);

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertAll(
        () -> assertTrue(db.fix("GLS09L").isPresent()),
        () -> assertEquals("GLS09L", db.fix("GLS09L").orElseThrow().fixIdentifier())
    );
  }

  @Test
  void standard_missingReferenceIdReturnsEmpty() {
    ArincRecords records = ArincRecords.standard();
    records.addWaypoint(testWaypoint("WPT1", POSITION, MAG_VAR));

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertTrue(db.fix("DOES_NOT_EXIST").isEmpty());
  }

  @Test
  void standard_emptyRecordsProducesEmptyDatabase() {
    ArincRecords records = ArincRecords.standard();

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertTrue(db.fix("ANYTHING").isEmpty());
  }

  @Test
  void standard_multipleRecordTypesAllIndexed() {
    ArincRecords records = ArincRecords.standard();
    records.addWaypoint(testWaypoint("WPT1", POSITION, MAG_VAR));
    records.addNdbNavaid(testNdb("NDB1", POSITION, MAG_VAR));
    records.addVhfNavaid(testVhf("VHF1", POSITION, MAG_VAR));
    records.addAirport(testAirport("KATL", POSITION, MAG_VAR));

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertAll(
        () -> assertTrue(db.fix("WPT1").isPresent()),
        () -> assertTrue(db.fix("NDB1").isPresent()),
        () -> assertTrue(db.fix("VHF1").isPresent()),
        () -> assertTrue(db.fix("KATL").isPresent())
    );
  }

  @Test
  void standard_airportWithAllNestedRecordTypes() {
    ArincRunway runway = ArincRunway.builder()
        .pointInfo(testPointInfo("RW27R", POSITION, MAG_VAR))
        .recordInfo(testRecordInfo())
        .build();

    ArincAirportGate gate = ArincAirportGate.builder()
        .pointInfo(testPointInfo("GATE_B2", POSITION, MAG_VAR))
        .recordInfo(testRecordInfo())
        .build();

    ArincWaypoint termWp = testWaypoint("TWPT2", POSITION, MAG_VAR);
    ArincNdbNavaid termNdb = testNdb("TNDB2", POSITION, MAG_VAR);

    ArincHelipad helipad = ArincHelipad.builder()
        .pointInfo(testPointInfo("HP02", POSITION, MAG_VAR))
        .recordInfo(testRecordInfo())
        .build();

    ArincLocalizerGlideSlope loc = ArincLocalizerGlideSlope.builder()
        .pointInfo(testPointInfo("ILS27R", POSITION, MAG_VAR))
        .recordInfo(testRecordInfo())
        .build();

    ArincLocalizerGlideslopeMarker marker = ArincLocalizerGlideslopeMarker.builder()
        .pointInfo(testPointInfo("OM27R", POSITION, MAG_VAR))
        .recordInfo(testRecordInfo())
        .build();

    ArincGnssLandingSystem gls = ArincGnssLandingSystem.builder()
        .pointInfo(testPointInfo("GLS27R", POSITION, MAG_VAR))
        .recordInfo(testRecordInfo())
        .build();

    ArincAirport airport = ArincAirport.builder()
        .portInfo(ArincPortInfo.builder()
            .pointInfo(testPointInfo("KJFK", POSITION, MAG_VAR))
            .recordInfo(testRecordInfo())
            .terminalWaypoints(List.of(termWp))
            .ndbNavaid(List.of(termNdb))
            .helipads(List.of(helipad))
            .localizerGlideSlopes(List.of(loc))
            .markers(List.of(marker))
            .gnssLandingSystems(List.of(gls))
            .build())
        .runways(List.of(runway))
        .airportGates(List.of(gate))
        .build();

    ArincRecords records = ArincRecords.standard();
    records.addAirport(airport);

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertAll(
        () -> assertTrue(db.fix("KJFK").isPresent(), "airport"),
        () -> assertTrue(db.fix("RW27R").isPresent(), "runway"),
        () -> assertTrue(db.fix("GATE_B2").isPresent(), "gate"),
        () -> assertTrue(db.fix("TWPT2").isPresent(), "terminal waypoint"),
        () -> assertTrue(db.fix("TNDB2").isPresent(), "terminal NDB"),
        () -> assertTrue(db.fix("HP02").isPresent(), "helipad"),
        () -> assertTrue(db.fix("ILS27R").isPresent(), "localizer/glideslope"),
        () -> assertTrue(db.fix("OM27R").isPresent(), "marker"),
        () -> assertTrue(db.fix("GLS27R").isPresent(), "GNSS landing system")
    );
  }

  @Test
  void standard_fixPositionAndMagVarPreserved() {
    ArincRecords records = ArincRecords.standard();
    records.addWaypoint(testWaypoint("WPT1", POSITION, MAG_VAR));

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    Fix fix = db.fix("WPT1").orElseThrow();
    assertAll(
        () -> assertEquals(POSITION, fix.latLong()),
        () -> assertEquals(MAG_VAR, fix.magneticVariation().orElseThrow())
    );
  }

  @Test
  void standard_laterReferenceIdOverwritesEarlier() {
    LatLong position2 = LatLong.of(33.6367, -84.4281);

    ArincRecords records = ArincRecords.standard();
    records.addWaypoint(testWaypoint("SAME_ID", POSITION, MAG_VAR));
    records.addNdbNavaid(testNdb("SAME_ID", position2, MAG_VAR));

    FixDatabase<Fix> db = FixDatabaseFactory.standard(records);

    assertTrue(db.fix("SAME_ID").isPresent());
  }

  // -- test helpers --

  private static ArincWaypoint testWaypoint(String identifier, LatLong position, MagneticVariation magVar) {
    return ArincWaypoint.builder()
        .pointInfo(testPointInfo(identifier, position, magVar))
        .recordInfo(testRecordInfo())
        .waypointType(ArincWaypointType.builder().build())
        .waypointUsage(ArincWaypointUsage.of(false, false, false))
        .build();
  }

  private static ArincNdbNavaid testNdb(String identifier, LatLong position, MagneticVariation magVar) {
    return ArincNdbNavaid.builder()
        .pointInfo(testPointInfo(identifier, position, magVar))
        .recordInfo(testRecordInfo())
        .build();
  }

  private static ArincVhfNavaid testVhf(String identifier, LatLong position, MagneticVariation magVar) {
    return ArincVhfNavaid.builder()
        .pointInfo(testPointInfo(identifier, position, magVar))
        .recordInfo(testRecordInfo())
        .build();
  }

  private static ArincAirport testAirport(String identifier, LatLong position, MagneticVariation magVar) {
    return testAirportBuilder(identifier, position, magVar).build();
  }

  private static ArincAirport.Builder testAirportBuilder(String identifier, LatLong position, MagneticVariation magVar) {
    return ArincAirport.builder()
        .portInfo(ArincPortInfo.builder()
            .pointInfo(testPointInfo(identifier, position, magVar))
            .recordInfo(testRecordInfo())
            .build());
  }

  private static ArincAirport testAirportWithPortInfo(String identifier, LatLong position, MagneticVariation magVar,
      ArincPortInfo portInfo) {
    return ArincAirport.builder()
        .portInfo(portInfo)
        .build();
  }

  private static ArincPointInfo testPointInfo(String identifier, LatLong position, MagneticVariation magVar) {
    return ArincPointInfo.builder()
        .identifier(identifier)
        .icaoCode("K6")
        .latLong(position)
        .magneticVariation(magVar)
        .referenceId(identifier)
        .build();
  }

  private static ArincRecordInfo testRecordInfo() {
    return ArincRecordInfo.builder()
        .cycleDate(CYCLE)
        .recordType(ArincRecordType.STANDARD)
        .build();
  }
}
