package org.mitre.tdp.boogie.v18.record;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.ArincRecord;
import org.mitre.tdp.boogie.ArincVersion;
import org.mitre.tdp.boogie.v18.spec.field.CustomerAreaCode;
import org.mitre.tdp.boogie.v18.spec.field.RecordType;
import org.mitre.tdp.boogie.v18.spec.field.SectionCode;
import org.mitre.tdp.boogie.v18.spec.record.NdbNavaidSpec;

public class TestNdbNavaidSpec {

  public static final String navaid1 = "SEEUPNUUOLUU D     UU104000HMLW N52431540E039313750                       E0100           RPELIPETSK LMM RW15              353032003";

  @Test
  public void testSpecMatchesNavaidRecord1() {
    assertTrue(new NdbNavaidSpec().matchesRecord(navaid1));
  }

  @Test
  public void testParseNavaid1() {
    ArincRecord record = ArincVersion.V18.parse(navaid1);
    assertNotNull(record);

    assertEquals(RecordType.S, record.getRequiredField("recordType"));
    assertEquals(CustomerAreaCode.EEU, record.getRequiredField("customerAreaCode"));
    assertEquals(SectionCode.P, record.getRequiredField("sectionCode"));
    assertEquals("N", record.getRequiredField("subSectionCode"));
    assertEquals("UUOL", record.getRequiredField("airportIdentifier"));
    assertEquals("UU", record.getRequiredField("airportIcaoRegion"));
    assertEquals("D", record.getRequiredField("vorNdbIdentifier"));
    assertEquals("UU", record.getRequiredField("icaoRegion"));
    assertEquals("1", record.getRequiredField("continuationRecordNumber"));
    assertEquals(400.0d, record.getRequiredField("vorNdbFrequency"));
    assertEquals("HMLW ", record.getRequiredField("navaidClass"));
    assertEquals(52.72094444444445d, record.getRequiredField("latitude"));
    assertEquals(39.52708333333333d, record.getRequiredField("longitude"));
    assertEquals(10.0d, record.getRequiredField("magneticVariation"));
    assertEquals("RPE", record.getRequiredField("datumCode"));
    assertEquals("LIPETSK LMM RW15", record.getRequiredField("ndbNavaidName"));
    assertEquals(Integer.valueOf(35303), record.getRequiredField("fileRecordNumber"));
    assertEquals("2003", record.getRequiredField("cycle"));
  }
}
