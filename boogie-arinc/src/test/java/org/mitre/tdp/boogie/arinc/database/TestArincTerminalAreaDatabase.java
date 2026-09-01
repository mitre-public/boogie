package org.mitre.tdp.boogie.arinc.database;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.TestArincFileParser;
import org.mitre.tdp.boogie.arinc.model.*;
import org.mitre.tdp.boogie.arinc.v18.*;
import org.mitre.tdp.boogie.arinc.v19.ProcedureLegSpec;
import org.mitre.tdp.boogie.arinc.v21.HelipadConverter;
import org.mitre.tdp.boogie.arinc.v21.HelipadSpec;

import java.io.File;
import java.util.Collection;
import java.util.Map;
import java.util.Optional;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class TestArincTerminalAreaDatabase {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/arinc-kjfk_yssy-v18.txt"));
  private static final File withHelipad = new File(System.getProperty("user.dir").concat("/src/test/resources/02nh-helipads.txt"));
  private static final File withLoc = new File(System.getProperty("user.dir").concat("/src/test/resources/loc.txt"));

  private static final File heliports = new File(System.getProperty("user.dir").concat("/src/test/resources/kjra_9vak5-and-friends"));

  private static ArincTerminalAreaDatabase arincTerminalAreaDatabase;
  private static ArincTerminalAreaDatabase arincTerminalAreaDatabase2;
  private static ArincTerminalAreaDatabase oneShotTerminalAreaDatabase;
  private static ArincTerminalAreaDatabase oneShotTerminalAreaDatabase2;

  @BeforeAll
  static void setup() {
    ConvertingArincRecordConsumer v18Consumer = newV18Consumer();
    fileParser.parseAll(arincTestFile).forEach(v18Consumer);
    fileParser.parseAll(withHelipad).forEach(v18Consumer); //this only works because the default consumer has a helipad implementation
    fileParser.parseAll(withLoc).forEach(v18Consumer);
    ConvertedArincRecords testV18Records = v18Consumer.snapshot();

    ConvertingArincRecordConsumer v22Consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V22);
    fileParser2.parseAll(heliports).forEach(v22Consumer);
    ConvertedArincRecords testV22Records = v22Consumer.snapshot();

    arincTerminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        testV18Records.arincAirports(),
        testV18Records.arincRunways(),
        testV18Records.arincLocalizerGlideSlopes(),
        testV18Records.arincNdbNavaids(),
        testV18Records.arincVhfNavaids(),
        testV18Records.arincWaypoints(),
        testV18Records.arincProcedureLegs(),
        testV18Records.arincGnssLandingSystems(),
        testV18Records.arincHelipads(),
        testV18Records.arincHeliports()
    );

    arincTerminalAreaDatabase2 = ArincDatabaseFactory.newTerminalAreaDatabase(
        testV22Records.arincAirports(),
        testV22Records.arincRunways(),
        testV22Records.arincLocalizerGlideSlopes(),
        testV22Records.arincNdbNavaids(),
        testV22Records.arincVhfNavaids(),
        testV22Records.arincWaypoints(),
        testV22Records.arincProcedureLegs(),
        testV22Records.arincGnssLandingSystems(),
        testV22Records.arincHelipads(),
        testV22Records.arincHeliports()
    );

    oneShotTerminalAreaDatabase = ArincDatabaseFactory.newOneShotTerminalAreaDatabase(
        testV18Records.arincAirports(),
        testV18Records.arincRunways(),
        testV18Records.arincLocalizerGlideSlopes(),
        testV18Records.arincWaypoints(),
        testV18Records.arincGnssLandingSystems(),
        testV18Records.arincHelipads(),
        testV18Records.arincHeliports()
    );

    oneShotTerminalAreaDatabase2 = ArincDatabaseFactory.newOneShotTerminalAreaDatabase(
        testV22Records.arincAirports(),
        testV22Records.arincRunways(),
        testV22Records.arincLocalizerGlideSlopes(),
        testV22Records.arincWaypoints(),
        testV22Records.arincGnssLandingSystems(),
        testV22Records.arincHelipads(),
        testV22Records.arincHeliports()
    );
  }

  @Test
  void oneShotDatabaseKeepsAssemblyLookupsWithoutProcedureSupportingIndices() {
    ArincHeliport heliport = oneShotTerminalAreaDatabase2.heliport("KJRA").orElseThrow();

    assertAll(
        () -> assertTrue(oneShotTerminalAreaDatabase.airport("KJFK", "K6").isPresent()),
        () -> assertTrue(oneShotTerminalAreaDatabase.runwayAt("KJFK", "RW22L").isPresent(), "Procedure dereferencing uses the identifier-only runway lookup."),
        () -> assertTrue(oneShotTerminalAreaDatabase.primaryLocalizerGlideSlopeOf("KJFK", "K6", "RW22L").isPresent()),
        () -> assertTrue(oneShotTerminalAreaDatabase.localizerGlideSlopeAt("KJFK", "IIWY").isPresent()),
        () -> assertTrue(oneShotTerminalAreaDatabase.waypointAt("KJFK", "K6", "AROKE").isPresent()),
        () -> assertTrue(oneShotTerminalAreaDatabase.gnssLandingSystemAt("YSSY", "G25A").isPresent()),
        () -> assertTrue(oneShotTerminalAreaDatabase.helipadAt("02NH", "H1").isPresent()),
        () -> assertEquals(2, oneShotTerminalAreaDatabase2.heliportsWaypointsAt("KJRA").size()),
        () -> assertFalse(oneShotTerminalAreaDatabase2.heliportsHelipadsAt(heliport.heliportIdentifier(), heliport.heliportIcaoRegion()).isEmpty()),
        () -> assertTrue(oneShotTerminalAreaDatabase.allProcedureLegsAt("KJFK").isEmpty(), "OneShot assembles its procedure-leg collection directly."),
        () -> assertTrue(oneShotTerminalAreaDatabase.legsForProcedure("KJFK", "ROBER2").isEmpty()),
        () -> assertTrue(oneShotTerminalAreaDatabase.ndbNavaidsAt("KJFK", "K6").isEmpty(), "OneShot resolves NDBs through its fix database."),
        () -> assertEquals(454, arincTerminalAreaDatabase.allProcedureLegsAt("KJFK").size(), "The standard factory still indexes procedure legs.")
    );
  }

  @Test
  void heliports() {
    Set<ArincHeliport> ports = arincTerminalAreaDatabase2.heliports("KJRA");
    ArincHeliport heliport = ports.iterator().next();
    assertAll(
        () -> assertEquals(1, ports.size()),
        () -> assertEquals("EAST 29TH ST", heliport.heliportName().orElseThrow())
    );
  }

  @Test
  void heliportTerminalStuff() {
    Collection<ArincWaypoint> points = arincTerminalAreaDatabase2.heliportsWaypointsAt("KJRA");
    assertEquals(2, points.size());
  }


  @Test
  void ruwwaysNoLocs() {
    Map<String, ArincLocalizerGlideSlope> theTwo = arincTerminalAreaDatabase.allLocalizerGlideSlopeAt("KLYH", "K6");
    assertAll("need to make sure that rec navs work out even if the runway did not tag it",
        () -> assertEquals(2, theTwo.size(), "both made it"),
        () -> assertTrue(arincTerminalAreaDatabase.secondaryLocalizerGlideSlopeOf("KLYH", "K6", "RW04").isPresent(), "did both"),
        () -> assertTrue(arincTerminalAreaDatabase.primaryLocalizerGlideSlopeOf("KLYH", "K6", "RW04").isPresent(), "did both"),
        () -> assertNotNull(theTwo.get("ILYH")),
        () -> assertNotNull(theTwo.get("ILYZ"))
    );
  }

  @Test
  void noHelipads() {
    assertTrue(arincTerminalAreaDatabase.helipadsAt("KJFK").isEmpty());
  }

  @Test
  void helipads() {
    ArincHelipad pad = arincTerminalAreaDatabase.helipadAt("02NH", "H1").orElseThrow();
    assertEquals("H1", pad.helipadIdentifier());
  }

  @Test
  void testAirportQueries() {
    assertAll(
        () -> assertEquals(Optional.of("KJFK"), arincTerminalAreaDatabase.airport("KJFK").map(ArincAirport::airportIdentifier)),
        () -> assertEquals(Optional.of("KJFK"), arincTerminalAreaDatabase.airport("KJFK", "K6").map(ArincAirport::airportIdentifier))
    );
  }

  @Test
  void testGls() {
    assertAll(
        () -> assertEquals("G25A", arincTerminalAreaDatabase.gnssLandingSystemAt("YSSY", "G25A").map(ArincGnssLandingSystem::glsRefPathIdentifier).orElseThrow()),
        () -> assertEquals("G16E", arincTerminalAreaDatabase.gnssLandingSystemAt("YSSY", "G16E").map(ArincGnssLandingSystem::glsRefPathIdentifier).orElseThrow()),
        () -> assertTrue(arincTerminalAreaDatabase.allProcedureLegsAt("YSSY").stream().filter(i -> i.recommendedNavaidIdentifier().isPresent()).anyMatch(i -> i.recommendedNavaidIdentifier().get().equals("G25A"))),
        () -> assertEquals(7, arincTerminalAreaDatabase.gnssLandingSystemsAt("YSSY").size())
    );
  }

  @Test
  void testRunwayQueries() {
    assertAll(
        () -> assertEquals(Optional.of("RW13R"), arincTerminalAreaDatabase.runwayAt("KJFK", "RW13R").map(ArincRunway::runwayIdentifier)),
        () -> assertEquals(Optional.of("RW22L"), arincTerminalAreaDatabase.runwayAt("KJFK", "RW22L").map(ArincRunway::runwayIdentifier)),
        () -> assertEquals(Optional.of("RW22R"), arincTerminalAreaDatabase.runwayAt("KJFK", "RW22R").map(ArincRunway::runwayIdentifier)),
        () -> assertEquals(Optional.of("RW22R"), arincTerminalAreaDatabase.runwayAt("KJFK", "K6", "RW22R").map(ArincRunway::runwayIdentifier)),
        () -> assertEquals(8, arincTerminalAreaDatabase.runwaysAt("KJFK").size()),
        () -> assertEquals(8, arincTerminalAreaDatabase.runwaysAt("KJFK", "K6").size())
    );
  }

  @Test
  void testLocalizerGlideSlopeQueries() {
    assertAll(
        () -> assertEquals(Optional.of("IIWY"), arincTerminalAreaDatabase.primaryLocalizerGlideSlopeOf("KJFK", "RW22L").map(ArincLocalizerGlideSlope::localizerIdentifier)),
        () -> assertEquals(Optional.of("IIWY"), arincTerminalAreaDatabase.primaryLocalizerGlideSlopeOf("KJFK", "K6", "RW22L").map(ArincLocalizerGlideSlope::localizerIdentifier)),
        () -> assertEquals(Optional.empty(), arincTerminalAreaDatabase.secondaryLocalizerGlideSlopeOf("KJFK", "RW22L")),

        () -> assertEquals(Optional.of("IJOC"), arincTerminalAreaDatabase.primaryLocalizerGlideSlopeOf("KJFK", "RW22R").map(ArincLocalizerGlideSlope::localizerIdentifier)),
        () -> assertEquals(Optional.of("IJOC"), arincTerminalAreaDatabase.primaryLocalizerGlideSlopeOf("KJFK", "K6", "RW22R").map(ArincLocalizerGlideSlope::localizerIdentifier)),
        () -> assertEquals(Optional.empty(), arincTerminalAreaDatabase.primaryLocalizerGlideSlopeOf("KJFK", "K4", "RW22R")),
        () -> assertEquals(Optional.empty(), arincTerminalAreaDatabase.secondaryLocalizerGlideSlopeOf("KJFK", "RW22R"))
    );
  }

  @Test
  void testWaypointQueries() {
    assertAll(
        () -> assertEquals(70, arincTerminalAreaDatabase.waypointsAt("KJFK").size()),
        () -> assertEquals(Optional.of("AROKE"), arincTerminalAreaDatabase.waypointAt("KJFK", "AROKE").map(ArincWaypoint::waypointIdentifier)),
        () -> assertEquals(Optional.of("AROKE"), arincTerminalAreaDatabase.waypointAt("KJFK", "K6", "AROKE").map(ArincWaypoint::waypointIdentifier)),
        () -> assertEquals(Optional.empty(), arincTerminalAreaDatabase.waypointAt("KJFK", "K4", "AROKE")),
        () -> assertEquals(Optional.empty(), arincTerminalAreaDatabase.waypointAt("KJFK", "AROME")),
        () -> assertEquals("K6", arincTerminalAreaDatabase.waypointAt("KJFK", "K6", "AROKE").orElseThrow().waypointIcaoRegion()),
        () -> assertEquals("YM", arincTerminalAreaDatabase.waypointAt("YSSY", "YM", "AROKE").orElseThrow().waypointIcaoRegion())
    );
  }

  @Test
  void testProcedureQueries() {
    assertAll(
        () -> assertEquals(454, arincTerminalAreaDatabase.allProcedureLegsAt("KJFK").size()),
        () -> assertEquals(11, arincTerminalAreaDatabase.legsForProcedure("KJFK", "ROBER2").size())
    );
  }

  private static final TestArincFileParser fileParser = new TestArincFileParser(ArincRecordParser.standard(
      new AirportSpec(),
      new AirwayLegSpec(),
      new LocalizerGlideSlopeSpec(),
      new NdbNavaidSpec(),
      new GnssLandingSystemSpec(),
      // the V19 leg spec - thanks CIFP
      new ProcedureLegSpec(),
      new RunwaySpec(),
      new VhfNavaidSpec(),
      new WaypointSpec(),
      new HelipadSpec(), //not really v18 but will be ok with the default consumer
      new HeliportSpec()
  ));
  private static final TestArincFileParser fileParser2 = new TestArincFileParser(ArincRecordParser.standard(ArincVersion.V22.specs()));

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
        .gnssLandingSystemConverter(new GnssLandingSystemConverter())
        .holdingPatternConverter(new HoldingPatternConverter())
        .firUirConverter(new FirUirLegConverter())
        .helipadConverter(new HelipadConverter()) //they need a consumer
        .arincControlledAirspaceConverter(new ControlledAirspaceLegConverter())
        .restrictiveAirspaceConverter(new RestrictiveAirspaceLegConverter())
        .headerConverter(new Header01Converter())
        .heliportConverter(new HeliportConverter())
        .build();
  }
}
