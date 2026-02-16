package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.dafif.TestObjects;
import org.mitre.tdp.boogie.dafif.database.DafifDatabaseFactory;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;
import org.mitre.tdp.boogie.dafif.utils.DafifMagVars;

public class FixAssemblerTest {
  static FixAssembler<Fix> assembler;

  @BeforeAll
  static void setUp() {
    Collection<DafifAirport> airports = Set.of(TestObjects.fakeAirport, TestObjects.fakeAirport2, TestObjects.fakeAirport3);
    Collection<DafifRunway> runways = Set.of(TestObjects.rwy1129, TestObjects.rwy0523, TestObjects.rwy0918);
    Collection<DafifAddRunway> addRunways = Set.of(TestObjects.addRunway, TestObjects.addRunway0918, TestObjects.addRunway0523);
    Collection<DafifIls> ils = Set.of(TestObjects.fakeIls, TestObjects.fakeIls05, TestObjects.fakeIls09);
    DafifTerminalAreaDatabase tad = DafifDatabaseFactory.newTerminalAreaDatabase(airports, runways, addRunways, ils, List.of());

    Collection<DafifWaypoint> waypoints = Set.of(TestObjects.fakeFix, TestObjects.bober, TestObjects.fakeWaypointVorCollocated, TestObjects.fakeAA);
    Collection<DafifNavaid> navaids = Set.of(TestObjects.aa, TestObjects.abr);
    DafifFixDatabase fdb = DafifDatabaseFactory.newFixDatabase(waypoints, navaids);
    assembler = FixAssembler.standard(tad, fdb);
  }

  @Test
  void testFakeNavaid() {
    Fix realFixNotNavaid = assembler.assemble(TestObjects.fakeAA).stream().findFirst().orElseThrow(AssertionError::new);
    assertAll(
        () -> assertEquals("AA", realFixNotNavaid.fixIdentifier()),
        () -> assertEquals(TestObjects.aa.degreesLatitude().orElseThrow(), realFixNotNavaid.latitude()),
        () -> assertEquals(TestObjects.aa.degreesLongitude().orElseThrow(), realFixNotNavaid.longitude())
    );
  }

  @Test
  void testsRegular() {
    Fix regular = assembler.assemble(TestObjects.bober).stream().findFirst().orElseThrow(AssertionError::new);
    assertAll(
        () -> assertEquals("BOBER", regular.fixIdentifier()),
        () -> assertEquals(TestObjects.bober.degreesLatitude().orElseThrow(), regular.latitude()),
        () -> assertEquals(TestObjects.bober.degreesLongitude().orElseThrow(), regular.longitude())
    );
  }

  @Test
  void testsNavaid() {
    Fix navaid = assembler.assemble(TestObjects.aa).stream().findFirst().orElseThrow(AssertionError::new);
    assertAll(
        () -> assertEquals("AA", navaid.fixIdentifier()),
        () -> assertEquals(TestObjects.aa.degreesLatitude().orElseThrow(), navaid.latitude()),
        () -> assertEquals(TestObjects.aa.degreesLongitude().orElseThrow(), navaid.longitude())
    );
  }

  @Test
  void testIls() {
    Fix ils = assembler.assemble(TestObjects.fakeIls).stream().findFirst().orElseThrow(AssertionError::new);
    assertAll(
        () -> assertEquals("I234", ils.fixIdentifier()),
        () -> assertEquals(TestObjects.fakeIls.degreesLatitude().orElseThrow(), ils.latitude()),
        () -> assertEquals(TestObjects.fakeIls.degreesLongitude().orElseThrow(), ils.longitude())
    );
  }

  @Test
  void testAirport() {
    Fix airport = assembler.assemble(TestObjects.fakeAirport).stream().findFirst().orElseThrow(AssertionError::new);
    MagneticVariation mvrRecord = DafifMagVars.fromRecord(TestObjects.fakeAirport.magVarOfRecord().orElseThrow(AssertionError::new));
    assertAll(
        () -> assertEquals("IDK1", airport.fixIdentifier()),
        () -> assertEquals(TestObjects.fakeAirport.degreesLatitude().orElseThrow(), airport.latitude()),
        () -> assertEquals(TestObjects.fakeAirport.degreesLongitude().orElseThrow(), airport.longitude()),
        () -> assertEquals(mvrRecord, airport.magneticVariation().orElseThrow())
    );
  }

  @Test
  void testRunway() {
    Fix runway = assembler.assemble(TestObjects.rwy1129).stream().min(Comparator.comparing(Fix::fixIdentifier)).orElseThrow(AssertionError::new);
    assertAll(
        () -> assertEquals("11", runway.fixIdentifier()),
        () -> assertEquals(TestObjects.addRunway.lowEndDisplacedThresholdDegreesLatitude().orElseThrow(), runway.latitude()),
        () -> assertEquals(TestObjects.addRunway.lowEndDisplacedThresholdDegreesLongitude().orElseThrow(), runway.longitude())
    );
  }
}
