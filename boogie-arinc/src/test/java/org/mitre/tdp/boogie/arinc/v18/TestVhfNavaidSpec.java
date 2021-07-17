package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.ArincRecord;

public class TestVhfNavaidSpec {

  public static final String navaid1 = "SCAND        GAL   PA011480VDHW N64441727W156463774    N64441730W156463774E0170001502     NARGALENA                        002621711";

  @Test
  void testSpecMatchesNavaidRecord() {
    assertTrue(new VhfNavaidSpec().matchesRecord(navaid1));
  }

  @Test
  void testParseNavaid1() {
    ArincRecord record = ArincVersion.V18.parser().apply(navaid1).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.CAN, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.D, record.requiredField("sectionCode")),
        () -> assertFalse(record.optionalField("subSectionCode").isPresent()),
        () -> assertFalse(record.optionalField("airportIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("airportIcaoRegion").isPresent()),
        () -> assertEquals("GAL", record.requiredField("vhfIdentifier")),
        () -> assertEquals("PA", record.requiredField("vhfIcaoRegion")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(1148.0d, record.requiredField("vhfFrequency")),
        () -> assertEquals("VDHW ", record.requiredField("navaidClass")),
        () -> assertEquals(64.73813055555556d, record.requiredField("latitude")),
        () -> assertEquals(-156.77715d, record.requiredField("longitude")),
        () -> assertFalse(record.optionalField("dmeIdentifier").isPresent()),
        () -> assertEquals(64.73813888888888d, record.requiredField("dmeLatitude")),
        () -> assertEquals(-156.77715d, record.requiredField("dmeLongitude")),
        () -> assertEquals(17.0d, record.requiredField("stationDeclination")),
        () -> assertEquals(150.0d, record.requiredField("dmeElevation")),
        () -> assertEquals(Integer.valueOf(2), record.requiredField("figureOfMerit")),
        () -> assertFalse(record.optionalField("ilsDmeBias").isPresent()),
        () -> assertFalse(record.optionalField("frequencyProtectionDistance").isPresent()),
        () -> assertEquals("NAR", record.requiredField("datumCode")),
        () -> assertEquals(Integer.valueOf(262), record.requiredField("fileRecordNumber")),
        () -> assertEquals("1711", record.requiredField("lastUpdateCycle"))
    );
  }

  public static final String navaid2 = "SUSAD        ABQ   K2111320VTHW N35023766W106485872ABQ N35023766W106485872E0130057492  423NARALBUQUERQUE                   057582003";

  @Test
  void testSpecMatchesNavaidRecord2() {
    assertTrue(new VhfNavaidSpec().matchesRecord(navaid2));
  }

  @Test
  void testParseNavaid2() {
    ArincRecord record = ArincVersion.V18.parser().apply(navaid2).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.D, record.requiredField("sectionCode")),
        () -> assertFalse(record.optionalField("subSectionCode").isPresent()),
        () -> assertFalse(record.optionalField("airportIdentifier").isPresent()),
        () -> assertFalse(record.optionalField("airportIcaoRegion").isPresent()),
        () -> assertEquals("ABQ", record.requiredField("vhfIdentifier")),
        () -> assertEquals("K2", record.requiredField("vhfIcaoRegion")),
        () -> assertEquals("1", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(1132.0d, record.requiredField("vhfFrequency")),
        () -> assertEquals("VTHW ", record.requiredField("navaidClass")),
        () -> assertEquals(35.043794444444444d, record.requiredField("latitude")),
        () -> assertEquals(-106.8163111111111d, record.requiredField("longitude")),
        () -> assertEquals("ABQ", record.requiredField("dmeIdentifier")),
        () -> assertEquals(35.043794444444444d, record.requiredField("dmeLatitude")),
        () -> assertEquals(-106.8163111111111d, record.requiredField("dmeLongitude")),
        () -> assertEquals(13.0d, record.requiredField("stationDeclination")),
        () -> assertEquals(5749.0d, record.requiredField("dmeElevation")),
        () -> assertEquals(Integer.valueOf(2), record.requiredField("figureOfMerit")),
        () -> assertFalse(record.optionalField("ilsDmeBias").isPresent()),
        () -> assertEquals(423.0d, record.requiredField("frequencyProtectionDistance")),
        () -> assertEquals("NAR", record.requiredField("datumCode")),
        () -> assertEquals(Integer.valueOf(5758), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2003", record.requiredField("lastUpdateCycle"))
    );
  }
}
