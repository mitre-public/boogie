package org.mitre.boogie.xml.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.ArincRunway;
import org.mitre.boogie.xml.model.fields.ArincPointInfo;
import org.mitre.boogie.xml.model.fields.ArincPortInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordInfo;
import org.mitre.boogie.xml.model.fields.ArincRecordType;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.MagneticVariation;

class AirportAssemblerTest {

  private static final LatLong AIRPORT_POSITION = LatLong.of(42.2124, -83.3534);
  private static final MagneticVariation MAG_VAR = MagneticVariation.ofDegrees(-5.0);
  private static final String CYCLE = "2504";

  @Test
  void assemblesEmptyRecords() {
    AirportAssembler<Airport> assembler = AirportAssembler.standard();
    ArincRecords records = ArincRecords.standard();

    Collection<Airport> airports = assembler.assemble(records);

    assertTrue(airports.isEmpty());
  }

  @Test
  void assemblesAirportWithNoRunways() {
    AirportAssembler<Airport> assembler = AirportAssembler.standard();
    ArincRecords records = ArincRecords.standard()
        .airports(Set.of(testAirport("KDTW", List.of())));

    Collection<Airport> airports = assembler.assemble(records);

    assertEquals(1, airports.size());
    Airport apt = airports.iterator().next();
    assertAll(
        () -> assertEquals("KDTW", apt.airportIdentifier()),
        () -> assertTrue(apt.runways().isEmpty())
    );
  }

  @Test
  void assemblesAirportWithRunways() {
    ArincRunway rw09L = testRunway("RW09L", LatLong.of(42.213, -83.360));
    ArincRunway rw27R = testRunway("RW27R", LatLong.of(42.213, -83.340));

    AirportAssembler<Airport> assembler = AirportAssembler.standard();
    ArincRecords records = ArincRecords.standard()
        .airports(Set.of(testAirport("KDTW", List.of(rw09L, rw27R))));

    Collection<Airport> airports = assembler.assemble(records);

    assertEquals(1, airports.size());
    Airport apt = airports.iterator().next();
    assertEquals(2, apt.runways().size(), "Should produce one Runway per ArincRunway (both directions)");
  }

  @Test
  void assemblesMultipleAirports() {
    AirportAssembler<Airport> assembler = AirportAssembler.standard();
    ArincRecords records = ArincRecords.standard()
        .airports(Set.of(
            testAirport("KDTW", List.of()),
            testAirport("KJFK", List.of())
        ));

    Collection<Airport> airports = assembler.assemble(records);

    assertEquals(2, airports.size());
  }

  private static ArincAirport testAirport(String identifier, List<ArincRunway> runways) {
    ArincPortInfo portInfo = ArincPortInfo.builder()
        .pointInfo(testPointInfo(identifier))
        .recordInfo(testRecordInfo())
        .build();

    return ArincAirport.builder()
        .portInfo(portInfo)
        .runways(runways)
        .build();
  }

  private static ArincRunway testRunway(String identifier, LatLong position) {
    return ArincRunway.builder()
        .pointInfo(ArincPointInfo.builder()
            .identifier(identifier)
            .icaoCode("K6")
            .latLong(position)
            .magneticVariation(MAG_VAR)
            .referenceId(identifier)
            .build())
        .recordInfo(testRecordInfo())
        .runwayTrueBearing(90.0)
        .runwayLength(10000L)
        .build();
  }

  private static ArincPointInfo testPointInfo(String identifier) {
    return ArincPointInfo.builder()
        .identifier(identifier)
        .icaoCode("K6")
        .latLong(AIRPORT_POSITION)
        .magneticVariation(MAG_VAR)
        .referenceId(identifier)
        .build();
  }

  private static ArincRecordInfo testRecordInfo() {
    return ArincRecordInfo.builder()
        .cycleDate(CYCLE)
        .recordType(ArincRecordType.STANDARD)
        .build();
  }
}
