package org.mitre.tdp.boogie.arinc.v19;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.HoldingPatternValidator;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v18.field.TurnDirection;

public class TestHoldingPatternSpec {

  private static final String HOLD = "SUSAEPENRT                 00BARWUK7EA00540R040                                                   BARWU                    353392003";
  private static final String HOLD2 = "SAFREPENRT                 10ABU  HLD 01400L   15FL150FL290265                                    ABU ARGUB                141282101";
  @Test
  void matchesRecord19() {
    assertTrue(new org.mitre.tdp.boogie.arinc.v19.HoldingPatternSpec().matchesRecord(HOLD));
  }

  @Test
  void validatorPasses() {
    assertTrue(new HoldingPatternValidator().test(ArincVersion.V19.parser().apply(HOLD).orElseThrow()));
  }

  @Test
  void parse19() {
    ArincRecord record = ArincVersion.V19.parser().apply(HOLD).orElseThrow(AssertionError::new);
    ArincRecord record1 = ArincVersion.V19.parser().apply(HOLD2).orElseThrow(AssertionError::new);

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
        () -> assertTrue(record.optionalField("verticalScaleFactor").isEmpty()),
        () -> assertTrue(record.optionalField("rvsmMinimumLevel").isEmpty()),
        () -> assertTrue(record.optionalField("rvsmMaximumLevel").isEmpty()),
        () -> assertEquals("BARWU", record.optionalField("holdingName").orElseThrow()),
        () -> assertEquals(Integer.valueOf(35339), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2003", record.requiredField("lastUpdatedCycle")),
        () -> assertEquals(Integer.valueOf(15), record1.requiredField("legTime"))
    );
  }
}
