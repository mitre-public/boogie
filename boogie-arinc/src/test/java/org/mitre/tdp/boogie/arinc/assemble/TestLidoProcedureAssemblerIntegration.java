package org.mitre.tdp.boogie.arinc.assemble;

import static java.util.stream.Collectors.toList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;

import java.util.Collection;
import java.util.List;
import java.util.Map;
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
import org.mitre.tdp.boogie.arinc.EmbeddedLidoFile;
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

@Tag("LIDO")
@Tag("INTEGRATION")
public class TestLidoProcedureAssemblerIntegration {
  private static final Logger LOG = LoggerFactory.getLogger(TestLidoProcedureAssemblerIntegration.class);

  private static Map<String, List<Procedure>> proceduresByAirport;

  @BeforeAll
  static void setup() {
    ArincTerminalAreaDatabase arincTerminalAreaDatabase = ArincDatabaseFactory.newTerminalAreaDatabase(
        EmbeddedLidoFile.instance().arincAirports(),
        EmbeddedLidoFile.instance().arincRunways(),
        EmbeddedLidoFile.instance().arincLocalizerGlideSlopes(),
        EmbeddedLidoFile.instance().arincNdbNavaids(),
        EmbeddedLidoFile.instance().arincVhfNavaids(),
        EmbeddedLidoFile.instance().arincWaypoints(),
        EmbeddedLidoFile.instance().arincProcedureLegs(),
        EmbeddedLidoFile.instance().arincGnssLandingSystems(),
        EmbeddedLidoFile.instance().arincHelipads(),
        EmbeddedLidoFile.instance().arincHeliports()
    );

    ArincFixDatabase arincFixDatabase = ArincDatabaseFactory.newFixDatabase(
        EmbeddedLidoFile.instance().arincNdbNavaids(),
        EmbeddedLidoFile.instance().arincVhfNavaids(),
        EmbeddedLidoFile.instance().arincWaypoints(),
        EmbeddedLidoFile.instance().arincAirports(),
        EmbeddedLidoFile.instance().arincHoldingPatterns(),
        EmbeddedLidoFile.instance().arincHeliports()
    );

    ProcedureAssembler<Procedure> assembler = ProcedureAssembler.withStrategy(
        arincTerminalAreaDatabase,
        arincFixDatabase,
        FixAssemblyStrategy.caching(FixAssemblyStrategy.standard()),
        ProcedureAssemblyStrategy.standard()
    );

    proceduresByAirport = assembler.assemble(EmbeddedLidoFile.instance().arincProcedureLegs()).collect(Collectors.groupingBy(Procedure::airportIdentifier));
  }

  @Test
  void testGlobalProcedureCountIsAccurate() {
    assertEquals(100716L, proceduresByAirport.values().stream().mapToLong(Collection::size).sum(), "This is less about the exact number and more about being able to parse/assemble without breaking.");
  }

  @Test
  void testCountsAtCommonAirports() {
    assertAll(
        () -> assertEquals(61, proceduresByAirport.get("KATL").size(), "KATL counts"),
        () -> assertEquals(63, proceduresByAirport.get("KORD").size(), "KORD counts"),
        () -> assertEquals(44, proceduresByAirport.get("KJFK").size(), "KJFK counts"),
        () -> assertEquals(41, proceduresByAirport.get("KEWR").size(), "KEWR counts"),
        () -> assertEquals(48, proceduresByAirport.get("KSFO").size(), "KSFO counts"),
        () -> assertEquals(101, proceduresByAirport.get("WSSS").size(), "WSSS counts"),
        () -> assertEquals(64, proceduresByAirport.get("EDDH").size(), "EDDH counts"),
        () -> assertEquals(43, proceduresByAirport.get("SBGL").size(), "SBGL counts"),
        () -> assertEquals(57, proceduresByAirport.get("GQNO").size(), "GQNO counts")
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
    try {
      ProcedureGraph graphed = ProcedureFactory.newProcedureGraph(procedure);

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

  /**
   * This test checks that the maximum number of invalid legs (of a given type) as parsed from the baseline {@link EmbeddedLidoFile}
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
