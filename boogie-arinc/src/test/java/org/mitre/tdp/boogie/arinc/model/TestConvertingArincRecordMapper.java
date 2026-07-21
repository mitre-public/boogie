package org.mitre.tdp.boogie.arinc.model;

import static java.util.Collections.emptyList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.io.File;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.TestArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;

class TestConvertingArincRecordMapper {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/arinc-kjfk-v18.txt"));

  @Test
  void testConvertingArincRecordConsumer() {
    Map<Class<?>, List<Object>> recordsByType = recordParser.parseAll(arincTestFile).stream().map(testV18Mapper)
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

  @Test
  void factoryBuildsCompleteV19Mapper() {
    assertDoesNotThrow(() -> ArincRecordConverterFactory.mapperForVersion(ArincVersion.V19));
  }

  private static final TestArincFileParser recordParser = new TestArincFileParser(ArincRecordParser.standard(ArincVersion.V18.specs()));

  private static final ConvertingArincRecordMapper testV18Mapper = ArincRecordConverterFactory.mapperForVersion(ArincVersion.V18);
}
