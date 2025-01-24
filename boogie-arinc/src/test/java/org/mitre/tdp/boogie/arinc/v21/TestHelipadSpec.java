package org.mitre.tdp.boogie.arinc.v21;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincHelipad;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.arinc.v21.field.HelicopterPerformanceRequirement;
import org.mitre.tdp.boogie.arinc.v21.field.PadShape;

public class TestHelipadSpec {
  static ArincRecordParser parser = ArincRecordParser.standard(new HelipadSpec());
  static HelipadConverter converter = new HelipadConverter();
  static String PAD_1 = "SCANP CYAWCYH16H     0R03500112 N44382665W063302420HASPH   U     00144                                                     194742409";
  static String PAD_2 = "SCANP CYAWCYH34H     0R03500112 N44380091W063295190HASPH   U     00144                                                     194752409";

  @Test
  public void testParse() {
    ArincRecord record = parser.parse(PAD_1).orElseThrow();
    assertAll(
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.CAN, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.P, record.requiredField("sectionCode")),
        () -> assertEquals("CYAW", record.requiredField("airportOrHeliportIdentifier")),
        () -> assertEquals("CY", record.requiredField("icaoCode")),
        () -> assertEquals("H", record.requiredField("subSectionCode")),
        () -> assertEquals("16H", record.requiredField("helipadIdentifier")),
        () -> assertEquals("0", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(PadShape.R, record.requiredField("padShape")),
        () -> assertEquals("03500112", record.requiredField("padDimensions")),
        () -> assertEquals(44.64073611111111, record.requiredField("latitude")),
        () -> assertEquals(-63.50672222222222, record.requiredField("longitude")),
        () -> assertFalse(record.containsParsedField("maximumAllowableHelicopterWeight")),
        () -> assertEquals(HelicopterPerformanceRequirement.U, record.requiredField("helicopterPerformanceRequirement")),
        () -> assertEquals(Integer.valueOf(144), record.requiredField("landingThresholdElevation")),
        () -> assertEquals(Integer.valueOf(19474), record.requiredField("fileRecordNumber")),
        () -> assertEquals("2409", record.requiredField("cycle"))
    );
  }

  @Test
  void testConvert() {
    ArincRecord record = parser.parse(PAD_2).orElseThrow();
    ArincHelipad helipad = converter.apply(record).orElseThrow();
    assertAll(
        () -> assertEquals(RecordType.S, helipad.recordType()),
        () -> assertEquals(CustomerAreaCode.CAN, helipad.customerAreaCode()),
        () -> assertEquals(SectionCode.P, helipad.sectionCode()),
        () -> assertEquals("CYAW", helipad.airportHeliportIdentifier()),
        () -> assertEquals("CY", helipad.icaoCode()),
        () -> assertEquals("H", helipad.subSectionCode().orElseThrow()),
        () -> assertEquals("34H", helipad.helipadIdentifier()),
        () -> assertEquals("0", helipad.continuationRecordNumber().orElseThrow()),
        () -> assertEquals(PadShape.R.name(), helipad.padShape()),
        () -> assertEquals("03500112", record.requiredField("padDimensions")),
        () -> assertEquals(112.0, helipad.padXDimension().orElseThrow()),
        () -> assertEquals(3500, helipad.padYDimension().orElseThrow()),
        () -> assertTrue(helipad.padDiameter().isEmpty()),
        () -> assertEquals(44.63358611111111, helipad.latitude()),
        () -> assertEquals(-63.49775, helipad.longitude()),
        () -> assertTrue(helipad.maximumHelicopterWeight().isEmpty()),
        () -> assertEquals(HelicopterPerformanceRequirement.U.name(), helipad.helicopterPerformanceRequirement().orElseThrow()),
        () -> assertEquals(144.0, helipad.padElevation().orElseThrow()),
        () -> assertEquals(19475, helipad.fileRecordNumber()),
        () -> assertEquals("2409", helipad.cycle())
    );
  }
}
