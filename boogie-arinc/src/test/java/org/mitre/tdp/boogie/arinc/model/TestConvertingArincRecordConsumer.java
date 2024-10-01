package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.*;
import org.mitre.tdp.boogie.arinc.v19.ProcedureLegSpec;

class TestConvertingArincRecordConsumer {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/arinc-kjfk-v18.txt"));

  @BeforeAll
  static void setup() {
    fileParser.apply(arincTestFile).forEach(testV18Consumer);
  }

  @Test
  void testConvertingArincRecordConsumer_V18() {
    assertAll(
        () -> assertEquals(1, testV18Consumer.arincAirports().size(), "Airport count"),
        () -> assertEquals(0, testV18Consumer.arincAirwayLegs().size(), "AirwayLeg count"),
        () -> assertEquals(7, testV18Consumer.arincLocalizerGlideSlopes().size(), "LocalizerGlideSlope count"),
        () -> assertEquals(0, testV18Consumer.arincNdbNavaids().size(), "NdbNavaid count"),
        () -> assertEquals(454, testV18Consumer.arincProcedureLegs().size(), "ProcedureLeg count"),
        () -> assertEquals(8, testV18Consumer.arincRunways().size(), "Runway count"),
        () -> assertEquals(6, testV18Consumer.arincVhfNavaids().size(), "VhfNavaid count"),
        () -> assertEquals(70, testV18Consumer.arincWaypoints().size(), "Waypoint count")
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
      .firUirConverter(new ArincFirUirLegConverter())
      .firUirDelegator(new ArincFirUirLegValidator())
      .build();
}
