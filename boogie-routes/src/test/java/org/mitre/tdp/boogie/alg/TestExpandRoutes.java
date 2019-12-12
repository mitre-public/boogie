package org.mitre.tdp.boogie.alg;

import java.util.Arrays;
import java.util.List;

import org.junit.Test;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;
import org.mitre.tdp.boogie.alg.split.SectionSplit;
import org.mitre.tdp.boogie.data.CONNR5;
import org.mitre.tdp.boogie.data.HOBTT2;
import org.mitre.tdp.boogie.models.ExpandedRoute;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.Assert.assertEquals;
import static org.mitre.tdp.boogie.ObjectMocks.IF;
import static org.mitre.tdp.boogie.ObjectMocks.TF;
import static org.mitre.tdp.boogie.ObjectMocks.airway;
import static org.mitre.tdp.boogie.ObjectMocks.fix;
import static org.mitre.tdp.boogie.data.Airports.KATL;
import static org.mitre.tdp.boogie.data.Airports.KDEN;

/**
 * Route inflation tests (the full package) used to test specific component expansions.
 *
 * Test names are abbreviated based on the composed infrastructure elements:
 *
 * A - Airport
 * W - Airway
 * F - Fix
 * L - LatLon
 * P - Procedure
 * T - Tailored Fix
 *
 * e.g. TestAPF would indicated a test for Airport.Procedure.Fix one of the more common
 * composite route elements.
 */
public class TestExpandRoutes {

  @Test
  public void testAPF() {
    String route = "KDEN.CONNR5.DBL";

    ExpandRoutes expander = ExpandRoutes.with(
        singletonList(fix("DBL", 39.439344444444444, -106.89468055555557)),
        emptyList(),
        singletonList(KDEN()),
        CONNR5.build().transitions());

    ExpandedRoute expandedRoute = expander.expand(route);

    List<SectionSplitLeg> legs = expandedRoute.legs();

    SectionSplitLeg leg;

    leg = legs.get(0);
    assertEquals("KDEN", leg.sectionSplit().value());
    assertEquals("KDEN", leg.leg().pathTerminator().identifier());

    leg = legs.get(1);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("GOROC", leg.leg().pathTerminator().identifier());

    leg = legs.get(2);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("HURDL", leg.leg().pathTerminator().identifier());

    leg = legs.get(3);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("HAWPE", leg.leg().pathTerminator().identifier());

    leg = legs.get(4);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("TUNNN", leg.leg().pathTerminator().identifier());

    leg = legs.get(5);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("TAVRN", leg.leg().pathTerminator().identifier());

    leg = legs.get(6);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("VONNN", leg.leg().pathTerminator().identifier());

    leg = legs.get(7);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("TEEBO", leg.leg().pathTerminator().identifier());

    leg = legs.get(8);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("CONNR", leg.leg().pathTerminator().identifier());

    leg = legs.get(9);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("CONNR", leg.leg().pathTerminator().identifier());

    leg = legs.get(10);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("BULDG", leg.leg().pathTerminator().identifier());

    leg = legs.get(11);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("DBL", leg.leg().pathTerminator().identifier());

    leg = legs.get(12);
    assertEquals("DBL", leg.sectionSplit().value());
    assertEquals("DBL", leg.leg().pathTerminator().identifier());

    assertEquals(13, legs.size());
  }

  @Test
  public void testFPA() {
    String route = "DRSDN.HOBTT2.KATL";

    ExpandRoutes expander = ExpandRoutes.with(
        singletonList(fix("DRSDN", 33.06475, -86.183083)),
        emptyList(),
        singletonList(KATL()),
        HOBTT2.build().transitions());

    ExpandedRoute expandedRoute = expander.expand(route);

    List<SectionSplitLeg> legs = expandedRoute.legs();

    SectionSplitLeg leg;

    leg = legs.get(0);
    assertEquals("DRSDN", leg.sectionSplit().value());
    assertEquals("DRSDN", leg.leg().pathTerminator().identifier());

    leg = legs.get(1);
    assertEquals("HOBTT2", leg.sectionSplit().value());
    assertEquals("DRSDN", leg.leg().pathTerminator().identifier());

    leg = legs.get(2);
    assertEquals("HOBTT2", leg.sectionSplit().value());
    assertEquals("SMAWG", leg.leg().pathTerminator().identifier());

    leg = legs.get(3);
    assertEquals("HOBTT2", leg.sectionSplit().value());
    assertEquals("HOBTT", leg.leg().pathTerminator().identifier());

    leg = legs.get(4);
    assertEquals("HOBTT2", leg.sectionSplit().value());
    assertEquals("HOBTT", leg.leg().pathTerminator().identifier());

    leg = legs.get(5);
    assertEquals("HOBTT2", leg.sectionSplit().value());
    assertEquals("ENSLL", leg.leg().pathTerminator().identifier());

    leg = legs.get(6);
    assertEquals("HOBTT2", leg.sectionSplit().value());
    assertEquals("ENSLL", leg.leg().pathTerminator().identifier());

    leg = legs.get(7);
    assertEquals("HOBTT2", leg.sectionSplit().value());
    assertEquals("RAIIN", leg.leg().pathTerminator().identifier());

    leg = legs.get(8);
    assertEquals("HOBTT2", leg.sectionSplit().value());
    assertEquals("KLOWD", leg.leg().pathTerminator().identifier());

    leg = legs.get(9);
    assertEquals("KATL", leg.sectionSplit().value());
    assertEquals("KATL", leg.leg().pathTerminator().identifier());

    assertEquals(10, legs.size());
  }

  @Test
  public void testFPA_NoProcedureMatch() {
    String route = "DRSDN.HOBTT3.KATL";

    ExpandRoutes expander = ExpandRoutes.with(
        singletonList(fix("DRSDN", 33.06475, -86.183083)),
        emptyList(),
        singletonList(KATL()),
        HOBTT2.build().transitions());

    ExpandedRoute expandedRoute = expander.expand(route);

    List<SectionSplitLeg> legs = expandedRoute.legs();

    SectionSplitLeg leg;

    leg = legs.get(0);
    assertEquals("DRSDN", leg.sectionSplit().value());
    assertEquals("DRSDN", leg.leg().pathTerminator().identifier());

    leg = legs.get(1);
    assertEquals("KATL", leg.sectionSplit().value());
    assertEquals("KATL", leg.leg().pathTerminator().identifier());

    assertEquals(2, legs.size());
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
  public void testFAF() {
    String route = "RNZ.J121.PDR";

    ExpandRoutes expander = fafExpander();

    ExpandedRoute expandedRoute = expander.expand(route);
  }

  @Test
  public void testFWFWFRepeatedAirway() {
    String route = "YYZ.J121.AAA.J121.BBB";
  }

  @Test
  public void testFLF() {
    String route = "WAYCO.5300N/14000W..YYZ";
  }

  @Test
  public void testALF() {
    String route = "KDEN./.2200N/12000W..YYZ";
  }

  @Test
  public void testATF() {
    String route = "KDEN./.JIMMY031018..YYZ";
  }

  @Test
  public void testFTF() {
    String route = "YYZ..JIMMY125045..HTM";
  }
}
