package org.mitre.tdp.boogie.test;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.function.Predicate;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.arinc.assemble.ProcedureAssembler;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.FixDatabase;
import org.mitre.tdp.boogie.arinc.database.TerminalAreaDatabase;
import org.mitre.tdp.boogie.validate.PathTerminatorBasedLegValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

/**
 * Integration test for the {@link ProcedureAssembler} but based on the entirety of a CIFP cycle.
 */
@Tag("INTEGRATION")
class TestProcedureAssemblerIntegration {

  private static final Logger LOG = LoggerFactory.getLogger(TestProcedureAssemblerIntegration.class);

  private static Map<String, List<Procedure>> proceduresByAirport;

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

    FixDatabase fixDatabase = ArincDatabaseFactory.newFixDatabase(
        EmbeddedCifpFile.instance().arincNdbNavaids(),
        EmbeddedCifpFile.instance().arincVhfNavaids(),
        EmbeddedCifpFile.instance().arincWaypoints(),
        EmbeddedCifpFile.instance().arincAirports(),
        EmbeddedCifpFile.instance().arincHoldingPatterns()
    );

    ProcedureAssembler assembler = new ProcedureAssembler(terminalAreaDatabase, fixDatabase);
    proceduresByAirport = assembler.apply(EmbeddedCifpFile.instance().arincProcedureLegs()).collect(Collectors.groupingBy(Procedure::airportIdentifier));
  }

  @Test
  void testGlobalProcedureCountIsAccurate() {
    assertEquals(14258L, proceduresByAirport.values().stream().mapToLong(Collection::size).sum(), "This is less about the exact number and more about being able to parse/assemble without breaking.");
  }

  @Test
  void testCountsAtCommonAirports() {
    assertAll(
        () -> assertEquals(65, proceduresByAirport.get("KATL").size(), "KATL counts"),
        () -> assertEquals(63, proceduresByAirport.get("KORD").size(), "KORD counts"),
        () -> assertEquals(39, proceduresByAirport.get("KJFK").size(), "KJFK counts"),
        () -> assertEquals(29, proceduresByAirport.get("KEWR").size(), "KEWR counts"),
        () -> assertEquals(41, proceduresByAirport.get("KSFO").size(), "KSFO counts")
    );
  }

  @Test
  void testCountsByRequiredNavigationEquipage() {
    Map<RequiredNavigationEquipage, Long> countsByEquip = proceduresByAirport.values().stream()
        .flatMap(Collection::stream).map(Procedure::requiredNavigationEquipage).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    assertAll(
        () -> assertEquals(410, countsByEquip.getOrDefault(RequiredNavigationEquipage.RNP, 0L), "RNP"),
        () -> assertEquals(8660, countsByEquip.getOrDefault(RequiredNavigationEquipage.RNAV, 0L), "RNAV"),
        () -> assertEquals(5188, countsByEquip.getOrDefault(RequiredNavigationEquipage.CONV, 0L), "CONV"),
        () -> assertEquals(0, countsByEquip.getOrDefault(RequiredNavigationEquipage.UNKNOWN, 0L), "UNKNOWN")
    );
  }

  /**
   * This test checks that the maximum number of invalid legs (of a given type) as parsed from the baseline {@link EmbeddedCifpFile}
   * is never larger 1% of all legs (of any type).
   * <br>
   * There will be some that come out of the conversion malformed - though most of them look to be issues in the raw data.
   */
  @Test
  void testRatioOfInvalidLegsInAssembledProcedures() {
    Predicate<Leg> validLeg = new PathTerminatorBasedLegValidator();

    Multimap<PathTerminator, Leg> legsByPathTerminator = proceduresByAirport.values().stream()
        .flatMap(Collection::stream)
        .flatMap(procedure -> procedure.transitions().stream())
        .flatMap(transition -> transition.legs().stream())
        .collect(ArrayListMultimap::create, (m, leg) -> m.put(leg.pathTerminator(), leg), Multimap::putAll);

    Map<PathTerminator, Pair<List<Leg>, List<Leg>>> invalidValid = legsByPathTerminator.asMap().entrySet().stream()
        .map(entry -> {
          List<Leg> validLegs = entry.getValue().stream().filter(validLeg).collect(Collectors.toList());
          List<Leg> invalidLegs = entry.getValue().stream().filter(validLeg.negate()).collect(Collectors.toList());

          LOG.debug("PathTerminator {}: Invalid {}, Valid {}.", entry.getKey(), invalidLegs.size(), validLegs.size());
          return Pair.of(entry.getKey(), Pair.of(invalidLegs, validLegs));
        })
        .collect(Collectors.toMap(Pair::first, Pair::second));

    Map<PathTerminator, Pair<Integer, Integer>> byTerminator = Maps.transformEntries(
        invalidValid,
        (key, value) -> Pair.of(value.first().size(), value.second().size())
    );

    Executable[] tests = byTerminator.entrySet().stream()
        .map(entry -> {
          PathTerminator pathTerminator = entry.getKey();
          Pair<Integer, Integer> pair = entry.getValue();

          double invalidRatio = (double) pair.first() / (double) (pair.first() + pair.second());
          String message = String.format("Invalid %s ratio exceeded: %f. Invalid %d, Total %d.", pathTerminator.name(), invalidRatio, pair.first(), pair.second());

          return (Executable) () -> assertEquals(0., invalidRatio, .01, message);
        })
        .toArray(Executable[]::new);

    assertAll("Check the ratios of the invalid/total leg counts across an entire file.", tests);
  }
}
