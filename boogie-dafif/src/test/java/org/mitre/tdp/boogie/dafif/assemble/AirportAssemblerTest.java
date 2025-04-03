package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;

import java.io.File;
import java.util.List;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.dafif.DafifFileParser;
import org.mitre.tdp.boogie.dafif.DafifVersion;
import org.mitre.tdp.boogie.dafif.database.DafifDatabaseFactory;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.ConvertingDafifRecordConsumer;
import org.mitre.tdp.boogie.dafif.model.DafifAddRunway;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifRecordConverterFactory;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;

@Tag("DAFIF")
@Tag("INTEGRATION")
class AirportAssemblerTest {
  private static final File testDafifAirportFile = new File(System.getProperty("user.dir").concat("/src/test/resources/ARPT.TXT"));
  private static final File testDafifRunwayFile = new File(System.getProperty("user.dir").concat("/src/test/resources/RWY.TXT"));
  private static final File testDafifAddRunwayFile = new File(System.getProperty("user.dir").concat("/src/test/resources/ADD_RWY.TXT"));
  private static final File testDafifIlsFile = new File(System.getProperty("user.dir").concat("/src/test/resources/ILS.TXT"));
  private static DafifTerminalAreaDatabase dafifTerminalAreaDatabase;
  private static AirportAssembler<Airport> airportAssembler;
  private static final DafifFileParser fileParser = new DafifFileParser(DafifVersion.V81);
  private static final ConvertingDafifRecordConsumer recordConsumer = DafifRecordConverterFactory.consumerForVersion(DafifVersion.V81);


  @BeforeAll
  static void setUp() {
    Stream.of(testDafifAirportFile, testDafifRunwayFile, testDafifAddRunwayFile, testDafifIlsFile)
        .map(fileParser)
        .forEach(i -> i.forEach(recordConsumer));
    dafifTerminalAreaDatabase = DafifDatabaseFactory.newTerminalAreaDatabase(recordConsumer);
    airportAssembler = AirportAssembler.standard(dafifTerminalAreaDatabase);
  }

  @Test
  void assembleKBOS() {
    DafifAirport dafifAirport = dafifTerminalAreaDatabase.airport("US05375").orElseThrow();
    Airport airport = airportAssembler.assemble(dafifAirport);
    assertAll(
        () -> assertEquals("US05375", airport.airportIdentifier()),
        () -> assertEquals(dafifAirport.degreesLatitude().orElseThrow(), airport.latitude()),
        () -> assertEquals(dafifAirport.degreesLongitude().orElseThrow(), airport.longitude()),
        () -> assertEquals(-15, airport.magneticVariation().orElseThrow().angle().inDegrees())
    );
  }

  @Test
  void assembleOMAA() {
    DafifAirport dafifAirport = dafifTerminalAreaDatabase.airport("AE19613").orElseThrow();
    DafifRunway both = dafifTerminalAreaDatabase.runway("AE19613", "13R").orElseThrow();
    DafifAddRunway addBoth = dafifTerminalAreaDatabase.addRunway("AE19613", "13R").orElseThrow();
    Airport airport = airportAssembler.assemble(dafifAirport);

    Distance runwayExpected = LatLong.of(both.lowEndDegreesLatitude().orElseThrow(), both.lowEndDegreesLongitude().orElseThrow()).distanceTo(LatLong.of(both.highEndDegreesLatitude().orElseThrow(), both.highEndDegreesLongitude().orElseThrow()));
    Distance lengthOnPrimary = Distance.of(both.length(), Distance.Unit.FEET);

    Runway rw13R = airport.runways().stream().filter(i -> i.runwayIdentifier().equals("13R")).findFirst().orElseThrow();
    assertAll(
        () -> assertEquals("AE19613", airport.airportIdentifier()),
        () -> assertEquals(dafifAirport.degreesLatitude().orElseThrow(), airport.latitude()),
        () -> assertEquals(dafifAirport.degreesLongitude().orElseThrow(), airport.longitude()),
        () -> assertEquals(2, airport.magneticVariation().orElseThrow().angle().inDegrees()),
        () -> assertEquals(4, airport.runways().size()),
        () -> assertEquals(runwayExpected.inFeet(), rw13R.length().orElseThrow().inFeet(), .1),
        () -> assertNotEquals(lengthOnPrimary, runwayExpected, "yeah we just want simple numbers here"),
        () -> assertEquals(24.444411, rw13R.origin().latitude()),
        () -> assertEquals(54.635186, rw13R.origin().longitude()),
        () -> assertNotEquals(rw13R.origin().latitude(), addBoth.lowEndDisplacedThresholdDegreesLatitude().orElse(null)),
        () -> assertNotEquals(rw13R.origin().longitude(), addBoth.lowEndDisplacedThresholdDegreesLongitude().orElse(null))
    );
  }

  @Test
  void allAirportsButNotAllRunways() {
    List<Airport> airports = recordConsumer.dafifAirports().stream()
        .map(i -> airportAssembler.assemble(i))
        .collect(Collectors.toList());

    assertAll(
        () -> assertEquals(9851, airports.size(), "They should all be there")
    );
  }
}