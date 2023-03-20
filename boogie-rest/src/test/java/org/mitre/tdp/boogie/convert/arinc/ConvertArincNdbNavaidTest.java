package org.mitre.tdp.boogie.convert.arinc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.convert.arinc.ConvertArincNdbNavaid;

class ConvertArincNdbNavaidTest {
  private static final ArincNdbNavaid navaid = new ArincNdbNavaid.Builder()
      .recordType(RecordType.S)
      .sectionCode(SectionCode.D)
      .subSectionCode("B")
      .ndbIdentifier("DD")
      .ndbIcaoRegion("K1")
      .latitude(90D)
      .longitude(90D)
      .fileRecordNumber(1)
      .lastUpdateCycle("2001")
      .build();

  private static final ConvertArincNdbNavaid converter = ConvertArincNdbNavaid.INSTANCE;

  @Test
  void testConvert() {
    org.mitre.tdp.boogie.contract.arinc.ArincNdbNavaid converted = converter.apply(navaid);
    assertAll("Checking nonnulls and one optional",
        () -> assertEquals(RecordType.S, converted.recordType()),
        () -> assertEquals(SectionCode.D, converted.sectionCode()),
        () -> assertEquals("B", converted.subSectionCode().orElseThrow()),
        () -> assertEquals("DD", converted.ndbIdentifier()),
        () -> assertEquals("K1", converted.ndbIcaoRegion()),
        () -> assertEquals(90D, converted.latitude()),
        () -> assertEquals(90D, converted.longitude()),
        () -> assertEquals(1, converted.fileRecordNumber()),
        () -> assertEquals("2001", converted.lastUpdateCycle()),
        () -> assertTrue(converted.datumCode().isEmpty())
    );
  }
}
