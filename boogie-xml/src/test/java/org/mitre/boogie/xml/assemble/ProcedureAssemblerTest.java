package org.mitre.boogie.xml.assemble;

import static org.junit.jupiter.api.Assertions.*;
import static org.mitre.boogie.xml.assemble.ProcedureTestFixtures.*;

import java.util.List;
import java.util.Set;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.CsvSource;
import org.mitre.tdp.boogie.CourseReference;
import org.mitre.boogie.xml.database.XmlFixDatabase;
import org.mitre.boogie.xml.database.FixDatabaseFactory;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.boogie.xml.model.ArincProcedure;
import org.mitre.boogie.xml.model.ArincProcedureLeg;
import org.mitre.boogie.xml.model.ArincRecords;
import org.mitre.boogie.xml.model.ArincTransition;

class ProcedureAssemblerTest {

  @ParameterizedTest
  @CsvSource({"TRUE,,TRUE", "MAGNETIC,,MAGNETIC", "BOTH,,MAGNETIC", ",,MAGNETIC", "TRUE,false,MAGNETIC", "MAGNETIC,true,TRUE", "BOTH,true,TRUE"})
  void resolvesCourseReferenceFromRecordThenAirport(String indicator, Boolean isTrue, CourseReference expected) {
    ArincProcedure procedure = ArincProcedure.builder().identifier("N15").procedureType("Approach")
        .transitions(List.of(ArincTransition.builder().transitionType("ApproachTransition")
            .legs(List.of(ArincProcedureLeg.builder().sequenceNumber(10).pathAndTermination("CF")
                .courseValue(125.0).courseIsTrue(isTrue).build())).build())).build();
    var airport = testAirport("CYYH", List.of(procedure));
    airport = airport.toBuilder().portInfo(airport.portInfo().toBuilder().magneticTrueIndicator(indicator).build()).build();
    var assembler = ProcedureAssembler.standard(FixDatabaseFactory.standard(ArincRecords.standard()));
    var leg = assembler.assemble(List.of(airport)).findFirst().orElseThrow().transitions().iterator().next().legs().get(0);
    assertAll(
        () -> assertEquals(expected, leg.outboundCourse().orElseThrow().reference()),
        () -> assertEquals(125.0, leg.outboundCourse().orElseThrow().degrees())
    );
  }

  @Test
  void assemblesAirportWithNoProceduresReturnsEmptyStream() {
    ArincRecords records = ArincRecords.standard();
    XmlFixDatabase<Fix> fixDb = FixDatabaseFactory.standard(records);
    ProcedureAssembler<Procedure> assembler = ProcedureAssembler.standard(fixDb);

    List<Procedure> procedures = assembler.assemble(List.of(testAirport("KDTW", List.of()))).toList();

    assertTrue(procedures.isEmpty());
  }

  @Test
  void assemblesAirportWithEmptyProceduresList() {
    ArincRecords records = ArincRecords.standard()
        .airports(Set.of(testAirport("KDTW", List.of())));

    XmlFixDatabase<Fix> fixDb = FixDatabaseFactory.standard(records);
    ProcedureAssembler<Procedure> assembler = ProcedureAssembler.standard(fixDb);

    List<Procedure> procedures = assembler.assemble(List.of(testAirport("KDTW", List.of()))).toList();

    assertTrue(procedures.isEmpty());
  }

  @Test
  void assemblesSidProcedure() {
    ArincProcedure sid = ArincProcedure.builder()
        .identifier("GLAVN1")
        .procedureType("Sid")
        .transitions(List.of(
            ArincTransition.builder()
                .identifier("RW09L")
                .transitionType("SidRunwayTransition")
                .legs(List.of(
                    ArincProcedureLeg.builder()
                        .sequenceNumber(10)
                        .pathAndTermination("CF")
                        .fixIdent("JMACK")
                        .fixRef("ref-jmack")
                        .build()))
                .build()))
        .build();

    ArincRecords records = ArincRecords.standard()
        .airports(Set.of(testAirport("KDTW", List.of(sid))))
        .waypoints(Set.of(testWaypoint("JMACK", "ref-jmack")));

    XmlFixDatabase<Fix> fixDb = FixDatabaseFactory.standard(records);
    ProcedureAssembler<Procedure> assembler = ProcedureAssembler.standard(fixDb);

    List<Procedure> procedures = assembler.assemble(List.of(testAirport("KDTW", List.of(sid)))).toList();

    assertEquals(1, procedures.size());
    Procedure proc = procedures.get(0);
    assertAll(
        () -> assertEquals("GLAVN1", proc.procedureIdentifier()),
        () -> assertEquals("KDTW", proc.airportIdentifier()),
        () -> assertEquals(ProcedureType.SID, proc.procedureType()),
        () -> assertEquals(1, proc.transitions().size())
    );

    var transition = proc.transitions().iterator().next();
    assertAll(
        () -> assertEquals("RW09L", transition.transitionIdentifier().orElseThrow()),
        () -> assertEquals(TransitionType.RUNWAY, transition.transitionType()),
        () -> assertEquals(1, transition.legs().size())
    );

    var leg = transition.legs().get(0);
    assertAll(
        () -> assertEquals(PathTerminator.CF, leg.pathTerminator()),
        () -> assertEquals(10, leg.sequenceNumber()),
        () -> assertTrue(leg.associatedFix().isPresent(), "Fix should be resolved from fixRef"),
        () -> assertEquals("JMACK", leg.associatedFix().get().fixIdentifier())
    );
  }

  @Test
  void assemblesProcedureWithUnresolvedFix() {
    ArincProcedure star = ArincProcedure.builder()
        .identifier("HOBBT2")
        .procedureType("Star")
        .transitions(List.of(
            ArincTransition.builder()
                .identifier("ENROUTE")
                .transitionType("StarEnrouteTransition")
                .legs(List.of(
                    ArincProcedureLeg.builder()
                        .sequenceNumber(10)
                        .pathAndTermination("TF")
                        .fixRef("ref-unknown")
                        .build()))
                .build()))
        .build();

    XmlFixDatabase<Fix> fixDb = FixDatabaseFactory.standard(ArincRecords.standard());
    ProcedureAssembler<Procedure> assembler = ProcedureAssembler.standard(fixDb);

    List<Procedure> procedures = assembler.assemble(List.of(testAirport("KJFK", List.of(star)))).toList();

    assertAll(
        () -> assertEquals(1, procedures.size()),
        () -> assertTrue(
            procedures.get(0).transitions().iterator().next().legs().get(0).associatedFix().isEmpty(),
            "Unresolved fixRef should result in empty fix")
    );
  }

  @Test
  void assemblesMultipleProceduresFromOneAirport() {
    ArincProcedure sid = ArincProcedure.builder()
        .identifier("GLAVN1")
        .procedureType("Sid")
        .build();

    ArincProcedure star = ArincProcedure.builder()
        .identifier("HOBBT2")
        .procedureType("Star")
        .build();

    ArincProcedure approach = ArincProcedure.builder()
        .identifier("I04R")
        .procedureType("Approach")
        .build();

    XmlFixDatabase<Fix> fixDb = FixDatabaseFactory.standard(ArincRecords.standard());
    ProcedureAssembler<Procedure> assembler = ProcedureAssembler.standard(fixDb);

    List<Procedure> procedures = assembler.assemble(List.of(testAirport("KDTW", List.of(sid, star, approach)))).toList();

    assertEquals(3, procedures.size());
  }

  @Test
  void assemblesProceduresFromMultipleAirports() {
    ArincProcedure sid = ArincProcedure.builder()
        .identifier("GLAVN1")
        .procedureType("Sid")
        .build();

    ArincProcedure star = ArincProcedure.builder()
        .identifier("CAMRN4")
        .procedureType("Star")
        .build();

    XmlFixDatabase<Fix> fixDb = FixDatabaseFactory.standard(ArincRecords.standard());
    ProcedureAssembler<Procedure> assembler = ProcedureAssembler.standard(fixDb);

    List<Procedure> procedures = assembler.assemble(List.of(
            testAirport("KDTW", List.of(sid)),
            testAirport("KJFK", List.of(star))))
        .toList();

    assertEquals(2, procedures.size());
  }
}
