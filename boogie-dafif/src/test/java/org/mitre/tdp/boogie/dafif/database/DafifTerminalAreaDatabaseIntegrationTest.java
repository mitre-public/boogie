package org.mitre.tdp.boogie.dafif.database;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.EmbeddedDafifFile;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;

@Tag("DAFIF")
@Tag("INTEGRATION")
class DafifTerminalAreaDatabaseIntegrationTest {

  static DafifTerminalAreaDatabase tad;

  @BeforeAll
  static void setUp() {
    EmbeddedDafifFile dafif = EmbeddedDafifFile.instance();
    tad = DafifDatabaseFactory.newTerminalAreaDatabase(
        dafif.dafifAirports(), dafif.dafifRunways(), dafif.dafifAddRunways(), dafif.dafifIls(), dafif.dafifTerminalSegments());
  }

  @Test
  void testAirportLookup() {
    assertAll(
        () -> assertTrue(tad.airport("AA30079").isPresent(), "AA30079 should be present"),
        () -> assertEquals("TNCA", tad.airport("AA30079").get().icaoCode()),
        () -> assertFalse(tad.airport("ZZZZZZZ").isPresent(), "Nonexistent airport should be absent")
    );
  }

  @Test
  void testAirportAtByNaturalKey() {
    assertAll(
        () -> assertTrue(tad.airportAt("TNCA", "AA").isPresent(), "TNCA/AA should resolve"),
        () -> assertEquals("AA30079", tad.airportAt("TNCA", "AA").get().airportIdentification()),
        () -> assertFalse(tad.airportAt("ZZZZ", "ZZ").isPresent(), "Nonexistent natural key should be absent")
    );
  }

  @Test
  void testRunwaysAt() {
    assertAll(
        () -> assertFalse(tad.runwaysAt("AA30079").isEmpty(), "AA30079 should have runways"),
        () -> assertTrue(tad.runwaysAt("ZZZZZZZ").isEmpty(), "Nonexistent airport should have no runways")
    );
  }

  @Test
  void testRunwayLookup() {
    DafifRunway rwy = tad.runwaysAt("AA30079").stream().findFirst().orElseThrow();
    assertAll(
        () -> assertTrue(tad.runway("AA30079", rwy.lowEndIdentifier()).isPresent(), "Should find runway by low end"),
        () -> assertTrue(tad.runway("AA30079", rwy.highEndIdentifier()).isPresent(), "Should find runway by high end"),
        () -> assertFalse(tad.runway("AA30079", "99").isPresent(), "Nonexistent runway identifier should be absent")
    );
  }

  @Test
  void testIlsComponentsPresent() {
    long airportsWithIls = tad.runwaysAt("AA30079").stream()
        .flatMap(r -> tad.ilsComponentsForRunway(r).stream())
        .count();
    assertTrue(airportsWithIls > 0, "AA30079 should have ILS components");
  }

  @Test
  void testTerminalSegmentsAt() {
    assertAll(
        () -> assertFalse(tad.terminalSegmentsAt("AE00007").isEmpty(), "AE00007 should have terminal segments"),
        () -> assertTrue(tad.terminalSegmentsAt("ZZZZZZZ").isEmpty(), "Nonexistent airport should have no segments")
    );
  }
}
