package org.mitre.tdp.boogie.arinc.v18.spec;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public class TestNdbNavaidSpec {

  public static final String navaid1 = "SEEUPNUUOLUU D     UU104000HMLW N52431540E039313750                       E0100           RPELIPETSK LMM RW15              353032003";

  @Test
  void testSpecMatchesNavaidRecord1() {
    assertTrue(new NdbNavaidSpec().matchesRecord(navaid1));
  }

  @Test
  void testParseNavaid1() {
    ArincRecord record = ArincVersion.V18.parser().apply(navaid1).orElseThrow(AssertionError::new);

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
        () -> assertEquals(52.72094444444445d, record.requiredField("latitude")),
        () -> assertEquals(39.52708333333333d, record.requiredField("longitude")),
        () -> assertEquals(10.0d, record.requiredField("magneticVariation")),
        () -> assertEquals("RPE", record.requiredField("datumCode")),
        () -> assertEquals("LIPETSK LMM RW15", record.requiredField("ndbNavaidName")),
        () -> assertEquals(Integer.valueOf(35303), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2003", record.requiredField("lastUpdateCycle"))
    );
  }
}
