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
import org.mitre.tdp.boogie.dafif.EmbeddedDafifFile;
import org.mitre.tdp.boogie.dafif.database.DafifDatabaseFactory;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalParent;

@Tag("DAFIF")
@Tag("INTEGRATION")
public class ProcedureAssemblerIntegrationTest {

  static Collection<DafifTerminalParent> allParents;
  static List<Procedure> allProcedures;

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
        () -> assertEquals(9608, byType.getOrDefault(ProcedureType.SID, List.of()).size(), "SID count (PROC=2)"),
        () -> assertEquals(15487, byType.getOrDefault(ProcedureType.APPROACH, List.of()).size(), "APPROACH count (PROC=3)")
    );
  }

  @Test
  void testProceduresByAirport() {
    Map<String, List<Procedure>> byAirport = allProcedures.stream()
        .collect(Collectors.groupingBy(Procedure::airportIdentifier));
    assertTrue(byAirport.size() > 1, "Procedures should span multiple airports");
  }
}
