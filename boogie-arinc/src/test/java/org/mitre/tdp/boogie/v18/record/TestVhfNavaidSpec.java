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
import org.mitre.tdp.boogie.v18.spec.record.VhfNavaidSpec;

public class TestVhfNavaidSpec {

  public static final String navaid1 = "SCAND        GAL   PA011480VDHW N64441727W156463774    N64441730W156463774E0170001502     NARGALENA                        002621711";

  @Test
  public void testSpecMatchesNavaidRecord() {
    assertTrue(new VhfNavaidSpec().matchesRecord(navaid1));
  }

  @Test
  public void testParseNavaid1() {
    ArincRecord record = ArincVersion.V18.parse(navaid1);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.CAN, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.D, record.getRequiredField("sectionCode"));
    assertFalse(record.getOptionalField("subSectionCode").isPresent());
    assertFalse(record.getOptionalField("airportIdentifier").isPresent());
    assertFalse(record.getOptionalField("airportIcaoRegion").isPresent());
    assertEquals("GAL", record.getRequiredField("vorNdbIdentifier"));
    assertEquals("PA", record.getRequiredField("icaoRegion"));
    assertEquals("0", record.getRequiredField("continuationRecordNumber"));
    assertEquals(1148.0d, record.getRequiredField("vorNdbFrequency"));
    assertEquals("VDHW ", record.getRequiredField("navaidClass"));
    assertEquals(64.73813055555556d, record.getRequiredField("latitude"));
    assertEquals(-156.77715d, record.getRequiredField("longitude"));
    assertFalse(record.getOptionalField("dmeIdentifier").isPresent());
    assertEquals(64.73813888888888d, record.getRequiredField("dmeLatitude"));
    assertEquals(-156.77715d, record.getRequiredField("dmeLongitude"));
    assertEquals("E0170", record.getRequiredField("stationDeclination"));
    assertEquals(150.0d, record.getRequiredField("dmeElevation"));
    assertEquals(Integer.valueOf(2), record.getRequiredField("figureOfMerit"));
    assertFalse(record.getOptionalField("ilsDmeBias").isPresent());
    assertFalse(record.getOptionalField("frequencyProtectionDistance").isPresent());
    assertEquals("NAR", record.getRequiredField("datumCode"));
    assertEquals(Integer.valueOf(262), record.getRequiredField("fileRecordNumber"));
    assertEquals("1711", record.getRequiredField("cycle"));
  }

  public static final String navaid2 = "SUSAD        ABQ   K2111320VTHW N35023766W106485872ABQ N35023766W106485872E0130057492  423NARALBUQUERQUE                   057582003";

  @Test
  public void testSpecMatchesNavaidRecord2() {
    assertTrue(new VhfNavaidSpec().matchesRecord(navaid2));
  }

  @Test
  public void testParseNavaid2() {
    ArincRecord record = ArincVersion.V18.parse(navaid2);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.USA, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.D, record.getRequiredField("sectionCode"));
    assertFalse(record.getOptionalField("subSectionCode").isPresent());
    assertFalse(record.getOptionalField("airportIdentifier").isPresent());
    assertFalse(record.getOptionalField("airportIcaoRegion").isPresent());
    assertEquals("ABQ", record.getRequiredField("vorNdbIdentifier"));
    assertEquals("K2", record.getRequiredField("icaoRegion"));
    assertEquals("1", record.getRequiredField("continuationRecordNumber"));
    assertEquals(1132.0d, record.getRequiredField("vorNdbFrequency"));
    assertEquals("VTHW ", record.getRequiredField("navaidClass"));
    assertEquals(35.043794444444444d, record.getRequiredField("latitude"));
    assertEquals(-106.8163111111111d, record.getRequiredField("longitude"));
    assertEquals("ABQ", record.getRequiredField("dmeIdentifier"));
    assertEquals(35.043794444444444d, record.getRequiredField("dmeLatitude"));
    assertEquals(-106.8163111111111d, record.getRequiredField("dmeLongitude"));
    assertEquals("E0130", record.getRequiredField("stationDeclination"));
    assertEquals(5749.0d, record.getRequiredField("dmeElevation"));
    assertEquals(Integer.valueOf(2), record.getRequiredField("figureOfMerit"));
    assertFalse(record.getOptionalField("ilsDmeBias").isPresent());
    assertEquals(423.0d, record.getRequiredField("frequencyProtectionDistance"));
    assertEquals("NAR", record.getRequiredField("datumCode"));
    assertEquals(Integer.valueOf(5758), record.getRequiredField("fileRecordNumber"));
    assertEquals("2003", record.getRequiredField("cycle"));
  }
}
