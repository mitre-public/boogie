package org.mitre.tdp.boogie.alg.resolve.resolver;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.DefaultLookupService.newLookupService;
import static org.mitre.tdp.boogie.model.ProcedureFactory.newProcedures;
import static org.mitre.tdp.boogie.test.MockObjects.IF;
import static org.mitre.tdp.boogie.test.MockObjects.transition;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;

class TestProcedureResolver {

  @Test
  void testResolvesBothProceduresWithNoAirportContext() {
    Leg l = IF("FOO", 0.0, 0.0);

    Transition atlJimmy = transition("JIMMY", "KATL", TransitionType.COMMON, ProcedureType.SID, singletonList(l));

    Transition bnyJimmy = transition("JIMMY", "KBNY", TransitionType.COMMON, ProcedureType.SID, singletonList(l));

    ProcedureResolver resolver = new ProcedureResolver(newLookupService(Procedure::procedureIdentifier, newProcedures(Arrays.asList(atlJimmy, bnyJimmy))));

    List<ResolvedElement<?>> resolved = resolver.resolve(null, split("JIMMY"), null);

    assertAll(
        () -> assertEquals(2, resolved.size()),
        () -> assertTrue(resolved.get(0).reference() instanceof Procedure)
    );

    Procedure resolvedProcedure = (Procedure) resolved.get(0).reference();

    assertAll(
        () -> assertEquals("JIMMY", resolvedProcedure.procedureIdentifier()),
        () -> assertEquals(ElementType.SID, resolved.get(0).type())
    );
  }

  @Test
  void testSidResolutionWithAirportContext() {
    Leg l = IF("FOO", 0.0, 0.0);

    Transition atlJimmy = transition("JIMMY", "KATL", TransitionType.COMMON, ProcedureType.SID, singletonList(l));

    Transition bnyJimmy = transition("JIMMY", "KBNY", TransitionType.COMMON, ProcedureType.SID, singletonList(l));

    ProcedureResolver resolver = new ProcedureResolver(newLookupService(Procedure::procedureIdentifier, newProcedures(Arrays.asList(atlJimmy, bnyJimmy))));

    List<ResolvedElement<?>> resolved = resolver.resolve(split("KATL"), split("JIMMY"), null);

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertTrue(resolved.get(0).reference() instanceof Procedure)
    );

    Procedure resolvedProcedure = (Procedure) resolved.get(0).reference();

    assertAll(
        () -> assertEquals("JIMMY", resolvedProcedure.procedureIdentifier()),
        () -> assertEquals("KATL", resolvedProcedure.airportIdentifier()),
        () -> assertEquals(ElementType.SID, resolved.get(0).type())
    );
  }

  @Test
  void testStarResolutionWithAirportContext() {
    Leg l = IF("FOO", 0.0, 0.0);

    Transition atlJimmy = transition("JIMMY", "KATL", TransitionType.COMMON, ProcedureType.STAR, singletonList(l));

    Transition bnyJimmy = transition("JIMMY", "KBNY", TransitionType.COMMON, ProcedureType.STAR, singletonList(l));

    ProcedureResolver resolver = new ProcedureResolver(newLookupService(Procedure::procedureIdentifier, newProcedures(Arrays.asList(atlJimmy, bnyJimmy))));

    List<ResolvedElement<?>> resolved = resolver.resolve(null, split("JIMMY"), split("KATL"));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertTrue(resolved.get(0).reference() instanceof Procedure)
    );

    Procedure resolvedProcedure = (Procedure) resolved.get(0).reference();

    assertAll(
        () -> assertEquals("JIMMY", resolvedProcedure.procedureIdentifier()),
        () -> assertEquals("KATL", resolvedProcedure.airportIdentifier()),
        () -> assertEquals(ElementType.STAR, resolved.get(0).type())
    );
  }

  private SectionSplit split(String name) {
    return new SectionSplit.Builder().setValue(name).build();
  }
}
