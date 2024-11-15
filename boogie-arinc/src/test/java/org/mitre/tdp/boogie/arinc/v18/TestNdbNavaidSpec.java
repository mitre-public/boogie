package org.mitre.tdp.boogie.arinc.v18;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

class TestNdbNavaidSpec {

  private static final ArincRecordParser PARSER = ArincRecordParser.standard(new NdbNavaidSpec());

  private static final String navaid1 = "SEEUPNUUOLUU D     UU104000HMLW N52431540E039313750                       E0100           RPELIPETSK LMM RW15              353032003";

  @Test
  void testSpecMatchesNavaidRecord1() {
    assertTrue(new NdbNavaidSpec().matchesRecord(navaid1));
  }

  @Test
  void testValidatorPasses_Navaid1() {
    assertTrue(new NdbNavaidValidator().test(PARSER.parse(navaid1).orElseThrow(AssertionError::new)));
  }

  @Test
  void testParseNavaid1() {
    ArincRecord record = PARSER.parse(navaid1).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.EEU, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("N", record.requiredField("subSectionCode")),
        () -> assertEquals("UUOL", record.requiredField("airportIdentifier")),
        () -> assertEquals("UU", record.requiredField("airportIcaoRegion")),
        () -> assertEquals("D", record.requiredField("ndbIdentifier")),
        () -> assertEquals("UU", record.requiredField("ndbIcaoRegion")),
        () -> assertEquals("1", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(400.0d, record.requiredField("ndbFrequency")),
        () -> assertEquals("HMLW ", record.requiredField("navaidClass")),
        () -> assertEquals(52.72094444444445d, record.requiredField("latitude"), .000001),
        () -> assertEquals(39.52708333333333d, record.requiredField("longitude"), .000001),
        () -> assertEquals(10.0d, record.requiredField("magneticVariation")),
        () -> assertEquals("RPE", record.requiredField("datumCode")),
        () -> assertEquals("LIPETSK LMM RW15", record.requiredField("ndbNavaidName")),
        () -> assertEquals(Integer.valueOf(35303), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2003", record.requiredField("lastUpdateCycle"))
    );
  }

  private static final String SU = "SUSAPNKRUQK7 RU    K7002750HO W N35435924W080292244                       W0060           NARROVDY                         417831605";

  @Test
  void testSpecMatchesRecordSU() {
    assertTrue(new NdbNavaidSpec().matchesRecord(SU));
  }

  @Test
  void testValidatorPasses_SU() {
    assertTrue(new NdbNavaidValidator().test(PARSER.parse(SU).orElseThrow(AssertionError::new)));
  }
}
