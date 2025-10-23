package org.mitre.tdp.boogie.arinc.model;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.*;
import org.mitre.tdp.boogie.arinc.v19.ProcedureLegSpec;
import org.mitre.tdp.boogie.arinc.v21.HelipadConverter;
import org.mitre.tdp.boogie.arinc.v21.HelipadValidator;

class TestConvertingArincRecordMapper {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/arinc-kjfk-v18.txt"));

  @Test
  void testConvertingArincRecordConsumer() {
    Map<Class<?>, List<Object>> recordsByType = fileParser.apply(arincTestFile).stream().map(testV18Mapper)
        .filter(Optional::isPresent).map(Optional::get).collect(Collectors.groupingBy(Object::getClass));

    assertAll(
        () -> assertEquals(1, recordsByType.getOrDefault(ArincAirport.class, emptyList()).size(), "Airport count"),
        () -> assertEquals(0, recordsByType.getOrDefault(ArincAirwayLeg.class, emptyList()).size(), "AirwayLeg count"),
        () -> assertEquals(7, recordsByType.getOrDefault(ArincLocalizerGlideSlope.class, emptyList()).size(), "LocalizerGlideSlope count"),
        () -> assertEquals(0, recordsByType.getOrDefault(ArincNdbNavaid.class, emptyList()).size(), "NdbNavaid count"),
        () -> assertEquals(454, recordsByType.getOrDefault(ArincProcedureLeg.class, emptyList()).size(), "ProcedureLeg count"),
        () -> assertEquals(8, recordsByType.getOrDefault(ArincRunway.class, emptyList()).size(), "Runway count"),
        () -> assertEquals(6, recordsByType.getOrDefault(ArincVhfNavaid.class, emptyList()).size(), "VhfNavaid count"),
        () -> assertEquals(70, recordsByType.getOrDefault(ArincWaypoint.class, emptyList()).size(), "Waypoint count")
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
  private static final ConvertingArincRecordMapper testV18Mapper = new ConvertingArincRecordMapper.Builder()
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
      .gnssLandingSystemDelegator(new GnssLandingSystemValidator())
      .gnssLandingSystemConverter(new GnssLandingSystemConverter())
      .holdingPatternConverter(new HoldingPatternConverter())
      .holdingPatternDelegator(new HoldingPatternValidator())
      .firUirConverter(new FirUirLegConverter())
      .firUirDelegator(new FirUirLegValidator())
      .helipadConverter(new HelipadConverter())
      .helipadDelegator(new HelipadValidator())
      .controlledAirspaceConverter(new ControlledAirspaceLegConverter())
      .controlledAirspaceDelegator(new ControlledAirspaceValidator())
      .headerOneDelegator(new Header01Validator())
      .headerOneConverter(new Header01Converter())
      .build();
}
