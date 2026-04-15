package org.mitre.boogie.xml.database;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;

class PortPageTest {

  private static final LatLong POSITION = LatLong.of(42.2124, -83.3534);
  private static final MagneticVariation MAG_VAR = MagneticVariation.ofDegrees(-5.0);

  @Test
  void lookupTerminalWaypointByIdentifier() {
    Fix wp = testFix("TWPT1");

    PortPage<Fix, Fix, Fix> page = PortPage.<Fix, Fix, Fix>builder()
        .referencePoint(testFix("KATL"))
        .identifier("KATL")
        .icaoCode("K6")
        .addTerminalWaypoint("TWPT1", wp)
        .build();

    assertAll(
        () -> assertTrue(page.terminalWaypoint("TWPT1").isPresent()),
        () -> assertEquals("TWPT1", page.terminalWaypoint("TWPT1").orElseThrow().fixIdentifier()),
        () -> assertTrue(page.terminalWaypoint("MISSING").isEmpty())
    );
  }

  @Test
  void lookupNdbNavaidByIdentifier() {
    Fix ndb = testFix("NDB1");

    PortPage<Fix, Fix, Fix> page = PortPage.<Fix, Fix, Fix>builder()
        .referencePoint(testFix("KATL"))
        .identifier("KATL")
        .icaoCode("K6")
        .addNdbNavaid("NDB1", ndb)
        .build();

    assertAll(
        () -> assertTrue(page.ndbNavaid("NDB1").isPresent()),
        () -> assertEquals("NDB1", page.ndbNavaid("NDB1").orElseThrow().fixIdentifier()),
        () -> assertTrue(page.ndbNavaid("MISSING").isEmpty())
    );
  }

  @Test
  void lookupRunwayByIdentifier() {
    Fix rwy = testFix("RW09L");

    PortPage<Fix, Fix, Fix> page = PortPage.<Fix, Fix, Fix>builder()
        .referencePoint(testFix("KATL"))
        .identifier("KATL")
        .icaoCode("K6")
        .addRunway("RW09L", rwy)
        .build();

    assertAll(
        () -> assertTrue(page.runway("RW09L").isPresent()),
        () -> assertEquals("RW09L", page.runway("RW09L").orElseThrow().fixIdentifier()),
        () -> assertTrue(page.runway("RW27R").isEmpty())
    );
  }

  @Test
  void lookupGateByIdentifier() {
    Fix gate = testFix("GATE_A1");

    PortPage<Fix, Fix, Fix> page = PortPage.<Fix, Fix, Fix>builder()
        .referencePoint(testFix("KATL"))
        .identifier("KATL")
        .icaoCode("K6")
        .addGate("GATE_A1", gate)
        .build();

    assertAll(
        () -> assertTrue(page.gate("GATE_A1").isPresent()),
        () -> assertEquals("GATE_A1", page.gate("GATE_A1").orElseThrow().fixIdentifier()),
        () -> assertTrue(page.gate("GATE_B2").isEmpty())
    );
  }

  @Test
  void lookupHelipadByIdentifier() {
    Fix hp = testFix("HP01");

    PortPage<Fix, Fix, Fix> page = PortPage.<Fix, Fix, Fix>builder()
        .referencePoint(testFix("KATL"))
        .identifier("KATL")
        .icaoCode("K6")
        .addHelipad("HP01", hp)
        .build();

    assertAll(
        () -> assertTrue(page.helipad("HP01").isPresent()),
        () -> assertEquals("HP01", page.helipad("HP01").orElseThrow().fixIdentifier()),
        () -> assertTrue(page.helipad("HP02").isEmpty())
    );
  }

  @Test
  void lookupLocalizerGlideSlopeByIdentifier() {
    Fix loc = testFix("ILS09L");

    PortPage<Fix, Fix, Fix> page = PortPage.<Fix, Fix, Fix>builder()
        .referencePoint(testFix("KATL"))
        .identifier("KATL")
        .icaoCode("K6")
        .addLocalizerGlideSlope("ILS09L", loc)
        .build();

    assertAll(
        () -> assertTrue(page.localizerGlideSlope("ILS09L").isPresent()),
        () -> assertEquals("ILS09L", page.localizerGlideSlope("ILS09L").orElseThrow().fixIdentifier()),
        () -> assertTrue(page.localizerGlideSlope("ILS27R").isEmpty())
    );
  }

  @Test
  void lookupMarkerByIdentifier() {
    Fix mkr = testFix("OM09L");

    PortPage<Fix, Fix, Fix> page = PortPage.<Fix, Fix, Fix>builder()
        .referencePoint(testFix("KATL"))
        .identifier("KATL")
        .icaoCode("K6")
        .addMarker("OM09L", mkr)
        .build();

    assertAll(
        () -> assertTrue(page.marker("OM09L").isPresent()),
        () -> assertEquals("OM09L", page.marker("OM09L").orElseThrow().fixIdentifier()),
        () -> assertTrue(page.marker("MM09L").isEmpty())
    );
  }

  @Test
  void lookupGnssLandingSystemByIdentifier() {
    Fix gls = testFix("GLS09L");

    PortPage<Fix, Fix, Fix> page = PortPage.<Fix, Fix, Fix>builder()
        .referencePoint(testFix("KATL"))
        .identifier("KATL")
        .icaoCode("K6")
        .addGnssLandingSystem("GLS09L", gls)
        .build();

    assertAll(
        () -> assertTrue(page.gnssLandingSystem("GLS09L").isPresent()),
        () -> assertEquals("GLS09L", page.gnssLandingSystem("GLS09L").orElseThrow().fixIdentifier()),
        () -> assertTrue(page.gnssLandingSystem("GLS27R").isEmpty())
    );
  }

  @Test
  void referencePointAndMetadata() {
    Fix ref = testFix("KATL");

    PortPage<Fix, Fix, Fix> page = PortPage.<Fix, Fix, Fix>builder()
        .referencePoint(ref)
        .identifier("KATL")
        .icaoCode("K6")
        .build();

    assertAll(
        () -> assertEquals("KATL", page.referencePoint().fixIdentifier()),
        () -> assertEquals("KATL", page.identifier()),
        () -> assertEquals("K6", page.icaoCode())
    );
  }

  @Test
  void collectionGettersReturnAllEntries() {
    PortPage<Fix, Fix, Fix> page = PortPage.<Fix, Fix, Fix>builder()
        .referencePoint(testFix("KATL"))
        .identifier("KATL")
        .icaoCode("K6")
        .addTerminalWaypoint("WP1", testFix("WP1"))
        .addTerminalWaypoint("WP2", testFix("WP2"))
        .addRunway("RW09L", testFix("RW09L"))
        .addRunway("RW27R", testFix("RW27R"))
        .addRunway("RW08R", testFix("RW08R"))
        .addNdbNavaid("NDB1", testFix("NDB1"))
        .build();

    assertAll(
        () -> assertEquals(2, page.terminalWaypoints().size()),
        () -> assertEquals(3, page.runways().size()),
        () -> assertEquals(1, page.ndbNavaids().size()),
        () -> assertEquals(0, page.gates().size()),
        () -> assertEquals(0, page.helipads().size()),
        () -> assertEquals(0, page.localizerGlideSlopes().size()),
        () -> assertEquals(0, page.markers().size()),
        () -> assertEquals(0, page.gnssLandingSystems().size())
    );
  }

  @Test
  void emptyPageReturnsEmptyCollections() {
    PortPage<Fix, Fix, Fix> page = PortPage.<Fix, Fix, Fix>builder()
        .referencePoint(testFix("KATL"))
        .identifier("KATL")
        .icaoCode("K6")
        .build();

    assertAll(
        () -> assertTrue(page.terminalWaypoints().isEmpty()),
        () -> assertTrue(page.ndbNavaids().isEmpty()),
        () -> assertTrue(page.runways().isEmpty()),
        () -> assertTrue(page.gates().isEmpty()),
        () -> assertTrue(page.helipads().isEmpty()),
        () -> assertTrue(page.localizerGlideSlopes().isEmpty()),
        () -> assertTrue(page.markers().isEmpty()),
        () -> assertTrue(page.gnssLandingSystems().isEmpty())
    );
  }

  private static Fix testFix(String identifier) {
    return Fix.builder()
        .fixIdentifier(identifier)
        .latLong(POSITION)
        .magneticVariation(MAG_VAR)
        .build();
  }
}
