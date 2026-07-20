package org.mitre.boogie.xml.assemble;

import java.util.List;

import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincProcedure;
import org.mitre.boogie.xml.model.ArincWaypoint;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordType;
import org.mitre.boogie.xml.model.fields.ArincWaypointType;
import org.mitre.boogie.xml.model.fields.ArincWaypointUsage;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;

/**
 * Shared test fixtures for procedure assembly tests.
 */
final class ProcedureTestFixtures {

  static final LatLong AIRPORT_POSITION = LatLong.of(42.2124, -83.3534);
  static final LatLong FIX_POSITION = LatLong.of(42.0, -83.0);
  static final MagneticVariation MAG_VAR = MagneticVariation.ofDegrees(-5.0);
  static final String CYCLE = "2504";

  private ProcedureTestFixtures() {
  }

  static ArincProcedure testProcedure(String identifier, String procedureType, boolean isRnav) {
    return ArincProcedure.builder()
        .identifier(identifier)
        .procedureType(procedureType)
        .isRnav(isRnav)
        .build();
  }

  static Fix testFix(String identifier) {
    return Fix.builder()
        .fixIdentifier(identifier)
        .latLong(FIX_POSITION)
        .magneticVariation(MAG_VAR)
        .build();
  }

  static ArincAirport testAirport(String identifier, List<ArincProcedure> procedures) {
    ArincPortInfo portInfo = ArincPortInfo.builder()
        .pointInfo(testPointInfo(identifier, AIRPORT_POSITION))
        .recordInfo(testRecordInfo())
        .procedures(procedures)
        .build();

    return ArincAirport.builder()
        .portInfo(portInfo)
        .build();
  }

  static ArincWaypoint testWaypoint(String identifier, String referenceId) {
    return ArincWaypoint.builder()
        .pointInfo(ArincPointInfo.builder()
            .identifier(identifier)
            .icaoCode("K6")
            .latLong(FIX_POSITION)
            .magneticVariation(MAG_VAR)
            .referenceId(referenceId)
            .build())
        .recordInfo(testRecordInfo())
        .waypointType(ArincWaypointType.builder().build())
        .waypointUsage(ArincWaypointUsage.of(false, false, false))
        .build();
  }

  static ArincPointInfo testPointInfo(String identifier, LatLong position) {
    return ArincPointInfo.builder()
        .identifier(identifier)
        .icaoCode("K6")
        .latLong(position)
        .magneticVariation(MAG_VAR)
        .referenceId(identifier)
        .build();
  }

  static ArincRecordInfo testRecordInfo() {
    return ArincRecordInfo.builder()
        .cycleDate(CYCLE)
        .recordType(ArincRecordType.STANDARD)
        .build();
  }
}
