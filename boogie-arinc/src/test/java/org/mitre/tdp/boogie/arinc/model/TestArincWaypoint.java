package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.v18.WaypointConverter;
import org.mitre.tdp.boogie.arinc.v18.WaypointSpec;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import nl.jqno.equalsverifier.EqualsVerifier;

class TestArincWaypoint {

  private static final ArincRecordParser PARSER = ArincRecordParser.standard(new WaypointSpec());

  private static final WaypointConverter converter = new WaypointConverter();

  private static final String enrouteWaypoint = "SSAMEAENRT   UMGOS SK1    R  RH N03190000W080465800                       W0023     WGE        P  UMGOS                    209142003";

  @Test
  void testEqualsHashCode() {
    EqualsVerifier.forClass(ArincWaypoint.class).verify();
  }

  @Test
  void testEnrouteFieldAccess() {
    ArincWaypoint waypoint = PARSER.parse(enrouteWaypoint).flatMap(converter).orElseThrow(AssertionError::new).toBuilder().build();

    assertAll(
        () -> Assertions.assertEquals(RecordType.S, waypoint.recordType()),
        () -> Assertions.assertEquals(CustomerAreaCode.SAM, waypoint.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> Assertions.assertEquals(SectionCode.E, waypoint.sectionCode()),
        () -> assertEquals("A", waypoint.enrouteSubSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("ENRT", waypoint.airportIdentifier().orElseThrow(AssertionError::new)),
        () -> assertFalse(waypoint.airportIcaoRegion().isPresent()),
        () -> assertEquals("UMGOS", waypoint.waypointIdentifier()),
        () -> assertEquals("SK", waypoint.waypointIcaoRegion()),
        () -> assertEquals("1", waypoint.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("R  ", waypoint.waypointType().orElseThrow(AssertionError::new)),
        () -> assertEquals("RH", waypoint.waypointUsage().orElseThrow(AssertionError::new)),
        () -> assertEquals(3.3166666666666664d, waypoint.latitude()),
        () -> assertEquals(-80.78277777777778d, waypoint.longitude()),
        () -> assertEquals(-2.3d, waypoint.magneticVariation().orElseThrow(AssertionError::new)),
        () -> assertEquals("WGE", waypoint.datumCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("P  ", waypoint.nameFormat().orElseThrow(AssertionError::new)),
        () -> assertEquals("UMGOS", waypoint.waypointNameDescription().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(20914), waypoint.fileRecordNumber()),
        () -> assertEquals("2003", waypoint.lastUpdateCycle())
    );
  }

  private static final String terminalWaypoint = "SUSAP KFMHK6CREDSX K61    RIF   N41552146W070140577                       W0154     NAR        P  REDSX                    520911902";

  @Test
  void tesTerminalFieldAccess() {
    ArincWaypoint waypoint = PARSER.parse(terminalWaypoint).flatMap(converter).orElseThrow(AssertionError::new).toBuilder().build();

    assertAll(
        () -> Assertions.assertEquals(RecordType.S, waypoint.recordType()),
        () -> Assertions.assertEquals(CustomerAreaCode.USA, waypoint.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> Assertions.assertEquals(SectionCode.P, waypoint.sectionCode()),
        () -> assertFalse(waypoint.enrouteSubSectionCode().isPresent()),
        () -> assertEquals("KFMH", waypoint.airportIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("K6", waypoint.airportIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals("C", waypoint.terminalSubSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("REDSX", waypoint.waypointIdentifier()),
        () -> assertEquals("K6", waypoint.waypointIcaoRegion()),
        () -> assertEquals("1", waypoint.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("RIF", waypoint.waypointType().orElseThrow(AssertionError::new)),
        () -> assertEquals("  ", waypoint.waypointUsage().orElseThrow(AssertionError::new)),
        () -> assertEquals(41.92262777777778d, waypoint.latitude()),
        () -> assertEquals(-70.23493611111111d, waypoint.longitude()),
        () -> assertEquals(-15.4d, waypoint.magneticVariation().orElseThrow(AssertionError::new)),
        () -> assertEquals("NAR", waypoint.datumCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("P  ", waypoint.nameFormat().orElseThrow(AssertionError::new)),
        () -> assertEquals("REDSX", waypoint.waypointNameDescription().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(52091), waypoint.fileRecordNumber()),
        () -> assertEquals("1902", waypoint.lastUpdateCycle())
    );
  }
}