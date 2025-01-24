package org.mitre.tdp.boogie.arinc.v20;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegValidator;
import org.mitre.tdp.boogie.arinc.v20.field.ProcedureDesignAircraftCategoryOrType;

public class TestProcedureLegSpec {
  static ArincRecordParser PARSER = ArincRecordParser.standard(new ProcedureLegSpec());
  static ProcedureLegConverter CONVERTER = new ProcedureLegConverter();
  static String HOLD_LEG = "SUSAP KDCAK6FR15   Z     J060DCA  K6DB0NE  L   HM                     00700040  I                                   A PAH  614982210";
  static String TRUPS = "SUSAP KDCAK6ETRUPS41BKW  K010BKW  K6D 0V       IF                                             18000                    DX  614082210";
  static String ERROR = "SUSAP KDCAK6FR15   Z     W060DCA  K6DB0NE  L   HM                     00700040  R                                   A PAH  614982210";

  @Test
  void matches20() {
    assertTrue(new ProcedureLegSpec().matchesRecord(HOLD_LEG));
  }

  @Test
  void validator() {
    assertTrue(new ProcedureLegValidator().test(PARSER.parse(TRUPS).orElseThrow()));
  }

  @Test
  void parse20() {
    ArincRecord pattern = PARSER.parse(HOLD_LEG).orElseThrow();
    assertAll(
        () -> assertEquals("I", pattern.optionalField("legInboundOutboundIndicator").orElseThrow()),
        () -> assertEquals(ProcedureDesignAircraftCategoryOrType.J, pattern.optionalField("procedureDesignAircraftCategoryOrType").orElseThrow())
    );
  }

  @Test
  void robust() {
    ArincRecord record = PARSER.parse(ERROR).orElseThrow();
    assertAll(
        () -> assertFalse(record.containsParsedField("legInboundOutboundIndicator")),
        () -> assertFalse(record.containsParsedField("procedureDesignAircraftCategoryOrType"))
    );
  }

  @Test
  void convert() {
    ArincProcedureLeg leg = PARSER.parse(HOLD_LEG).flatMap(CONVERTER).orElseThrow();
    assertAll(
        () -> assertEquals("I", leg.legInboundOutboundIdentifier().orElseThrow()),
        () -> assertEquals("J", leg.categoryOrType().orElseThrow(), "Strings to deal with the changing enums over time")
    );
  }
}
