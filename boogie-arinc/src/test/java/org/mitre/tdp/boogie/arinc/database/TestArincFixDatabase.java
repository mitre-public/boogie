package org.mitre.tdp.boogie.arinc.database;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.TestArincFileParser;
import org.mitre.tdp.boogie.arinc.model.*;
import org.mitre.tdp.boogie.arinc.v18.*;
import org.mitre.tdp.boogie.arinc.v19.ProcedureLegSpec;
import org.mitre.tdp.boogie.arinc.v21.HelipadConverter;

import java.io.File;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import static org.junit.jupiter.api.Assertions.*;

class TestArincFixDatabase {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kjfk-and-friends.txt"));

  private static ArincFixDatabase arincFixDatabase;
  private static ArincFixDatabase oneShotFixDatabase;

  @BeforeAll
  static void setup() {
    ConvertingArincRecordConsumer consumer = newV18Consumer();
    fileParser.parseAll(arincTestFile).forEach(consumer);
    ConvertedArincRecords testV18Records = consumer.snapshot();

    arincFixDatabase = ArincDatabaseFactory.newFixDatabase(
        testV18Records.arincNdbNavaids(),
        testV18Records.arincVhfNavaids(),
        testV18Records.arincWaypoints(),
        testV18Records.arincAirports(),
        testV18Records.arincHoldingPatterns(),
        testV18Records.arincHeliports()
    );

    oneShotFixDatabase = ArincDatabaseFactory.newOneShotFixDatabase(
        testV18Records.arincNdbNavaids(),
        testV18Records.arincVhfNavaids(),
        testV18Records.arincWaypoints(),
        testV18Records.arincAirports(),
        testV18Records.arincHeliports()
    );
  }

  @Test
  void testOneShotDatabaseKeepsOnlyRegionQualifiedAssemblyIndices() {
    assertAll(
        () -> assertTrue(oneShotFixDatabase.airport("KJFK", "K6").isPresent(), "Airports remain available by region."),
        () -> assertTrue(oneShotFixDatabase.heliport("00NJ", "K6").isPresent(), "Heliports remain available by region."),
        () -> assertTrue(oneShotFixDatabase.vhfNavaid("LGA", "K6").isPresent(), "VHF navaids remain available by region."),
        () -> assertTrue(oneShotFixDatabase.enrouteNdbNavaid("ACE", "PA").isPresent(), "NDB navaids remain available by region."),
        () -> assertTrue(oneShotFixDatabase.enrouteWaypoint("ATENE", "CY").isPresent(), "Enroute waypoints remain available by region."),
        () -> assertTrue(oneShotFixDatabase.airport("KJFK").isEmpty(), "OneShot does not retain the identifier-only airport alias."),
        () -> assertTrue(oneShotFixDatabase.vhfNavaid("LGA").isEmpty(), "OneShot does not retain the identifier-only VHF alias."),
        () -> assertTrue(oneShotFixDatabase.enrouteWaypoint("ATENE").isEmpty(), "OneShot does not retain the identifier-only waypoint alias."),
        () -> assertTrue(oneShotFixDatabase.terminalWaypoint("AROKE", "K6").isEmpty(), "Terminal waypoints live only in OneShot's terminal-area database."),
        () -> assertTrue(arincFixDatabase.airport("KJFK").isPresent(), "The standard factory still creates identifier-only aliases."),
        () -> assertTrue(arincFixDatabase.terminalWaypoint("AROKE", "K6").isPresent(), "The standard factory still indexes terminal waypoints.")
    );
  }

  @Test
  void testHeliport() {
    ArincHeliport heliport = arincFixDatabase.heliport("KJRA").orElseThrow();
    assertEquals("KJRA", heliport.heliportIdentifier());
  }

  @Test
  void testHoldingFunctionality() {
    ArincHoldingPattern abu = arincFixDatabase.enrouteHolds("ABU", "HL").stream().findFirst().orElseThrow();
    List<ArincHoldingPattern> vegers = arincFixDatabase.enrouteHolds("VEGER", "EE").stream().sorted().toList();
    assertAll(
        () -> assertEquals("ABU", abu.fixIdentifier()),
        () -> assertEquals(90, abu.legTime().orElseThrow().getSeconds(), "Should be 90 seconds aka 1.5 min a normal hold"),
        () -> assertEquals(4.3, vegers.get(0).legLength().orElseThrow(), "Should have a length"),
        () -> assertTrue(vegers.get(1).legLength().isEmpty(), "Both are not coded, so this should be empty"),
        () -> assertEquals(1, vegers.get(1).legTime().orElseThrow().toMinutes(), "Should have a time of 1 min")
    );
  }

  @Test
  void testDatabaseWaypointFunctionality() {
    assertAll(
        "Collection of common database waypoint queries and their expected outcomes (based on real embedded data).",
        () -> assertEquals(Optional.of("AROKE"), arincFixDatabase.waypoint("AROKE").map(ArincWaypoint::waypointIdentifier), "Waypoint AROKE should be in the database."),
        () -> assertEquals(Optional.empty(), arincFixDatabase.enrouteWaypoint("AROKE"), "AROKE is a terminal waypoint - it's indexed in the database but should not be returned by this call."),
        () -> assertEquals(Optional.of("AROKE"), arincFixDatabase.terminalWaypoint("AROKE").map(ArincWaypoint::waypointIdentifier), "AROKE is a terminal waypoint - and therefore should be returned."),
        () -> assertEquals("AROKE,CAXUN", arincFixDatabase.waypoints("AROKE", "CAXUN").stream().map(ArincWaypoint::waypointIdentifier).collect(Collectors.joining(",")), "Both waypoints exist in the database and so both should be returned.")
    );
  }

  @Test
  void testDatabaseNavaidFunctionality() {
    assertAll(
        () -> assertEquals(Optional.of("LGA"), arincFixDatabase.vhfNavaid("LGA").map(ArincVhfNavaid::vhfIdentifier), "VHF LGA should be in the database."),
        () -> assertEquals(Optional.of("SIE"), arincFixDatabase.vhfNavaid("SIE").map(ArincVhfNavaid::vhfIdentifier), "VHF SIE should be in the database."),
        () -> assertEquals("LGA,SIE", arincFixDatabase.vhfNavaids("LGA", "SIE").stream().map(ArincVhfNavaid::vhfIdentifier).collect(Collectors.joining(",")))
    );
  }

  @Test
  void testDatabaseAirportFunctionality() {
    assertAll(
        "Collection of common database airport queries and their expected outcomes (based on real embedded data).",
        () -> assertEquals(Optional.of("KJFK"), arincFixDatabase.airport("KJFK").map(ArincAirport::airportIdentifier), "Airport KJFK should be in the database."),
        () -> assertEquals(Optional.empty(), arincFixDatabase.airport("KCLT"), "KCLT is not in the database and shouldn't be returned."),
        () -> assertEquals("KJFK", arincFixDatabase.airports("KJFK").stream().map(ArincAirport::airportIdentifier).collect(Collectors.joining(",")), "KJFK is the only airport in the database and should be returned.")
    );
  }

  private static final TestArincFileParser fileParser = new TestArincFileParser(ArincRecordParser.standard(
      new AirportSpec(),
      new AirwayLegSpec(),
      new LocalizerGlideSlopeSpec(),
      new NdbNavaidSpec(),
      // the V19 leg spec - thanks CIFP
      new ProcedureLegSpec(),
      new RunwaySpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new HoldingPatternSpec(),
      new HeliportSpec()
  ));

  /**
   * In implementation this could be done from the factory class {@link ArincRecordConverterFactory}.
   */
  private static ConvertingArincRecordConsumer newV18Consumer() {
    return new ConvertingArincRecordConsumer.Builder()
        .airportConverter(new AirportConverter())
        .airportContinuationConverter(new AirportPrimaryExtensionConverter())
        .airwayLegConverter(new AirwayLegConverter())
        .localizerGlideSlopeConverter(new LocalizerGlideSlopeConverter())
        .ndbNavaidConverter(new NdbNavaidConverter())
        .procedureLegConverter(new ProcedureLegConverter())
        .runwayConverter(new RunwayConverter())
        .vhfNavaidConverter(new VhfNavaidConverter())
        .waypointConverter(new WaypointConverter())
        .holdingPatternConverter(new HoldingPatternConverter())
        .gnssLandingSystemConverter(new GnssLandingSystemConverter())
        .firUirConverter(new FirUirLegConverter())
        .helipadConverter(new HelipadConverter())
        .arincControlledAirspaceConverter(new ControlledAirspaceLegConverter())
        .restrictiveAirspaceConverter(new RestrictiveAirspaceLegConverter())
        .headerConverter(new Header01Converter())
        .heliportConverter(new HeliportConverter())
        .build();
  }
}
