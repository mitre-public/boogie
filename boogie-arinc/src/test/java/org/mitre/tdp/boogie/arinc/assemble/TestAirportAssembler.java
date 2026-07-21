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
import org.mitre.tdp.boogie.arinc.ArincRecordParser;
import org.mitre.tdp.boogie.arinc.TestArincFileParser;
import org.mitre.tdp.boogie.arinc.IsThisAPrimaryRecord;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincRecordConverterFactory;
import org.mitre.tdp.boogie.arinc.model.ConvertingArincRecordConsumer;
import org.mitre.tdp.boogie.arinc.ArincVersion;

class TestAirportAssembler {

  private static final File arincTestFile = new File(System.getProperty("user.dir").concat("/src/test/resources/kjfk-and-friends.txt"));

  private static ArincTerminalAreaDatabase arincTerminalAreaDatabase;

  private static AirportAssembler<Airport> assembler;

  @BeforeAll
  static void setup() {
    IsThisAPrimaryRecord isThisAPrimaryRecord = new IsThisAPrimaryRecord();
    recordParser.parseAll(arincTestFile).stream().filter(isThisAPrimaryRecord).forEach(testV18Consumer);

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
        () -> assertEquals(Course.ofDegrees(301.), runways.get("RW31L").course().orElse(null), "RW31L true course"),

        () -> assertEquals(Distance.ofFeet(13.), runways.get("RW13R").originElevation().orElse(null), "RW13R origin elevation"),
        () -> assertEquals(Distance.ofFeet(13.), runways.get("RW31L").originElevation().orElse(null), "RW31L origin elevation")
    );
  }

  private static final TestArincFileParser recordParser = new TestArincFileParser(ArincRecordParser.standard(ArincVersion.V19.specs()));

  private static final ConvertingArincRecordConsumer testV18Consumer = ArincRecordConverterFactory.consumerForVersion(ArincVersion.V19);
}
