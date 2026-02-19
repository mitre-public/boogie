package org.mitre.tdp.boogie.dafif.database;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.TestObjects;
import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalSegment;

class TestDafifTerminalAreaDatabase {

  static DafifTerminalAreaDatabase tad;

  @BeforeAll
  static void setUp() {
    Collection<DafifAirport> airports = Set.of(TestObjects.fakeAirport, TestObjects.fakeAirport2, TestObjects.fakeAirport3, TestObjects.arptAA30079, TestObjects.arptAS10286);
    Collection<DafifRunway> runways = Set.of(TestObjects.rwy1129, TestObjects.rwy0523, TestObjects.rwy0918, TestObjects.rwy0220, TestObjects.rwyAA30079_2911, TestObjects.rwyAS10286_3315);
    Collection<DafifAddRunway> addRunways = Set.of(TestObjects.addRunway, TestObjects.addRunway0523, TestObjects.addRunway0918);
    Collection<DafifIls> ils = Set.of(TestObjects.fakeIls, TestObjects.fakeIls05, TestObjects.fakeIls09);
    Collection<DafifTerminalSegment> segments = List.of();
    tad = DafifDatabaseFactory.newTerminalAreaDatabase(airports, runways, addRunways, ils, segments);
  }

  @Test
  void testAirportLookup() {
    assertAll(
        () -> assertTrue(tad.airport("FAKE1").isPresent(), "FAKE1 should be present"),
        () -> assertEquals("IDK1", tad.airport("FAKE1").get().icaoCode()),
        () -> assertTrue(tad.airport("AA30079").isPresent(), "AA30079 should be present"),
        () -> assertFalse(tad.airport("NONEXISTENT").isPresent(), "Nonexistent should be absent")
    );
  }

  @Test
  void testAirportAtByNaturalKey() {
    assertAll(
        () -> assertTrue(tad.airportAt("TNCA", "AA").isPresent(), "TNCA/AA should resolve to AA30079"),
        () -> assertEquals("AA30079", tad.airportAt("TNCA", "AA").get().airportIdentification()),
        () -> assertTrue(tad.airportAt("YBCS", "AS").isPresent(), "YBCS/AS should resolve to AS10286"),
        () -> assertFalse(tad.airportAt("ZZZZ", "XX").isPresent(), "Nonexistent natural key should be absent")
    );
  }

  @Test
  void testRunwaysAt() {
    assertAll(
        () -> assertEquals(2, tad.runwaysAt("FAKE1").size(), "FAKE1 should have 2 runways (11/29 and 02/20)"),
        () -> assertEquals(1, tad.runwaysAt("AA30079").size(), "AA30079 should have 1 runway"),
        () -> assertTrue(tad.runwaysAt("NONEXISTENT").isEmpty(), "Nonexistent airport should have no runways")
    );
  }

  @Test
  void testRunwayLookup() {
    assertAll(
        () -> assertTrue(tad.runway("FAKE1", "11").isPresent(), "Should find runway by low end"),
        () -> assertTrue(tad.runway("FAKE1", "29").isPresent(), "Should find runway by high end"),
        () -> assertEquals("11", tad.runway("FAKE1", "11").get().lowEndIdentifier()),
        () -> assertFalse(tad.runway("FAKE1", "99").isPresent(), "Nonexistent runway should be absent")
    );
  }

  @Test
  void testAddRunwayLookup() {
    assertAll(
        () -> assertTrue(tad.addRunway("FAKE1", "11").isPresent(), "Should find add runway by low end"),
        () -> assertTrue(tad.addRunway("FAKE1", "29").isPresent(), "Should find add runway by high end"),
        () -> assertFalse(tad.addRunway("AA30079", "11").isPresent(), "AA30079 has no add runway in test data")
    );
  }

  @Test
  void testAddRunwayFor() {
    assertAll(
        () -> assertTrue(tad.addRunwayFor(TestObjects.rwy1129).isPresent(), "Should find add runway for rwy1129"),
        () -> assertEquals("IDK1", tad.addRunwayFor(TestObjects.rwy1129).get().icaoCode()),
        () -> assertFalse(tad.addRunwayFor(TestObjects.rwyAA30079_2911).isPresent(), "No add runway for AA30079 runway")
    );
  }

  @Test
  void testIlsComponents() {
    assertAll(
        () -> assertEquals(1, tad.ilsComponents("FAKE1", "11").size(), "FAKE1 rwy 11 should have 1 ILS"),
        () -> assertTrue(tad.ilsComponents("FAKE1", "29").isEmpty(), "FAKE1 rwy 29 should have no ILS"),
        () -> assertEquals(1, tad.ilsComponents("FAKE2", "05").size(), "FAKE2 rwy 05 should have 1 ILS")
    );
  }

  @Test
  void testIlsComponentsForRunway() {
    assertAll(
        () -> assertEquals(1, tad.ilsComponentsForRunway(TestObjects.rwy1129).size(), "rwy1129 should have 1 ILS component"),
        () -> assertEquals(1, tad.ilsComponentsForRunway(TestObjects.rwy0523).size(), "rwy0523 should have 1 ILS component"),
        () -> assertTrue(tad.ilsComponentsForRunway(TestObjects.rwyAA30079_2911).isEmpty(), "AA30079 runway should have no ILS in test data")
    );
  }

  @Test
  void testIlsByNavaidIdentifier() {
    assertAll(
        () -> assertTrue(tad.ilsByNavaidIdentifier("FAKE1", "I234").isPresent(), "Should find ILS by navaid identifier"),
        () -> assertEquals("11", tad.ilsByNavaidIdentifier("FAKE1", "I234").get().runwayIdentifier()),
        () -> assertFalse(tad.ilsByNavaidIdentifier("FAKE1", "NOPE").isPresent(), "Nonexistent navaid identifier should be absent")
    );
  }
}