package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mitre.tdp.boogie.arinc.v18.spec.TestWaypointSpec.enrouteWaypoint;
import static org.mitre.tdp.boogie.arinc.v18.spec.TestWaypointSpec.terminalWaypoint;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestArincWaypoint {

  @Test
  void testEnrouteFieldAccess() {
    ArincWaypoint waypoint = ArincWaypoint.wrap(ArincVersion.V18.parser().apply(enrouteWaypoint).orElseThrow(AssertionError::new));

    assertAll(
        () -> assertEquals(RecordType.S, waypoint.recordType().orElseThrow(AssertionError::new)),
        () -> assertEquals(CustomerAreaCode.SAM, waypoint.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.E, waypoint.sectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("A", waypoint.enrouteSubSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("ENRT", waypoint.airportIdentifier().orElseThrow(AssertionError::new)),
        () -> assertFalse(waypoint.airportIcaoRegion().isPresent()),
        () -> assertEquals("UMGOS", waypoint.fixIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("SK", waypoint.icaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals("1", waypoint.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("R  ", waypoint.waypointType().orElseThrow(AssertionError::new)),
        () -> assertEquals("RH", waypoint.waypointUsage().orElseThrow(AssertionError::new)),
        () -> assertEquals(3.3166666666666664d, waypoint.latitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(-80.78277777777778d, waypoint.longitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(-2.3d, waypoint.magneticVariation().orElseThrow(AssertionError::new)),
        () -> assertEquals("WGE", waypoint.datumCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("P  ", waypoint.nameFormat().orElseThrow(AssertionError::new)),
        () -> assertEquals("UMGOS", waypoint.waypointNameDescription().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(20914), waypoint.fileRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("2003", waypoint.cycle().orElseThrow(AssertionError::new))
    );
  }

  @Test
  void tesTerminalFieldAccess() {
    ArincWaypoint waypoint = ArincWaypoint.wrap(ArincVersion.V18.parser().apply(terminalWaypoint).orElseThrow(AssertionError::new));

    assertAll(
        () -> assertEquals(RecordType.S, waypoint.recordType().orElseThrow(AssertionError::new)),
        () -> assertEquals(CustomerAreaCode.USA, waypoint.customerAreaCode().orElseThrow(AssertionError::new)),
        () -> assertEquals(SectionCode.P, waypoint.sectionCode().orElseThrow(AssertionError::new)),
        () -> assertFalse(waypoint.enrouteSubSectionCode().isPresent()),
        () -> assertEquals("KFMH", waypoint.airportIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("K6", waypoint.airportIcaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals("C", waypoint.terminalSubSectionCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("REDSX", waypoint.fixIdentifier().orElseThrow(AssertionError::new)),
        () -> assertEquals("K6", waypoint.icaoRegion().orElseThrow(AssertionError::new)),
        () -> assertEquals("1", waypoint.continuationRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("RIF", waypoint.waypointType().orElseThrow(AssertionError::new)),
        () -> assertEquals("  ", waypoint.waypointUsage().orElseThrow(AssertionError::new)),
        () -> assertEquals(41.92262777777778d, waypoint.latitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(-70.23493611111111d, waypoint.longitude().orElseThrow(AssertionError::new)),
        () -> assertEquals(-15.4d, waypoint.magneticVariation().orElseThrow(AssertionError::new)),
        () -> assertEquals("NAR", waypoint.datumCode().orElseThrow(AssertionError::new)),
        () -> assertEquals("P  ", waypoint.nameFormat().orElseThrow(AssertionError::new)),
        () -> assertEquals("REDSX", waypoint.waypointNameDescription().orElseThrow(AssertionError::new)),
        () -> assertEquals(Integer.valueOf(52091), waypoint.fileRecordNumber().orElseThrow(AssertionError::new)),
        () -> assertEquals("1902", waypoint.cycle().orElseThrow(AssertionError::new))
    );
  }
}