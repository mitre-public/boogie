package org.mitre.tdp.boogie.alg.graph;

import java.util.Collections;

import org.junit.Test;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.ObjectMocks;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;

import static java.util.Collections.singletonList;
import static org.mitre.tdp.boogie.ObjectMocks.IF;

public class TestProcedureGraph {

  @Test
  public void testSingleTransitionStar() {
    Leg leg = IF("YYT", 0.0, 0.0);
    Transition transition = ObjectMocks.transition("A", TransitionType.COMMON, ProcedureType.STAR, singletonList(leg));

    ProcedureGraph graph = ProcedureGraph.from(singletonList(transition));

    
  }
}
