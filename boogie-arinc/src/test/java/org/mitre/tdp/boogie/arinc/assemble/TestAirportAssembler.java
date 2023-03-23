package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.io.File;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.arinc.ArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.ContinuationRecordFilter;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.TerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;
import org.mitre.tdp.boogie.arinc.v18.*;
import org.mitre.tdp.boogie.arinc.v19.ProcedureLegSpec;
import org.mitre.tdp.boogie.model.BoogieAirport;
import org.mitre.tdp.boogie.model.BoogieRunway;

class TestAirportAssembler {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kjfk-and-friends.txt"));

  private static TerminalAreaDatabase terminalAreaDatabase;

  private static AirportAssembler<BoogieAirport, BoogieRunway> assembler;

  @BeforeAll
  static void setup() {
    ContinuationRecordFilter continuationRecordFilter = new ContinuationRecordFilter();
    fileParser.apply(arincTestFile).stream().filter(continuationRecordFilter).forEach(testV18Consumer);

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

    assembler = ArincToBoogieConverterFactory.newAirportAssembler(terminalAreaDatabase);
  }

  @Test
  void testKjfkAssembly() {
    BoogieAirport airport = assembler.apply(terminalAreaDatabase.airport("KJFK").orElseThrow(AssertionError::new));

    Map<String, Runway> runways = airport.runways().stream().collect(Collectors.toMap(Runway::runwayIdentifier, Function.identity()));

    assertAll(
        () -> assertEquals("KJFK", airport.airportIdentifier()),
        () -> assertEquals("K6", airport.airportRegion()),
        () -> assertEquals(-13., airport.publishedVariation().orElseThrow(AssertionError::new)),
        () -> assertEquals(13., airport.elevation().orElseThrow(AssertionError::new)),
        () -> assertEquals(8, airport.runways().size(), "Expected KJFK to have 8 runways."),
        () -> assertTrue(airport.runways().stream().allMatch(runway -> runway.departureRunwayEnd().isPresent()), "All runways should have departure ends."),

        () -> assertEquals("RW13R", runways.get("RW13R").runwayIdentifier()),
        () -> assertEquals(200., runways.get("RW13R").width().orElseThrow(AssertionError::new), "RW13R width"),
        () -> assertEquals(14511., runways.get("RW13R").length().orElseThrow(AssertionError::new), "RW13R length"),
        () -> assertEquals(121., runways.get("RW13R").trueCourse().orElseThrow(AssertionError::new), "RW13R true course"),

        () -> assertEquals("RW31L", runways.get("RW31L").runwayIdentifier()),
        () -> assertEquals(200., runways.get("RW31L").width().orElseThrow(AssertionError::new), "RW31L width"),
        () -> assertEquals(14511., runways.get("RW31L").length().orElseThrow(AssertionError::new), "RW31L length"),
        () -> assertEquals(301., runways.get("RW31L").trueCourse().orElseThrow(AssertionError::new), "RW31L true course")
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
