package org.mitre.tdp.boogie.arinc.v21;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.junit.jupiter.api.Assertions.fail;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;

public class TestProcedureLegSpec {
  static ArincRecordParser parser = ArincRecordParser.standard(new ProcedureLegSpec());
  static ProcedureLegConverter converter = new ProcedureLegConverter(); //just new fields so same converter
  static String SID = "SUSAP KBOSK6DHYLND61RW15RW035BOATTK6PC0E    010TF                                 + 04000          250               - DX  324422210";
  static String NO_M = "SUSAP KBOSK6DHYLND61RW15RW035BOATTK6PC0E    010TF                                 + 04000          250               - DM  324422210";

  @Test
  void parse() {
    ArincRecord record = parser.parse(SID).orElseThrow();
    assertAll(
        () -> assertFalse(record.containsParsedField("routeTypeQualifier1")),
        () -> assertEquals("D", record.requiredField("routeTypeQualifier2")),
        () -> assertEquals("X", record.requiredField("routeTypeQualifier3"))
    );

  }

  @Test
  void convert() {
    ArincProcedureLeg leg = parser.parse(SID).flatMap(converter).orElseThrow();
    assertAll(
        () -> assertTrue(leg.routeTypeQualifier1().isEmpty()),
        () -> assertEquals("D", leg.routeTypeQualifier2().get()),
        () -> assertEquals("X", leg.routeTypeQualifier3().get())
    );
  }
  @Test
  void robust() {
    ArincProcedureLeg leg = parser.parse(NO_M).flatMap(converter).orElseThrow();
    assertAll(
        () -> assertTrue(leg.routeTypeQualifier1().isEmpty()),
        () -> assertEquals("D", leg.routeTypeQualifier2().get()),
        () -> assertTrue( leg.routeTypeQualifier3().isEmpty())
    );
  }
}
