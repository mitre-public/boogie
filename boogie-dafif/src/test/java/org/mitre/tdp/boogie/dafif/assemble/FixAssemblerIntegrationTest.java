package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.stream.Collectors;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.dafif.EmbeddedDafifFile;
import org.mitre.tdp.boogie.dafif.database.DafifDatabaseFactory;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifIls;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

@Tag("DAFIF")
@Tag("INTEGRATION")
class FixAssemblerIntegrationTest {
  private static final Logger LOG = LoggerFactory.getLogger(FixAssemblerIntegrationTest.class);

  static FixAssembler<Fix> assembler;
  static EmbeddedDafifFile dafif;

  @BeforeAll
  static void setUp() {
    dafif = EmbeddedDafifFile.instance();

    DafifTerminalAreaDatabase tad = DafifDatabaseFactory.newTerminalAreaDatabase(
        dafif.dafifAirports(), dafif.dafifRunways(), dafif.dafifAddRunways(), dafif.dafifIls(), dafif.dafifTerminalSegments());

    DafifFixDatabase fdb = DafifDatabaseFactory.newFixDatabase(dafif.dafifWaypoints(), dafif.dafifNavaids());

    assembler = FixAssembler.standard(tad, fdb);
  }

  @Test
  void testAssembleAllWaypoints() {
    assertAll(dafif.dafifWaypoints().stream()
        .<Executable>map(w -> () -> assertFalse(assembler.assemble(w).isEmpty(),
            "Waypoint " + w.waypointIdentifier() + "/" + w.countryCode() + " should produce at least one fix"))
        .collect(Collectors.toList()));
  }

  @Test
  void testAssembleAllNavaids() {
    assertAll(dafif.dafifNavaids().stream()
        .<Executable>map(n -> () -> assertFalse(assembler.assemble(n).isEmpty(),
            "Navaid " + n.navaidIdentifier() + "/" + n.countryCode() + " should produce at least one fix"))
        .collect(Collectors.toList()));
  }

  @Test
  void testAssembleAllAirports() {
    assertAll(dafif.dafifAirports().stream()
        .<Executable>map(a -> () -> assertFalse(assembler.assemble(a).isEmpty(),
            "Airport " + a.airportIdentification() + " should produce at least one fix"))
        .collect(Collectors.toList()));
  }

  @Test
  void testAssembleAllRunways() {
    assertAll(dafif.dafifRunways().stream()
        .<Executable>map(r -> () -> assertFalse(assembler.assemble(r).isEmpty(),
            "Runway " + r.lowEndIdentifier() + "/" + r.highEndIdentifier() + " at " + r.airportIdentification() + " should produce at least one fix"))
        .collect(Collectors.toList()));
  }

  @Test
  void testAssembleAllIls() {
    assertAll(dafif.dafifIls().stream()
        .<Executable>map(i -> () -> assertFalse(assembler.assemble(i).isEmpty(),
            "ILS at " + i.airportIdentification() + " rwy " + i.runwayIdentifier() + " comp " + i.componentType() + " should produce at least one fix"))
        .collect(Collectors.toList()));
  }

  @Test
  void testAllFixesHaveCoordinates() {
    Collection<Fix> waypointFixes = dafif.dafifWaypoints().stream()
        .flatMap(w -> assembler.assemble(w).stream())
        .collect(Collectors.toList());

    assertAll(waypointFixes.stream()
        .<Executable>map(f -> () -> assertTrue(
            Double.isFinite(f.latitude()) && Double.isFinite(f.longitude()),
            "Fix " + f.fixIdentifier() + " should have finite coordinates"))
        .collect(Collectors.toList()));
  }
}
