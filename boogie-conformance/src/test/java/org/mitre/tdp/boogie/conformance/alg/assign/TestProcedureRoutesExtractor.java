package org.mitre.tdp.boogie.conformance.alg.assign;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.model.ProcedureFactory;
import org.mitre.tdp.boogie.model.ProcedureGraph;

class TestProcedureRoutesExtractor {
  static LatLong one = LatLong.of(0.0, 0.0);
  static LatLong two = LatLong.of(1.0, 0.0);
  static LatLong three = LatLong.of(2.0, 0.0);

  static Fix fix1 = Fix.builder().fixIdentifier("ONE").latLong(one).build();
  static Fix fix2 = Fix.builder().fixIdentifier("TWO").latLong(two).build();
  static Fix fix3 = Fix.builder().fixIdentifier("THREE").latLong(three).build();

  static Leg ifLeg = Leg.ifBuilder(fix1, 0).build();
  static Leg tfLeg = Leg.tfBuilder(fix2, 2).build();
  static Leg tfLeg2 = Leg.tfBuilder(fix3, 3).build();

  static Transition transition = Transition.builder()
      .transitionType(TransitionType.COMMON)
      .transitionIdentifier("ALL")
      .legs(List.of(ifLeg, tfLeg, tfLeg2))
      .build();

  static Procedure proc = Procedure.builder()
      .procedureIdentifier("FAKE1")
      .procedureType(ProcedureType.STAR)
      .airportIdentifier("FAKK")
      .requiredNavigationEquipage(RequiredNavigationEquipage.RNAV)
      .transitions(List.of(transition))
      .build();

  static ProcedureGraph graph = ProcedureFactory.newProcedureGraph(proc);

  static ProcedureRoutesExtractor procRoutesExtractor = ProcedureRoutesExtractor.INSTANCE;

  @Test
  void testExtract() {
    Collection<Route> routes = procRoutesExtractor.apply(graph);
    assertEquals(1, routes.size());
  }
}