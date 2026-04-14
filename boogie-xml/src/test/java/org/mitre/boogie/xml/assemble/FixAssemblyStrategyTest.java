package org.mitre.boogie.xml.assemble;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.boogie.xml.model.ArincNdbNavaid;
import org.mitre.boogie.xml.model.ArincVhfNavaid;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordType;
import org.mitre.boogie.xml.model.fields.ArincWaypointType;
import org.mitre.boogie.xml.model.fields.ArincWaypointUsage;

class FixAssemblyStrategyTest {

  private static final FixAssemblyStrategy<Fix> STRATEGY = FixAssemblyStrategy.standard();

  private static final LatLong POSITION = LatLong.of(42.2124, -83.3534);
  private static final MagneticVariation MAG_VAR = MagneticVariation.ofDegrees(-5.0);
  private static final String CYCLE = "2504";

  @Test
  void convertWaypoint_withPublishedMagVar() {
    ArincWaypoint waypoint = testWaypoint("JMACK", POSITION, MAG_VAR);

    Fix fix = STRATEGY.convertWaypoint(waypoint);

    assertAll(
        () -> assertEquals("JMACK", fix.fixIdentifier()),
        () -> assertEquals(POSITION, fix.latLong()),
        () -> assertEquals(MAG_VAR, fix.magneticVariation().orElseThrow())
    );
  }

  @Test
  void convertWaypoint_withoutMagVar_computesFallback() {
    ArincWaypoint waypoint = testWaypoint("CHPPR", POSITION, null);

    Fix fix = STRATEGY.convertWaypoint(waypoint);

    assertAll(
        () -> assertEquals("CHPPR", fix.fixIdentifier()),
        () -> assertEquals(POSITION, fix.latLong()),
        () -> assertTrue(fix.magneticVariation().isPresent(), "Should compute fallback mag var")
    );
  }

  @Test
  void convertNdbNavaid_withPublishedMagVar() {
    ArincNdbNavaid ndb = testNdb("DC", POSITION, MAG_VAR);

    Fix fix = STRATEGY.convertNdbNavaid(ndb);

    assertAll(
        () -> assertEquals("DC", fix.fixIdentifier()),
        () -> assertEquals(POSITION, fix.latLong()),
        () -> assertEquals(MAG_VAR, fix.magneticVariation().orElseThrow())
    );
  }

  @Test
  void convertNdbNavaid_withoutMagVar_computesFallback() {
    ArincNdbNavaid ndb = testNdb("DC", POSITION, null);

    Fix fix = STRATEGY.convertNdbNavaid(ndb);

    assertAll(
        () -> assertEquals("DC", fix.fixIdentifier()),
        () -> assertEquals(POSITION, fix.latLong()),
        () -> assertTrue(fix.magneticVariation().isPresent(), "Should compute fallback mag var")
    );
  }

  @Test
  void convertVhfNavaid_prefersStationDeclination() {
    MagneticVariation stationDeclination = MagneticVariation.ofDegrees(-7.0);
    MagneticVariation pointMagVar = MagneticVariation.ofDegrees(-5.0);

    ArincVhfNavaid vhf = testVhf("DTW", POSITION, pointMagVar, stationDeclination);

    Fix fix = STRATEGY.convertVhfNavaid(vhf);

    assertAll(
        () -> assertEquals("DTW", fix.fixIdentifier()),
        () -> assertEquals(POSITION, fix.latLong()),
        () -> assertEquals(stationDeclination, fix.magneticVariation().orElseThrow())
    );
  }

  @Test
  void convertVhfNavaid_fallsBackToPointMagVar() {
    ArincVhfNavaid vhf = testVhf("DTW", POSITION, MAG_VAR, null);

    Fix fix = STRATEGY.convertVhfNavaid(vhf);

    assertAll(
        () -> assertEquals("DTW", fix.fixIdentifier()),
        () -> assertEquals(POSITION, fix.latLong()),
        () -> assertEquals(MAG_VAR, fix.magneticVariation().orElseThrow())
    );
  }

  @Test
  void convertVhfNavaid_withoutAnyMagVar_computesFallback() {
    ArincVhfNavaid vhf = testVhf("DTW", POSITION, null, null);

    Fix fix = STRATEGY.convertVhfNavaid(vhf);

    assertAll(
        () -> assertEquals("DTW", fix.fixIdentifier()),
        () -> assertEquals(POSITION, fix.latLong()),
        () -> assertTrue(fix.magneticVariation().isPresent(), "Should compute fallback mag var")
    );
  }

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

  private static ArincVhfNavaid testVhf(String identifier, LatLong position, MagneticVariation pointMagVar, MagneticVariation stationDeclination) {
    return ArincVhfNavaid.builder()
        .pointInfo(testPointInfo(identifier, position, pointMagVar))
        .recordInfo(testRecordInfo())
        .stationDeclination(stationDeclination)
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
