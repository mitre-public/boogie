package org.mitre.tdp.boogie.alg.resolve;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.IF;
import static org.mitre.tdp.boogie.MockObjects.transition;
import static org.mitre.tdp.boogie.alg.LookupService.inMemory;
import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedures;

import java.util.Arrays;
import java.util.stream.Stream;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.model.BoogieTransition;

class TestSidStarResolver {

  @Test
  void testResolvesBothProceduresWithNoAirportContext() {
    Leg l = IF("FOO", 0.0, 0.0);

    BoogieTransition atlJimmy = transition("JIMMY", "KATL", TransitionType.COMMON, ProcedureType.SID, singletonList(l));

    BoogieTransition bnyJimmy = transition("JIMMY", "KBNY", TransitionType.COMMON, ProcedureType.SID, singletonList(l));

    SidStarResolver resolver = new SidStarResolver(
        inMemory(newProcedures(Arrays.asList(atlJimmy, bnyJimmy)), p -> Stream.of(p.procedureIdentifier()))
    );

    ResolvedSection resolved = resolver.resolve(null, split("JIMMY"), null);

    assertAll(
        () -> assertEquals(2, resolved.elements().size()),
        () -> assertTrue(resolved.elements().iterator().next() instanceof SidElement)
    );

    Procedure resolvedProcedure = ((ProcedureElement) resolved.elements().iterator().next()).procedure();

    assertAll(
        () -> assertEquals("JIMMY", resolvedProcedure.procedureIdentifier()),
        () -> assertEquals(ProcedureType.SID, resolvedProcedure.procedureType())
    );
  }

  @Test
  void testSidResolutionWithAirportContext() {
    Leg l = IF("FOO", 0.0, 0.0);

    BoogieTransition atlJimmy = transition("JIMMY", "KATL", TransitionType.COMMON, ProcedureType.SID, singletonList(l));

    BoogieTransition bnyJimmy = transition("JIMMY", "KBNY", TransitionType.COMMON, ProcedureType.SID, singletonList(l));

    SidStarResolver resolver = new SidStarResolver(
        inMemory(newProcedures(Arrays.asList(atlJimmy, bnyJimmy)), p -> Stream.of(p.procedureIdentifier()))
    );

    ResolvedSection resolved = resolver.resolve(split("KATL"), split("JIMMY"), null);

    assertAll(
        () -> assertEquals(1, resolved.elements().size()),
        () -> assertTrue(resolved.elements().iterator().next() instanceof SidElement)
    );

    Procedure resolvedProcedure = ((ProcedureElement) resolved.elements().iterator().next()).procedure();

    assertAll(
        () -> assertEquals("JIMMY", resolvedProcedure.procedureIdentifier()),
        () -> assertEquals("KATL", resolvedProcedure.airportIdentifier())
    );
  }

  @Test
  void testStarResolutionWithAirportContext() {
    Leg l = IF("FOO", 0.0, 0.0);

    BoogieTransition atlJimmy = transition("JIMMY", "KATL", TransitionType.COMMON, ProcedureType.STAR, singletonList(l));

    BoogieTransition bnyJimmy = transition("JIMMY", "KBNY", TransitionType.COMMON, ProcedureType.STAR, singletonList(l));

    SidStarResolver resolver = new SidStarResolver(
        inMemory(newProcedures(Arrays.asList(atlJimmy, bnyJimmy)), p -> Stream.of(p.procedureIdentifier()))
    );

    ResolvedSection resolved = resolver.resolve(null, split("JIMMY"), split("KATL"));

    assertAll(
        () -> assertEquals(1, resolved.elements().size()),
        () -> assertTrue(resolved.elements().iterator().next() instanceof StarElement)
    );

    Procedure resolvedProcedure = ((ProcedureElement) resolved.elements().iterator().next()).procedure();

    assertAll(
        () -> assertEquals("JIMMY", resolvedProcedure.procedureIdentifier()),
        () -> assertEquals("KATL", resolvedProcedure.airportIdentifier())
    );
  }

  private SectionSplit split(String name) {
    return new SectionSplit.Builder().setValue(name).build();
  }
}
