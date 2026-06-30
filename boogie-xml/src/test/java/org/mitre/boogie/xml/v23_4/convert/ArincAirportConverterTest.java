package org.mitre.boogie.xml.v23_4.convert;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.boogie.xml.model.ArincAirport;
import org.mitre.boogie.xml.model.fields.RunwaySurfaceCode;
import org.mitre.boogie.xml.v23_4.generated.Airport;

class ArincAirportConverterTest {
  private final ArincAirportConverter converter = ArincAirportConverter.INSTANCE;

  @Test
  void nullAirportReturnsEmpty() {
    assertEquals(Optional.empty(), converter.apply(null));
  }

  @Test
  void invalidAirportReturnsEmpty() {
    Airport airport = new Airport();
    assertEquals(Optional.empty(), converter.apply(airport));
  }

  @Test
  void validAirportConverts() {
    Optional<ArincAirport> result = converter.apply(TestGeneratedObjects.newValidAirport());
    assertTrue(result.isPresent());

    ArincAirport ap = result.get();

    assertAll(
        () -> assertNotNull(ap.portInfo()),
        () -> assertNotNull(ap.portInfo().pointInfo()),
        () -> assertEquals("K6", ap.portInfo().pointInfo().icaoCode()),
        () -> assertEquals("KDCA", ap.portInfo().pointInfo().identifier()),
        () -> assertEquals(Optional.of(6869L), ap.longestRunway()),
        () -> assertEquals(Optional.of(RunwaySurfaceCode.HARD), ap.longestRunwaySurfaceCode()),
        () -> assertEquals(1, ap.runways().size()),
        () -> assertEquals(1, ap.airportGates().size()),
        () -> assertFalse(ap.portInfo().helipads().orElseThrow().isEmpty()),
        () -> assertEquals(3, ap.portInfo().procedures().orElseThrow().size())
    );
  }

  @Test
  void convertsWithNullOptionalFields() {
    Airport airport = TestGeneratedObjects.newValidAirport();
    airport.setLongestRunway(null);
    airport.setLongestRunwaySurfaceCode(null);

    Optional<ArincAirport> result = converter.apply(airport);
    assertTrue(result.isPresent());

    ArincAirport ap = result.get();
    assertAll(
        () -> assertEquals(Optional.empty(), ap.longestRunway()),
        () -> assertEquals(Optional.empty(), ap.longestRunwaySurfaceCode())
    );
  }

  @Test
  void convertsWithNoRunways() {
    Airport airport = TestGeneratedObjects.newValidAirport();
    airport.getRunway().clear();

    Optional<ArincAirport> result = converter.apply(airport);
    assertTrue(result.isPresent());
    assertTrue(result.get().runways().isEmpty());
  }

  @Test
  void convertsWithNoGates() {
    Airport airport = TestGeneratedObjects.newValidAirport();
    airport.getAirportGate().clear();

    Optional<ArincAirport> result = converter.apply(airport);
    assertTrue(result.isPresent());
    assertTrue(result.get().airportGates().isEmpty());
  }

  @Test
  void convertsWithNoHelipads() {
    Airport airport = TestGeneratedObjects.newValidAirport();
    airport.getHelipad().clear();

    Optional<ArincAirport> result = converter.apply(airport);
    assertTrue(result.isPresent());
    assertTrue(result.get().portInfo().helipads().orElseThrow().isEmpty());
  }

  @Test
  void convertsWithNoProcedures() {
    Airport airport = TestGeneratedObjects.newValidAirport();
    airport.setTerminalProcedures(null);

    Optional<ArincAirport> result = converter.apply(airport);
    assertTrue(result.isPresent());
    assertTrue(result.get().portInfo().procedures().orElseThrow().isEmpty());
  }
}
