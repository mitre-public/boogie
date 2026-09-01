package org.mitre.tdp.boogie.arinc.model;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.arinc.ArincRecord;
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.ArincVersion;
import org.mitre.tdp.boogie.arinc.TestArincFileParser;
import org.mitre.tdp.boogie.arinc.v18.AirportValidator;
import org.mitre.tdp.boogie.arinc.v18.FirUirLegSpec;
import org.mitre.tdp.boogie.arinc.v18.ProcedureLegValidator;

import java.io.File;
import java.util.Collection;

import static org.junit.jupiter.api.Assertions.*;

class TestConvertingArincRecordConsumer {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/arinc-kjfk-v18.txt"));
  private static final File duplicateFirFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kjfk-and-friends.txt"));

  @BeforeAll
  static void setup() {
    parsedRecords = recordParser.parseAll(arincTestFile);
    ConvertingArincRecordConsumer consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V18);
    parsedRecords.forEach(consumer);
    testV18Records = consumer.snapshot();
  }

  @Test
  void testConvertingArincRecordConsumer_V18() {
    assertAll(
        () -> assertEquals(1, testV18Records.arincAirports().size(), "Airport count"),
        () -> assertEquals(0, testV18Records.arincAirwayLegs().size(), "AirwayLeg count"),
        () -> assertEquals(7, testV18Records.arincLocalizerGlideSlopes().size(), "LocalizerGlideSlope count"),
        () -> assertEquals(0, testV18Records.arincNdbNavaids().size(), "NdbNavaid count"),
        () -> assertEquals(454, testV18Records.arincProcedureLegs().size(), "ProcedureLeg count"),
        () -> assertEquals(8, testV18Records.arincRunways().size(), "Runway count"),
        () -> assertEquals(6, testV18Records.arincVhfNavaids().size(), "VhfNavaid count"),
        () -> assertEquals(70, testV18Records.arincWaypoints().size(), "Waypoint count"),
        () -> assertEquals(0, testV18Records.arincRestrictiveAirspaceLegs().size(), "RestrictiveAirspaceLeg count")
    );
  }

  @Test
  void deduplicatesNonProcedureRecords() {
    ArincRecord airportRecord = parsedRecords.stream()
        .filter(new AirportValidator())
        .findFirst()
        .orElseThrow();
    ConvertingArincRecordConsumer consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V18);

    consumer.accept(airportRecord);
    consumer.accept(airportRecord);

    ConvertedArincRecords records = consumer.snapshot();
    assertAll(
        () -> assertEquals(1, records.arincAirports().size()),
        () -> assertThrows(UnsupportedOperationException.class, records.arincAirports()::clear)
    );
  }

  @Test
  void snapshotsAreImmutableAndIndependent() {
    ArincRecord procedureRecord = parsedRecords.stream()
        .filter(new ProcedureLegValidator())
        .findFirst()
        .orElseThrow();
    ConvertingArincRecordConsumer consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V18);

    consumer.accept(procedureRecord);
    ConvertedArincRecords first = consumer.snapshot();
    consumer.accept(procedureRecord);
    ConvertedArincRecords second = consumer.snapshot();

    assertAll(
        () -> assertEquals(1, first.arincProcedureLegs().size()),
        () -> assertEquals(2, second.arincProcedureLegs().size()),
        () -> assertThrows(UnsupportedOperationException.class, first.arincProcedureLegs()::clear)
    );
  }

  @Test
  void preservesProcedureDuplicatesAndEncounterOrder() {
    ArincRecord procedureRecord = parsedRecords.stream()
        .filter(new ProcedureLegValidator())
        .findFirst()
        .orElseThrow();
    ConvertingArincRecordConsumer consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V18);

    consumer.accept(procedureRecord);
    consumer.accept(procedureRecord);
    ConvertedArincRecords records = consumer.snapshot();

    assertAll(
        () -> assertEquals(2, records.arincProcedureLegs().size()),
        () -> assertEquals(records.arincProcedureLegs().get(0), records.arincProcedureLegs().get(1))
    );
  }

  @Test
  void deduplicatesFirUirRecords() {
    TestArincFileParser firParser = new TestArincFileParser(ArincRecordParser.standard(new FirUirLegSpec()));
    Collection<ArincRecord> firRecords = firParser.parseAll(duplicateFirFile);
    ConvertingArincRecordConsumer consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V19);

    firRecords.forEach(consumer);
    ConvertedArincRecords records = consumer.snapshot();

    assertAll(
        () -> assertEquals(6, firRecords.size(), "Fixture contains three duplicated FIR/UIR rows"),
        () -> assertEquals(3, records.arincFirUirLegs().size())
    );
  }

  private static final TestArincFileParser recordParser = new TestArincFileParser(ArincRecordParser.standard(ArincVersion.V18.specs()));

  private static Collection<ArincRecord> parsedRecords;
  private static ConvertedArincRecords testV18Records;
}
