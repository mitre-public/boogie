package org.mitre.tdp.boogie.arinc.v19;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegValidator;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestProcedureLegSpec {

  private static final String IF = "SUSAP KJFKK6FH22LZ ADPK   010DPK  K6D 0V  A    IF                                             18000                 A FS   204071113";

  @Test
  void testSpecMatchesIF() {
    assertTrue(new ProcedureLegSpec().matchesRecord(IF));
  }

  @Test
  void testValidatorPassesIF() {
    assertTrue(new ProcedureLegValidator().test(ArincVersion.V19.parser().apply(IF).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseIF() {
    ArincRecord record = ArincVersion.V19.parser().apply(IF).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("KJFK", record.requiredField("airportIdentifier")),
        () -> assertEquals("K6", record.requiredField("airportIcaoRegion")),
        () -> assertEquals("F", record.requiredField("subSectionCode")),
        () -> assertEquals("H22LZ", record.requiredField("sidStarIdentifier")),
        () -> assertEquals("A", record.requiredField("routeType")),
        () -> assertTrue(record.optionalField("transitionIdentifier").isPresent()),
        () -> assertEquals(Integer.valueOf(10), record.requiredField("sequenceNumber")),
        () -> assertEquals("DPK", record.requiredField("fixIdentifier")),
        () -> assertEquals("K6", record.requiredField("fixIcaoRegion")),
        () -> assertEquals(SectionCode.D, record.requiredField("fixSectionCode")),
        () -> assertFalse(record.optionalField("fixSubSectionCode").isPresent()),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals("V  A", record.requiredField("waypointDescription")),
        () -> assertFalse(record.optionalField("turnDirection").isPresent()),
        () -> assertFalse(record.optionalField("rnp").isPresent()),
        () -> assertEquals(PathTerminator.IF, record.requiredField("pathTerm")),
        () -> assertEquals(false, record.requiredField("turnDirectionValid")),
        () -> assertFalse(record.optionalField("recommendedNavaidIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("recommendedNavaidIcaoRegion").isPresent()),
        () -> assertFalse(record.optionalField("arcRadius").isPresent()),
        () -> assertFalse(record.optionalField("theta").isPresent()),
        () -> assertFalse(record.optionalField("rho").isPresent()),
        () -> assertFalse(record.optionalField("outboundMagneticCourse").isPresent()),
        () -> assertFalse(record.optionalField("routeHoldDistanceTime").isPresent()),
        () -> assertFalse(record.optionalField("recommendedNavaidSectionCode").isPresent()),
        () -> assertFalse(record.optionalField("recommendedNavaidSubSectionCode").isPresent()),
        () -> assertFalse(record.optionalField("altitudeDescription").isPresent()),
        () -> assertFalse(record.optionalField("minAltitude1").isPresent()),
        () -> assertFalse(record.optionalField("minAltitude2").isPresent()),
        () -> assertEquals(18000.0d, record.requiredField("transitionAltitude")),
        () -> assertFalse(record.optionalField("speedLimit").isPresent()),
        () -> assertFalse(record.optionalField("verticalAngle").isPresent()),
        () -> assertFalse(record.optionalField("centerFixIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("centerFixIcaoRegion").isPresent()),
        () -> assertFalse(record.optionalField("centerFixSectionCode").isPresent()),
        () -> assertFalse(record.optionalField("centerFixSubSectionCode").isPresent()),
        () -> assertEquals("F", record.optionalField("routeTypeQualifier1").get()),
        () -> assertEquals("S", record.optionalField("routeTypeQualifier2").get()),
        () -> assertEquals(Integer.valueOf(20407), record.requiredField("fileRecordNumber")),
        () -> assertEquals("1113", record.requiredField("lastUpdateCycle"))
    );
  }
}
