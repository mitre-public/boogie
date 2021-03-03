package org.mitre.tdp.boogie.alg.resolve.resolver;

import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.test.MockObjects.IF;
import static org.mitre.tdp.boogie.test.MockObjects.transition;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.element.ResolvedElement;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.service.impl.ProcedureGraphService;

class TestProcedureResolver {

  @Test
  void testSingleProcedureResolution() {
    Leg l = IF("FOO", 0.0, 0.0);

    Transition atlJimmy = transition("JIMMY", "KATL", TransitionType.COMMON, ProcedureType.SID, singletonList(l));

    Transition bnyJimmy = transition("JIMMY", "KBNY", TransitionType.COMMON, ProcedureType.SID, singletonList(l));

    ProcedureResolver resolver = new ProcedureResolver(ProcedureGraphService.withTransitions(Arrays.asList(atlJimmy, bnyJimmy)));

    List<ResolvedElement<?>> resolved = resolver.resolve(null, split("JIMMY"), null);

    assertAll(
        () -> assertEquals(2, resolved.size()),
        () -> assertEquals("JIMMY", resolved.get(0).reference().identifier()),
        () -> assertEquals(ElementType.SID, resolved.get(0).type())
    );
  }

  @Test
  void testSidResolutionWithAirportContext() {
    Leg l = IF("FOO", 0.0, 0.0);

    Transition atlJimmy = transition("JIMMY", "KATL", TransitionType.COMMON, ProcedureType.SID, singletonList(l));

    Transition bnyJimmy = transition("JIMMY", "KBNY", TransitionType.COMMON, ProcedureType.SID, singletonList(l));

    ProcedureResolver resolver = new ProcedureResolver(ProcedureGraphService.withTransitions(Arrays.asList(atlJimmy, bnyJimmy)));

    List<ResolvedElement<?>> resolved = resolver.resolve(split("KATL"), split("JIMMY"), null);

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertEquals("JIMMY", resolved.get(0).reference().identifier()),
        () -> assertEquals("KATL", ((Procedure) resolved.get(0).reference()).airport()),
        () -> assertEquals(ElementType.SID, resolved.get(0).type())
    );
  }

  @Test
  void testStarResolutionWithAirportContext() {
    Leg l = IF("FOO", 0.0, 0.0);

    Transition atlJimmy = transition("JIMMY", "KATL", TransitionType.COMMON, ProcedureType.STAR, singletonList(l));

    Transition bnyJimmy = transition("JIMMY", "KBNY", TransitionType.COMMON, ProcedureType.STAR, singletonList(l));

    ProcedureResolver resolver = new ProcedureResolver(ProcedureGraphService.withTransitions(Arrays.asList(atlJimmy, bnyJimmy)));

    List<ResolvedElement<?>> resolved = resolver.resolve(null, split("JIMMY"), split("KATL"));

    assertAll(
        () -> assertEquals(1, resolved.size()),
        () -> assertEquals("JIMMY", resolved.get(0).reference().identifier()),
        () -> assertEquals("KATL", ((Procedure) resolved.get(0).reference()).airport()),
        () -> assertEquals(ElementType.STAR, resolved.get(0).type())
    );
  }

  private List<Transition> sampleTransitions() {
    Transition t1 = mock(Transition.class);
    when(t1.identifier()).thenReturn("RW28R");
    when(t1.transitionType()).thenReturn(TransitionType.RUNWAY);
    when(t1.procedureType()).thenReturn(ProcedureType.STAR);

    Transition t2 = mock(Transition.class);
    when(t2.identifier()).thenReturn("RW26B");
    when(t2.transitionType()).thenReturn(TransitionType.RUNWAY);
    when(t2.procedureType()).thenReturn(ProcedureType.SID);

    Transition t3 = mock(Transition.class);
    when(t3.identifier()).thenReturn("RW09R");
    when(t3.transitionType()).thenReturn(TransitionType.RUNWAY);
    when(t3.procedureType()).thenReturn(ProcedureType.STAR);

    Transition t4 = mock(Transition.class);
    when(t4.identifier()).thenReturn("RW31R");
    when(t4.transitionType()).thenReturn(TransitionType.RUNWAY);
    when(t4.procedureType()).thenReturn(ProcedureType.SID);

    Transition t5 = mock(Transition.class);
    when(t5.identifier()).thenReturn("RW31R");
    when(t5.transitionType()).thenReturn(TransitionType.COMMON);
    when(t5.procedureType()).thenReturn(ProcedureType.SID);

    return Arrays.asList(t1, t2, t3, t4, t5);
  }

  private SectionSplit split(String name) {
    return new SectionSplit.Builder().setValue(name).build();
  }
}
