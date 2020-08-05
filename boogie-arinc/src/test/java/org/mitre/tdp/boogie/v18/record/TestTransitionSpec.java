package org.mitre.tdp.boogie.v18.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.field.TurnDirection;
import org.mitre.tdp.boogie.v18.spec.record.TransitionSpec;

public class TestTransitionSpec {

  public static final String IF = "SUSAP KJFKK6FV13R  V      010ASALTK6EA1E  D    IF CRI K6      22480060        D     03000     18000                 3 DS   154932004";

  @Test
  public void testSpecMatchesIF_PE() {
    assertTrue(new TransitionSpec().matchesRecord(IF));
  }

  @Test
  public void testParseIF() {
    ArincRecord record = ArincVersion.V18.parse(IF);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.USA, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.P, record.getRequiredField("sectionCode"));
    assertEquals("KJFK", record.getRequiredField("airportIdentifier"));
    assertEquals("K6", record.getRequiredField("airportIcaoRegion"));
    assertEquals("F", record.getRequiredField("subSectionCode"));
    assertEquals("V13R", record.getRequiredField("sidStarIdentifier"));
    assertEquals("V", record.getRequiredField("routeType"));
    assertFalse(record.getOptionalField("transitionIdentifier").isPresent());
    assertEquals(Integer.valueOf(10), record.getRequiredField("sequenceNumber"));
    assertEquals("ASALT", record.getRequiredField("fixIdentifier"));
    assertEquals("K6", record.getRequiredField("fixIcaoRegion"));
    assertEquals(SectionCode.E, record.getRequiredField("fixSectionCode"));
    assertEquals("A", record.getRequiredField("fixSubSectionCode"));
    assertEquals("1", record.getRequiredField("continuationRecordNumber"));
    assertEquals("E  D", record.getRequiredField("waypointDescription"));
    assertFalse(record.getOptionalField("turnDirection").isPresent());
    assertFalse(record.getOptionalField("rnp").isPresent());
    assertEquals(PathTerm.IF, record.getRequiredField("pathTerm"));
    assertEquals(false, record.getRequiredField("turnDirectionValid"));
    assertEquals("CRI", record.getRequiredField("recommendedNavaid"));
    assertEquals("K6", record.getRequiredField("recommendedNavaidIcaoRegion"));
    assertFalse(record.getOptionalField("arcRadius").isPresent());
    assertEquals(224.8d, record.getRequiredField("theta"));
    assertEquals(6.0d, record.getRequiredField("rho"));
    assertFalse(record.getOptionalField("outboundMagneticCourse").isPresent());
    assertFalse(record.getOptionalField("routeHoldDistanceTime").isPresent());
    assertEquals(SectionCode.D, record.getRequiredField("recommendedNavaidSectionCode"));
    assertFalse(record.getOptionalField("recommendedNavaidSubSectionCode").isPresent());
    assertFalse(record.getOptionalField("altitudeDescription").isPresent());
    assertEquals(3000.0d, record.getRequiredField("minAltitude1"));
    assertFalse(record.getOptionalField("minAltitude2").isPresent());
    assertEquals(18000.0d, record.getRequiredField("transitionAltitude"));
    assertFalse(record.getOptionalField("speedLimit").isPresent());
    assertFalse(record.getOptionalField("verticalAngle").isPresent());
    assertFalse(record.getOptionalField("centerFix").isPresent());
    assertFalse(record.getOptionalField("centerFixIcaoRegion").isPresent());
    assertFalse(record.getOptionalField("centerFixSectionCode").isPresent());
    assertFalse(record.getOptionalField("centerFixSubSectionCode").isPresent());
    assertEquals("D", record.getOptionalField("routeQualifier1").get());
    assertEquals("S", record.getOptionalField("routeQualifier2").get());
    assertEquals(Integer.valueOf(15493), record.getRequiredField("fileRecordNumber"));
    assertEquals("2004", record.getRequiredField("cycle"));
  }

  public static final String TF = "SUSAP KJFKK6FL22R  L      020MATTRK6EA0E  F    TF IJOCK6      0411006622130050PI  + 01900             -301          U NS   153382004";

  @Test
  public void testSpecMatchesTF_PF() {
    assertTrue(new TransitionSpec().matchesRecord(TF));
  }

  @Test
  public void testParseTF() {
    ArincRecord record = ArincVersion.V18.parse(TF);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.USA, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.P, record.getRequiredField("sectionCode"));
    assertEquals("KJFK", record.getRequiredField("airportIdentifier"));
    assertEquals("K6", record.getRequiredField("airportIcaoRegion"));
    assertEquals("F", record.getRequiredField("subSectionCode"));
    assertEquals("L22R", record.getRequiredField("sidStarIdentifier"));
    assertEquals("L", record.getRequiredField("routeType"));
    assertFalse(record.getOptionalField("transitionIdentifier").isPresent());
    assertEquals(Integer.valueOf(20), record.getRequiredField("sequenceNumber"));
    assertEquals("MATTR", record.getRequiredField("fixIdentifier"));
    assertEquals("K6", record.getRequiredField("fixIcaoRegion"));
    assertEquals(SectionCode.E, record.getRequiredField("fixSectionCode"));
    assertEquals("A", record.getRequiredField("fixSubSectionCode"));
    assertEquals("0", record.getRequiredField("continuationRecordNumber"));
    assertEquals("E  F", record.getRequiredField("waypointDescription"));
    assertFalse(record.getOptionalField("turnDirection").isPresent());
    assertFalse(record.getOptionalField("rnp").isPresent());
    assertEquals(PathTerm.TF, record.getRequiredField("pathTerm"));
    assertEquals(false, record.getRequiredField("turnDirectionValid"));
    assertEquals("IJOC", record.getRequiredField("recommendedNavaid"));
    assertEquals("K6", record.getRequiredField("recommendedNavaidIcaoRegion"));
    assertFalse(record.getOptionalField("arcRadius").isPresent());
    assertEquals(41.1d, record.getRequiredField("theta"));
    assertEquals(6.6d, record.getRequiredField("rho"));
    assertEquals(221.3d, record.getRequiredField("outboundMagneticCourse"));
    assertEquals("0050", record.getRequiredField("routeHoldDistanceTime"));
    assertEquals("+", record.getRequiredField("altitudeDescription"));
    assertEquals(1900.0d, record.getRequiredField("minAltitude1"));
    assertFalse(record.getOptionalField("minAltitude2").isPresent());
    assertFalse(record.getOptionalField("transitionAltitude").isPresent());
    assertFalse(record.getOptionalField("speedLimit").isPresent());
    assertEquals(-3.01, record.getRequiredField("verticalAngle"));
    assertFalse(record.getOptionalField("centerFix").isPresent());
    assertFalse(record.getOptionalField("centerFixIcaoRegion").isPresent());
    assertFalse(record.getOptionalField("centerFixSectionCode").isPresent());
    assertFalse(record.getOptionalField("centerFixSubSectionCode").isPresent());
    assertEquals("N", record.getOptionalField("routeQualifier1").get());
    assertEquals("S", record.getOptionalField("routeQualifier2").get());
    assertEquals(Integer.valueOf(15338), record.getRequiredField("fileRecordNumber"));
    assertEquals("2004", record.getRequiredField("cycle"));
  }

  public static final String RF = "SSAMP SBCXSBFR15-Y R      025CX211SBPC0E   L302RF                         0031    + 03158             -300CX210 SBPC  PS   939941713";

  @Test
  public void testSpecMatchesRF_PF() {
    assertTrue(new TransitionSpec().matchesRecord(RF));
  }

  @Test
  public void testParseRF() {
    ArincRecord record = ArincVersion.V18.parse(RF);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.SAM, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.P, record.getRequiredField("sectionCode"));
    assertEquals("SBCX", record.getRequiredField("airportIdentifier"));
    assertEquals("SB", record.getRequiredField("airportIcaoRegion"));
    assertEquals("F", record.getRequiredField("subSectionCode"));
    assertEquals("R15-Y", record.getRequiredField("sidStarIdentifier"));
    assertEquals("R", record.getRequiredField("routeType"));
    assertFalse(record.getOptionalField("transitionIdentifier").isPresent());
    assertEquals(Integer.valueOf(25), record.getRequiredField("sequenceNumber"));
    assertEquals("CX211", record.getRequiredField("fixIdentifier"));
    assertEquals("SB", record.getRequiredField("fixIcaoRegion"));
    assertEquals(SectionCode.P, record.getRequiredField("fixSectionCode"));
    assertEquals("C", record.getRequiredField("fixSubSectionCode"));
    assertEquals("0", record.getRequiredField("continuationRecordNumber"));
    assertEquals("E   ", record.getRequiredField("waypointDescription"));
    assertEquals(TurnDirection.L, record.getRequiredField("turnDirection"));
    assertEquals(0.3, record.getRequiredField("rnp"));
    assertEquals(PathTerm.RF, record.getRequiredField("pathTerm"));
    assertEquals(false, record.getRequiredField("turnDirectionValid"));
    assertFalse(record.getOptionalField("recommendedNavaid").isPresent());
    assertFalse(record.getOptionalField("recommendedNavaidIcaoRegion").isPresent());
    assertFalse(record.getOptionalField("arcRadius").isPresent());
    assertFalse(record.getOptionalField("theta").isPresent());
    assertFalse(record.getOptionalField("rho").isPresent());
    assertFalse(record.getOptionalField("outboundMagneticCourse").isPresent());
    assertEquals("0031", record.getRequiredField("routeHoldDistanceTime"));
    assertEquals("+", record.getRequiredField("altitudeDescription"));
    assertEquals(3158.0d, record.getRequiredField("minAltitude1"));
    assertFalse(record.getOptionalField("minAltitude2").isPresent());
    assertFalse(record.getOptionalField("transitionAltitude").isPresent());
    assertFalse(record.getOptionalField("speedLimit").isPresent());
    assertEquals(-3.0, record.getRequiredField("verticalAngle"));
    assertEquals("CX210", record.getRequiredField("centerFix"));
    assertEquals("SB", record.getRequiredField("centerFixIcaoRegion"));
    assertEquals(SectionCode.P, record.getRequiredField("centerFixSectionCode"));
    assertEquals("C", record.getRequiredField("centerFixSubSectionCode"));

    assertEquals(Integer.valueOf(93994), record.getRequiredField("fileRecordNumber"));
    assertEquals("1713", record.getRequiredField("cycle"));
  }

  public static final String CF = "SUSAP KACVK2DHOCUT51RW01  020HOCUTK2EA0EE  L   CF ACV K2      2500000025000050D                                            749081713";

  @Test
  public void testSpecMatchesCF_PD() {
    assertTrue(new TransitionSpec().matchesRecord(CF));
  }

  @Test
  public void testParseCF() {
    ArincRecord record = ArincVersion.V18.parse(CF);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.USA, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.P, record.getRequiredField("sectionCode"));
    assertEquals("KACV", record.getRequiredField("airportIdentifier"));
    assertEquals("K2", record.getRequiredField("airportIcaoRegion"));
    assertEquals("D", record.getRequiredField("subSectionCode"));
    assertEquals("HOCUT5", record.getRequiredField("sidStarIdentifier"));
    assertEquals("1", record.getRequiredField("routeType"));
    assertEquals("RW01", record.getRequiredField("transitionIdentifier"));
    assertEquals(Integer.valueOf(20), record.getRequiredField("sequenceNumber"));
    assertEquals("HOCUT", record.getRequiredField("fixIdentifier"));
    assertEquals("K2", record.getRequiredField("fixIcaoRegion"));
    assertEquals(SectionCode.E, record.getRequiredField("fixSectionCode"));
    assertEquals("A", record.getRequiredField("fixSubSectionCode"));
    assertEquals("0", record.getRequiredField("continuationRecordNumber"));
    assertEquals("EE  ", record.getRequiredField("waypointDescription"));
    assertEquals(TurnDirection.L, record.getRequiredField("turnDirection"));
    assertFalse(record.getOptionalField("rnp").isPresent());
    assertEquals(PathTerm.CF, record.getRequiredField("pathTerm"));
    assertEquals(false, record.getRequiredField("turnDirectionValid"));
    assertEquals("ACV", record.getRequiredField("recommendedNavaid"));
    assertEquals("K2", record.getRequiredField("recommendedNavaidIcaoRegion"));
    assertFalse(record.getOptionalField("arcRadius").isPresent());
    assertEquals(250.0d, record.getRequiredField("theta"));
    assertEquals(0.0d, record.getRequiredField("rho"));
    assertEquals(250.0d, record.getRequiredField("outboundMagneticCourse"));
    assertEquals("0050", record.getRequiredField("routeHoldDistanceTime"));
    assertFalse(record.getOptionalField("altitudeDescription").isPresent());
    assertFalse(record.getOptionalField("minAltitude1").isPresent());
    assertFalse(record.getOptionalField("minAltitude2").isPresent());
    assertFalse(record.getOptionalField("transitionAltitude").isPresent());
    assertFalse(record.getOptionalField("speedLimit").isPresent());
    assertFalse(record.getOptionalField("verticalAngle").isPresent());
    assertFalse(record.getOptionalField("centerFix").isPresent());
    assertFalse(record.getOptionalField("centerFixIcaoRegion").isPresent());
    assertFalse(record.getOptionalField("centerFixSectionCode").isPresent());
    assertFalse(record.getOptionalField("centerFixSubSectionCode").isPresent());

    assertEquals(Integer.valueOf(74908), record.getRequiredField("fileRecordNumber"));
    assertEquals("1713", record.getRequiredField("cycle"));
  }
}
