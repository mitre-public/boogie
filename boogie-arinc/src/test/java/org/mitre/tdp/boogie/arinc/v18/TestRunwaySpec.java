package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.RunwaySpec;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.ArincRecord;

public class TestRunwaySpec {

  public static final String runway1 = "SUSAP KJFKK6GRW04L   1120790440 N40372318W073470505+0000          00012046057200 IHIQ10000           CONC     090RBWT      155192003";

  @Test
  void testSpecMatchesRunwayRecord1() {
    assertTrue(new RunwaySpec().matchesRecord(runway1));
  }

  @Test
  void testValidatorPasses_Runway1(){
    assertTrue(new RunwayValidator().test(ArincVersion.V18.parser().apply(runway1).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseRunway1() {
    ArincRecord record = ArincVersion.V18.parser().apply(runway1).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("KJFK", record.requiredField("airportIdentifier")),
        () -> assertEquals("K6", record.requiredField("airportIcaoRegion")),
        () -> assertEquals("G", record.requiredField("subSectionCode")),
        () -> assertEquals("RW04L", record.requiredField("runwayIdentifier")),
        () -> assertEquals("1", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(Integer.valueOf(12079), record.requiredField("runwayLength")),
        () -> assertEquals(44.0d, record.requiredField("runwayMagneticBearing")),
        () -> assertEquals(40.623105555555554d, record.requiredField("latitude")),
        () -> assertEquals(-73.78473611111112d, record.requiredField("longitude")),
        () -> assertEquals(0.0d, record.requiredField("runwayGradient")),
        () -> assertEquals(Integer.valueOf(12), record.requiredField("landingThresholdElevation")),
        () -> assertEquals(Integer.valueOf(460), record.requiredField("thresholdDisplacementDistance")),
        () -> assertEquals(Integer.valueOf(57), record.requiredField("thresholdCrossingHeight")),
        () -> assertEquals(Integer.valueOf(200), record.requiredField("runwayWidth")),
        () -> assertEquals("IHIQ", record.requiredField("ilsMlsGlsIdentifier")),
        () -> assertEquals("1", record.requiredField("ilsMlsGlsCategory")),
        () -> assertEquals(Integer.valueOf(0), record.requiredField("stopway")),
        () -> assertFalse(record.optionalField("secondaryIlsMlsGlsIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("secondaryIlsMlsGlsCategory").isPresent()),
        () -> assertEquals("CONC     090RBWT", record.requiredField("runwayDescription")),
        () -> assertEquals(Integer.valueOf(15519), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2003", record.requiredField("lastUpdateCycle"))
    );
  }

  public static final String runway2 = "SUSAP KJFKK6GRW22L   1084002240 N40384285W073451751+0000          00012000053200 IIWY30000           ASPH     090FBWT      155232003";

  @Test
  void testSpecMatchesRunway2() {
    assertTrue(new RunwaySpec().matchesRecord(runway2));
  }

  @Test
  void testValidatorPasses_Runway2(){
    assertTrue(new RunwayValidator().test(ArincVersion.V18.parser().apply(runway2).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseRunway2() {
    ArincRecord record = ArincVersion.V18.parser().apply(runway2).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("KJFK", record.requiredField("airportIdentifier")),
        () -> assertEquals("K6", record.requiredField("airportIcaoRegion")),
        () -> assertEquals("G", record.requiredField("subSectionCode")),
        () -> assertEquals("RW22L", record.requiredField("runwayIdentifier")),
        () -> assertEquals("1", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(Integer.valueOf(8400), record.requiredField("runwayLength")),
        () -> assertEquals(224.0d, record.requiredField("runwayMagneticBearing")),
        () -> assertEquals(40.64523611111111d, record.requiredField("latitude")),
        () -> assertEquals(-73.75486388888889d, record.requiredField("longitude")),
        () -> assertEquals(0.0d, record.requiredField("runwayGradient")),
        () -> assertEquals(Integer.valueOf(12), record.requiredField("landingThresholdElevation")),
        () -> assertEquals(Integer.valueOf(0), record.requiredField("thresholdDisplacementDistance")),
        () -> assertEquals(Integer.valueOf(53), record.requiredField("thresholdCrossingHeight")),
        () -> assertEquals(Integer.valueOf(200), record.requiredField("runwayWidth")),
        () -> assertEquals("IIWY", record.requiredField("ilsMlsGlsIdentifier")),
        () -> assertEquals("3", record.requiredField("ilsMlsGlsCategory")),
        () -> assertEquals(Integer.valueOf(0), record.requiredField("stopway")),
        () -> assertFalse(record.optionalField("secondaryIlsMlsGlsIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("secondaryIlsMlsGlsCategory").isPresent()),
        () -> assertEquals("ASPH     090FBWT", record.requiredField("runwayDescription")),
        () -> assertEquals(Integer.valueOf(15523), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2003", record.requiredField("lastUpdateCycle"))
    );
  }

  public static final String runway3 = "SUSAP KBWIK6GRW28    0105032852 N39102198W076391849+0300          00130070055000      0000                                 179421907";

  @Test
  void testSpecMatchesRunway3() {
    assertTrue(new RunwaySpec().matchesRecord(runway3));
  }

  @Test
  void testValidatorPasses_Runway3(){
    assertTrue(new RunwayValidator().test(ArincVersion.V18.parser().apply(runway3).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseRunway3() {
    ArincRecord record = ArincVersion.V18.parser().apply(runway3).orElseThrow(AssertionError::new);
    assertEquals(0.3, record.requiredField("runwayGradient"));
  }

  public static final String runway4 = "SCANP PAAQPAGRW16S   0015601640 N61355318W149051694               00239000050060D                                          049571812";

  @Test
  void testSpecMatchesRunway4() {
    assertTrue(new RunwaySpec().matchesRecord(runway4));
  }

  @Test
  void testValidatorPasses_Runway4(){
    assertTrue(new RunwayValidator().test(ArincVersion.V18.parser().apply(runway4).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseRunway4() {
    ArincRecord record = ArincVersion.V18.parser().apply(runway4).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.CAN, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("PAAQ", record.requiredField("airportIdentifier")),
        () -> assertEquals("PA", record.requiredField("airportIcaoRegion")),
        () -> assertEquals("G", record.requiredField("subSectionCode")),
        () -> assertEquals("RW16S", record.requiredField("runwayIdentifier")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(Integer.valueOf(1560), record.requiredField("runwayLength")),
        () -> assertEquals(164.0d, record.requiredField("runwayMagneticBearing")),
        () -> assertEquals(61.598105555555556, record.requiredField("latitude")),
        () -> assertEquals(-149.0880388888889, record.requiredField("longitude")),
        () -> assertEquals(Optional.empty(), record.optionalField("runwayGradient")),
        () -> assertEquals(Integer.valueOf(239), record.requiredField("landingThresholdElevation")),
        () -> assertEquals(Integer.valueOf(0), record.requiredField("thresholdDisplacementDistance")),
        () -> assertEquals(Integer.valueOf(50), record.requiredField("thresholdCrossingHeight")),
        () -> assertEquals(Integer.valueOf(60), record.requiredField("runwayWidth")),
        () -> assertEquals(Optional.empty(), record.optionalField("ilsMlsGlsIdentifier")),
        () -> assertEquals(Optional.empty(), record.optionalField("ilsMlsGlsCategory")),
        () -> assertEquals(Optional.empty(), record.optionalField("stopway")),
        () -> assertFalse(record.optionalField("secondaryIlsMlsGlsIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("secondaryIlsMlsGlsCategory").isPresent()),
        () -> assertEquals(Optional.empty(), record.optionalField("runwayDescription")),
        () -> assertEquals(Integer.valueOf(4957), record.requiredField("fileRecordNumber")),
        () -> assertEquals("1812", record.requiredField("lastUpdateCycle"))
    );
  }
}
