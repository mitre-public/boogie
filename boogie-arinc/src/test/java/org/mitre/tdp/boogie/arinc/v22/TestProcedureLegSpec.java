package org.mitre.tdp.boogie.arinc.v22;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;

public class TestProcedureLegSpec {
  static ArincRecordParser parser = ArincRecordParser.standard(new ProcedureLegSpec());
  static ProcedureLegConverter converter = new ProcedureLegConverter(); //just new fields so same converter

  static String SID = "SUSAP KBOSK6DHYLND71RW04RW010         1     010VA                     0350        + 00520     18000                    DX  980422312";
  static String APPROACH = "SUSAP KBOSK6FR32   AFREDOJ010FREDOK6EA1E       IF                                             18000                 C PSH  989242406";
  static String sameTransitionIdent = "SAFRP GQNOGQFD34-Y AOT   F010OT   GQD 1V  H    IF                                 + 02000                           U  S   988462210";

  @Test
  void parseSidStar() {
    ArincRecord record = parser.parse(SID).orElseThrow();
    assertAll(
        () -> assertFalse(record.containsParsedField("routeTypeQualifier1")),
        () -> assertEquals("D", record.requiredField("routeTypeQualifier2")),
        () -> assertEquals("X", record.requiredField("routeTypeQualifier3"))
    );
  }

  @Test
  void parseApproach() {
    ArincRecord record = parser.parse(APPROACH).orElseThrow();
    assertAll(
        () -> assertEquals("P", record.requiredField("routeTypeQualifier1")),
        () -> assertEquals("S", record.requiredField("routeTypeQualifier2")),
        () -> assertEquals("H", record.requiredField("routeTypeQualifier3"))
    );
  }

  @Test
  void convertSidStar() {
    ArincProcedureLeg leg = parser.parse(SID).flatMap(converter).orElseThrow();
    assertAll(
        () -> assertTrue(leg.routeTypeQualifier1().isEmpty()),
        () -> assertEquals("D", leg.routeTypeQualifier2().orElseThrow()),
        () -> assertEquals("X", leg.routeTypeQualifier3().orElseThrow())
    );
  }

  @Test
  void convertApproach() {
    ArincProcedureLeg leg = parser.parse(APPROACH).flatMap(converter).orElseThrow();
    assertAll(
        () -> assertEquals("P", leg.routeTypeQualifier1().orElseThrow()),
        () -> assertEquals("S", leg.routeTypeQualifier2().orElseThrow()),
        () -> assertEquals("H", leg.routeTypeQualifier3().orElseThrow())
    );
  }

  static String noCourse = "SSPAP YWKSYMFH09   Z     J040         0  M  010CA                     094T        + 03400                           B PAH  392642312";
  @Test
  void testNoCourse() {
    Optional<ArincProcedureLeg> leg = parser.parse(noCourse).flatMap(converter);
    assertTrue(leg.isEmpty(), "this is bad we dont' want to be dropping legs");
    //assertFalse(record.containsParsedField("outboundMagneticCourse"), "we should implement true processing but this is expected for now");
  }

  @Test
  void testSameTransitionIdent() {
    ArincProcedureLeg leg = parser.parse(sameTransitionIdent).flatMap(converter).orElseThrow();
    assertEquals("A", leg.routeType(), "duplicate approach transitions");
  }
}
