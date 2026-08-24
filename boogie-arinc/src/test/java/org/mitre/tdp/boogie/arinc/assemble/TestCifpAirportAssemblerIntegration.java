package org.mitre.tdp.boogie.arinc.assemble;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Runway;
import org.mitre.tdp.boogie.arinc.EmbeddedCifpFile;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import static java.util.Objects.nonNull;
import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

@Tag("CIFP")
@Tag("INTEGRATION")
class TestCifpAirportAssemblerIntegration {

  private static final Logger LOG = LoggerFactory.getLogger(TestCifpAirportAssemblerIntegration.class);

  private static Map<String, List<Airport>> airports;

  @BeforeAll
  static void setup() {
    ArincTerminalAreaDatabase arincTerminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        EmbeddedCifpFile.instance().arincAirports(),
        EmbeddedCifpFile.instance().arincRunways(),
        EmbeddedCifpFile.instance().arincLocalizerGlideSlopes(),
        EmbeddedCifpFile.instance().arincNdbNavaids(),
        EmbeddedCifpFile.instance().arincVhfNavaids(),
        EmbeddedCifpFile.instance().arincWaypoints(),
        EmbeddedCifpFile.instance().arincProcedureLegs(),
        EmbeddedCifpFile.instance().arincGnssLandingSystems(),
        Collections.emptySet(),
        EmbeddedCifpFile.instance().arincHeliports()
    );

    AirportAssembler<Airport> assembler = AirportAssembler.standard(arincTerminalAreaDatabase);
    airports = EmbeddedCifpFile.instance().arincAirports().stream().map(assembler::assemble).collect(Collectors.groupingBy(Airport::airportIdentifier));
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
   *
   * <p>The other standard case is runways labeled S/N, E/W, NE/SW, etc. these are fairly non-standard and so we leave them unsupported
   * for now.
   */
  @Test
  void testUnpairedRunwayCountIsLow() {

    long allRunways = airports.values().stream()
        .flatMap(Collection::stream)
        .map(Airport::runways)
        .mapToLong(Collection::size)
        .sum();

    Map<Pair<Runway, Runway>, Airport> paired = airports.values().stream()
        .flatMap(Collection::stream)
        .flatMap(a -> pairsAt(a).stream().map(p -> Pair.of(p, a)))
        .collect(toMap(Pair::first, Pair::second));

    long unpairedRunways = allRunways - paired.size();

    double unpairedRatio = (double) unpairedRunways / (double) allRunways;
    LOG.debug("Unpaired runway ratio was {} with {}/{} runways unpaired.", unpairedRatio, unpairedRunways, allRunways);

    assertEquals(0., unpairedRatio, .015, "Ratio of runways without a reciprocal pair should be less than 1.5%.");
  }

  List<Pair<Runway, Runway>> pairsAt(Airport airport) {
    Map<String, Runway> map = airport.runways().stream().collect(toMap(Runway::runwayIdentifier, Function.identity()));
    return airport.runways().stream()
        .map(r -> Pair.<Runway, Runway>of(r, ReciprocalRunwayIdentifier.INSTANCE.apply(r.runwayIdentifier()).map(map::get).orElse(null)))
        .filter(p -> nonNull(p.second()))
        .collect(Collectors.toList());
  }
}
