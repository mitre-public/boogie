package org.mitre.tdp.boogie.arinc.database;

import static org.junit.jupiter.api.Assertions.*;

import java.io.File;
import java.util.Optional;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.model.*;
import org.mitre.tdp.boogie.arinc.v18.*;
import org.mitre.tdp.boogie.arinc.v19.ProcedureLegSpec;

class TestTerminalAreaDatabase {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/arinc-kjfk_yssy-v18.txt"));

  private static TerminalAreaDatabase terminalAreaDatabase;

  @BeforeAll
  static void setup() {
    fileParser.apply(arincTestFile).forEach(testV18Consumer);

    terminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        testV18Consumer.arincAirports(),
        testV18Consumer.arincRunways(),
        testV18Consumer.arincLocalizerGlideSlopes(),
        testV18Consumer.arincNdbNavaids(),
        testV18Consumer.arincVhfNavaids(),
        testV18Consumer.arincWaypoints(),
        testV18Consumer.arincProcedureLegs(),
        testV18Consumer.arincGnssLandingSystems()
    );
  }

  @Test
  void testAirportQueries() {
    assertAll(
        () -> assertEquals(Optional.of("KJFK"), terminalAreaDatabase.airport("KJFK").map(ArincAirport::airportIdentifier)),
        () -> assertEquals(Optional.of("KJFK"), terminalAreaDatabase.airport("KJFK", "K6").map(ArincAirport::airportIdentifier))
    );
  }

  @Test
  void testGls() {
    assertAll(
        () -> assertEquals("G25A", terminalAreaDatabase.gnssLandingSystemAt("YSSY", "G25A").map(ArincGnssLandingSystem::glsRefPathIdentifier).orElseThrow()),
        () -> assertEquals("G16E", terminalAreaDatabase.gnssLandingSystemAt("YSSY", "G16E").map(ArincGnssLandingSystem::glsRefPathIdentifier).orElseThrow()),
        () -> assertTrue(terminalAreaDatabase.allProcedureLegsAt("YSSY").stream().filter(i -> i.recommendedNavaidIdentifier().isPresent()).anyMatch(i -> i.recommendedNavaidIdentifier().get().equals("G25A"))),
        () -> assertEquals(7, terminalAreaDatabase.gnssLandingSystemsAt("YSSY").size())
    );
  }

  @Test
  void testRunwayQueries() {
    assertAll(
        () -> assertEquals(Optional.of("RW13R"), terminalAreaDatabase.runwayAt("KJFK", "RW13R").map(ArincRunway::runwayIdentifier)),
        () -> assertEquals(Optional.of("RW22L"), terminalAreaDatabase.runwayAt("KJFK", "RW22L").map(ArincRunway::runwayIdentifier)),
        () -> assertEquals(Optional.of("RW22R"), terminalAreaDatabase.runwayAt("KJFK", "RW22R").map(ArincRunway::runwayIdentifier)),
        () -> assertEquals(Optional.of("RW22R"), terminalAreaDatabase.runwayAt("KJFK", "K6", "RW22R").map(ArincRunway::runwayIdentifier)),
        () -> assertEquals(8, terminalAreaDatabase.runwaysAt("KJFK").size()),
        () -> assertEquals(8, terminalAreaDatabase.runwaysAt("KJFK", "K6").size())
    );
  }

  @Test
  void testLocalizerGlideSlopeQueries() {
    assertAll(
        () -> assertEquals(Optional.of("IIWY"), terminalAreaDatabase.primaryLocalizerGlideSlopeOf("KJFK", "RW22L").map(ArincLocalizerGlideSlope::localizerIdentifier)),
        () -> assertEquals(Optional.of("IIWY"), terminalAreaDatabase.primaryLocalizerGlideSlopeOf("KJFK", "K6", "RW22L").map(ArincLocalizerGlideSlope::localizerIdentifier)),
        () -> assertEquals(Optional.empty(), terminalAreaDatabase.secondaryLocalizerGlideSlopeOf("KJFK", "RW22L")),

        () -> assertEquals(Optional.of("IJOC"), terminalAreaDatabase.primaryLocalizerGlideSlopeOf("KJFK", "RW22R").map(ArincLocalizerGlideSlope::localizerIdentifier)),
        () -> assertEquals(Optional.of("IJOC"), terminalAreaDatabase.primaryLocalizerGlideSlopeOf("KJFK", "K6", "RW22R").map(ArincLocalizerGlideSlope::localizerIdentifier)),
        () -> assertEquals(Optional.empty(), terminalAreaDatabase.primaryLocalizerGlideSlopeOf("KJFK", "K4", "RW22R")),
        () -> assertEquals(Optional.empty(), terminalAreaDatabase.secondaryLocalizerGlideSlopeOf("KJFK", "RW22R"))
    );
  }

  @Test
  void testWaypointQueries() {
    assertAll(
        () -> assertEquals(70, terminalAreaDatabase.waypointsAt("KJFK").size()),
        () -> assertEquals(Optional.of("AROKE"), terminalAreaDatabase.waypointAt("KJFK", "AROKE").map(ArincWaypoint::waypointIdentifier)),
        () -> assertEquals(Optional.of("AROKE"), terminalAreaDatabase.waypointAt("KJFK", "K6", "AROKE").map(ArincWaypoint::waypointIdentifier)),
        () -> assertEquals(Optional.empty(), terminalAreaDatabase.waypointAt("KJFK", "K4", "AROKE")),
        () -> assertEquals(Optional.empty(), terminalAreaDatabase.waypointAt("KJFK", "AROME"))
    );
  }

  @Test
  void testProcedureQueries() {
    assertAll(
        () -> assertEquals(454, terminalAreaDatabase.allProcedureLegsAt("KJFK").size()),
        () -> assertEquals(11, terminalAreaDatabase.legsForProcedure("KJFK", "ROBER2").size())
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
      new GnssLandingSystemSpec(),
      // the V19 leg spec - thanks CIFP
      new ProcedureLegSpec(),
      new RunwaySpec(),
      new VhfNavaidSpec(),
      new WaypointSpec()
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
      .gnssLandingSystemConverter(new GnssLandingSystemConverter())
      .gnssLandingSystemDelegator(new GnssLandingSystemValidator())
      .holdingPatternConverter(new HoldingPatternConverter())
      .holdingPatternDelegator(new HoldingPatternValidator())
      .build();
}
