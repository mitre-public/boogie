package org.mitre.tdp.boogie.arinc.v18;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

public class TestAirportGateSpec {

  private static final ArincRecordParser PARSER = ArincRecordParser.standard(new AirportGateSpec());

  public static final String RAW_RECORD = "SUSAP KJFKK6B2T29    0          N40383000W073472400                                                                        733432003";

  @Test
  void testRawRecord() {
    ArincRecord record = PARSER.parse(RAW_RECORD).orElseThrow();

    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("KJFK", record.requiredField("airportIdentifier")),
        () -> assertEquals("K6", record.requiredField("airportIcaoRegion")),
        () -> assertEquals("B", record.requiredField("subSectionCode")),
        () -> assertEquals("2T29", record.requiredField("gateIdentifier")),
        () -> assertEquals(40.641666666666666, record.requiredField("latitude")),
        () -> assertEquals(-73.78999999999999, record.requiredField("longitude")),
        () -> assertFalse(record.optionalField("gateName").isPresent()),
        () -> assertEquals(Integer.valueOf(73343), record.requiredField("fileRecordNumber")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals("2003", record.requiredField("lastUpdatedCycle"))
    );
  }
}