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
import org.mitre.tdp.boogie.arinc.v18.field.TurnDirection;

class TestHoldingPatternSpec {

  private static final ArincRecordParser V18 = ArincRecordParser.standard(new HoldingPatternSpec());

  private static final ArincRecordParser V19 = ArincRecordParser.standard(new org.mitre.tdp.boogie.arinc.v19.HoldingPatternSpec());

  private static final String HOLD = "SUSAEPENRT                 00BARWUK7EA00540R040                                                   BARWU                    353392003";

  @Test
  void matchesRecord18() {
    assertTrue(new HoldingPatternSpec().matchesRecord(HOLD));

  }

  @Test
  void validatorPasses() {
    assertTrue(new HoldingPatternValidator().test(V18.parse(HOLD).orElseThrow()));
    assertTrue(new HoldingPatternValidator().test(V19.parse(HOLD).orElseThrow()));
  }

  @Test
  void parse18() {

    ArincRecord record = V18.parse(HOLD).orElseThrow(AssertionError::new);

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.E, record.requiredField("sectionCode")),
        () -> assertEquals("P", record.requiredField("subSectionCode")),
        () -> assertEquals("ENRT", record.requiredField("regionCode")),
        () -> assertTrue(record.optionalField("icaoRegion").isEmpty()),
        () -> assertEquals("00", record.requiredField("duplicateIdentifier")),
        () -> assertEquals("BARWU", record.requiredField("fixIdentifier")),
        () -> assertEquals("K7", record.requiredField("fixIcaoRegion")),
        () -> assertEquals(SectionCode.E, record.requiredField("fixSectionCode")),
        () -> assertEquals("A", record.requiredField("fixSubSectionCode")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(54.0, record.requiredField("inboundHoldingCourse")),
        () -> assertEquals(TurnDirection.R, record.requiredField("turnDirection")),
        () -> assertEquals(Double.valueOf(40), record.requiredField("legLength")),
        () -> assertTrue(record.optionalField("legTime").isEmpty()),
        () -> assertTrue(record.optionalField("minimumAltitude").isEmpty()),
        () -> assertTrue(record.optionalField("maxAltitude").isEmpty()),
        () -> assertTrue(record.optionalField("holdingSpeed").isEmpty()),
        () -> assertTrue(record.optionalField("rnp").isEmpty()),
        () -> assertTrue(record.optionalField("arcRadius").isEmpty()),
        () -> assertEquals("BARWU", record.optionalField("holdingName").orElseThrow()),
        () -> assertEquals(Integer.valueOf(35339), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2003", record.requiredField("lastUpdatedCycle"))
    );
  }
}
