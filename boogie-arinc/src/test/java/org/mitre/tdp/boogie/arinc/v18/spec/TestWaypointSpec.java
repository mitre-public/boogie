package org.mitre.tdp.boogie.arinc.v18.spec;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public class TestWaypointSpec {

  public static final String enrouteWaypoint = "SSAMEAENRT   UMGOS SK1    R  RH N03190000W080465800                       W0023     WGE        P  UMGOS                    209142003";

  @Test
  void testSpecMatchesEnrouteRecord() {
    assertTrue(new WaypointSpec().matchesRecord(enrouteWaypoint));
  }

  @Test
  void testParseEnrouteWaypoint() {
    ArincRecord arincRecord = ArincVersion.V18.parser().apply(enrouteWaypoint).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, arincRecord.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.SAM, arincRecord.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.E, arincRecord.requiredField("sectionCode")),
        () -> assertEquals("A", arincRecord.requiredField("enrouteSubSectionCode")),
        () -> assertEquals("ENRT", arincRecord.requiredField("airportIdentifier")),
        () -> assertFalse(arincRecord.optionalField("airportIcaoRegion").isPresent()),
        () -> assertEquals("UMGOS", arincRecord.requiredField("fixIdentifier")),
        () -> assertEquals("SK", arincRecord.requiredField("icaoRegion")),
        () -> assertEquals("1", arincRecord.requiredField("continuationRecordNumber")),
        () -> assertEquals("R  ", arincRecord.requiredField("waypointType")),
        () -> assertEquals("RH", arincRecord.requiredField("waypointUsage")),
        () -> assertEquals(3.3166666666666664d, arincRecord.requiredField("latitude")),
        () -> assertEquals(-80.78277777777778d, arincRecord.requiredField("longitude")),
        () -> assertEquals(-2.3d, arincRecord.optionalField("magneticVariation").get()),
        () -> assertEquals("WGE", arincRecord.requiredField("datumCode")),
        () -> assertEquals("P  ", arincRecord.requiredField("nameFormat")),
        () -> assertEquals("UMGOS", arincRecord.requiredField("waypointNameDescription")),
        () -> assertEquals(Integer.valueOf(20914), arincRecord.requiredField("fileRecordNumber")),
        () -> assertEquals("2003", arincRecord.requiredField("cycle"))
    );
  }

  public static final String terminalWaypoint = "SUSAP KFMHK6CREDSX K61    RIF   N41552146W070140577                       W0154     NAR        P  REDSX                    520911902";

  @Test
  void testSpecMatchesTerminalRecord() {
    assertTrue(new WaypointSpec().matchesRecord(terminalWaypoint));
  }

  @Test
  void testParseTerminalWaypoint() {
    ArincRecord arincRecord = ArincVersion.V18.parser().apply(terminalWaypoint).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, arincRecord.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, arincRecord.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, arincRecord.requiredField("sectionCode")),
        () -> assertFalse(arincRecord.optionalField("enrouteSubSectionCode").isPresent()),
        () -> assertEquals("KFMH", arincRecord.requiredField("airportIdentifier")),
        () -> assertEquals("K6", arincRecord.requiredField("airportIcaoRegion")),
        () -> assertEquals("C", arincRecord.requiredField("terminalSubSectionCode")),
        () -> assertEquals("REDSX", arincRecord.requiredField("fixIdentifier")),
        () -> assertEquals("K6", arincRecord.requiredField("icaoRegion")),
        () -> assertEquals("1", arincRecord.requiredField("continuationRecordNumber")),
        () -> assertEquals("RIF", arincRecord.requiredField("waypointType")),
        () -> assertEquals("  ", arincRecord.requiredField("waypointUsage")),
        () -> assertEquals(41.92262777777778d, arincRecord.requiredField("latitude")),
        () -> assertEquals(-70.23493611111111d, arincRecord.requiredField("longitude")),
        () -> assertEquals(-15.4d, arincRecord.optionalField("magneticVariation").get()),
        () -> assertEquals("NAR", arincRecord.requiredField("datumCode")),
        () -> assertEquals("P  ", arincRecord.requiredField("nameFormat")),
        () -> assertEquals("REDSX", arincRecord.requiredField("waypointNameDescription")),
        () -> assertEquals(Integer.valueOf(52091), arincRecord.requiredField("fileRecordNumber")),
        () -> assertEquals("1902", arincRecord.requiredField("cycle"))
    );
  }
}
