package org.mitre.tdp.boogie.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.NavigationSource;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.models.Procedure;
import org.mitre.tdp.boogie.service.impl.ProcedureGraphService;

public class TestProcedureGraphService {

  private Transition transition(ProcedureType type) {
    return new Transition() {
      @Override
      public String airport() {
        return "APT";
      }

      @Override
      public String procedure() {
        return "PROC";
      }

      @Override
      public ProcedureType procedureType() {
        return type;
      }

      @Override
      public TransitionType transitionType() {
        return TransitionType.COMMON;
      }

      @Override
      public List<? extends Leg> legs() {
        return Collections.emptyList();
      }

      @Override
      public String identifier() {
        return "RW01";
      }

      @Override
      public NavigationSource navigationSource() {
        return () -> "TEST";
      }
    };
  }

  @Test
  public void testProcedureGraphServiceAllowsTransitionsWithSameNameAndAirportButDifferentSID_STAR_type() {
    Transition t1 = transition(ProcedureType.STAR);
    Transition t2 = transition(ProcedureType.SID);

    ProcedureGraphService ps = ProcedureGraphService.withTransitions(Arrays.asList(t1, t2));
    Collection<Procedure> procedures = ps.allMatchingIdentifier("PROC");

    assertEquals(2, procedures.size());
    assertTrue(procedures.stream().anyMatch(p -> p.type().equals(ProcedureType.STAR)));
    assertTrue(procedures.stream().anyMatch(p -> p.type().equals(ProcedureType.SID)));
  }
}
