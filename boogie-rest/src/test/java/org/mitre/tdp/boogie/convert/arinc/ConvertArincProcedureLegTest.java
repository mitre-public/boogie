package org.mitre.tdp.boogie.convert.arinc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.convert.arinc.ConvertArincProcedureLeg;

class ConvertArincProcedureLegTest {
 private static final org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg leg = new ArincProcedureLeg.Builder()
     .recordType(RecordType.S)
     .sectionCode(SectionCode.P)
     .airportIdentifier("KALX")
     .airportIcaoRegion("IS")
     .sidStarIdentifier("GOING")
     .routeType("T")
     .sequenceNumber(0)
     .pathTerm(PathTerminator.DF)
     .subSectionCode("A")
     .lastUpdateCycle("VACATION")
     .fileRecordNumber(1)
     .build();

 private static final ConvertArincProcedureLeg converter = ConvertArincProcedureLeg.INSTANCE;

 @Test
 void testConvert() {
  org.mitre.tdp.boogie.contract.arinc.ArincProcedureLeg converted = converter.apply(leg);
  assertAll("Checking the non null fields and an optional",
      () -> assertNotNull(converted),
      () -> assertEquals(RecordType.S, converted.recordType()),
      () -> assertEquals(SectionCode.P, converted.sectionCode()),
      () -> assertEquals("KALX", converted.airportIdentifier()),
      () -> assertEquals("IS", converted.airportIcaoRegion()),
      () -> assertEquals("GOING", converted.sidStarIdentifier()),
      () -> assertEquals("T", converted.routeType()),
      () -> assertEquals(0, converted.sequenceNumber()),
      () -> assertEquals(PathTerminator.DF, converted.pathTerm()),
      () -> assertEquals("VACATION", converted.lastUpdateCycle()),
      () -> assertEquals(1, converted.fileRecordNumber()),
      () -> assertEquals("A", converted.subSectionCode().orElseThrow()),
      () -> assertTrue(converted.speedLimitDescription().isEmpty())

  );
 }
}
