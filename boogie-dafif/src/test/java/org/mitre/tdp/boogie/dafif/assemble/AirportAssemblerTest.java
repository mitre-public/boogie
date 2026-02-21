package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Comparator;
import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.dafif.TestObjects;
import org.mitre.tdp.boogie.dafif.database.DafifDatabaseFactory;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;

public class AirportAssemblerTest {
  static AirportAssembler<Airport> assembler;

  @BeforeAll
  static void setUp() {
    Collection<DafifAirport> airports = Set.of(TestObjects.fakeAirport, TestObjects.fakeAirport2, TestObjects.fakeAirport3);
    Collection<DafifRunway> runways = Set.of(TestObjects.rwy1129, TestObjects.rwy0523, TestObjects.rwy0918);
    Collection<DafifAddRunway> addRunways = Set.of(TestObjects.addRunway, TestObjects.addRunway0918, TestObjects.addRunway0523);
    Collection<DafifIls> ils = Set.of(TestObjects.fakeIls, TestObjects.fakeIls05, TestObjects.fakeIls09);
    DafifTerminalAreaDatabase tad = DafifDatabaseFactory.newTerminalAreaDatabase(airports, runways, addRunways, ils, List.of());
    assembler = AirportAssembler.standard(tad);
  }

  @Test
  void test() {
    Airport airport = assembler.assemble(TestObjects.fakeAirport);

    assertAll(
        () -> assertEquals(2, airport.runways().size()),
        () -> assertEquals(12.35, airport.latitude()),
        () -> assertEquals(23.34, airport.longitude()),
        () -> assertEquals("FAKE1", airport.airportIdentifier()),
        () -> assertEquals(2, airport.magneticVariation().orElseThrow().angle().inDegrees()),
        () -> assertEquals("11", airport.runways().stream().sorted(Comparator.comparing(Runway::runwayIdentifier)).findFirst().get().runwayIdentifier())
    );
  }
}
