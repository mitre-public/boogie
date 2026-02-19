package org.mitre.tdp.boogie.dafif.database;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.TestObjects;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

class TestDafifFixDatabase {

  static DafifFixDatabase fdb;

  @BeforeAll
  static void setUp() {
    Collection<DafifWaypoint> waypoints = Set.of(
        TestObjects.fakeFix, TestObjects.bober, TestObjects.fakeWaypointVorCollocated,
        TestObjects.fakeAA, TestObjects.wptAdrivAA, TestObjects.wptCa400AA,
        TestObjects.wptMidvuAA, TestObjects.wptAndopAS, TestObjects.wptCodieAS);
    Collection<DafifNavaid> navaids = Set.of(TestObjects.aa, TestObjects.abr, TestObjects.fakeVOR);
    fdb = DafifDatabaseFactory.newFixDatabase(waypoints, navaids);
  }

  @Test
  void testWaypointLookup() {
    assertAll(
        () -> assertTrue(fdb.waypoint("BOBER", "UI").isPresent(), "BOBER/UI should be present"),
        () -> assertTrue(fdb.waypoint("FIX01", "UI").isPresent(), "FIX01/UI should be present"),
        () -> assertTrue(fdb.waypoint("ADRIV", "AA").isPresent(), "ADRIV/AA should be present"),
        () -> assertTrue(fdb.waypoint("CODIE", "AS").isPresent(), "CODIE/AS should be present"),
        () -> assertFalse(fdb.waypoint("NOPE", "XX").isPresent(), "Nonexistent waypoint should be absent"),
        () -> assertFalse(fdb.waypoint("BOBER", "XX").isPresent(), "Wrong country code should not match")
    );
  }

  @Test
  void testNavaidLookup() {
    assertAll(
        () -> assertTrue(fdb.navaid("AA", "UI", 5, 0).isPresent(), "AA navaid should be present"),
        () -> assertEquals(47.009053, fdb.navaid("AA", "UI", 5, 0).get().degreesLatitude().orElseThrow()),
        () -> assertTrue(fdb.navaid("FVR", "UI", 4, 0).isPresent(), "FVR navaid should be present"),
        () -> assertFalse(fdb.navaid("AA", "UI", 4, 0).isPresent(), "Wrong type should not match"),
        () -> assertFalse(fdb.navaid("AA", "XX", 5, 0).isPresent(), "Wrong country should not match"),
        () -> assertFalse(fdb.navaid("NOPE", "UI", 5, 0).isPresent(), "Nonexistent navaid should be absent")
    );
  }

  @Test
  void testNavaidForWaypoint() {
    assertAll(
        () -> assertTrue(fdb.navaidFor(TestObjects.fakeAA).isPresent(), "fakeAA has navaid flag true, should resolve"),
        () -> assertEquals("AA", fdb.navaidFor(TestObjects.fakeAA).get().navaidIdentifier()),
        () -> assertFalse(fdb.navaidFor(TestObjects.bober).isPresent(), "bober has navaid flag false, should not resolve"),
        () -> assertFalse(fdb.navaidFor(TestObjects.fakeFix).isPresent(), "fakeFix has navaid flag false, should not resolve")
    );
  }
}