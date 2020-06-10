package org.mitre.tdp.boogie.v18.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;

public class TestWaypointSpec {

  public static final String waypoint1 = "SSAMEAENRT   UMGOS SK1    R  RH N03190000W080465800                       W0023     WGE        P  UMGOS                    209142003";

  @Test
  public void testParseWaypoint1() {
    ArincRecord arincRecord = ArincVersion.V18.parse(waypoint1);
    assertNotNull(arincRecord);

    assertEquals(RecordType.S, arincRecord.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.SAM, arincRecord.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.E, arincRecord.getRequiredField("sectionCode"));
    assertEquals("A", arincRecord.getRequiredField("subSectionCode"));
    assertEquals("ENRT", arincRecord.getRequiredField("airportIdentifier"));
    assertFalse(arincRecord.getOptionalField("airportIcaoRegion").isPresent());
    assertEquals("UMGOS", arincRecord.getRequiredField("fixIdentifier"));
    assertEquals("SK", arincRecord.getRequiredField("icaoRegion"));
    assertEquals("1", arincRecord.getRequiredField("continuationRecordNumber"));
    assertEquals("R  ", arincRecord.getRequiredField("waypointType"));
    assertEquals("RH", arincRecord.getRequiredField("waypointUsage"));
    assertEquals(3.3166666666666664d, arincRecord.getRequiredField("latitude"));
    assertEquals(-80.78277777777778d, arincRecord.getRequiredField("longitude"));
    assertEquals(-2.3d, arincRecord.getOptionalField("magneticVariation").get());
    assertEquals("WGE", arincRecord.getRequiredField("datumCode"));
    assertEquals("P  ", arincRecord.getRequiredField("nameFormat"));
    assertEquals("UMGOS", arincRecord.getRequiredField("waypointNameDescription"));
    assertEquals(Integer.valueOf(20914), arincRecord.getRequiredField("fileRecordNumber"));
    assertEquals("2003", arincRecord.getRequiredField("cycle"));
  }
}
