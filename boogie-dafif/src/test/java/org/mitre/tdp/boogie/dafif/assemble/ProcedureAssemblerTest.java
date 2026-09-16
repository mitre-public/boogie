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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.ValueSource;
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
import org.mitre.tdp.boogie.dafif.model.DafifTerminalParent;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

public class ProcedureAssemblerTest {

  static ProcedureAssembler<Procedure> assembler;

  @ParameterizedTest
  @ValueSource(ints = {1, 2})
  void assembledLegsUseProcedureSpeedRulesAndDesignVariation(int procedureType) {
    var parent = DafifTerminalParent.builder().airportIdentification("US00001").terminalProcedureType(procedureType)
        .terminalIdentifier("TEST1").icaoCode("KAAA").cycleDate(202601).procedureDesignMagvar("E010020")
        .levelOfService1("N").levelOfService2("N").levelOfService3("N").build();
    var first = DafifTerminalSegment.builder().airportIdentification("US00001").terminalProcedureType(procedureType)
        .terminalIdentifier("TEST1").terminalApproachType("5").terminalSequenceNumber(10).icaoCode("KAAA")
        .trackDescriptionCode("VA").terminalMagneticCourse("341.T").speedLimit1(procedureType == 1 ? 210.0 : null)
        .cycleDate(202601).build();
    var second = DafifTerminalSegment.builder().airportIdentification("US00001").terminalProcedureType(procedureType)
        .terminalIdentifier("TEST1").terminalApproachType("5").terminalSequenceNumber(20).icaoCode("KAAA")
        .trackDescriptionCode("TF").speedLimit1(procedureType == 2 ? 210.0 : null).cycleDate(202601).build();
    var database = DafifDatabaseFactory.newTerminalAreaDatabase(List.of(), List.of(), List.of(), List.of(), List.of(first, second));
    var procedure = ProcedureAssembler.standard(database, DafifDatabaseFactory.newFixDatabase(List.of(), List.of()),
        ProcedureAssemblyStrategy.standard(), FixAssemblyStrategy.standard()).assemble(List.of(parent)).findFirst().orElseThrow();
    var legs = procedure.transitions().iterator().next().legs();
    assertAll(
        () -> assertEquals(331.0, legs.get(0).outboundMagneticCourse().orElseThrow(), 1e-9),
        () -> assertEquals(210.0, legs.get(0).speedConstraint().upperEndpoint()),
        () -> assertEquals(210.0, legs.get(1).speedConstraint().upperEndpoint())
    );
  }

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

    ProcedureAssemblyStrategy<Procedure, Transition, Leg, Fix> procedureStrategy = ProcedureAssemblyStrategy.standard();
    assembler = ProcedureAssembler.standard(tad, fdb, procedureStrategy, FixAssemblyStrategy.standard());
  }

  @Test
  void testAssembleAA30079() {
    List<Procedure> procedures = assembler.assemble(List.of(TestObjects.trmParAA30079Adri1C))
        .toList();

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
        .toList();

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
