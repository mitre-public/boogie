package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.Collections;
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
import org.mitre.tdp.boogie.arinc.EmbeddedCifpFile;
import org.mitre.tdp.boogie.arinc.database.ArincDatabaseFactory;
import org.mitre.tdp.boogie.arinc.database.ArincFixDatabase;
import org.mitre.tdp.boogie.arinc.database.ArincTerminalAreaDatabase;
import org.mitre.tdp.boogie.model.ProcedureFactory;
import org.mitre.tdp.boogie.model.ProcedureGraph;
import org.mitre.tdp.boogie.validate.PathTerminatorBasedLegValidator;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import com.google.common.collect.ArrayListMultimap;
import com.google.common.collect.Maps;
import com.google.common.collect.Multimap;

/**
 * Integration test for the {@link ProcedureAssembler} but based on the entirety of a CIFP cycle.
 */
@Tag("CIFP")
@Tag("INTEGRATION")
class TestCifpProcedureAssemblerIntegration {

  private static final Logger LOG = LoggerFactory.getLogger(TestCifpProcedureAssemblerIntegration.class);

  private static Map<String, List<Procedure>> proceduresByAirport;

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

    ArincFixDatabase arincFixDatabase = ArincDatabaseFactory.newFixDatabase(
        EmbeddedCifpFile.instance().arincNdbNavaids(),
        EmbeddedCifpFile.instance().arincVhfNavaids(),
        EmbeddedCifpFile.instance().arincWaypoints(),
        EmbeddedCifpFile.instance().arincAirports(),
        EmbeddedCifpFile.instance().arincHoldingPatterns(),
        EmbeddedCifpFile.instance().arincHeliports()
    );

    ProcedureAssembler<Procedure> assembler = ProcedureAssembler.withStrategy(
        arincTerminalAreaDatabase,
        arincFixDatabase,
        FixAssemblyStrategy.caching(FixAssemblyStrategy.standard()),
        ProcedureAssemblyStrategy.standard()
    );

    proceduresByAirport = assembler.assemble(EmbeddedCifpFile.instance().arincProcedureLegs()).collect(Collectors.groupingBy(Procedure::airportIdentifier));
  }

  @Test
  void testGlobalProcedureCountIsAccurate() {
    assertEquals(14262L, proceduresByAirport.values().stream().mapToLong(Collection::size).sum(), "This is less about the exact number and more about being able to parse/assemble without breaking.");
  }

  @Test
  void testCountsAtCommonAirports() {
    assertAll(
        () -> assertEquals(65, proceduresByAirport.get("KATL").size(), "KATL counts"),
        () -> assertEquals(63, proceduresByAirport.get("KORD").size(), "KORD counts"),
        () -> assertEquals(39, proceduresByAirport.get("KJFK").size(), "KJFK counts"),
        () -> assertEquals(29, proceduresByAirport.get("KEWR").size(), "KEWR counts"),
        () -> assertEquals(41, proceduresByAirport.get("KSFO").size(), "KSFO counts"),
        () -> assertEquals(1, proceduresByAirport.get("KJRA").size(), "KJRA (Heliport) counts")
    );
  }

  @Test
  void testProcedureGraphAssembly() {

    List<Procedure> notGraphable = proceduresByAirport.values().stream()
        .flatMap(Collection::stream)
        .filter(this::notGraphable)
        .toList();

    assertEquals(0, notGraphable.size(), "Expected 0 procedures to not be graphable.");
  }

  private boolean notGraphable(Procedure procedure) {
    ProcedureGraph graphed = ProcedureFactory.newProcedureGraph(procedure);
    try {
      if (procedure.transitions().size() != graphed.transitions().size()) {
        throw new IllegalStateException("Should never loose transitions in the process");
      }

      if (graphed.allPaths().isEmpty()) {
        throw new IllegalStateException("Should never have no paths in a procedure");
      }

      return false;
    } catch (Exception e) {
      return true;
    }
  }

  @Test
  void testCountsByRequiredNavigationEquipage() {
    Map<RequiredNavigationEquipage, Long> countsByEquip = proceduresByAirport.values().stream()
        .flatMap(Collection::stream).map(Procedure::requiredNavigationEquipage).collect(Collectors.groupingBy(Function.identity(), Collectors.counting()));

    assertAll(
        () -> assertEquals(410, countsByEquip.getOrDefault(RequiredNavigationEquipage.RNP, 0L), "RNP"),
        () -> assertEquals(8663, countsByEquip.getOrDefault(RequiredNavigationEquipage.RNAV, 0L), "RNAV"),
        () -> assertEquals(5189, countsByEquip.getOrDefault(RequiredNavigationEquipage.CONV, 0L), "CONV"),
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
          List<Leg> validLegs = entry.getValue().stream().filter(validLeg).collect(toList());
          List<Leg> invalidLegs = entry.getValue().stream().filter(validLeg.negate()).collect(toList());

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
