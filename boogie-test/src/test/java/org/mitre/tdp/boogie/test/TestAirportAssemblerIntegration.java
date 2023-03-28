package org.mitre.tdp.boogie.test;

import static java.util.stream.Collectors.toCollection;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.arinc.assemble.AirportAssembler;
import org.mitre.tdp.boogie.arinc.assemble.ArincToBoogieConverterFactory;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.TerminalAreaDatabase;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.model.BoogieAirport;
import org.mitre.tdp.boogie.model.BoogieRunway;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag("INTEGRATION")
class TestAirportAssemblerIntegration {

  private static final Logger LOG = LoggerFactory.getLogger(TestProcedureAssemblerIntegration.class);

  private static Map<String, List<Airport>> airports;

  @BeforeAll
  static void setup() {
     TerminalAreaDatabase terminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        EmbeddedCifpFile.instance().arincAirports(),
        EmbeddedCifpFile.instance().arincRunways(),
        EmbeddedCifpFile.instance().arincLocalizerGlideSlopes(),
        EmbeddedCifpFile.instance().arincNdbNavaids(),
        EmbeddedCifpFile.instance().arincVhfNavaids(),
        EmbeddedCifpFile.instance().arincWaypoints(),
        EmbeddedCifpFile.instance().arincProcedureLegs(),
         EmbeddedCifpFile.instance().arincGnssLandingSystems()
    );

    AirportAssembler<Airport, Runway> assembler = ArincToBoogieConverterFactory.newAirportAssembler(terminalAreaDatabase);
    airports = EmbeddedCifpFile.instance().arincAirports().stream().map(assembler).collect(Collectors.groupingBy(Airport::airportIdentifier));
  }

  @Test
  void testGlobalAirportCountIsAccurate() {
    assertEquals(13779, airports.values().stream().mapToInt(List::size).sum());
  }

  @Test
  void testCommonAirportsHaveSingleImplementations() {
    assertAll(
        () -> assertEquals(1, airports.get("KJFK").size(), "KJFK"),
        () -> assertEquals(1, airports.get("KORD").size(), "KORD"),
        () -> assertEquals(1, airports.get("KSEA").size(), "KSEA"),
        () -> assertEquals(1, airports.get("KATL").size(), "KATL"),
        () -> assertEquals(1, airports.get("KDFW").size(), "KDFW"),
        () -> assertEquals(1, airports.get("KSFO").size(), "KSFO"),
        () -> assertEquals(1, airports.get("KMSP").size(), "KMSP")
    );
  }

  /**
   * The majority of our unpaired runways are due to missing runways in the input CIFP source data.
   * <br>
   * The other standard case is runways labeled S/N, E/W, NE/SW, etc. these are fairly non-standard and so we leave them unsupported
   * for now.
   */
  @Test
  void testUnpairedRunwayCountIsLow() {
    Set<Runway> allRunways = airports.values().stream().flatMap(Collection::stream)
        .flatMap(airport -> airport.runways().stream()).collect(toCollection(LinkedHashSet::new));

    Set<Runway> unpairedRunways = allRunways.stream().filter(runway -> !runway.departureRunwayEnd().isPresent()).collect(toCollection(LinkedHashSet::new));

    double unpairedRatio = (double) unpairedRunways.size() / (double) allRunways.size();
    LOG.debug("Unpaired runway ratio was {} with {}/{} runways unpaired.", unpairedRatio, unpairedRunways.size(), allRunways.size());

    assertEquals(0., unpairedRatio, .015, "Ratio of runways without a reciprocal pair should be less than 1.5%.");
  }
}
