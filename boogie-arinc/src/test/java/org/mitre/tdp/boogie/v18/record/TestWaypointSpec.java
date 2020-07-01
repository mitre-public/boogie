package org.mitre.tdp.boogie.v18.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.record.WaypointSpec;

public class TestWaypointSpec {

  public static final String enrouteWaypoint = "SSAMEAENRT   UMGOS SK1    R  RH N03190000W080465800                       W0023     WGE        P  UMGOS                    209142003";

  @Test
  public void testSpecMatchesEnrouteRecord() {
    assertTrue(new WaypointSpec().matchesRecord(enrouteWaypoint));
  }

  @Test
  public void testParseEnrouteWaypoint() {
    ArincRecord arincRecord = ArincVersion.V18.parse(enrouteWaypoint);
    assertNotNull(arincRecord);

    assertEquals(RecordType.S, arincRecord.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.SAM, arincRecord.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.E, arincRecord.getRequiredField("sectionCode"));
    assertEquals("A", arincRecord.getRequiredField("enrouteSubSectionCode"));
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

  public static final String terminalWaypoint = "SUSAP KFMHK6CREDSX K61    RIF   N41552146W070140577                       W0154     NAR        P  REDSX                    520911902";

  @Test
  public void testSpecMatchesTerminalRecord() {
    assertTrue(new WaypointSpec().matchesRecord(terminalWaypoint));
  }

  @Test
  public void testParseTerminalWaypoint() {
    ArincRecord arincRecord = ArincVersion.V18.parse(terminalWaypoint);
    assertNotNull(arincRecord);

    assertEquals(RecordType.S, arincRecord.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.USA, arincRecord.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.P, arincRecord.getRequiredField("sectionCode"));
    assertFalse(arincRecord.getOptionalField("enrouteSubSectionCode").isPresent());
    assertEquals("KFMH", arincRecord.getRequiredField("airportIdentifier"));
    assertEquals("K6", arincRecord.getRequiredField("airportIcaoRegion"));
    assertEquals("C", arincRecord.getRequiredField("terminalSubSectionCode"));
    assertEquals("REDSX", arincRecord.getRequiredField("fixIdentifier"));
    assertEquals("K6", arincRecord.getRequiredField("icaoRegion"));
    assertEquals("1", arincRecord.getRequiredField("continuationRecordNumber"));
    assertEquals("RIF", arincRecord.getRequiredField("waypointType"));
    assertEquals("  ", arincRecord.getRequiredField("waypointUsage"));
    assertEquals(41.92262777777778d, arincRecord.getRequiredField("latitude"));
    assertEquals(-70.23493611111111d, arincRecord.getRequiredField("longitude"));
    assertEquals(-15.4d, arincRecord.getOptionalField("magneticVariation").get());
    assertEquals("NAR", arincRecord.getRequiredField("datumCode"));
    assertEquals("P  ", arincRecord.getRequiredField("nameFormat"));
    assertEquals("REDSX", arincRecord.getRequiredField("waypointNameDescription"));
    assertEquals(Integer.valueOf(52091), arincRecord.getRequiredField("fileRecordNumber"));
    assertEquals("1902", arincRecord.getRequiredField("cycle"));
  }
}
