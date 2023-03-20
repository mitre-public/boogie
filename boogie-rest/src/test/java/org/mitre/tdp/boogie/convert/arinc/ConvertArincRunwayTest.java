package org.mitre.tdp.boogie.convert.arinc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.convert.arinc.ConvertArincRunway;

public class ConvertArincRunwayTest {
  private static final ArincRunway runway = new ArincRunway.Builder()
      .recordType(RecordType.S)
      .sectionCode(SectionCode.P)
      .airportIdentifier("KBOS")
      .airportIcaoRegion("K1")
      .subSectionCode("G")
      .runwayIdentifier("RW26")
      .latitude(90D)
      .longitude(10D)
      .fileRecordNumber(1)
      .lastUpdateCycle("2010")
      .build();

  private static final ConvertArincRunway converter = ConvertArincRunway.INSTANCE;

  @Test
  void testConvert() {
    org.mitre.tdp.boogie.contract.arinc.ArincRunway converted = converter.apply(runway);
    assertAll("Checking manditory fields and an optoinal",
        () -> assertNotNull(converted),
        () -> assertEquals(SectionCode.P, converted.sectionCode()),
        () -> assertEquals(RecordType.S, converted.recordType()),
        () -> assertEquals("KBOS", converted.airportIdentifier()),
        () -> assertEquals("K1", converted.airportIcaoRegion()),
        () -> assertEquals("G", converted.subSectionCode().orElseThrow()),
        () -> assertEquals("RW26", converted.runwayIdentifier()),
        () -> assertEquals(90D, converted.latitude()),
        () -> assertEquals(10D, converted.longitude()),
        () -> assertEquals(1, converted.fileRecordNumber()),
        () -> assertEquals("2010", converted.lastUpdateCycle())
    );
  }
}
