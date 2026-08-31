package org.mitre.tdp.boogie.arinc.model;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertIterableEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.io.File;
import java.util.Collection;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.TestArincFileParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.v18.AirportValidator;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegSpec;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegValidator;

class TestConvertingArincRecordConsumer {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/arinc-kjfk-v18.txt"));
  private static final File duplicateFirFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kjfk-and-friends.txt"));

  @BeforeAll
  static void setup() {
    parsedRecords = recordParser.parseAll(arincTestFile);
    parsedRecords.forEach(testV18Consumer);
    recordParser.parseAll(arincTestFile).forEach(testV18OneShotConsumer);
    testV18OneShotRecords = testV18OneShotConsumer.snapshot();
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
        () -> assertEquals(70, testV18Consumer.arincWaypoints().size(), "Waypoint count"),
        () -> assertEquals(0, testV18Consumer.arincRestrictiveAirspaceLegs().size(), "RestrictiveAirspaceLeg count")
    );
  }

  @Test
  void reusesImmutableRecordSnapshotBetweenWrites() {
    assertSame(testV18Consumer.arincProcedureLegs(), testV18Consumer.arincProcedureLegs());
  }

  @Test
  void oneShotConsumerPreservesConvertedRecordCountsAndOrder() {
    assertAll(
        () -> assertIterableEquals(testV18Consumer.arincAirports(), testV18OneShotRecords.arincAirports()),
        () -> assertIterableEquals(testV18Consumer.arincAirportExtensions(), testV18OneShotRecords.arincAirportExtensions()),
        () -> assertIterableEquals(testV18Consumer.arincRunways(), testV18OneShotRecords.arincRunways()),
        () -> assertIterableEquals(testV18Consumer.arincLocalizerGlideSlopes(), testV18OneShotRecords.arincLocalizerGlideSlopes()),
        () -> assertIterableEquals(testV18Consumer.arincNdbNavaids(), testV18OneShotRecords.arincNdbNavaids()),
        () -> assertIterableEquals(testV18Consumer.arincVhfNavaids(), testV18OneShotRecords.arincVhfNavaids()),
        () -> assertIterableEquals(testV18Consumer.arincWaypoints(), testV18OneShotRecords.arincWaypoints()),
        () -> assertIterableEquals(testV18Consumer.arincAirwayLegs(), testV18OneShotRecords.arincAirwayLegs()),
        () -> assertIterableEquals(testV18Consumer.arincProcedureLegs(), testV18OneShotRecords.arincProcedureLegs()),
        () -> assertIterableEquals(testV18Consumer.arincGnssLandingSystems(), testV18OneShotRecords.arincGnssLandingSystems()),
        () -> assertIterableEquals(testV18Consumer.arincHoldingPatterns(), testV18OneShotRecords.arincHoldingPatterns()),
        () -> assertIterableEquals(testV18Consumer.arincFirUirLegs(), testV18OneShotRecords.arincFirUirLegs()),
        () -> assertIterableEquals(testV18Consumer.arincHelipads(), testV18OneShotRecords.arincHelipads()),
        () -> assertIterableEquals(testV18Consumer.arincControlledAirspaceLegs(), testV18OneShotRecords.arincControlledAirspaceLegs()),
        () -> assertIterableEquals(testV18Consumer.arincRestrictiveAirspaceLegs(), testV18OneShotRecords.arincRestrictiveAirspaceLegs()),
        () -> assertEquals(testV18Consumer.arincHeaderOne(), testV18OneShotRecords.arincHeaderOne()),
        () -> assertIterableEquals(testV18Consumer.arincHeliports(), testV18OneShotRecords.arincHeliports())
    );
  }

  @Test
  void standardConsumerStillDeduplicatesRecords() {
    ArincRecord airportRecord = parsedRecords.stream()
        .filter(new AirportValidator())
        .findFirst()
        .orElseThrow();
    ConvertingArincRecordConsumer standardConsumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V18);

    standardConsumer.accept(airportRecord);
    standardConsumer.accept(airportRecord);

    Collection<ArincAirport> airports = standardConsumer.arincAirports();
    assertAll(
        () -> assertEquals(1, airports.size()),
        () -> assertSame(airports, standardConsumer.arincAirports()),
        () -> assertThrows(UnsupportedOperationException.class, airports::clear)
    );
  }

  @Test
  void snapshotCreatesAnImmutableDeduplicatedResult() {
    ArincRecord airportRecord = parsedRecords.stream()
        .filter(new AirportValidator())
        .findFirst()
        .orElseThrow();
    ConvertingArincRecordConsumer oneShotConsumer = ArincRecordConverterFactory.oneShotConsumerForVersion(ArincVersion.V18);

    oneShotConsumer.accept(airportRecord);
    oneShotConsumer.accept(airportRecord);
    ConvertedArincRecords convertedRecords = oneShotConsumer.snapshot();

    assertAll(
        () -> assertEquals(1, convertedRecords.arincAirports().size()),
        () -> assertThrows(UnsupportedOperationException.class, convertedRecords.arincAirports()::clear)
    );
  }

  @Test
  void oneShotConsumerUsesAnUnmodifiableAppendOnlyProcedureList() {
    ArincRecord procedureRecord = parsedRecords.stream()
        .filter(new ProcedureLegValidator())
        .findFirst()
        .orElseThrow();
    ConvertingArincRecordConsumer oneShotConsumer = ArincRecordConverterFactory.oneShotConsumerForVersion(ArincVersion.V18);

    oneShotConsumer.accept(procedureRecord);
    oneShotConsumer.accept(procedureRecord);
    ConvertedArincRecords firstResult = oneShotConsumer.snapshot();
    oneShotConsumer.accept(procedureRecord);
    ConvertedArincRecords secondResult = oneShotConsumer.snapshot();

    assertAll(
        () -> assertEquals(2, firstResult.arincProcedureLegs().size()),
        () -> assertEquals(3, secondResult.arincProcedureLegs().size()),
        () -> assertEquals(firstResult.arincProcedureLegs().get(0), firstResult.arincProcedureLegs().get(1)),
        () -> assertThrows(UnsupportedOperationException.class, firstResult.arincProcedureLegs()::clear)
    );
  }

  @Test
  void oneShotConsumerStillDeduplicatesFirUirRecords() {
    TestArincFileParser firParser = new TestArincFileParser(ArincRecordParser.standard(new FirUirLegSpec()));
    Collection<ArincRecord> firRecords = firParser.parseAll(duplicateFirFile);
    ConvertingArincRecordConsumer standardConsumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V19);
    ConvertingArincRecordConsumer oneShotConsumer = ArincRecordConverterFactory.oneShotConsumerForVersion(ArincVersion.V19);

    firRecords.forEach(standardConsumer);
    firRecords.forEach(oneShotConsumer);
    ConvertedArincRecords oneShotRecords = oneShotConsumer.snapshot();

    assertAll(
        () -> assertEquals(6, firRecords.size(), "Fixture contains three duplicated FIR/UIR rows"),
        () -> assertEquals(3, standardConsumer.arincFirUirLegs().size()),
        () -> assertEquals(3, oneShotRecords.arincFirUirLegs().size()),
        () -> assertIterableEquals(standardConsumer.arincFirUirLegs(), oneShotRecords.arincFirUirLegs())
    );
  }

  private static final TestArincFileParser recordParser = new TestArincFileParser(ArincRecordParser.standard(ArincVersion.V18.specs()));

  private static Collection<ArincRecord> parsedRecords;
  private static final ConvertingArincRecordConsumer testV18Consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V18);
  private static final ConvertingArincRecordConsumer testV18OneShotConsumer = ArincRecordConverterFactory.oneShotConsumerForVersion(ArincVersion.V18);
  private static ConvertedArincRecords testV18OneShotRecords;
}
