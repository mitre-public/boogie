package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Tag;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.function.Executable;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.model.ProcedureFactory;
import org.mitre.tdp.boogie.model.ProcedureGraph;
import org.mitre.tdp.boogie.dafif.EmbeddedDafifFile;
import org.mitre.tdp.boogie.dafif.database.DafifDatabaseFactory;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalParent;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

@Tag("DAFIF")
@Tag("INTEGRATION")
public class ProcedureAssemblerIntegrationTest {
  private static final Logger LOG = LoggerFactory.getLogger(ProcedureAssemblerIntegrationTest.class);

  static Collection<DafifTerminalParent> allParents;
  static List<Procedure> allProcedures;
  static int totalTerminalSegments;

  @BeforeAll
  static void setUp() {
    EmbeddedDafifFile dafif = EmbeddedDafifFile.instance();

    DafifTerminalAreaDatabase tad = DafifDatabaseFactory.newTerminalAreaDatabase(
        dafif.dafifAirports(), dafif.dafifRunways(), dafif.dafifAddRunways(), dafif.dafifIls(), dafif.dafifTerminalSegments());

    DafifFixDatabase fdb = DafifDatabaseFactory.newFixDatabase(dafif.dafifWaypoints(), dafif.dafifNavaids());

    FixAssemblyStrategy<Fix> fixStrategy = FixAssemblyStrategy.standard(tad, fdb);
    ProcedureAssemblyStrategy<Procedure, Transition, Leg, Fix> procedureStrategy = new ProcedureAssemblyStrategy.Standard();
    ProcedureAssembler<Procedure> assembler = ProcedureAssembler.standard(tad, fdb, procedureStrategy, fixStrategy);

    allParents = dafif.dafifTerminalParents();
    totalTerminalSegments = dafif.dafifTerminalSegments().size();
    allProcedures = assembler.assemble(allParents).collect(Collectors.toList());
  }

  @Test
  void testParsedNonEmptyData() {
    assertFalse(allParents.isEmpty(), "Should have parsed terminal parent records from TRM_PAR.TXT");
  }

  @Test
  void testAssembleAllProcedures() {
    assertFalse(allProcedures.isEmpty(), "Should assemble at least one procedure from the full dataset");
  }

  @Test
  void testAllProceduresHaveTransitions() {
    assertAll(allProcedures.stream()
        .<Executable>map(p -> () -> assertFalse(p.transitions().isEmpty(),
            "Procedure " + p.procedureIdentifier() + " at " + p.airportIdentifier() + " should have transitions"))
        .collect(Collectors.toList()));
  }

  @Test
  void testAllTransitionsHaveLegs() {
    assertAll(allProcedures.stream()
        .flatMap(p -> p.transitions().stream()
            .<Executable>map(t -> () -> assertFalse(t.legs().isEmpty(),
                "Transition " + t.transitionIdentifier() + " in " + p.procedureIdentifier() + " should have legs")))
        .collect(Collectors.toList()));
  }

  @Test
  void testContainsFixLegsHaveAssociatedFix() {
    assertAll(allProcedures.stream()
        .flatMap(p -> p.transitions().stream()
            .flatMap(t -> t.legs().stream()
                .filter(l -> l.pathTerminator().containsFix())
                .<Executable>map(l -> () -> assertTrue(l.associatedFix().isPresent(),
                    "Leg seq=" + l.sequenceNumber() + " pt=" + l.pathTerminator()
                        + " in " + t.transitionIdentifier() + "/" + p.procedureIdentifier()
                        + " at " + p.airportIdentifier() + " should have associatedFix"))))
        .collect(Collectors.toList()));
  }

  @Test
  void testProcedureTypeCounts() {
    Map<ProcedureType, List<Procedure>> byType = allProcedures.stream()
        .collect(Collectors.groupingBy(Procedure::procedureType));
    assertAll(
        () -> assertEquals(6063, byType.getOrDefault(ProcedureType.STAR, List.of()).size(), "STAR count (PROC=1)"),
        () -> assertEquals(9603, byType.getOrDefault(ProcedureType.SID, List.of()).size(), "SID count (PROC=2)"),
        () -> assertEquals(16266, byType.getOrDefault(ProcedureType.APPROACH, List.of()).size(), "APPROACH count (PROC=3)")
    );
  }

  @Test
  void testTotalLegsMatchTerminalSegments() {
    long totalLegs = allProcedures.stream()
        .flatMap(p -> p.transitions().stream())
        .mapToLong(t -> t.legs().size())
        .sum();
    LOG.info("Raw TRM_SEG rows in DAFIFT/TRM/TRM_SEG.TXT: 349430");
    LOG.info("Parsed dafifTerminalSegments: {}", totalTerminalSegments);
    LOG.info("Total assembled legs: {}", totalLegs);
    assertEquals(349430, totalTerminalSegments, "Parsed segments should match raw DAFIFT/TRM/TRM_SEG.TXT row count");
    assertEquals(totalTerminalSegments, totalLegs, "Total assembled legs should match parsed TRM_SEG records");
  }

  @Test
  void testProceduresByAirport() {
    Map<String, List<Procedure>> byAirport = allProcedures.stream()
        .collect(Collectors.groupingBy(Procedure::airportIdentifier));
    assertEquals(4384, byAirport.size(), "Unique airports with procedures should match TRM_PAR unique airport identifiers");
  }

  @Test
  void testProcedureGraphAssembly() {
    List<String> failures = new java.util.ArrayList<>();
    List<Procedure> notGraphable = allProcedures.stream()
        .filter(p -> notGraphable(p, failures))
        .toList();

    if (!failures.isEmpty()) {
      Map<String, Long> reasonCounts = failures.stream()
          .collect(Collectors.groupingBy(s -> s.contains("lose transitions") ? "lost_transitions"
              : s.contains("no paths") ? "no_paths" : "exception", Collectors.counting()));
      failures.stream().limit(10).forEach(f -> LOG.info("FAIL: {}", f));
      LOG.info("Failure breakdown: {}", reasonCounts);
    }

    // Diagnostic: show transition boundary fixes for failing SID/STAR
    String transitionDiag = allProcedures.stream()
        .filter(p -> notGraphable(p, new java.util.ArrayList<>()))
        .limit(3)
        .map(p -> {
          StringBuilder sb = new StringBuilder();
          sb.append(p.procedureIdentifier()).append("@").append(p.airportIdentifier()).append(" ").append(p.procedureType()).append("\n");
          for (Transition t : (List<Transition>) (List<?>) new java.util.ArrayList<>(p.transitions())) {
            String firstFix = t.legs().isEmpty() ? "?" : t.legs().get(0).associatedFix().map(Fix::fixIdentifier).orElse("NONE");
            String lastFix = t.legs().isEmpty() ? "?" : t.legs().get(t.legs().size() - 1).associatedFix().map(Fix::fixIdentifier).orElse("NONE");
            sb.append("  ").append(t.transitionIdentifier()).append("(").append(t.transitionType()).append(") ")
                .append(t.legs().size()).append("legs first=").append(firstFix).append(" last=").append(lastFix).append("\n");
          }
          return sb.toString();
        })
        .collect(Collectors.joining("\n"));

    Map<ProcedureType, Long> failsByType = allProcedures.stream()
        .filter(p -> notGraphable(p, new java.util.ArrayList<>()))
        .collect(Collectors.groupingBy(Procedure::procedureType, Collectors.counting()));

    assertEquals(0, notGraphable.size(), "Expected 0 not graphable. By type: " + failsByType + "\nDiag:\n" + transitionDiag);
  }

  private boolean notGraphable(Procedure procedure, List<String> failures) {
    try {
      ProcedureGraph graphed = ProcedureFactory.newProcedureGraph(procedure);

      if (procedure.transitions().size() != graphed.transitions().size()) {
        String msg = procedure.procedureIdentifier() + "@" + procedure.airportIdentifier() + " lost transitions: input=" + procedure.transitions().size() + " graphed=" + graphed.transitions().size();
        failures.add(msg);
        return true;
      }

      if (graphed.allPaths().isEmpty()) {
        String msg = procedure.procedureIdentifier() + "@" + procedure.airportIdentifier() + " no paths";
        failures.add(msg);
        return true;
      }

      return false;
    } catch (Exception e) {
      String msg = procedure.procedureIdentifier() + "@" + procedure.airportIdentifier() + " exception: " + e.getMessage();
      failures.add(msg);
      return true;
    }
  }
}
