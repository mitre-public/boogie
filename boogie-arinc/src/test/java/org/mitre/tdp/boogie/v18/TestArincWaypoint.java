package org.mitre.tdp.boogie.v18;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.mitre.tdp.boogie.v18.record.TestWaypointSpec.waypoint1;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

public class TestArincWaypoint {

  @Test
  public void testFieldAccess() {
    ArincWaypoint waypoint = ArincWaypoint.wrap(ArincVersion.V18.parse(waypoint1));

    assertEquals(RecordType.S, waypoint.recordType());
    assertEquals(CustomerAreaCode.SAM, waypoint.customerAreaCode());
    assertEquals(SectionCode.E, waypoint.sectionCode());
    assertEquals("A", waypoint.subSectionCode());
    assertEquals("ENRT", waypoint.airportIdentifier().get());
    assertFalse(waypoint.airportIcaoRegion().isPresent());
    assertEquals("UMGOS", waypoint.fixIdentifier());
    assertEquals("SK", waypoint.icaoRegion());
    assertEquals("1", waypoint.continuationRecordNumber());
    assertEquals("R  ", waypoint.waypointType().get());
    assertEquals("RH", waypoint.waypointUsage().get());
    assertEquals(3.3166666666666664d, waypoint.latitude());
    assertEquals(-80.78277777777778d, waypoint.longitude());
    assertEquals(-2.3d, waypoint.magneticVariation().get());
    assertEquals("WGE", waypoint.datumCode().get());
    assertEquals("P  ", waypoint.nameFormat().get());
    assertEquals("UMGOS", waypoint.waypointNameDescription().get());
    assertEquals(Integer.valueOf(20914), waypoint.fileRecordNumber());
    assertEquals("2003", waypoint.cycle());
  }
}