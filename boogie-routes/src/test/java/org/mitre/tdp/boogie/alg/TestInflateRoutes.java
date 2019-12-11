package org.mitre.tdp.boogie.alg;

import java.util.Arrays;

import org.junit.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.ProcedureType;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.TransitionType;
import org.mitre.tdp.boogie.models.ExpandedRoute;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.mitre.tdp.boogie.ObjectMocks.IF;
import static org.mitre.tdp.boogie.ObjectMocks.TF;
import static org.mitre.tdp.boogie.ObjectMocks.airport;
import static org.mitre.tdp.boogie.ObjectMocks.airway;
import static org.mitre.tdp.boogie.ObjectMocks.transition;

public class TestInflateRoutes {

  private static ExpandRoutes apfExpander() {
    Airport kind = airport("KIND", 0.0, 0.0);

    Leg l1 = IF("BNDRR", 0.0, 0.0);
    Leg l2 = TF("HRRDR", 0.0, 1.0);
    Leg l3 = TF("GRRDR", 0.0, 2.0);
    Leg l4 = TF("VNY", 0.0, 3.0);

    Transition t = transition("BLSTR1", TransitionType.COMMON, ProcedureType.SID, Arrays.asList(l1, l2, l3, l4));
    return ExpandRoutes.with(singletonList(l4.pathTerminator()), emptyList(), singletonList(kind), singletonList(t));
  }

  @Test
  public void testAirportProcedureFix() {
    String route = "KIND.BLSTR1.VNY";

    ExpandRoutes expander = apfExpander();

    ExpandedRoute expandedRoute = expander.expand(route);

    expandedRoute.legs();
  }

  private static ExpandRoutes fpaExpander() {
    Airport kind = airport("KORD", 0.0, 0.0);

    Leg l1 = IF("BNDRR", 0.0, 0.0);
    Leg l2 = TF("HRRDR", 0.0, 1.0);
    Leg l3 = TF("GRRDR", 0.0, 2.0);
    Leg l4 = TF("GNRLY", 0.0, 3.0);

    Transition t = transition("WYNDE8", TransitionType.COMMON, ProcedureType.SID, Arrays.asList(l1, l2, l3, l4));
    return ExpandRoutes.with(singletonList(l4.pathTerminator()), emptyList(), singletonList(kind), singletonList(t));
  }

  @Test
  public void testFixProcedureAirport() {
    String route = "GNRLY.WYNDE8.KORD";

    ExpandRoutes expander = fpaExpander();

    ExpandedRoute expandedRoute = expander.expand(route);
  }

  private static ExpandRoutes fafExpander() {
    Leg l1 = IF("BNDRR", 0.0, 0.0);
    Leg l2 = TF("RNZ", 0.0, 1.0);
    Leg l3 = TF("PDR", 0.0, 2.0);
    Leg l4 = TF("GNRLY", 0.0, 3.0);

    Airway j121 = airway("J121", Arrays.asList(l1, l2, l3, l4));
    return ExpandRoutes.with(
        Arrays.asList(l2.pathTerminator(), l3.pathTerminator()),
        singletonList(j121),
        emptyList(),
        emptyList());
  }

  @Test
  public void testFixAirwayFix() {
    String route = "RNZ.J121.PDR";

    ExpandRoutes expander = fafExpander();

    ExpandedRoute expandedRoute = expander.expand(route);
  }
}
