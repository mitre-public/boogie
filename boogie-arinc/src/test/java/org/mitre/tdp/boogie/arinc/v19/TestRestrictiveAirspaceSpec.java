package org.mitre.tdp.boogie.arinc.v19;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.v18.RestrictiveAirspaceValidator;
import org.mitre.tdp.boogie.arinc.v18.field.BoundaryVia;
import org.mitre.tdp.boogie.arinc.v18.field.CustomerAreaCode;
import org.mitre.tdp.boogie.arinc.v18.field.Level;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v19.field.RestrictiveType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

public class TestRestrictiveAirspaceSpec {
  static String string = "SUSAURK1NBOARDMAN  A00101L    G N45525900W119310400                              04000M17999MBOARDMAN MOA                  714171703";

  static ArincRecordParser V18 = ArincRecordParser.standard(new RestrictiveAirspaceLegSpec());
  static RestrictiveAirspaceValidator VALIDATOR = new RestrictiveAirspaceValidator();
  @Test
  void testRecord() {
    ArincRecord record = V18.parse(string).orElseThrow(AssertionError::new);
    assertAll(
        () -> assertTrue(VALIDATOR.test(record)),
        () -> assertEquals(RecordType.S, record.requiredField("recordType")),
        () -> assertEquals(CustomerAreaCode.USA, record.requiredField("customerAreaCode")),
        () -> assertEquals(SectionCode.U, record.requiredField("sectionCode")),
        () -> assertEquals("R", record.requiredField("subSectionCode")),
        () -> assertEquals("K1", record.requiredField("icaoRegion")),
        () -> assertEquals(RestrictiveType.N, record.requiredField("restrictiveType")),
        () -> assertEquals("BOARDMAN", record.requiredField("restrictiveAirspaceDesignation")),
        () -> assertEquals("A", record.requiredField("multipleCode")),
        () -> assertEquals(Integer.valueOf(10), record.requiredField("sequenceNumber")),
        () -> assertEquals("1", record.requiredField("continuationRecordNumber")),
        () -> assertEquals(Level.L, record.requiredField("level")),
        () -> assertFalse(record.optionalField("timeCode").isPresent()),
        () -> assertFalse(record.optionalField("notam").isPresent()),
        () -> assertEquals(BoundaryVia.G, record.requiredField("boundaryVia")),
        () -> assertEquals(45.88305555555556, record.requiredField("latitude")),
        () -> assertEquals(-119.51777777777778, record.requiredField("longitude")),
        () -> assertFalse(record.optionalField("arcOriginLatitude").isPresent()),
        () -> assertFalse(record.optionalField("arcOriginLongitude").isPresent()),
        () -> assertFalse(record.optionalField("arcDistance").isPresent()),
        () -> assertFalse(record.optionalField("arcBearing").isPresent()),
        () -> assertEquals(4000.0, record.requiredField("lowerLimit")),
        () -> assertEquals("M", record.requiredField("lowerIndicator")),
        () -> assertEquals(17999.0, record.requiredField("upperLimit")),
        () -> assertEquals("M", record.requiredField("upperIndicator")),
        () -> assertEquals("BOARDMAN MOA", record.requiredField("restrictiveAirspaceName")),
        () -> assertEquals(Integer.valueOf(71417), record.requiredField("fileRecordNumber")),
        () -> assertEquals("1703", record.requiredField("cycle"))
    );
  }
}
