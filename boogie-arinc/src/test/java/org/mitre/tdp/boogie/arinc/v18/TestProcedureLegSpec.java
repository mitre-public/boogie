package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.SubSectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.TurnDirection;

public class TestProcedureLegSpec {

  private static final ArincRecordParser V18 = ArincRecordParser.standard(new ProcedureLegSpec());

  private static final String IF = "SUSAP KJFKK6FV13R  V      010ASALTK6EA1E  D    IF CRI K6      22480060        D     03000     18000                 3 DS   154932004";
  private static final String ENG_OUT = "SPACP VVTHVVDEO03  0RW03  010         1        VA                     0290        + 01600     09022                        720981902\n";

  @Test
  void testEngineOutParse() {
    ArincRecord leg = V18.parse(ENG_OUT).orElseThrow();
    assertAll(
        () -> assertEquals("EO03", leg.requiredField("sidStarIdentifier"), "get it E .. 0 ... Runway Name"),
        () -> assertEquals("D", leg.requiredField("subSectionCode"), "its a sid"),
        () -> assertEquals("0", leg.requiredField("routeType"), "its an engine out sid")
    );
  }
  @Test
  void testSpecMatchesIF() {
    assertTrue(new ProcedureLegSpec().matchesRecord(IF));
  }

  @Test
  void testValidatorPassesIF() {
    assertTrue(new ProcedureLegValidator().test(V18.parse(IF).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseIF() {
    ArincRecord record = V18.parse(IF).orElseThrow(AssertionError::new);

    assertAll(
        () -> Assertions.assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> Assertions.assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> Assertions.assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("KJFK", record.requiredField("airportIdentifier")),
        () -> assertEquals("K6", record.requiredField("airportIcaoRegion")),
        () -> assertEquals("F", record.requiredField("subSectionCode")),
        () -> assertEquals("V13R", record.requiredField("sidStarIdentifier")),
        () -> assertEquals("V", record.requiredField("routeType")),
        () -> assertFalse(record.optionalField("transitionIdentifier").isPresent()),
        () -> assertEquals(Integer.valueOf(10), record.requiredField("sequenceNumber")),
        () -> assertEquals("ASALT", record.requiredField("fixIdentifier")),
        () -> assertEquals("K6", record.requiredField("fixIcaoRegion")),
        () -> assertEquals(SectionCode.E, record.requiredField("fixSectionCode")),
        () -> assertEquals("A", record.requiredField("fixSubSectionCode")),
        () -> assertEquals("1", record.requiredField("continuationRecordNumber")),
        () -> assertEquals("E  D", record.requiredField("waypointDescription")),
        () -> assertFalse(record.optionalField("turnDirection").isPresent()),
        () -> assertFalse(record.optionalField("rnp").isPresent()),
        () -> assertEquals(PathTerminator.IF, record.requiredField("pathTerm")),
        () -> assertEquals(false, record.requiredField("turnDirectionValid")),
        () -> assertEquals("CRI", record.requiredField("recommendedNavaidIdentifier")),
        () -> assertEquals("K6", record.requiredField("recommendedNavaidIcaoRegion")),
        () -> assertFalse(record.optionalField("arcRadius").isPresent()),
        () -> assertEquals(224.8d, record.requiredField("theta")),
        () -> assertEquals(6.0d, record.requiredField("rho")),
        () -> assertFalse(record.optionalField("outboundMagneticCourse").isPresent()),
        () -> assertFalse(record.optionalField("routeHoldDistanceTime").isPresent()),
        () -> assertEquals(SectionCode.D, record.requiredField("recommendedNavaidSectionCode")),
        () -> assertFalse(record.optionalField("recommendedNavaidSubSectionCode").isPresent()),
        () -> assertEquals(Optional.of("@"), record.optionalField("altitudeDescription")),
        () -> assertEquals(3000.0d, record.requiredField("minAltitude1")),
        () -> assertFalse(record.optionalField("minAltitude2").isPresent()),
        () -> assertEquals(18000.0d, record.requiredField("transitionAltitude")),
        () -> assertFalse(record.optionalField("speedLimit").isPresent()),
        () -> assertFalse(record.optionalField("verticalAngle").isPresent()),
        () -> assertFalse(record.optionalField("centerFixIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("centerFixIcaoRegion").isPresent()),
        () -> assertFalse(record.optionalField("centerFixSectionCode").isPresent()),
        () -> assertFalse(record.optionalField("centerFixSubSectionCode").isPresent()),
        () -> assertEquals("D", record.optionalField("routeTypeQualifier1").get()),
        () -> assertEquals("S", record.optionalField("routeTypeQualifier2").get()),
        () -> assertEquals(Integer.valueOf(15493), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2004", record.requiredField("lastUpdateCycle"))
    );
  }

  private static final String TF = "SUSAP KJFKK6FL22R  L      020MATTRK6EA0E  F    TF IJOCK6      0411006622130050PI  + 01900             -301          U NS   153382004";

  @Test
  void testSpecMatchesTF_PF() {
    assertTrue(new ProcedureLegSpec().matchesRecord(TF));
  }

  @Test
  void testValidatorPassesTF() {
    assertTrue(new ProcedureLegValidator().test(V18.parse(TF).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseTF() {
    ArincRecord record = V18.parse(TF).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("KJFK", record.requiredField("airportIdentifier")),
        () -> assertEquals("K6", record.requiredField("airportIcaoRegion")),
        () -> assertEquals("F", record.requiredField("subSectionCode")),
        () -> assertEquals("L22R", record.requiredField("sidStarIdentifier")),
        () -> assertEquals("L", record.requiredField("routeType")),
        () -> assertFalse(record.optionalField("transitionIdentifier").isPresent()),
        () -> assertEquals(Integer.valueOf(20), record.requiredField("sequenceNumber")),
        () -> assertEquals("MATTR", record.requiredField("fixIdentifier")),
        () -> assertEquals("K6", record.requiredField("fixIcaoRegion")),
        () -> assertEquals(SectionCode.E, record.requiredField("fixSectionCode")),
        () -> assertEquals("A", record.requiredField("fixSubSectionCode")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals("E  F", record.requiredField("waypointDescription")),
        () -> assertFalse(record.optionalField("turnDirection").isPresent()),
        () -> assertFalse(record.optionalField("rnp").isPresent()),
        () -> assertEquals(PathTerminator.TF, record.requiredField("pathTerm")),
        () -> assertEquals(false, record.requiredField("turnDirectionValid")),
        () -> assertEquals("IJOC", record.requiredField("recommendedNavaidIdentifier")),
        () -> assertEquals("K6", record.requiredField("recommendedNavaidIcaoRegion")),
        () -> assertFalse(record.optionalField("arcRadius").isPresent()),
        () -> assertEquals(41.1d, record.requiredField("theta")),
        () -> assertEquals(6.6d, record.requiredField("rho")),
        () -> assertEquals(221.3d, record.requiredField("outboundMagneticCourse")),
        () -> assertEquals("0050", record.requiredField("routeHoldDistanceTime")),
        () -> assertEquals("+", record.requiredField("altitudeDescription")),
        () -> assertEquals(1900.0d, record.requiredField("minAltitude1")),
        () -> assertFalse(record.optionalField("minAltitude2").isPresent()),
        () -> assertFalse(record.optionalField("transitionAltitude").isPresent()),
        () -> assertFalse(record.optionalField("speedLimit").isPresent()),
        () -> assertEquals(-3.01, record.requiredField("verticalAngle")),
        () -> assertFalse(record.optionalField("centerFixIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("centerFixIcaoRegion").isPresent()),
        () -> assertFalse(record.optionalField("centerFixSectionCode").isPresent()),
        () -> assertFalse(record.optionalField("centerFixSubSectionCode").isPresent()),
        () -> assertEquals("N", record.optionalField("routeTypeQualifier1").get()),
        () -> assertEquals("S", record.optionalField("routeTypeQualifier2").get()),
        () -> assertEquals(Integer.valueOf(15338), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2004", record.requiredField("lastUpdateCycle"))
    );
  }

  private static final String RF = "SSAMP SBCXSBFR15-Y R      025CX211SBPC0E   L302RF                         0031    + 03158             -300CX210 SBPC  PS   939941713";

  @Test
  void testSpecMatchesRF_PF() {
    assertTrue(new ProcedureLegSpec().matchesRecord(RF));
  }

  @Test
  void testValidatorPassesRF() {
    assertTrue(new ProcedureLegValidator().test(V18.parse(RF).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseRF() {
    ArincRecord record = V18.parse(RF).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.SAM, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("SBCX", record.requiredField("airportIdentifier")),
        () -> assertEquals("SB", record.requiredField("airportIcaoRegion")),
        () -> assertEquals("F", record.requiredField("subSectionCode")),
        () -> assertEquals("R15-Y", record.requiredField("sidStarIdentifier")),
        () -> assertEquals("R", record.requiredField("routeType")),
        () -> assertFalse(record.optionalField("transitionIdentifier").isPresent()),
        () -> assertEquals(Integer.valueOf(25), record.requiredField("sequenceNumber")),
        () -> assertEquals("CX211", record.requiredField("fixIdentifier")),
        () -> assertEquals("SB", record.requiredField("fixIcaoRegion")),
        () -> assertEquals(SectionCode.P, record.requiredField("fixSectionCode")),
        () -> assertEquals("C", record.requiredField("fixSubSectionCode")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals("E   ", record.requiredField("waypointDescription")),
        () -> Assertions.assertEquals(TurnDirection.L, record.requiredField("turnDirection")),
        () -> assertEquals(0.3, record.requiredField("rnp")),
        () -> assertEquals(PathTerminator.RF, record.requiredField("pathTerm")),
        () -> assertEquals(false, record.requiredField("turnDirectionValid")),
        () -> assertFalse(record.optionalField("recommendedNavaidIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("recommendedNavaidIcaoRegion").isPresent()),
        () -> assertFalse(record.optionalField("arcRadius").isPresent()),
        () -> assertFalse(record.optionalField("theta").isPresent()),
        () -> assertFalse(record.optionalField("rho").isPresent()),
        () -> assertFalse(record.optionalField("outboundMagneticCourse").isPresent()),
        () -> assertEquals("0031", record.requiredField("routeHoldDistanceTime")),
        () -> assertEquals("+", record.requiredField("altitudeDescription")),
        () -> assertEquals(3158.0d, record.requiredField("minAltitude1")),
        () -> assertFalse(record.optionalField("minAltitude2").isPresent()),
        () -> assertFalse(record.optionalField("transitionAltitude").isPresent()),
        () -> assertFalse(record.optionalField("speedLimit").isPresent()),
        () -> assertEquals(-3.0, record.requiredField("verticalAngle")),
        () -> assertEquals("CX210", record.requiredField("centerFixIdentifier")),
        () -> assertEquals("SB", record.requiredField("centerFixIcaoRegion")),
        () -> assertEquals(SectionCode.P, record.requiredField("centerFixSectionCode")),
        () -> assertEquals("C", record.requiredField("centerFixSubSectionCode")),
        () -> assertEquals(Integer.valueOf(93994), record.requiredField("fileRecordNumber")),
        () -> assertEquals("1713", record.requiredField("lastUpdateCycle"))
    );
  }

  private static final String CF = "SUSAP KACVK2DHOCUT51RW01  020HOCUTK2EA0EE  L   CF ACV K2      2500000025000050D                                            749081713";

  @Test
  void testSpecMatchesCF_PD() {
    assertTrue(new ProcedureLegSpec().matchesRecord(CF));
  }

  @Test
  void testValidatorPassesCF() {
    assertTrue(new ProcedureLegValidator().test(V18.parse(CF).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseCF() {
    ArincRecord record = V18.parse(CF).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("KACV", record.requiredField("airportIdentifier")),
        () -> assertEquals("K2", record.requiredField("airportIcaoRegion")),
        () -> assertEquals("D", record.requiredField("subSectionCode")),
        () -> assertEquals("HOCUT5", record.requiredField("sidStarIdentifier")),
        () -> assertEquals("1", record.requiredField("routeType")),
        () -> assertEquals("RW01", record.requiredField("transitionIdentifier")),
        () -> assertEquals(Integer.valueOf(20), record.requiredField("sequenceNumber")),
        () -> assertEquals("HOCUT", record.requiredField("fixIdentifier")),
        () -> assertEquals("K2", record.requiredField("fixIcaoRegion")),
        () -> assertEquals(SectionCode.E, record.requiredField("fixSectionCode")),
        () -> assertEquals("A", record.requiredField("fixSubSectionCode")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals("EE  ", record.requiredField("waypointDescription")),
        () -> assertEquals(TurnDirection.L, record.requiredField("turnDirection")),
        () -> assertFalse(record.optionalField("rnp").isPresent()),
        () -> assertEquals(PathTerminator.CF, record.requiredField("pathTerm")),
        () -> assertEquals(false, record.requiredField("turnDirectionValid")),
        () -> assertEquals("ACV", record.requiredField("recommendedNavaidIdentifier")),
        () -> assertEquals("K2", record.requiredField("recommendedNavaidIcaoRegion")),
        () -> assertFalse(record.optionalField("arcRadius").isPresent()),
        () -> assertEquals(250.0d, record.requiredField("theta")),
        () -> assertEquals(0.0d, record.requiredField("rho")),
        () -> assertEquals(250.0d, record.requiredField("outboundMagneticCourse")),
        () -> assertEquals("0050", record.requiredField("routeHoldDistanceTime")),
        () -> assertEquals(Optional.of("@"), record.optionalField("altitudeDescription")),
        () -> assertFalse(record.optionalField("minAltitude1").isPresent()),
        () -> assertFalse(record.optionalField("minAltitude2").isPresent()),
        () -> assertFalse(record.optionalField("transitionAltitude").isPresent()),
        () -> assertFalse(record.optionalField("speedLimit").isPresent()),
        () -> assertFalse(record.optionalField("verticalAngle").isPresent()),
        () -> assertFalse(record.optionalField("centerFixIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("centerFixIcaoRegion").isPresent()),
        () -> assertFalse(record.optionalField("centerFixSectionCode").isPresent()),
        () -> assertFalse(record.optionalField("centerFixSubSectionCode").isPresent()),
        () -> assertEquals(Integer.valueOf(74908), record.requiredField("fileRecordNumber")),
        () -> assertEquals("1713", record.requiredField("lastUpdateCycle"))
    );
  }

  private static final String CR = "SUSAP KROAK6FVDM-A D      040         0  M L   CR ODR K6      1640    1640    D   + 04000                           0  C   904921909";

  @Test
  void testSpecMatchesCR() {
    assertTrue(new ProcedureLegSpec().matchesRecord(CR));
  }

  @Test
  void testValidatorPassesCR() {
    assertTrue(new ProcedureLegValidator().test(V18.parse(CR).orElseThrow(AssertionError::new)));
  }

  private static final String PI = "SCANP PABEPAFI19RZ ABET   030KAYSEPAPC0EE AR   PI IBETPA      0125006432750100PI  + 01800                           0 DS   050461707";

  @Test
  void testSpecMatchesPI() {
    assertTrue(new ProcedureLegSpec().matchesRecord(PI));
  }

  @Test
  void testValidatorPassesPI() {
    assertTrue(new ProcedureLegValidator().test(V18.parse(PI).orElseThrow(AssertionError::new)));
  }

  private static final String PI2 = "SUSAP KDNLK7FN11   AEMR   030GARFIK7PC0EE AL   PI EMR K7              32860100DB  + 02100                           0  S   656342010";

  @Test
  void testSpecMatchesPI2() {
    assertTrue(new ProcedureLegSpec().matchesRecord(PI2));
  }

  @Test
  void testValidatorPassesPI2() {
    ArincRecord arincRecord = V18.parse(PI2).orElseThrow(AssertionError::new);
    assertFalse(new ProcedureLegValidator().test(arincRecord));
  }

  private static final String AF = "SLAMP MKJSMKEOMAXI51ENARI 050SIA13MKPC0EE  L   AF SIA MK      252301303430    D                                            533482014";

  @Test
  void testSpecMatchesAF() {
    assertTrue(new ProcedureLegSpec().matchesRecord(AF));
  }

  @Test
  void testValidatorPassesAF() {
    assertTrue(new ProcedureLegValidator().test(V18.parse(AF).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseAF() {
    ArincRecord record = V18.parse(AF).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType"), "recordType"),
        () -> assertEquals(CustomerAreaCode.LAM, record.requiredField("customerAreaCode"), "customerAreaCode"),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode"), "sectionCode"),
        () -> assertEquals("MKJS", record.requiredField("airportIdentifier"), "airportIdentifier"),
        () -> assertEquals("MK", record.requiredField("airportIcaoRegion"), "airportIcaoRegion"),
        () -> assertEquals("E", record.requiredField("subSectionCode"), "subSectionCode"),
        () -> assertEquals("OMAXI5", record.requiredField("sidStarIdentifier"), "sidStarIdentifier"),
        () -> assertEquals("1", record.requiredField("routeType"), "routeType"),
        () -> assertEquals("ENARI", record.requiredField("transitionIdentifier"), "transitionIdentifier"),
        () -> assertEquals(Integer.valueOf(50), record.requiredField("sequenceNumber"), "sequenceNumber"),
        () -> assertEquals("SIA13", record.requiredField("fixIdentifier"), "fixIdentifier"),
        () -> assertEquals("MK", record.requiredField("fixIcaoRegion"), "fixIcaoRegion"),
        () -> assertEquals(SectionCode.P, record.requiredField("fixSectionCode"), "fixSectionCode"),
        () -> assertEquals("C", record.requiredField("fixSubSectionCode"), "fixSubSectionCode"),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber"), "continuationRecordNumber"),
        () -> assertEquals("EE  ", record.requiredField("waypointDescription"), "waypointDescription"),
        () -> assertEquals(TurnDirection.L, record.requiredField("turnDirection"), "turnDirection"),
        () -> assertFalse(record.optionalField("rnp").isPresent()),
        () -> assertEquals(PathTerminator.AF, record.requiredField("pathTerm"), "pathTerm"),
        () -> assertEquals(false, record.requiredField("turnDirectionValid"), "turnDirectionValid"),
        () -> assertEquals("SIA", record.requiredField("recommendedNavaidIdentifier"), "recommendedNavaidIdentifier"),
        () -> assertEquals("MK", record.requiredField("recommendedNavaidIcaoRegion"), "recommendedNavaidIcaoRegion"),
        () -> assertFalse(record.optionalField("arcRadius").isPresent()),
        () -> assertEquals(252.3d, record.requiredField("theta"), "theta"),
        () -> assertEquals(13.0d, record.requiredField("rho"), "rho"),
        () -> assertEquals(343.0d, record.requiredField("outboundMagneticCourse"), "outboundMagneticCourse"),
        () -> assertFalse(record.optionalField("routeHoldDistanceTime").isPresent(), "routeHoldDistanceTime"),
        () -> assertEquals(Optional.of("@"), record.optionalField("altitudeDescription")),
        () -> assertFalse(record.optionalField("minAltitude1").isPresent(), "minAltitude1"),
        () -> assertFalse(record.optionalField("minAltitude2").isPresent(), "minAltitude2"),
        () -> assertFalse(record.optionalField("transitionAltitude").isPresent(), "transitionAltitude"),
        () -> assertFalse(record.optionalField("speedLimit").isPresent(), "speedLimit"),
        () -> assertFalse(record.optionalField("verticalAngle").isPresent(), "verticalAngle"),
        () -> assertFalse(record.optionalField("centerFixIdentifier").isPresent(), "centerFixIdentifier"),
        () -> assertFalse(record.optionalField("centerFixIcaoRegion").isPresent(), "centerFixIcaoRegion"),
        () -> assertFalse(record.optionalField("centerFixSectionCode").isPresent(), "centerFixSectionCode"),
        () -> assertFalse(record.optionalField("centerFixSubSectionCode").isPresent(), "centerFixSubSectionCode"),
        () -> assertEquals(Integer.valueOf(53348), record.requiredField("fileRecordNumber"), "fileRecordNumber"),
        () -> assertEquals("2014", record.requiredField("lastUpdateCycle"), "lastUpdateCycle")
    );
  }
}
