package org.mitre.tdp.boogie.convert.arinc;


import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.convert.arinc.ConvertArincVhfNavaid;

public class ConvertArincVhfNavaidTest {
  private static final ArincVhfNavaid navaid = new ArincVhfNavaid.Builder()
      .recordType(RecordType.S)
      .sectionCode(SectionCode.P)
      .subSectionCode(" ")
      .vhfIdentifier("KRK")
      .vhfIcaoRegion("K1")
      .latitude(40D)
      .longitude(20D)
      .fileRecordNumber(1)
      .lastUpdateCycle("4040")
      .build();
  private static final ConvertArincVhfNavaid converter = ConvertArincVhfNavaid.INSTANCE;

  @Test
  void testConvert() {
    org.mitre.tdp.boogie.contract.arinc.ArincVhfNavaid converted = converter.apply(navaid);
    assertAll("Check manditory fields and an optoinal one",
        () -> assertNotNull(converted),
        () -> assertEquals(RecordType.S, converted.recordType()),
        () -> assertEquals(SectionCode.P, converted.sectionCode()),
        () -> assertEquals(" ", converted.subSectionCode().orElseThrow()),
        () -> assertEquals(40D, converted.latitude()),
        () -> assertEquals(20D, converted.longitude()),
        () -> assertEquals(1, converted.fileRecordNumber()),
        () -> assertEquals("4040", converted.lastUpdateCycle()),
        () -> assertTrue(converted.dmeIdentifier().isEmpty())
    );
  }
}
