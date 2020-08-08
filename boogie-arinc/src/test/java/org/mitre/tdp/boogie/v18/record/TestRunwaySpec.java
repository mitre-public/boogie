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
import org.mitre.tdp.boogie.v18.spec.record.RunwaySpec;

public class TestRunwaySpec {

  public static final String runway1 = "SUSAP KJFKK6GRW04L   1120790440 N40372318W073470505+0000          00012046057200 IHIQ10000           CONC     090RBWT      155192003";

  @Test
  public void testSpecMatchesRunwayRecord1() {
    assertTrue(new RunwaySpec().matchesRecord(runway1));
  }

  @Test
  public void testParseRunway1() {
    ArincRecord record = ArincVersion.V18.parse(runway1);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.USA, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.P, record.getRequiredField("sectionCode"));
    assertEquals("KJFK", record.getRequiredField("airportIdentifier"));
    assertEquals("K6", record.getRequiredField("airportIcaoRegion"));
    assertEquals("G", record.getRequiredField("subSectionCode"));
    assertEquals("RW04L", record.getRequiredField("runwayIdentifier"));
    assertEquals("1", record.getRequiredField("continuationRecordNumber"));
    assertEquals(Integer.valueOf(12079), record.getRequiredField("runwayLength"));
    assertEquals(44.0d, record.getRequiredField("runwayMagneticBearing"));
    assertEquals(40.623105555555554d, record.getRequiredField("latitude"));
    assertEquals(-73.78473611111112d, record.getRequiredField("longitude"));
    assertEquals(0.0d, record.getRequiredField("runwayGradient"));
    assertEquals(Integer.valueOf(12), record.getRequiredField("landingThresholdElevation"));
    assertEquals(Integer.valueOf(460), record.getRequiredField("thresholdDisplacementDistance"));
    assertEquals(Integer.valueOf(57), record.getRequiredField("thresholdCrossingHeight"));
    assertEquals(Integer.valueOf(200), record.getRequiredField("runwayWidth"));
    assertEquals("IHIQ", record.getRequiredField("ilsMlsGlsIdentifier"));
    assertEquals("1", record.getRequiredField("cat"));
    assertEquals(Integer.valueOf(0), record.getRequiredField("stopway"));
    assertFalse(record.getOptionalField("secondaryIlsMlsGlsIdentifier").isPresent());
    assertFalse(record.getOptionalField("secondaryCat").isPresent());
    assertEquals("CONC     090RBWT", record.getRequiredField("runwayDescription"));
    assertEquals(Integer.valueOf(15519), record.getRequiredField("fileRecordNumber"));
    assertEquals("2003", record.getRequiredField("cycle"));
  }

  public static final String runway2 = "SUSAP KJFKK6GRW22L   1084002240 N40384285W073451751+0000          00012000053200 IIWY30000           ASPH     090FBWT      155232003";

  @Test
  public void testSpecMatchesRunway2() {
    assertTrue(new RunwaySpec().matchesRecord(runway2));
  }

  @Test
  public void testParseRunway2() {
    ArincRecord record = ArincVersion.V18.parse(runway2);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.USA, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.P, record.getRequiredField("sectionCode"));
    assertEquals("KJFK", record.getRequiredField("airportIdentifier"));
    assertEquals("K6", record.getRequiredField("airportIcaoRegion"));
    assertEquals("G", record.getRequiredField("subSectionCode"));
    assertEquals("RW22L", record.getRequiredField("runwayIdentifier"));
    assertEquals("1", record.getRequiredField("continuationRecordNumber"));
    assertEquals(Integer.valueOf(8400), record.getRequiredField("runwayLength"));
    assertEquals(224.0d, record.getRequiredField("runwayMagneticBearing"));
    assertEquals(40.64523611111111d, record.getRequiredField("latitude"));
    assertEquals(-73.75486388888889d, record.getRequiredField("longitude"));
    assertEquals(0.0d, record.getRequiredField("runwayGradient"));
    assertEquals(Integer.valueOf(12), record.getRequiredField("landingThresholdElevation"));
    assertEquals(Integer.valueOf(0), record.getRequiredField("thresholdDisplacementDistance"));
    assertEquals(Integer.valueOf(53), record.getRequiredField("thresholdCrossingHeight"));
    assertEquals(Integer.valueOf(200), record.getRequiredField("runwayWidth"));
    assertEquals("IIWY", record.getRequiredField("ilsMlsGlsIdentifier"));
    assertEquals("3", record.getRequiredField("cat"));
    assertEquals(Integer.valueOf(0), record.getRequiredField("stopway"));
    assertFalse(record.getOptionalField("secondaryIlsMlsGlsIdentifier").isPresent());
    assertFalse(record.getOptionalField("secondaryCat").isPresent());
    assertEquals("ASPH     090FBWT", record.getRequiredField("runwayDescription"));
    assertEquals(Integer.valueOf(15523), record.getRequiredField("fileRecordNumber"));
    assertEquals("2003", record.getRequiredField("cycle"));
  }

  public static final String runway3 = "SUSAP KBWIK6GRW28    0105032852 N39102198W076391849+0300          00130070055000      0000                                 179421907";

  @Test
  public void testSpecMatchesRunway3() {
    assertTrue(new RunwaySpec().matchesRecord(runway3));
  }

  @Test
  public void testParseRunway3() {
    ArincRecord record = ArincVersion.V18.parse(runway3);
    assertNotNull(record);

    assertEquals(0.3, record.getRequiredField("runwayGradient"));
  }
}
