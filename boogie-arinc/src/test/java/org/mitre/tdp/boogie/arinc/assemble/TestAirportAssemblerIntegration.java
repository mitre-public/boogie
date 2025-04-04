package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.stream.Collectors.toMap;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.*;
import java.util.regex.MatchResult;
import java.util.regex.Pattern;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Disabled;
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

@Tag("INTEGRATION")
class TestAirportAssemblerIntegration {

  private static final Logger LOG = LoggerFactory.getLogger(TestCifpProcedureAssemblerIntegration.class);

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
        Collections.emptySet()
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
  @Disabled
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

  private List<Pair<Runway, Runway>> pairsAt(Airport airport) {

    Map<String, Runway> byReciprocal = airport.runways().stream()
        .flatMap(r -> RunwayIdExtractor.invert(r.runwayIdentifier())
            .stream()
            .map(num -> Pair.of(num, r)))
        .collect(toMap(Pair::first, Pair::second));

    return airport.runways().stream()
        .flatMap(r -> RunwayIdExtractor.normalize(r.runwayIdentifier())
            .map(byReciprocal::get)
            .map(o -> Pair.of((Runway) r, o))
            .stream())
        .collect(Collectors.toList());
  }

  /**
   * Singleton class for extracting the numeric portion of a runway identifier.
   */
  private static final class RunwayIdExtractor {

    private static final Pattern RUNWAY_NUMBER = Pattern.compile("(0[1-9]|[1-2]\\d|3[0-6])");

    private static final Pattern PARALLEL_DESIGNATOR = Pattern.compile("[LRC]");

    /**
     * Simple regex for common runway ids (00-36).
     */
    public static Optional<String> runwayNumber(String runwayId) {
      return RUNWAY_NUMBER.matcher(runwayId).results().findFirst().map(MatchResult::group);
    }

    public static Optional<String> parallelDesignator(String runwayId) {
      return PARALLEL_DESIGNATOR.matcher(String.valueOf(runwayId.charAt(runwayId.length() - 1))).results().findFirst().map(MatchResult::group);
    }

    public static Optional<String> normalize(String runwayId) {
      return runwayNumber(runwayId).flatMap(id -> parallelDesignator(runwayId).map(id::concat));
    }

    public static Optional<String> invert(String runwayId) {
      return normalize(runwayId).map(id -> invertNumber(id).concat(invertDesignator(id)));
    }

    private static String invertDesignator(String runwayId) {
      if (runwayId.length() == 3) {
        char designator = runwayId.charAt(2);
        if ('L' == designator) {
          return "R";
        } else if ('R' == designator) {
          return "L";
        } else {
          return runwayId.substring(2, 3);
        }
      }
      return "";
    }

    private static String invertNumber(String id) {
      return String.format("%02d", (Integer.parseInt(id.substring(0, 2) + 18) % 36));
    }
  }
}
