package org.mitre.tdp.boogie.convert.arinc;

import static org.junit.jupiter.api.Assertions.*;


import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.v18.field.RecordType;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;
import org.mitre.tdp.boogie.contract.arinc.ArincAirport;
import org.mitre.tdp.boogie.convert.arinc.ConvertArincAirport;


class ConvertArincAirportTest {

  private static final org.mitre.tdp.boogie.arinc.model.ArincAirport airport = new org.mitre.tdp.boogie.arinc.model.ArincAirport.Builder()
      .airportIdentifier("KDAV")
      .airportIcaoRegion("DB")
      .recordType(RecordType.S)
      .sectionCode(SectionCode.P)
      .subSectionCode(" ")
      .latitude(90D)
      .longitude(90D)
      .fileRecordNumber(1)
      .lastUpdateCycle("NOW")
      .build();

  private static final ConvertArincAirport converter = ConvertArincAirport.INSTANCE;

  @Test
  void convert() {
    ArincAirport converted = converter.apply(airport);
    assertAll("Better not be  null",
        () -> assertNotNull(converted, "This thing had better exit"),
        () -> assertEquals("KDAV", converted.airportIdentifier()),
        () -> assertEquals(90D, converted.latitude()),
        () -> assertEquals(90D, converted.longitude()),
        () -> assertTrue(converted.longestRunway().isEmpty())
        );
  }
}