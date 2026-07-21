package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

import java.io.File;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.TestArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;

class TestConvertingArincRecordConsumer {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/arinc-kjfk-v18.txt"));

  @BeforeAll
  static void setup() {
    recordParser.parseAll(arincTestFile).forEach(testV18Consumer);
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

  @Test
  void reusesImmutableRecordSnapshotBetweenWrites() {
    assertSame(testV18Consumer.arincProcedureLegs(), testV18Consumer.arincProcedureLegs());
  }

  private static final TestArincFileParser recordParser = new TestArincFileParser(ArincRecordParser.standard(ArincVersion.V18.specs()));

  private static final ConvertingArincRecordConsumer testV18Consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V18);
}
