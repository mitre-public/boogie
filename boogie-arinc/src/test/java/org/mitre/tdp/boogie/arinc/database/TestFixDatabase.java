package org.mitre.tdp.boogie.arinc.database;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Collection;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.model.*;
import org.mitre.tdp.boogie.arinc.v18.*;
import org.mitre.tdp.boogie.arinc.v19.ProcedureLegSpec;

class TestFixDatabase {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kjfk-and-friends.txt"));

  private static FixDatabase fixDatabase;

  @BeforeAll
  static void setup() {
    fileParser.apply(arincTestFile).forEach(testV18Consumer);

    fixDatabase = ArincDatabaseFactory.newFixDatabase(
        testV18Consumer.arincNdbNavaids(),
        testV18Consumer.arincVhfNavaids(),
        testV18Consumer.arincWaypoints(),
        testV18Consumer.arincAirports(),
        testV18Consumer.arincHoldingPatterns()
    );
  }

  @Test
  void testHoldingFunctionality() {
    ArincHoldingPattern abu = fixDatabase.enrouteHolds("ABU", "HL").stream().findFirst().orElseThrow();
    List<ArincHoldingPattern> vegers = fixDatabase.enrouteHolds("VEGER", "EE").stream().sorted().collect(Collectors.toList());
    assertAll(
        () -> assertEquals("ABU", abu.fixIdentifier()),
        () -> assertEquals(90, abu.legTime().orElseThrow().getSeconds(), "Should be 90 seconds aka 1.5 min a normal hold"),
        () -> assertEquals(43.0, vegers.get(0).legLength().orElseThrow(), "Should have a length"),
        () -> assertTrue(vegers.get(1).legLength().isEmpty(), "Both are not coded, so this should be empty"),
        () -> assertEquals(1, vegers.get(1).legTime().orElseThrow().toMinutes(), "Should have a time of 1 min")
    );
  }

  @Test
  void testDatabaseWaypointFunctionality() {
    assertAll(
        "Collection of common database waypoint queries and their expected outcomes (based on real embedded data).",
        () -> assertEquals(Optional.of("AROKE"), fixDatabase.waypoint("AROKE").map(ArincWaypoint::waypointIdentifier), "Waypoint AROKE should be in the database."),
        () -> assertEquals(Optional.empty(), fixDatabase.enrouteWaypoint("AROKE"), "AROKE is a terminal waypoint - it's indexed in the database but should not be returned by this call."),
        () -> assertEquals(Optional.of("AROKE"), fixDatabase.terminalWaypoint("AROKE").map(ArincWaypoint::waypointIdentifier), "AROKE is a terminal waypoint - and therefore should be returned."),
        () -> assertEquals("AROKE,CAXUN", fixDatabase.waypoints("AROKE", "CAXUN").stream().map(ArincWaypoint::waypointIdentifier).collect(Collectors.joining(",")), "Both waypoints exist in the database and so both should be returned.")
    );
  }

  @Test
  void testDatabaseNavaidFunctionality() {
    assertAll(
        () -> assertEquals(Optional.of("LGA"), fixDatabase.vhfNavaid("LGA").map(ArincVhfNavaid::vhfIdentifier), "VHF LGA should be in the database."),
        () -> assertEquals(Optional.of("SIE"), fixDatabase.vhfNavaid("SIE").map(ArincVhfNavaid::vhfIdentifier), "VHF SIE should be in the database."),
        () -> assertEquals("LGA,SIE", fixDatabase.vhfNavaids("LGA", "SIE").stream().map(ArincVhfNavaid::vhfIdentifier).collect(Collectors.joining(",")))
    );
  }

  @Test
  void testDatabaseAirportFunctionality() {
    assertAll(
        "Collection of common database airport queries and their expected outcomes (based on real embedded data).",
        () -> assertEquals(Optional.of("KJFK"), fixDatabase.airport("KJFK").map(ArincAirport::airportIdentifier), "Airport KJFK should be in the database."),
        () -> assertEquals(Optional.empty(), fixDatabase.airport("KCLT"), "KCLT is not in the database and shouldn't be returned."),
        () -> assertEquals("KJFK", fixDatabase.airports("KJFK").stream().map(ArincAirport::airportIdentifier).collect(Collectors.joining(",")), "KJFK is the only airport in the database and should be returned.")
    );
  }

  /**
   * In implementation this could be done from {@link ArincVersion} - e.g. new ArincFileParser(ArincVersion.V19.parser());
   */
  private static final ArincFileParser fileParser = new ArincFileParser(
      new AirportSpec(),
      new AirwayLegSpec(),
      new LocalizerGlideSlopeSpec(),
      new NdbNavaidSpec(),
      // the V19 leg spec - thanks CIFP
      new ProcedureLegSpec(),
      new RunwaySpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new HoldingPatternSpec()
  );

  /**
   * In implementation this could be done from the factory class {@link ArincRecordConverterFactory}.
   */
  private static final ConvertingArincRecordConsumer testV18Consumer = new ConvertingArincRecordConsumer.Builder()
      .airportDelegator(new AirportValidator())
      .airportConverter(new AirportConverter())
      .airwayLegDelegator(new AirwayLegValidator())
      .airwayLegConverter(new AirwayLegConverter())
      .localizerGlideSlopeDelegator(new LocalizerGlideSlopeValidator())
      .localizerGlideSlopeConverter(new LocalizerGlideSlopeConverter())
      .ndbNavaidDelegator(new NdbNavaidValidator())
      .ndbNavaidConverter(new NdbNavaidConverter())
      .procedureLegDelegator(new ProcedureLegValidator())
      .procedureLegConverter(new ProcedureLegConverter())
      .runwayDelegator(new RunwayValidator())
      .runwayConverter(new RunwayConverter())
      .vhfNavaidDelegator(new VhfNavaidValidator())
      .vhfNavaidConverter(new VhfNavaidConverter())
      .waypointDelegator(new WaypointValidator())
      .waypointConverter(new WaypointConverter())
      .holdingPatternConverter(new HoldingPatternConverter())
      .holdingPatternDelegator(new HoldingPatternValidator())
      .gnssLandingSystemConverter(new GnssLandingSystemConverter())
      .gnssLandingSystemDelegator(new GnssLandingSystemValidator())
      .build();
}
