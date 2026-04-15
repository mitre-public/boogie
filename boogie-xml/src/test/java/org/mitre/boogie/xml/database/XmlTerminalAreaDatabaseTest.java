package org.mitre.boogie.xml.database;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;

class XmlTerminalAreaDatabaseTest {

  private static final LatLong POSITION = LatLong.of(42.2124, -83.3534);
  private static final MagneticVariation MAG_VAR = MagneticVariation.ofDegrees(-5.0);

  @Test
  void airportPage_exactLookupByIdentifierAndIcaoCode() {
    PortPage<Fix> page = testPage("KATL", "K6");

    XmlTerminalAreaDatabase<Fix> db = XmlTerminalAreaDatabase.<Fix>builder()
        .withAirportPage(page)
        .build();

    assertAll(
        () -> assertTrue(db.airportPage("KATL", "K6").isPresent()),
        () -> assertEquals("KATL", db.airportPage("KATL", "K6").orElseThrow().identifier()),
        () -> assertTrue(db.airportPage("KATL", "XX").isEmpty(), "wrong ICAO code"),
        () -> assertTrue(db.airportPage("KJFK", "K6").isEmpty(), "wrong identifier")
    );
  }

  @Test
  void airportPages_byIdentifierReturnsAllIcaoVariants() {
    PortPage<Fix> pageK6 = testPage("KATL", "K6");
    PortPage<Fix> pageK7 = testPage("KATL", "K7");

    XmlTerminalAreaDatabase<Fix> db = XmlTerminalAreaDatabase.<Fix>builder()
        .withAirportPage(pageK6)
        .withAirportPage(pageK7)
        .build();

    Collection<PortPage<Fix>> pages = db.airportPages("KATL");

    assertAll(
        () -> assertEquals(2, pages.size()),
        () -> assertTrue(db.airportPage("KATL", "K6").isPresent()),
        () -> assertTrue(db.airportPage("KATL", "K7").isPresent())
    );
  }

  @Test
  void airportPages_byIdentifierReturnsEmptyForMissing() {
    XmlTerminalAreaDatabase<Fix> db = XmlTerminalAreaDatabase.<Fix>builder().build();

    assertTrue(db.airportPages("KATL").isEmpty());
  }

  @Test
  void airportPages_allReturnsAllPages() {
    XmlTerminalAreaDatabase<Fix> db = XmlTerminalAreaDatabase.<Fix>builder()
        .withAirportPage(testPage("KATL", "K6"))
        .withAirportPage(testPage("KJFK", "K6"))
        .build();

    assertEquals(2, db.airportPages().size());
  }

  @Test
  void heliportPage_exactLookupByIdentifierAndIcaoCode() {
    PortPage<Fix> page = testPage("2FL7", "K6");

    XmlTerminalAreaDatabase<Fix> db = XmlTerminalAreaDatabase.<Fix>builder()
        .withHeliportPage(page)
        .build();

    assertAll(
        () -> assertTrue(db.heliportPage("2FL7", "K6").isPresent()),
        () -> assertEquals("2FL7", db.heliportPage("2FL7", "K6").orElseThrow().identifier()),
        () -> assertTrue(db.heliportPage("2FL7", "XX").isEmpty()),
        () -> assertTrue(db.heliportPage("XXXX", "K6").isEmpty())
    );
  }

  @Test
  void heliportPages_byIdentifierReturnsAllIcaoVariants() {
    PortPage<Fix> pageK6 = testPage("2FL7", "K6");
    PortPage<Fix> pageK7 = testPage("2FL7", "K7");

    XmlTerminalAreaDatabase<Fix> db = XmlTerminalAreaDatabase.<Fix>builder()
        .withHeliportPage(pageK6)
        .withHeliportPage(pageK7)
        .build();

    assertEquals(2, db.heliportPages("2FL7").size());
  }

  @Test
  void heliportPages_allReturnsAllPages() {
    XmlTerminalAreaDatabase<Fix> db = XmlTerminalAreaDatabase.<Fix>builder()
        .withHeliportPage(testPage("2FL7", "K6"))
        .withHeliportPage(testPage("3FL8", "K6"))
        .build();

    assertEquals(2, db.heliportPages().size());
  }

  @Test
  void emptyDatabaseReturnsEmptyCollections() {
    XmlTerminalAreaDatabase<Fix> db = XmlTerminalAreaDatabase.<Fix>builder().build();

    assertAll(
        () -> assertTrue(db.airportPages().isEmpty()),
        () -> assertTrue(db.heliportPages().isEmpty()),
        () -> assertTrue(db.airportPage("KATL", "K6").isEmpty()),
        () -> assertTrue(db.heliportPage("2FL7", "K6").isEmpty())
    );
  }

  @Test
  void airportAndHeliportNamespacesAreIndependent() {
    PortPage<Fix> airport = testPage("KATL", "K6");
    PortPage<Fix> heliport = testPage("KATL", "K6");

    XmlTerminalAreaDatabase<Fix> db = XmlTerminalAreaDatabase.<Fix>builder()
        .withAirportPage(airport)
        .withHeliportPage(heliport)
        .build();

    assertAll(
        () -> assertTrue(db.airportPage("KATL", "K6").isPresent()),
        () -> assertTrue(db.heliportPage("KATL", "K6").isPresent()),
        () -> assertEquals(1, db.airportPages().size()),
        () -> assertEquals(1, db.heliportPages().size())
    );
  }

  @Test
  void sameIdentifierDifferentIcaoCodesAreDistinct() {
    PortPage<Fix> page1 = PortPage.<Fix>builder()
        .referencePoint(testFix("SAME"))
        .identifier("SAME")
        .icaoCode("K6")
        .addRunway("RW09L", testFix("RW09L"))
        .build();

    PortPage<Fix> page2 = PortPage.<Fix>builder()
        .referencePoint(testFix("SAME"))
        .identifier("SAME")
        .icaoCode("EG")
        .addRunway("RW27R", testFix("RW27R"))
        .build();

    XmlTerminalAreaDatabase<Fix> db = XmlTerminalAreaDatabase.<Fix>builder()
        .withAirportPage(page1)
        .withAirportPage(page2)
        .build();

    assertAll(
        () -> assertEquals(2, db.airportPages("SAME").size()),
        () -> assertTrue(db.airportPage("SAME", "K6").orElseThrow().runway("RW09L").isPresent()),
        () -> assertTrue(db.airportPage("SAME", "EG").orElseThrow().runway("RW27R").isPresent()),
        () -> assertTrue(db.airportPage("SAME", "K6").orElseThrow().runway("RW27R").isEmpty()),
        () -> assertTrue(db.airportPage("SAME", "EG").orElseThrow().runway("RW09L").isEmpty())
    );
  }

  private static PortPage<Fix> testPage(String identifier, String icaoCode) {
    return PortPage.<Fix>builder()
        .referencePoint(testFix(identifier))
        .identifier(identifier)
        .icaoCode(icaoCode)
        .build();
  }

  private static Fix testFix(String identifier) {
    return Fix.builder()
        .fixIdentifier(identifier)
        .latLong(POSITION)
        .magneticVariation(MAG_VAR)
        .build();
  }
}
