package org.mitre.tdp.boogie.arinc.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.Collections;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Course;
import org.mitre.caasd.commons.Distance;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.arinc.ArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.IsThisAPrimaryRecord;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;
import org.mitre.tdp.boogie.arinc.v18.AirportConverter;
import org.mitre.tdp.boogie.arinc.v18.AirportPrimaryExtensionConverter;
import org.mitre.tdp.boogie.arinc.v18.AirportPrimaryExtensionValidator;
import org.mitre.tdp.boogie.arinc.v18.AirportSpec;
import org.mitre.tdp.boogie.arinc.v18.AirportValidator;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegConverter;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegValidator;
import org.mitre.tdp.boogie.arinc.v18.ControlledAirspaceLegConverter;
import org.mitre.tdp.boogie.arinc.v18.ControlledAirspaceValidator;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegConverter;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegValidator;
import org.mitre.tdp.boogie.arinc.v18.GnssLandingSystemConverter;
import org.mitre.tdp.boogie.arinc.v18.GnssLandingSystemValidator;
import org.mitre.tdp.boogie.arinc.v18.Header01Converter;
import org.mitre.tdp.boogie.arinc.v18.Header01Validator;
import org.mitre.tdp.boogie.arinc.v18.HeliportConverter;
import org.mitre.tdp.boogie.arinc.v18.HeliportValidator;
import org.mitre.tdp.boogie.arinc.v18.HoldingPatternConverter;
import org.mitre.tdp.boogie.arinc.v18.HoldingPatternValidator;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeConverter;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeSpec;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeValidator;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidConverter;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidValidator;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegConverter;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegValidator;
import org.mitre.tdp.boogie.arinc.v18.RunwayConverter;
import org.mitre.tdp.boogie.arinc.v18.RunwaySpec;
import org.mitre.tdp.boogie.arinc.v18.RunwayValidator;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidConverter;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.VhfNavaidValidator;
import org.mitre.tdp.boogie.arinc.v18.WaypointConverter;
import org.mitre.tdp.boogie.arinc.v18.WaypointSpec;
import org.mitre.tdp.boogie.arinc.v18.WaypointValidator;
import org.mitre.tdp.boogie.arinc.v19.ProcedureLegSpec;
import org.mitre.tdp.boogie.arinc.v21.HelipadConverter;
import org.mitre.tdp.boogie.arinc.v21.HelipadValidator;

class TestAirportAssembler {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kjfk-and-friends.txt"));

  private static ArincTerminalAreaDatabase arincTerminalAreaDatabase;

  private static AirportAssembler<Airport> assembler;

  @BeforeAll
  static void setup() {
    IsThisAPrimaryRecord isThisAPrimaryRecord = new IsThisAPrimaryRecord();
    fileParser.apply(arincTestFile).stream().filter(isThisAPrimaryRecord).forEach(testV18Consumer);

    arincTerminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        testV18Consumer.arincAirports(),
        testV18Consumer.arincRunways(),
        testV18Consumer.arincLocalizerGlideSlopes(),
        testV18Consumer.arincNdbNavaids(),
        testV18Consumer.arincVhfNavaids(),
        testV18Consumer.arincWaypoints(),
        testV18Consumer.arincProcedureLegs(),
        testV18Consumer.arincGnssLandingSystems(),
        Collections.emptySet(),
        testV18Consumer.arincHeliports()
    );

    assembler = AirportAssembler.standard(arincTerminalAreaDatabase);
  }

  @Test
  void testKjfkAssembly() {
    Airport airport = assembler.assemble(arincTerminalAreaDatabase.airport("KJFK").orElseThrow(AssertionError::new));

    Map<String, Runway> runways = airport.runways().stream().collect(Collectors.toMap(Runway::runwayIdentifier, Function.identity()));

    assertAll(
        () -> assertEquals("KJFK", airport.airportIdentifier()),
        () -> assertEquals(MagneticVariation.ofDegrees(-13.), airport.magneticVariation().orElseThrow(), "MagneticVariation"),
        () -> assertEquals(8, airport.runways().size(), "Expected KJFK to have 8 runways."),

        () -> assertEquals("RW13R", runways.get("RW13R").runwayIdentifier()),
        () -> assertEquals(Distance.ofFeet(14511.), runways.get("RW13R").length().orElse(null), "RW13R length"),
        () -> assertEquals(Course.ofDegrees(121.), runways.get("RW13R").course().orElse(null), "RW13R true course"),

        () -> assertEquals("RW31L", runways.get("RW31L").runwayIdentifier()),
        () -> assertEquals(Distance.ofFeet(14511.), runways.get("RW31L").length().orElse(null), "RW31L length"),
        () -> assertEquals(Course.ofDegrees(301.), runways.get("RW31L").course().orElse(null), "RW31L true course")
    );
  }

  /**
   * In implementation this could be done from {@link ArincVersion} - e.g. new ArincFileParser(ArincVersion.V19.parser());
   */
  private static final ArincFileParser fileParser = new ArincFileParser(
      new AirportSpec(),
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
      .airportContinuationConverter(new AirportPrimaryExtensionConverter())
      .airportContinuationDelegator(new AirportPrimaryExtensionValidator())
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
      .firUirConverter(new FirUirLegConverter())
      .firUirDelegator(new FirUirLegValidator())
      .helipadDelegator(new HelipadValidator())
      .helipadConverter(new HelipadConverter())
      .arincControlledAirspaceConverter(new ControlledAirspaceLegConverter())
      .arincControlledAirspaceLegDelegator(new ControlledAirspaceValidator())
      .headerDelegator(new Header01Validator())
      .headerConverter(new Header01Converter())
      .heliportDelegator(new HeliportValidator())
      .heliportConverter(new HeliportConverter())
      .build();
}
