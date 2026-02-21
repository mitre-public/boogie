package org.mitre.tdp.boogie.dafif.assemble;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.dafif.TestObjects;
import org.mitre.tdp.boogie.dafif.database.DafifDatabaseFactory;
import org.mitre.tdp.boogie.dafif.database.DafifFixDatabase;
import org.mitre.tdp.boogie.dafif.database.DafifTerminalAreaDatabase;
import org.mitre.tdp.boogie.dafif.model.DafifAirport;
import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifRunway;
import org.mitre.tdp.boogie.dafif.model.DafifTerminalSegment;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

public class ProcedureAssemblerTest {

  static ProcedureAssembler<Procedure> assembler;

  @BeforeAll
  static void setUp() {
    Collection<DafifAirport> airports = Set.of(TestObjects.arptAA30079, TestObjects.arptAS10286);
    Collection<DafifRunway> runways = Set.of(TestObjects.rwyAA30079_2911, TestObjects.rwyAS10286_3315);

    List<DafifTerminalSegment> allSegments = new ArrayList<>();
    allSegments.addAll(TestObjects.trmSegAA30079Adri1CRw29);
    allSegments.addAll(TestObjects.trmSegAS10286Codi8AAndop);

    DafifTerminalAreaDatabase tad = DafifDatabaseFactory.newTerminalAreaDatabase(
        airports, runways, List.of(), List.of(), allSegments);

    Collection<DafifWaypoint> waypoints = Set.of(
        TestObjects.wptAdrivAA, TestObjects.wptCa400AA, TestObjects.wptMidvuAA,
        TestObjects.wptAndopAS, TestObjects.wptCodieAS);
    Collection<DafifNavaid> navaids = Set.of();
    DafifFixDatabase fdb = DafifDatabaseFactory.newFixDatabase(waypoints, navaids);

    FixAssemblyStrategy<Fix> fixStrategy = FixAssemblyStrategy.standard(tad, fdb);
    ProcedureAssemblyStrategy<Procedure, Transition, Leg, Fix> procedureStrategy = new ProcedureAssemblyStrategy.Standard();
    assembler = ProcedureAssembler.standard(tad, fdb, procedureStrategy, fixStrategy);
  }

  @Test
  void testAssembleAA30079() {
    List<Procedure> procedures = assembler.assemble(List.of(TestObjects.trmParAA30079Adri1C))
        .collect(Collectors.toList());

    assertFalse(procedures.isEmpty(), "Should assemble at least one procedure for AA30079");
    Procedure proc = procedures.get(0);
    assertAll(
        () -> assertEquals("ADRI1C", proc.procedureIdentifier()),
        () -> assertEquals("AA30079", proc.airportIdentifier()),
        () -> assertEquals(ProcedureType.STAR, proc.procedureType()),
        () -> assertFalse(proc.transitions().isEmpty(), "Procedure should have transitions")
    );
  }

  @Test
  void testAssembleAS10286() {
    List<Procedure> procedures = assembler.assemble(List.of(TestObjects.trmParAS10286Codi8A))
        .collect(Collectors.toList());

    assertFalse(procedures.isEmpty(), "Should assemble at least one procedure for AS10286");
    Procedure proc = procedures.get(0);
    assertAll(
        () -> assertEquals("CODI8A", proc.procedureIdentifier()),
        () -> assertEquals("AS10286", proc.airportIdentifier()),
        () -> assertEquals(ProcedureType.STAR, proc.procedureType()),
        () -> assertFalse(proc.transitions().isEmpty(), "Procedure should have transitions")
    );
  }

  @Test
  void testAssembleBothAirports() {
    List<Procedure> procedures = assembler.assemble(
        List.of(TestObjects.trmParAA30079Adri1C, TestObjects.trmParAS10286Codi8A)
    ).collect(Collectors.toList());

    assertEquals(2, procedures.size(), "Should assemble two procedures (one per airport/parent)");
  }
}
