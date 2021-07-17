package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.AirportConverter;
import org.mitre.tdp.boogie.arinc.v18.AirportSpec;
import org.mitre.tdp.boogie.arinc.v18.AirportValidator;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegConverter;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegSpec;
import org.mitre.tdp.boogie.arinc.v18.AirwayLegValidator;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeConverter;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeSpec;
import org.mitre.tdp.boogie.arinc.v18.LocalizerGlideSlopeValidator;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidConverter;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidSpec;
import org.mitre.tdp.boogie.arinc.v18.NdbNavaidValidator;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegConverter;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegSpec;
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

class TestConvertingArincRecordConsumer {

  /**
   * In implementation this could be done from {@link ArincVersion} - e.g. new ArincFileParser(ArincVersion.V18.parser());
   */
  private static final ArincFileParser fileParser = new ArincFileParser(
      new ArincRecordParser(
          new AirportSpec(),
          new AirwayLegSpec(),
          new LocalizerGlideSlopeSpec(),
          new NdbNavaidSpec(),
          new ProcedureLegSpec(),
          new RunwaySpec(),
          new VhfNavaidSpec(),
          new WaypointSpec()
      ));

  /**
   * In implementation this could be done from the factory class {@link ConvertingArincRecordConsumerFactory}.
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
      .build();

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/arinc-kjfk-v18.txt"));

  @BeforeAll
  static void setup() {
    fileParser.apply(arincTestFile).forEach(testV18Consumer);
  }

  @Test
  void testConvertingArincRecordConsumer_V18() {
    assertAll(
        () -> assertEquals(0, testV18Consumer.arincAirports().size()),
        () -> assertEquals(0, testV18Consumer.arincAirwayLegs().size()),
        () -> assertEquals(0, testV18Consumer.arincLocalizerGlideSlopes().size()),
        () -> assertEquals(0, testV18Consumer.arincNdbNavaids().size()),
        () -> assertEquals(0, testV18Consumer.arincProcedureLegs().size()),
        () -> assertEquals(0, testV18Consumer.arincRunways().size()),
        () -> assertEquals(0, testV18Consumer.arincVhfNavaids().size()),
        () -> assertEquals(0, testV18Consumer.arincWaypoints().size())
    );
  }
}
