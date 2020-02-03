package org.mitre.tdp.boogie.alg;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.alg.resolve.SectionSplitLeg;
import org.mitre.tdp.boogie.alg.split.Wildcard;
import org.mitre.tdp.boogie.data.Airports;
import org.mitre.tdp.boogie.data.Airways;
import org.mitre.tdp.boogie.data.CONNR5;
import org.mitre.tdp.boogie.data.HOBTT2;
import org.mitre.tdp.boogie.models.ExpandedRoute;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.MockObjects.fix;
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
public class TestRouteExpander {

  @Test
  public void testAPF() {
    String route = "KDEN.CONNR5.DBL";

    RouteExpander expander = RouteExpander.with(
        singletonList(fix("DBL", 39.439344444444444, -106.89468055555557)),
        emptyList(),
        singletonList(KDEN()),
        CONNR5.build().transitions());

    ExpandedRoute expandedRoute = expander.expand(route);

    List<SectionSplitLeg> legs = expandedRoute.legs();

    SectionSplitLeg leg;

    int i = 0;

    leg = legs.get(i);
    assertEquals("KDEN", leg.sectionSplit().value());
    assertEquals("KDEN", leg.leg().pathTerminator().identifier());
    i++;

    leg = legs.get(i);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("GOROC", leg.leg().pathTerminator().identifier());
    i++;

    leg = legs.get(i);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("HURDL", leg.leg().pathTerminator().identifier());
    i++;

    leg = legs.get(i);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("HAWPE", leg.leg().pathTerminator().identifier());
    i++;

    leg = legs.get(i);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("TUNNN", leg.leg().pathTerminator().identifier());
    i++;

    leg = legs.get(i);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("TAVRN", leg.leg().pathTerminator().identifier());
    i++;

    leg = legs.get(i);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("VONNN", leg.leg().pathTerminator().identifier());
    i++;

    leg = legs.get(i);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("TEEBO", leg.leg().pathTerminator().identifier());
    i++;

    leg = legs.get(i);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("CONNR", leg.leg().pathTerminator().identifier());
    i++;

    leg = legs.get(i);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("CONNR", leg.leg().pathTerminator().identifier());
    i++;

    leg = legs.get(i);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("BULDG", leg.leg().pathTerminator().identifier());
    i++;

    leg = legs.get(i);
    assertEquals("CONNR5", leg.sectionSplit().value());
    assertEquals("DBL", leg.leg().pathTerminator().identifier());
    i++;

    leg = legs.get(i);
    assertEquals("DBL", leg.sectionSplit().value());
    assertEquals("DBL", leg.leg().pathTerminator().identifier());

    assertEquals(13, legs.size());
  }

  @Test
  public void testFPA() {
    String route = "DRSDN.HOBTT2.KATL";

    RouteExpander expander = RouteExpander.with(
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

    RouteExpander expander = RouteExpander.with(
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

  @Test
  public void testFAF() {
    String route = "JMACK.J121.KALDA";

    RouteExpander expander = RouteExpander.with(
        Arrays.asList(
            fix("JMACK", 33.98850277777778, -78.96658333333333),
            fix("KALDA", 37.84195833333334, -75.62648333333333)),
        singletonList(Airways.J121()),
        emptyList(),
        emptyList());

    ExpandedRoute expandedRoute = expander.expand(route);

    List<SectionSplitLeg> legs = expandedRoute.legs();

    SectionSplitLeg leg;

    leg = legs.get(0);
    assertEquals("JMACK", leg.sectionSplit().value());
    assertEquals("JMACK", leg.leg().pathTerminator().identifier());

    leg = legs.get(1);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("JMACK", leg.leg().pathTerminator().identifier());

    leg = legs.get(2);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("BARTL", leg.leg().pathTerminator().identifier());

    leg = legs.get(3);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("ISO", leg.leg().pathTerminator().identifier());

    leg = legs.get(4);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("WEAVR", leg.leg().pathTerminator().identifier());

    leg = legs.get(5);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("ORF", leg.leg().pathTerminator().identifier());

    leg = legs.get(6);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("SAWED", leg.leg().pathTerminator().identifier());

    leg = legs.get(7);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("KALDA", leg.leg().pathTerminator().identifier());

    leg = legs.get(8);
    assertEquals("KALDA", leg.sectionSplit().value());
    assertEquals("KALDA", leg.leg().pathTerminator().identifier());
  }

  @Test
  public void testFAFReverse() {
    String route = "KALDA.J121.JMACK";

    RouteExpander expander = RouteExpander.with(
        Arrays.asList(
            fix("JMACK", 33.98850277777778, -78.96658333333333),
            fix("KALDA", 37.84195833333334, -75.62648333333333)),
        singletonList(Airways.J121()),
        emptyList(),
        emptyList());

    ExpandedRoute expandedRoute = expander.expand(route);

    List<SectionSplitLeg> legs = expandedRoute.legs();

    SectionSplitLeg leg;

    leg = legs.get(8);
    assertEquals("JMACK", leg.sectionSplit().value());
    assertEquals("JMACK", leg.leg().pathTerminator().identifier());

    leg = legs.get(7);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("JMACK", leg.leg().pathTerminator().identifier());

    leg = legs.get(6);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("BARTL", leg.leg().pathTerminator().identifier());

    leg = legs.get(5);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("ISO", leg.leg().pathTerminator().identifier());

    leg = legs.get(4);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("WEAVR", leg.leg().pathTerminator().identifier());

    leg = legs.get(3);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("ORF", leg.leg().pathTerminator().identifier());

    leg = legs.get(2);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("SAWED", leg.leg().pathTerminator().identifier());

    leg = legs.get(1);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("KALDA", leg.leg().pathTerminator().identifier());

    leg = legs.get(0);
    assertEquals("KALDA", leg.sectionSplit().value());
    assertEquals("KALDA", leg.leg().pathTerminator().identifier());
  }

  @Test
  public void testFWFWFRepeatedAirway() {
    String route = "MILIE.J121.BARTL.J121.ORF";

    RouteExpander expander = RouteExpander.with(
        Arrays.asList(
            fix("MILIE", 31.328622222222222, -81.17371944444444),
            fix("BARTL", 34.303177777777776, -78.65149444444445),
            fix("ORF", 36.89190555555555, -76.20032777777779)),
        singletonList(Airways.J121()),
        emptyList(),
        emptyList());

    ExpandedRoute expandedRoute = expander.expand(route);

    List<SectionSplitLeg> legs = expandedRoute.legs();

    SectionSplitLeg leg;

    leg = legs.get(0);
    assertEquals("MILIE", leg.sectionSplit().value());
    assertEquals("MILIE", leg.leg().pathTerminator().identifier());

    leg = legs.get(1);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("MILIE", leg.leg().pathTerminator().identifier());

    leg = legs.get(2);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("CHS", leg.leg().pathTerminator().identifier());

    leg = legs.get(3);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("JMACK", leg.leg().pathTerminator().identifier());

    leg = legs.get(4);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("BARTL", leg.leg().pathTerminator().identifier());

    leg = legs.get(5);
    assertEquals("BARTL", leg.sectionSplit().value());
    assertEquals("BARTL", leg.leg().pathTerminator().identifier());

    leg = legs.get(6);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("BARTL", leg.leg().pathTerminator().identifier());

    leg = legs.get(7);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("ISO", leg.leg().pathTerminator().identifier());

    leg = legs.get(8);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("WEAVR", leg.leg().pathTerminator().identifier());

    leg = legs.get(9);
    assertEquals("J121", leg.sectionSplit().value());
    assertEquals("ORF", leg.leg().pathTerminator().identifier());

    leg = legs.get(10);
    assertEquals("ORF", leg.sectionSplit().value());
    assertEquals("ORF", leg.leg().pathTerminator().identifier());
  }

  @Test
  public void testFLF() {
    String route = "MILIE..5300N/14000W..BARTL";

    RouteExpander expander = RouteExpander.with(
        Arrays.asList(
            fix("MILIE", 31.328622222222222, -81.17371944444444),
            fix("BARTL", 34.303177777777776, -78.65149444444445)),
        emptyList(),
        emptyList(),
        emptyList());

    ExpandedRoute expandedRoute = expander.expand(route);

    List<SectionSplitLeg> legs = expandedRoute.legs();

    SectionSplitLeg leg;

    leg = legs.get(0);
    assertEquals("MILIE", leg.sectionSplit().value());
    assertEquals("MILIE", leg.leg().pathTerminator().identifier());

    leg = legs.get(1);
    assertEquals("5300N/14000W", leg.sectionSplit().value());
    assertEquals("5300N/14000W", leg.leg().pathTerminator().identifier());
    assertEquals(LatLong.of(53.0, -140.0), leg.leg().pathTerminator().latLong());

    leg = legs.get(2);
    assertEquals("BARTL", leg.sectionSplit().value());
    assertEquals("BARTL", leg.leg().pathTerminator().identifier());
  }

  @Test
  public void testALF() {
    String route = "KDEN./.2200N/12000W..BARTL";

    RouteExpander expander = RouteExpander.with(
        singletonList(fix("BARTL", 34.303177777777776, -78.65149444444445)),
        emptyList(),
        singletonList(Airports.KDEN()),
        emptyList());

    ExpandedRoute expandedRoute = expander.expand(route);

    List<SectionSplitLeg> legs = expandedRoute.legs();

    SectionSplitLeg leg;

    leg = legs.get(0);
    assertEquals("KDEN", leg.sectionSplit().value());
    assertEquals("KDEN", leg.leg().pathTerminator().identifier());

    leg = legs.get(1);
    assertEquals("2200N/12000W", leg.sectionSplit().value());
    assertTrue(Wildcard.TAILORED.test(leg.sectionSplit().wildcards()));
    assertEquals("2200N/12000W", leg.leg().pathTerminator().identifier());

    leg = legs.get(2);
    assertEquals("BARTL", leg.sectionSplit().value());
    assertEquals("BARTL", leg.leg().pathTerminator().identifier());
  }

  @Test
  public void testATF() {
    String route = "KDEN./.BARTL031018..BARTL";

    Fix bartl = fix("BARTL", 34.303177777777776, -78.65149444444445);

    RouteExpander expander = RouteExpander.with(singletonList(bartl), emptyList(), singletonList(Airports.KDEN()), emptyList());

    ExpandedRoute expandedRoute = expander.expand(route);

    List<SectionSplitLeg> legs = expandedRoute.legs();

    SectionSplitLeg leg;

    leg = legs.get(0);
    assertEquals("KDEN", leg.sectionSplit().value());
    assertEquals("KDEN", leg.leg().pathTerminator().identifier());

    leg = legs.get(1);
    assertEquals("BARTL031018", leg.sectionSplit().value());
    assertEquals("BARTL", leg.leg().pathTerminator().identifier());
    assertNotEquals(bartl.latLong(), leg.leg().pathTerminator().latLong());

    leg = legs.get(2);
    assertEquals("BARTL", leg.sectionSplit().value());
    assertEquals("BARTL", leg.leg().pathTerminator().identifier());
  }

  @Test
  public void testFTF() {
    String route = "BARTL..BARTL125045..BARTL";

    Fix bartl = fix("BARTL", 34.303177777777776, -78.65149444444445);

    RouteExpander expander = RouteExpander.with(singletonList(bartl), emptyList(), singletonList(Airports.KDEN()), emptyList());

    ExpandedRoute expandedRoute = expander.expand(route);

    List<SectionSplitLeg> legs = expandedRoute.legs();

    SectionSplitLeg leg;

    leg = legs.get(0);
    assertEquals("BARTL", leg.sectionSplit().value());
    assertEquals("BARTL", leg.leg().pathTerminator().identifier());

    leg = legs.get(1);
    assertEquals("BARTL125045", leg.sectionSplit().value());
    assertEquals("BARTL", leg.leg().pathTerminator().identifier());
    assertNotEquals(bartl.latLong(), leg.leg().pathTerminator().latLong());

    leg = legs.get(2);
    assertEquals("BARTL", leg.sectionSplit().value());
    assertEquals("BARTL", leg.leg().pathTerminator().identifier());
  }
}
