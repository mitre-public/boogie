package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.field.ApplicationType;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public class TestAirportPrimaryExtensionSpec {
  private static final String RAW = "SUSAP K1A0K7A        3E1A0                                                                                                 007132003";

  @Test
  void testSpecMatches() {
    assertFalse(new AirportSpec().matchesRecord(RAW));
    assertTrue(new AirportPrimaryExtensionSpec().matchesRecord(RAW));
  }

  @Test
  void testValidatorPasses() {
    assertTrue(new AirportPrimaryExtensionValidator().test(ArincVersion.V19.parser().parse(RAW).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParse() {
    ArincRecord record = ArincVersion.V19.parser().parse(RAW).orElseThrow(AssertionError::new);
    assertNotNull(record);
    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("K1A0", record.requiredField("airportIdentifier")),
        () -> assertEquals("K7", record.requiredField("icaoRegion")),
        () -> assertEquals("A", record.requiredField("subSectionCode")),
        () -> assertFalse(record.optionalField("iataDesignator").isPresent()),
        () -> assertEquals("3", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(ApplicationType.E, record.requiredField("applicationType")),
        () -> assertEquals("1A0", record.requiredField("notes")),
        () -> assertEquals(Integer.valueOf(713), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2003", record.requiredField("cycle"))
    );

  }


}
