package org.mitre.tdp.boogie.convert.arinc;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.convert.arinc.ConvertArincLocalizerGlideSlope;

class ConvertLocalizerGlideSlopeTest {

  private static final ArincLocalizerGlideSlope localizerGlideSlope = new ArincLocalizerGlideSlope.Builder()
      .recordType(RecordType.S)
      .sectionCode(SectionCode.P)
      .subSectionCode(" ")
      .airportIdentifier("DAVID")
      .airportIcaoRegion("DB")
      .localizerIdentifier("IS")
      .runwayIdentifier("VERY")
      .lastUpdateCycle("AWESOME")
      .fileRecordNumber(1)
      .build();

  private static final ConvertArincLocalizerGlideSlope converter = ConvertArincLocalizerGlideSlope.INSTANCE;

  @Test
  void convert() {
    org.mitre.tdp.boogie.contract.arinc.ArincLocalizerGlideSlope glideSlope = converter.apply(localizerGlideSlope);
    assertAll("Checking nonnull fields and an optional ",
        () -> assertNotNull(glideSlope),
        () -> assertEquals(RecordType.S, glideSlope.recordType()),
        () -> assertEquals(SectionCode.P, glideSlope.sectionCode()),
        () -> assertEquals(" ", glideSlope.subSectionCode().orElseThrow()),
        () -> assertEquals("DAVID", glideSlope.airportIdentifier()),
        () -> assertEquals("DB", glideSlope.airportIcaoRegion()),
        () -> assertEquals("IS", glideSlope.localizerIdentifier()),
        () -> assertEquals("VERY", glideSlope.runwayIdentifier()),
        () -> assertEquals("AWESOME", glideSlope.lastUpdateCycle()),
        () -> assertEquals(1, glideSlope.fileRecordNumber()),
        () -> assertTrue(glideSlope.glideSlopeElevation().isEmpty())
        );
  }
}
