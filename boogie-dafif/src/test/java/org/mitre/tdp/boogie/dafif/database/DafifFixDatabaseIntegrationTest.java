package org.mitre.tdp.boogie.dafif.database;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.dafif.EmbeddedDafifFile;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

@Tag("DAFIF")
@Tag("INTEGRATION")
class DafifFixDatabaseIntegrationTest {

  static DafifFixDatabase fdb;

  @BeforeAll
  static void setUp() {
    EmbeddedDafifFile dafif = EmbeddedDafifFile.instance();
    fdb = DafifDatabaseFactory.newFixDatabase(dafif.dafifWaypoints(), dafif.dafifNavaids());
  }

  @Test
  void testWaypointLookup() {
    assertAll(
        () -> assertTrue(fdb.waypoint("ADRIV", "AA").isPresent(), "ADRIV/AA should be present"),
        () -> assertTrue(fdb.waypoint("CODIE", "AS").isPresent(), "CODIE/AS should be present"),
        () -> assertFalse(fdb.waypoint("ZZZZZ", "ZZ").isPresent(), "Nonexistent waypoint should be absent")
    );
  }

  @Test
  void testNavaidLookupByKey() {
    assertFalse(fdb.navaid("ZZZZZ", "ZZ", 1, 9999).isPresent(), "Nonexistent navaid should be absent");
  }

  @Test
  void testNavaidForWaypointWithNavaidFlag() {
    DafifWaypoint withFlag = fdb.waypoint("ADRIV", "AA").orElseThrow();
    if (withFlag.waypointPointNavaidFlag()) {
      assertTrue(fdb.navaidFor(withFlag).isPresent(), "Waypoint with navaid flag should resolve to navaid");
    } else {
      assertFalse(fdb.navaidFor(withFlag).isPresent(), "Waypoint without navaid flag should not resolve");
    }
  }

  @Test
  void testNavaidForWaypointWithoutNavaidFlag() {
    DafifWaypoint wpt = fdb.waypoint("CODIE", "AS").orElseThrow();
    assertFalse(fdb.navaidFor(wpt).isPresent(), "CODIE should not have navaid flag");
  }
}
