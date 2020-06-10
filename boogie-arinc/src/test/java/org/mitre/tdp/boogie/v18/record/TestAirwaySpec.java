package org.mitre.tdp.boogie.v18.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.Level;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.record.AirwaySpec;

public class TestAirwaySpec {

  public static final String airway1 = "SCANER       A1          0100XX   CYDB0N    O                         22250425     UNKNN                                   024131710";

  @Test
  public void testSpecMatchesAirwayRecord1() {
    assertTrue(new AirwaySpec().matchesRecord(airway1));
  }

  @Test
  public void testParseAirway1() {
    ArincRecord record = ArincVersion.V18.parse(airway1);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.CAN, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.E, record.getRequiredField("sectionCode"));
    assertEquals("R", record.getRequiredField("subSectionCode"));
    assertEquals("A1", record.getRequiredField("routeIdentifier"));
    assertFalse(record.getOptionalField("sixthCharacter").isPresent());
    assertEquals(Integer.valueOf(100), record.getRequiredField("sequenceNumber"));
    assertEquals("XX", record.getRequiredField("fixIdentifier"));
    assertEquals("CY", record.getRequiredField("fixIcaoRegion"));
    assertEquals(SectionCode.D, record.getRequiredField("fixSectionCode"));
    assertEquals("B", record.getRequiredField("fixSubSectionCode"));
    assertEquals("0", record.getRequiredField("continuationRecordNumber"));
    assertEquals("N   ", record.getRequiredField("fixDescription"));
    assertFalse(record.getOptionalField("boundaryCode").isPresent());
    assertEquals("O", record.getRequiredField("routeType"));
    assertFalse(record.getOptionalField("level").isPresent());
    assertEquals(" ", record.getRequiredField("directionRestriction"));
//    assertFalse(record.getOptionalField("tcIndicator").isPresent());
    assertEquals(false, record.getRequiredField("euIndicator"));
    assertFalse(record.getOptionalField("recommendedNavaid").isPresent());
    assertFalse(record.getOptionalField("recommendedNavaidIcaoRegion").isPresent());
    assertFalse(record.getOptionalField("rnp").isPresent());
    assertFalse(record.getOptionalField("theta").isPresent());
    assertFalse(record.getOptionalField("rho").isPresent());
    assertEquals(222.5, record.getRequiredField("outboundMagneticCourse"));
    assertEquals("0425", record.getRequiredField("routeDistance"));
    assertFalse(record.getOptionalField("inboundMagneticCourse").isPresent());
    assertFalse(record.getOptionalField("minAltitude1").isPresent());
    assertFalse(record.getOptionalField("minAltitude2").isPresent());
    assertFalse(record.getOptionalField("maxAltitude").isPresent());
    assertFalse(record.getOptionalField("fixedRadiusTransitionIndicator").isPresent());
    assertEquals(Integer.valueOf(2413), record.getRequiredField("fileRecordNumber"));
    assertEquals("1710", record.getRequiredField("cycle"));
  }

  public static final String airway2 = "SAFRER       A18         6280KDR  HLDB0E C                                                                                 357801711";

  @Test
  public void testSpecMatchesAirwayRecord2() {
    assertTrue(new AirwaySpec().matchesRecord(airway2));
  }

  @Test
  public void testParseAirway2() {
    ArincRecord record = ArincVersion.V18.parse(airway2);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.AFR, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.E, record.getRequiredField("sectionCode"));
    assertEquals("R", record.getRequiredField("subSectionCode"));
    assertEquals("A18", record.getRequiredField("routeIdentifier"));
    assertFalse(record.getOptionalField("sixthCharacter").isPresent());
    assertEquals(Integer.valueOf(6280), record.getRequiredField("sequenceNumber"));
    assertEquals("KDR", record.getRequiredField("fixIdentifier"));
    assertEquals("HL", record.getRequiredField("fixIcaoRegion"));
    assertEquals(SectionCode.D, record.getRequiredField("fixSectionCode"));
    assertEquals("B", record.getRequiredField("fixSubSectionCode"));
    assertEquals("0", record.getRequiredField("continuationRecordNumber"));
    assertEquals("E C ", record.getRequiredField("fixDescription"));
    assertFalse(record.getOptionalField("boundaryCode").isPresent());
    assertFalse(record.getOptionalField("routeType").isPresent());
    assertFalse(record.getOptionalField("level").isPresent());
    assertEquals(" ", record.getRequiredField("directionRestriction"));
//    assertFalse(record.getOptionalField("tcIndicator").isPresent());
    assertEquals(false, record.getRequiredField("euIndicator"));
    assertFalse(record.getOptionalField("recommendedNavaid").isPresent());
    assertFalse(record.getOptionalField("recommendedNavaidIcaoRegion").isPresent());
    assertFalse(record.getOptionalField("rnp").isPresent());
    assertFalse(record.getOptionalField("theta").isPresent());
    assertFalse(record.getOptionalField("rho").isPresent());
    assertFalse(record.getOptionalField("outboundMagneticCourse").isPresent());
    assertFalse(record.getOptionalField("routeDistance").isPresent());
    assertFalse(record.getOptionalField("inboundMagneticCourse").isPresent());
    assertFalse(record.getOptionalField("minAltitude1").isPresent());
    assertFalse(record.getOptionalField("minAltitude2").isPresent());
    assertFalse(record.getOptionalField("maxAltitude").isPresent());
    assertFalse(record.getOptionalField("fixedRadiusTransitionIndicator").isPresent());
    assertEquals(Integer.valueOf(35780), record.getRequiredField("fileRecordNumber"));
    assertEquals("1711", record.getRequiredField("cycle"));
  }

  public static final String airway3 = "SCANER       A590        0210PASROPAEA0E C  OHFRA                     059422850590 FL180     FL600                         233652006";

  @Test
  public void testSpecMatchesAirwayRecord3() {
    assertTrue(new AirwaySpec().matchesRecord(airway3));
  }

  @Test
  public void testParseAirway3() {
    ArincRecord record = ArincVersion.V18.parse(airway3);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.CAN, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.E, record.getRequiredField("sectionCode"));
    assertEquals("R", record.getRequiredField("subSectionCode"));
    assertEquals("A590", record.getRequiredField("routeIdentifier"));
    assertFalse(record.getOptionalField("sixthCharacter").isPresent());
    assertEquals(Integer.valueOf(210), record.getRequiredField("sequenceNumber"));
    assertEquals("PASRO", record.getRequiredField("fixIdentifier"));
    assertEquals("PA", record.getRequiredField("fixIcaoRegion"));
    assertEquals(SectionCode.E, record.getRequiredField("fixSectionCode"));
    assertEquals("A", record.getRequiredField("fixSubSectionCode"));
    assertEquals("0", record.getRequiredField("continuationRecordNumber"));
    assertEquals("E C ", record.getRequiredField("fixDescription"));
    assertFalse(record.getOptionalField("boundaryCode").isPresent());
    assertEquals("O", record.getRequiredField("routeType"));
    assertEquals(Level.H, record.getRequiredField("level"));
    assertEquals("F", record.getRequiredField("directionRestriction"));
//    assertFalse(record.getOptionalField("tcIndicator").isPresent());
    assertEquals(false, record.getRequiredField("euIndicator"));
    assertFalse(record.getOptionalField("recommendedNavaid").isPresent());
    assertFalse(record.getOptionalField("recommendedNavaidIcaoRegion").isPresent());
    assertFalse(record.getOptionalField("rnp").isPresent());
    assertFalse(record.getOptionalField("theta").isPresent());
    assertFalse(record.getOptionalField("rho").isPresent());
    assertEquals(59.4d, record.getRequiredField("outboundMagneticCourse"));
    assertEquals("2285", record.getRequiredField("routeDistance"));
    assertEquals(59.0d, record.getRequiredField("inboundMagneticCourse"));
    assertEquals(18000.0d, record.getRequiredField("minAltitude1"));
    assertFalse(record.getOptionalField("minAltitude2").isPresent());
    assertEquals(60000.0d, record.getRequiredField("maxAltitude"));
    assertFalse(record.getOptionalField("fixedRadiusTransitionIndicator").isPresent());
    assertEquals(Integer.valueOf(23365), record.getRequiredField("fileRecordNumber"));
    assertEquals("2006", record.getRequiredField("cycle"));
  }


}
