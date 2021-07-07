package org.mitre.tdp.boogie.arinc.v18.spec;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.ArincRecord;

public class TestAirwayLegSpec {

  public static final String airway1 = "SCANER       A1          0100XX   CYDB0N    O                         22250425     UNKNN                                   024131710";

  @Test
  void testSpecMatchesAirwayRecord1() {
    assertTrue(new AirwayLegSpec().matchesRecord(airway1));
  }

  @Test
  void testParseAirway1() {
    ArincRecord record = ArincVersion.V18.parser().apply(airway1).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.CAN, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.E, record.requiredField("sectionCode")),
        () -> assertEquals("R", record.requiredField("subSectionCode")),
        () -> assertEquals("A1", record.requiredField("routeIdentifier")),
        () -> assertFalse(record.optionalField("sixthCharacter").isPresent()),
        () -> assertEquals(Integer.valueOf(100), record.requiredField("sequenceNumber")),
        () -> assertEquals("XX", record.requiredField("fixIdentifier")),
        () -> assertEquals("CY", record.requiredField("fixIcaoRegion")),
        () -> assertEquals(SectionCode.D, record.requiredField("fixSectionCode")),
        () -> assertEquals("B", record.requiredField("fixSubSectionCode")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals("N   ", record.requiredField("waypointDescription")),
        () -> assertFalse(record.optionalField("boundaryCode").isPresent()),
        () -> assertEquals("O", record.requiredField("routeType")),
        () -> assertFalse(record.optionalField("level").isPresent()),
        () -> assertEquals(" ", record.requiredField("directionRestriction")),
//    () -> assertFalse(record.getOptionalField("tcIndicator").isPresent()),
        () -> assertEquals(false, record.requiredField("euIndicator")),
        () -> assertFalse(record.optionalField("recommendedNavaidIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("recommendedNavaidIcaoRegion").isPresent()),
        () -> assertFalse(record.optionalField("rnp").isPresent()),
        () -> assertFalse(record.optionalField("theta").isPresent()),
        () -> assertFalse(record.optionalField("rho").isPresent()),
        () -> assertEquals(222.5, record.requiredField("outboundMagneticCourse")),
        () -> assertEquals("0425", record.requiredField("routeHoldDistanceTime")),
        () -> assertFalse(record.optionalField("inboundMagneticCourse").isPresent()),
        () -> assertFalse(record.optionalField("minAltitude1").isPresent()),
        () -> assertFalse(record.optionalField("minAltitude2").isPresent()),
        () -> assertFalse(record.optionalField("maxAltitude").isPresent()),
        () -> assertFalse(record.optionalField("fixedRadiusTransitionIndicator").isPresent()),
        () -> assertEquals(Integer.valueOf(2413), record.requiredField("fileRecordNumber")),
        () -> assertEquals("1710", record.requiredField("lastUpdateCycle"))
    );
  }

  public static final String airway2 = "SAFRER       A18         6280KDR  HLDB0E C                                                                                 357801711";

  @Test
  void testSpecMatchesAirwayRecord2() {
    assertTrue(new AirwayLegSpec().matchesRecord(airway2));
  }

  @Test
  void testParseAirway2() {
    ArincRecord record = ArincVersion.V18.parser().apply(airway2).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.AFR, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.E, record.requiredField("sectionCode")),
        () -> assertEquals("R", record.requiredField("subSectionCode")),
        () -> assertEquals("A18", record.requiredField("routeIdentifier")),
        () -> assertFalse(record.optionalField("sixthCharacter").isPresent()),
        () -> assertEquals(Integer.valueOf(6280), record.requiredField("sequenceNumber")),
        () -> assertEquals("KDR", record.requiredField("fixIdentifier")),
        () -> assertEquals("HL", record.requiredField("fixIcaoRegion")),
        () -> assertEquals(SectionCode.D, record.requiredField("fixSectionCode")),
        () -> assertEquals("B", record.requiredField("fixSubSectionCode")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals("E C ", record.requiredField("waypointDescription")),
        () -> assertFalse(record.optionalField("boundaryCode").isPresent()),
        () -> assertFalse(record.optionalField("routeType").isPresent()),
        () -> assertFalse(record.optionalField("level").isPresent()),
        () -> assertEquals(" ", record.requiredField("directionRestriction")),
//    () -> assertFalse(record.getOptionalField("tcIndicator").isPresent()),
        () -> assertEquals(false, record.requiredField("euIndicator")),
        () -> assertFalse(record.optionalField("recommendedNavaidIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("recommendedNavaidIcaoRegion").isPresent()),
        () -> assertFalse(record.optionalField("rnp").isPresent()),
        () -> assertFalse(record.optionalField("theta").isPresent()),
        () -> assertFalse(record.optionalField("rho").isPresent()),
        () -> assertFalse(record.optionalField("outboundMagneticCourse").isPresent()),
        () -> assertFalse(record.optionalField("routeHoldDistanceTime").isPresent()),
        () -> assertFalse(record.optionalField("inboundMagneticCourse").isPresent()),
        () -> assertFalse(record.optionalField("minAltitude1").isPresent()),
        () -> assertFalse(record.optionalField("minAltitude2").isPresent()),
        () -> assertFalse(record.optionalField("maxAltitude").isPresent()),
        () -> assertFalse(record.optionalField("fixedRadiusTransitionIndicator").isPresent()),
        () -> assertEquals(Integer.valueOf(35780), record.requiredField("fileRecordNumber")),
        () -> assertEquals("1711", record.requiredField("lastUpdateCycle"))
    );
  }

  public static final String airway3 = "SCANER       A590        0210PASROPAEA0E C  OHFRA                     059422850590 FL180     FL600                         233652006";

  @Test
  void testSpecMatchesAirwayRecord3() {
    assertTrue(new AirwayLegSpec().matchesRecord(airway3));
  }

  @Test
  void testParseAirway3() {
    ArincRecord record = ArincVersion.V18.parser().apply(airway3).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.CAN, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.E, record.requiredField("sectionCode")),
        () -> assertEquals("R", record.requiredField("subSectionCode")),
        () -> assertEquals("A590", record.requiredField("routeIdentifier")),
        () -> assertFalse(record.optionalField("sixthCharacter").isPresent()),
        () -> assertEquals(Integer.valueOf(210), record.requiredField("sequenceNumber")),
        () -> assertEquals("PASRO", record.requiredField("fixIdentifier")),
        () -> assertEquals("PA", record.requiredField("fixIcaoRegion")),
        () -> assertEquals(SectionCode.E, record.requiredField("fixSectionCode")),
        () -> assertEquals("A", record.requiredField("fixSubSectionCode")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals("E C ", record.requiredField("waypointDescription")),
        () -> assertFalse(record.optionalField("boundaryCode").isPresent()),
        () -> assertEquals("O", record.requiredField("routeType")),
        () -> assertEquals(Level.H, record.requiredField("level")),
        () -> assertEquals("F", record.requiredField("directionRestriction")),
//    () -> assertFalse(record.getOptionalField("tcIndicator").isPresent()),
        () -> assertEquals(false, record.requiredField("euIndicator")),
        () -> assertFalse(record.optionalField("recommendedNavaidIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("recommendedNavaidIcaoRegion").isPresent()),
        () -> assertFalse(record.optionalField("rnp").isPresent()),
        () -> assertFalse(record.optionalField("theta").isPresent()),
        () -> assertFalse(record.optionalField("rho").isPresent()),
        () -> assertEquals(59.4d, record.requiredField("outboundMagneticCourse")),
        () -> assertEquals("2285", record.requiredField("routeHoldDistanceTime")),
        () -> assertEquals(59.0d, record.requiredField("inboundMagneticCourse")),
        () -> assertEquals(18000.0d, record.requiredField("minAltitude1")),
        () -> assertFalse(record.optionalField("minAltitude2").isPresent()),
        () -> assertEquals(60000.0d, record.requiredField("maxAltitude")),
        () -> assertFalse(record.optionalField("fixedRadiusTransitionIndicator").isPresent()),
        () -> assertEquals(Integer.valueOf(23365), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2006", record.requiredField("lastUpdateCycle"))
    );
  }
}
