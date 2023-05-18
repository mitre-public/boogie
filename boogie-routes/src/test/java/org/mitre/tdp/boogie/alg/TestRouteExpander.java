package org.mitre.tdp.boogie.alg;

import static java.util.Arrays.asList;
import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.Airports.KATL;
import static org.mitre.tdp.boogie.Airports.KDEN;
import static org.mitre.tdp.boogie.MockObjects.fix;

import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airports;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Airways;
import org.mitre.tdp.boogie.CONNR5;
import org.mitre.tdp.boogie.COSTR3;
import org.mitre.tdp.boogie.CUN;
import org.mitre.tdp.boogie.CZM;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.HOBTT2;
import org.mitre.tdp.boogie.JIIMS3;
import org.mitre.tdp.boogie.KATL_R27R;
import org.mitre.tdp.boogie.KMCO_I17L;
import org.mitre.tdp.boogie.KMCO_I17R;
import org.mitre.tdp.boogie.PLMMR2;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
import org.mitre.tdp.boogie.SUMMA2;
import org.mitre.tdp.boogie.alg.resolve.ElementType;
import org.mitre.tdp.boogie.alg.resolve.RouteTokenResolver;
import org.mitre.tdp.boogie.alg.split.Wildcard;

/**
 * Route inflation tests (the full package) used to test specific component expansions.
 * <br>
 * Test names are abbreviated based on the composed infrastructure elements:
 * <br>
 * A - Airport
 * W - Airway
 * F - Fix
 * L - LatLon
 * P - Procedure
 * T - Tailored Fix
 * <br>
 * e.g. TestAPF would indicated a test for Airport.Procedure.Fix one of the more common composite route elements.
 */
class TestRouteExpander {

  @Test
  void testOddComboDoesNotHappen() {
    Fix start = fix("START", 33.3336, -84.7909);
    Fix end = fix("ENDDD", 33.5496, -84.1357);

    String route = "KATL..START..ZALLE.J000.GGOLF..ENDDD..KMCO"; //these are also on the sid

    RouteExpander expander = newExpander(
        asList(start, end),
        singletonList(Airways.J000()),
        asList(Airports.KATL(), Airports.KMCO()),
        singletonList(PLMMR2.INSTANCE)
    );

    ExpandedRoute expandedRoute = expander.apply(route).orElseThrow();

    assertAll("We want the airway to be taken over the sid",
        () -> assertEquals("J000", expandedRoute.legs().get(2).section(), "This better not be the sid"),
        () -> assertEquals("J000", expandedRoute.legs().get(3).section(), "This better not be the sid")
    );
  }

  @Test
  void testExitsEarlyWithNoResolvedElements() {
    String route = "KDEN.CONNR5.DBL";

    RouteExpander expander = newExpander(emptyList(), emptyList(), emptyList(), emptyList());
    assertFalse(expander.apply(route).isPresent());
  }

  @Test
  void surlyDropTest() {
    String route = "KDEN.CONNR5.DBL";

    RouteExpander expander = newSurlyExpander(
        singletonList(fix("DBL", 39.439344444444444, -106.89468055555557)),
        emptyList(),
        singletonList(KDEN()),
        singletonList(CONNR5.INSTANCE));

    ExpandedRoute expandedRoute = expander.apply(route, "RW16R", null).orElseThrow(IllegalStateException::new);

    RouteSummary routeSummary = expandedRoute.routeSummary().orElseThrow(AssertionError::new);
    assertAll(
        "This surly expander had better still work",
        () -> assertEquals("KDEN", routeSummary.departureAirport()),
        () -> assertEquals(Optional.of("RW16R"), routeSummary.departureRunway()),
        () -> assertEquals(Optional.of("CONNR5"), routeSummary.sid()),
        () -> assertEquals(Optional.of("DBL"), routeSummary.sidExitFix()),
        () -> assertEquals(Optional.of("CONNR"), routeSummary.departureFix()),
        () -> assertEquals(Optional.of(RequiredNavigationEquipage.RNAV), routeSummary.requiredSidEquipage())
    );
  }

  @Test
  void testAPF() {
    String route = "KDEN.CONNR5.DBL";

    RouteExpander expander = newExpander(
        singletonList(fix("DBL", 39.439344444444444, -106.89468055555557)),
        emptyList(),
        singletonList(KDEN()),
        singletonList(CONNR5.INSTANCE));

    ExpandedRoute expandedRoute = expander.apply(route, "RW16R", null).orElseThrow(IllegalStateException::new);

    RouteSummary routeSummary = expandedRoute.routeSummary().orElseThrow(AssertionError::new);
    assertAll(
        "Check expanded SID summary statistics.",
        () -> assertEquals("KDEN", routeSummary.departureAirport()),
        () -> assertEquals(Optional.of("RW16R"), routeSummary.departureRunway()),
        () -> assertEquals(Optional.of("CONNR5"), routeSummary.sid()),
        () -> assertEquals(Optional.of("DBL"), routeSummary.sidExitFix()),
        () -> assertEquals(Optional.of("CONNR"), routeSummary.departureFix()),
        () -> assertEquals(Optional.of(RequiredNavigationEquipage.RNAV), routeSummary.requiredSidEquipage())
    );

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("KDEN", legs.get(0).section()),
        () -> assertEquals("KDEN", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("CONNR5", legs.get(1).section()),
        () -> assertEquals(PathTerminator.VI, legs.get(1).pathTerminator()),

        () -> assertEquals("CONNR5", legs.get(2).section()),
        () -> assertEquals("GOROC", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("CONNR5", legs.get(3).section()),
        () -> assertEquals("HURDL", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("CONNR5", legs.get(4).section()),
        () -> assertEquals("HAWPE", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("CONNR5", legs.get(5).section()),
        () -> assertEquals("TUNNN", legs.get(5).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("CONNR5", legs.get(6).section()),
        () -> assertEquals("TAVRN", legs.get(6).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("CONNR5", legs.get(7).section()),
        () -> assertEquals("VONNN", legs.get(7).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("CONNR5", legs.get(8).section()),
        () -> assertEquals("TEEBO", legs.get(8).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("CONNR5", legs.get(9).section()),
        () -> assertEquals("CONNR", legs.get(9).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("CONNR5", legs.get(10).section()),
        () -> assertEquals("BULDG", legs.get(10).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("CONNR5", legs.get(11).section()),
        () -> assertEquals("DBL", legs.get(11).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(12, legs.size())
    );
  }

  @Test
  void testFPA() {
    String route = "DRSDN.HOBTT2.KATL";

    RouteExpander expander = newExpander(
        singletonList(fix("DRSDN", 33.06475, -86.183083)),
        emptyList(),
        singletonList(KATL()),
        singletonList(HOBTT2.INSTANCE));

    ExpandedRoute expandedRoute = expander.apply(route, null, "RW26B").orElseThrow(IllegalStateException::new);

    RouteSummary routeSummary = expandedRoute.routeSummary().orElseThrow(AssertionError::new);
    assertAll(
        "Check expanded STAR summary statistics.",
        () -> assertEquals("KATL", routeSummary.arrivalAirport()),
        () -> assertEquals(Optional.of("RW26B"), routeSummary.arrivalRunway()),
        () -> assertEquals(Optional.of("HOBTT2"), routeSummary.star()),
        () -> assertEquals(Optional.of("DRSDN"), routeSummary.starEntryFix()),
        () -> assertEquals(Optional.of("HOBTT"), routeSummary.arrivalFix()),
        () -> assertEquals(Optional.of(RequiredNavigationEquipage.RNAV), routeSummary.requiredStarEquipage())
    );

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("HOBTT2", legs.get(0).section()),
        () -> assertEquals("DRSDN", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("HOBTT2", legs.get(1).section()),
        () -> assertEquals("SMAWG", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("HOBTT2", legs.get(2).section()),
        () -> assertEquals("HOBTT", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("HOBTT2", legs.get(3).section()),
        () -> assertEquals("ENSLL", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("HOBTT2", legs.get(4).section()),
        () -> assertEquals("RAIIN", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("HOBTT2", legs.get(5).section()),
        () -> assertEquals("KLOWD", legs.get(5).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("HOBTT2", legs.get(6).section()),
        () -> assertEquals("SWEPT", legs.get(6).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("HOBTT2", legs.get(7).section()),
        () -> assertEquals("KYMMY", legs.get(7).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("HOBTT2", legs.get(8).section()),
        () -> assertEquals("KEAVY", legs.get(8).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("HOBTT2", legs.get(9).section()),
        () -> assertEquals("KEAVY", legs.get(9).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("KATL", legs.get(10).section()),
        () -> assertEquals("KATL", legs.get(10).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(11, legs.size())
    );
  }

  @Test
  void testFPA_NoProcedureMatch() {
    String route = "DRSDN.HOBTT3.KATL";

    RouteExpander expander = newExpander(
        singletonList(fix("DRSDN", 33.06475, -86.183083)),
        emptyList(),
        singletonList(KATL()),
        singletonList(HOBTT2.INSTANCE));

    ExpandedRoute expandedRoute = expander.apply(route).get();

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("DRSDN", legs.get(0).section()),
        () -> assertEquals("DRSDN", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("KATL", legs.get(1).section()),
        () -> assertEquals("KATL", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(2, legs.size())
    );
  }

  @Test
  void testFAF() {
    String route = "JMACK.J121.KALDA";

    RouteExpander expander = newExpander(
        asList(
            fix("JMACK", 33.98850277777778, -78.96658333333333),
            fix("KALDA", 37.84195833333334, -75.62648333333333)),
        singletonList(Airways.J121()),
        emptyList(),
        emptyList());

    ExpandedRoute expandedRoute = expander.apply(route).get();

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("J121", legs.get(0).section()),
        () -> assertEquals("JMACK", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(1).section()),
        () -> assertEquals("BARTL", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(2).section()),
        () -> assertEquals("ISO", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(3).section()),
        () -> assertEquals("WEAVR", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(4).section()),
        () -> assertEquals("ORF", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(5).section()),
        () -> assertEquals("SAWED", legs.get(5).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(6).section()),
        () -> assertEquals("KALDA", legs.get(6).associatedFix().map(Fix::fixIdentifier).orElse(null))
    );
  }

  @Test
  void testFAFReverse() {
    String route = "KALDA.J121.JMACK";

    RouteExpander expander = newExpander(
        asList(
            fix("JMACK", 33.98850277777778, -78.96658333333333),
            fix("KALDA", 37.84195833333334, -75.62648333333333)),
        singletonList(Airways.J121()),
        emptyList(),
        emptyList());

    ExpandedRoute expandedRoute = expander.apply(route).get();

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("J121", legs.get(0).section()),
        () -> assertEquals("KALDA", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(1).section()),
        () -> assertEquals("SAWED", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(2).section()),
        () -> assertEquals("ORF", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(3).section()),
        () -> assertEquals("WEAVR", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(4).section()),
        () -> assertEquals("ISO", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(5).section()),
        () -> assertEquals("BARTL", legs.get(5).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(6).section()),
        () -> assertEquals("JMACK", legs.get(6).associatedFix().map(Fix::fixIdentifier).orElse(null))
    );
  }

  @Test
  void testFWFWFRepeatedAirway() {
    String route = "MILIE.J121.BARTL.J121.ORF";

    RouteExpander expander = newExpander(
        asList(
            fix("MILIE", 31.328622222222222, -81.17371944444444),
            fix("BARTL", 34.303177777777776, -78.65149444444445),
            fix("ORF", 36.89190555555555, -76.20032777777779)),
        singletonList(Airways.J121()),
        emptyList(),
        emptyList());

    ExpandedRoute expandedRoute = expander.apply(route).get();

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("J121", legs.get(0).section()),
        () -> assertEquals("MILIE", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(1).section()),
        () -> assertEquals("CHS", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(2).section()),
        () -> assertEquals("JMACK", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(3).section()),
        () -> assertEquals("BARTL", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(4).section()),
        () -> assertEquals("ISO", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(5).section()),
        () -> assertEquals("WEAVR", legs.get(5).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(6).section()),
        () -> assertEquals("ORF", legs.get(6).associatedFix().map(Fix::fixIdentifier).orElse(null))
    );
  }

  @Test
  void testFLF() {
    String route = "MILIE..5300N/14000W..BARTL";

    RouteExpander expander = newExpander(
        asList(
            fix("MILIE", 31.328622222222222, -81.17371944444444),
            fix("BARTL", 34.303177777777776, -78.65149444444445)),
        emptyList(),
        emptyList(),
        emptyList());

    ExpandedRoute expandedRoute = expander.apply(route).get();

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("MILIE", legs.get(0).section()),
        () -> assertEquals("MILIE", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("5300N/14000W", legs.get(1).section()),
        () -> assertEquals("5300N/14000W", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(1).pathTerminator()),
        () -> assertEquals(LatLong.of(53.0, -140.0), legs.get(1).associatedFix().map(Fix::latLong).orElse(null)),

        () -> assertEquals("BARTL", legs.get(2).section()),
        () -> assertEquals("BARTL", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null))
    );
  }

  @Test
  void testALF() {
    String route = "KDEN./.2200N/12000W..BARTL";

    RouteExpander expander = newExpander(
        singletonList(fix("BARTL", 34.303177777777776, -78.65149444444445)),
        emptyList(),
        singletonList(Airports.KDEN()),
        emptyList());

    ExpandedRoute expandedRoute = expander.apply(route).get();

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("KDEN", legs.get(0).section()),
        () -> assertEquals("KDEN", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("2200N/12000W", legs.get(1).section()),
        () -> assertTrue(Wildcard.TAILORED.test(legs.get(1).wildcards())),
        () -> assertEquals("2200N/12000W", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, legs.get(1).pathTerminator()),

        () -> assertEquals("BARTL", legs.get(2).section()),
        () -> assertEquals("BARTL", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null))
    );
  }

  @Test
  void testATF() {
    String route = "KDEN./.BARTL031018..BARTL";

    Fix bartl = fix("BARTL", 34.303177777777776, -78.65149444444445);

    RouteExpander expander = newExpander(
        singletonList(bartl),
        emptyList(),
        singletonList(Airports.KDEN()),
        emptyList());

    ExpandedRoute expandedRoute = expander.apply(route).get();

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("KDEN", legs.get(0).section()),
        () -> assertEquals("KDEN", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("BARTL031018", legs.get(1).section()),
        () -> assertEquals("BARTL031018", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, legs.get(0).pathTerminator()),
        () -> assertNotEquals(bartl.latLong(), legs.get(1).associatedFix().map(Fix::latLong).orElse(null)),

        () -> assertEquals("BARTL", legs.get(2).section()),
        () -> assertEquals("BARTL", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.DF, legs.get(2).pathTerminator(), "Path Terminator for direct-to after tailored should be DF.")
    );
  }

  @Test
  void testFTF() {
    String route = "BARTL..BARTL125045..BARTL";

    Fix bartl = fix("BARTL", 34.303177777777776, -78.65149444444445);

    RouteExpander expander = newExpander(
        singletonList(bartl),
        emptyList(),
        singletonList(Airports.KDEN()),
        emptyList());

    ExpandedRoute expandedRoute = expander.apply(route).get();

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("BARTL", legs.get(0).section()),
        () -> assertEquals("BARTL", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("BARTL125045", legs.get(1).section()),
        () -> assertEquals("BARTL125045", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(1).pathTerminator()),
        () -> assertNotEquals(bartl.latLong(), legs.get(1).associatedFix().map(Fix::latLong).orElse(null)),

        () -> assertEquals("BARTL", legs.get(2).section()),
        () -> assertEquals("BARTL", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null))
    );
  }

  /**
   * The mocked COSTR3 procedure into KMCO has a common portion which looks identical to most runway transitions and is therefore
   * vulnerable to many of the same issues (the closest fix the the airport is not the end of the common portion).
   * <br>
   * This test is to make sure we still resolve through the end of the common portion correctly.
   */
  @Test
  void testFPA_Runway_Like_Common_Portion() {
    String route = "RSW.COSTR3.KMCO";

    Fix rsw = fix("RSW", 26.529875, -81.77576666666667);

    RouteExpander expander = newExpander(
        singletonList(rsw),
        emptyList(),
        singletonList(Airports.KMCO()),
        singletonList(COSTR3.INSTANCE));

    ExpandedRoute expandedRoute = expander.apply(route).get();

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("COSTR3", legs.get(0).section()),
        () -> assertEquals("RSW", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(1).section()),
        () -> assertEquals("DOWNN", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(2).section()),
        () -> assertEquals("MOANS", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(3).section()),
        () -> assertEquals("COSTR", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(4).section()),
        () -> assertEquals("BIGGR", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(5).section()),
        () -> assertEquals("TINKR", legs.get(5).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(6).section()),
        () -> assertEquals("KRAKN", legs.get(6).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(7).section()),
        () -> assertEquals("TWONA", legs.get(7).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(8).section()),
        () -> assertEquals("KNUKL", legs.get(8).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(9).section()),
        () -> assertEquals("KNUKL", legs.get(9).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("KMCO", legs.get(10).section()),
        () -> assertEquals("KMCO", legs.get(10).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(11, legs.size())
    );
  }

  @Test
  void testLeavingEarly() {
    Fix drsdn = fix("DRSDN", 33.06475, -86.183083);
    Fix khmya = fix("KHMYA", 32.33565277777778, -87.04967777777777);
    Fix fnley = fix("FNLEY", 32.70103888888889, -86.09133055555554);
    Fix shurt = fix("SHURT", 33.53684166666667, -84.43048055555556);
    RouteExpander expander = newExpander(
        List.of(drsdn, khmya, fnley, shurt),
        emptyList(),
        singletonList(Airports.KATL()),
        List.of(HOBTT2.INSTANCE, KATL_R27R.INSTANCE)
    );

    String leftStarEarly = "KHMYA.HOBTT2.FNLEY..KATL";
    ExpandedRoute leftStarEarlyRoute = expander.apply(leftStarEarly, null, "RW27R", RequiredNavigationEquipage.RNAV).get();

    assertAll(
        () -> assertEquals("CNDLR", leftStarEarlyRoute.legs().get(1).associatedFix().get().fixIdentifier(), "Should pull the runway transition"),
        () -> assertEquals("FNLEY", leftStarEarlyRoute.legs().get(2).associatedFix().get().fixIdentifier(), "Should have MMCAP transition"),
        () -> assertTrue(leftStarEarlyRoute.legs().stream().noneMatch(i -> i.associatedFix().get().fixIdentifier().equals("SMAWG"))),
        () -> assertEquals("MMCAP", leftStarEarlyRoute.legs().get(3).associatedFix().get().fixIdentifier(), "The nearest is mmcap")
    );

    String leftStarLate = "DRSDN.HOBTT2.SHURT..KATL";
    ExpandedRoute leftStarLateRoute = expander.apply(leftStarLate, null, "RW27R", RequiredNavigationEquipage.RNAV).get();

    assertAll(
        () -> assertEquals("SHURT", leftStarLateRoute.legs().get(4).associatedFix().get().fixIdentifier(), "This should be the last one now"),
        () -> assertEquals("MMCAP", leftStarLateRoute.legs().get(5).associatedFix().get().fixIdentifier(), "Still the nearest one")
    );
  }

  @Test
  void joinApproachAt() {
    Fix mmcap = fix("MMCAP", 33.6341, -84.0600);
    Fix youyu = fix("YOUYU", 33.6339, -83.9911);
    Fix david = fix("DAVID", 33.6858, -83.9001);

    RouteExpander expander = newExpander(
        List.of(mmcap, youyu, david),
        emptyList(),
        singletonList(Airports.KATL()),
        List.of(HOBTT2.INSTANCE, KATL_R27R.INSTANCE)
    );

    String youyuString = "DAVID..KATL";
    ExpandedRoute youyuRoute = expander.apply(youyuString, null, "RW27R", RequiredNavigationEquipage.RNAV).get();

    assertAll(
        () -> assertEquals("YOUYU", youyuRoute.legs().get(1).associatedFix().get().fixIdentifier(), "Nearest to youyu")
    );

    String mmcapString = "MMCAP..KATL";
    ExpandedRoute mmcapRoute = expander.apply(mmcapString, null, "RW27R", RequiredNavigationEquipage.RNAV).get();

    assertAll(
        () -> assertEquals("MMCAP", mmcapRoute.legs().get(0).associatedFix().get().fixIdentifier(), "Should collapse to the first fix only")
    );
  }

  @Test
  void testFPApchA() {
    String route = "RSW.COSTR3.KMCO";

    Fix rsw = fix("RSW", 26.529875, -81.77576666666667);

    RouteExpander expander = newExpander(
        singletonList(rsw),
        emptyList(),
        singletonList(Airports.KMCO()),
        asList(COSTR3.INSTANCE, KMCO_I17R.I17R, KMCO_I17L.I17L)
    );

    ExpandedRoute expandedRoute = expander.apply(route, null, "RW17R", RequiredNavigationEquipage.CONV).get();

    RouteSummary routeSummary = expandedRoute.routeSummary().orElseThrow(AssertionError::new);
    assertAll(
        "Check expanded STAR/Approach summary statistics.",
        () -> assertEquals("KMCO", routeSummary.arrivalAirport()),
        () -> assertEquals(Optional.of("RW17R"), routeSummary.arrivalRunway()),

        // STAR
        () -> assertEquals(Optional.of("COSTR3"), routeSummary.star()),
        () -> assertEquals(Optional.of("RSW"), routeSummary.starEntryFix()),
        () -> assertEquals(Optional.of("COSTR"), routeSummary.arrivalFix()),
        () -> assertEquals(Optional.of(RequiredNavigationEquipage.RNAV), routeSummary.requiredStarEquipage()),

        // Approach
        () -> assertEquals(Optional.of("I17R"), routeSummary.approach()),
        () -> assertEquals(Optional.of("RATOY"), routeSummary.approachEntryFix()),
        () -> assertEquals(Optional.of(RequiredNavigationEquipage.CONV), routeSummary.requiredApproachEquipage())
    );

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("COSTR3", legs.get(0).section()),
        () -> assertEquals("RSW", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(1).section()),
        () -> assertEquals("DOWNN", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(2).section()),
        () -> assertEquals("MOANS", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(3).section()),
        () -> assertEquals("COSTR", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(4).section()),
        () -> assertEquals("BIGGR", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(5).section()),
        () -> assertEquals("TINKR", legs.get(5).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(6).section()),
        () -> assertEquals("KRAKN", legs.get(6).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(7).section()),
        () -> assertEquals("TWONA", legs.get(7).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(8).section()),
        () -> assertEquals("KNUKL", legs.get(8).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(8).pathTerminator()),

        () -> assertEquals("I17R", legs.get(9).section()),
        () -> assertEquals("RATOY", legs.get(9).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.DF, legs.get(9).pathTerminator()),

        () -> assertEquals("I17R", legs.get(10).section()),
        () -> assertEquals("SACRO", legs.get(10).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(11).section()),
        () -> assertEquals("TACOT", legs.get(11).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(12).section()),
        () -> assertEquals("DALTY", legs.get(12).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(13).section()),
        () -> assertEquals("ELLAN", legs.get(13).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(14).section()),
        () -> assertEquals("GLOSI", legs.get(14).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(15).section()),
        () -> assertEquals("MINCO", legs.get(15).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(16).section()),
        () -> assertEquals("RW17R", legs.get(16).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("KMCO", legs.get(17).section()),
        () -> assertEquals("KMCO", legs.get(17).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(18, legs.size())
    );
  }

  @Test
  void testFPApchASurly() {
    String route = "RSW.COSTR3.KMCO";

    Fix rsw = fix("RSW", 26.529875, -81.77576666666667);

    RouteExpander expander = newSurlyExpander(
        singletonList(rsw),
        emptyList(),
        singletonList(Airports.KMCO()),
        asList(COSTR3.INSTANCE, KMCO_I17R.I17R)
    );

    ExpandedRoute expandedRoute = expander.apply(route, null, "RW17R", RequiredNavigationEquipage.CONV).get();

    RouteSummary routeSummary = expandedRoute.routeSummary().orElseThrow(AssertionError::new);
    assertAll(
        "Check expanded STAR/Approach summary statistics.",
        () -> assertEquals("KMCO", routeSummary.arrivalAirport()),
        () -> assertEquals(Optional.of("RW17R"), routeSummary.arrivalRunway()),

        // STAR
        () -> assertEquals(Optional.of("COSTR3"), routeSummary.star()),
        () -> assertEquals(Optional.of("RSW"), routeSummary.starEntryFix()),
        () -> assertEquals(Optional.of("COSTR"), routeSummary.arrivalFix()),
        () -> assertEquals(Optional.of(RequiredNavigationEquipage.RNAV), routeSummary.requiredStarEquipage()),

        // Approach
        () -> assertEquals(Optional.of("I17R"), routeSummary.approach()),
        () -> assertEquals(Optional.of("RATOY"), routeSummary.approachEntryFix()),
        () -> assertEquals(Optional.of(RequiredNavigationEquipage.CONV), routeSummary.requiredApproachEquipage())
    );

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("COSTR3", legs.get(0).section()),
        () -> assertEquals("RSW", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(1).section()),
        () -> assertEquals("DOWNN", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(2).section()),
        () -> assertEquals("MOANS", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(3).section()),
        () -> assertEquals("COSTR", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(4).section()),
        () -> assertEquals("BIGGR", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(5).section()),
        () -> assertEquals("TINKR", legs.get(5).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(6).section()),
        () -> assertEquals("KRAKN", legs.get(6).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(7).section()),
        () -> assertEquals("TWONA", legs.get(7).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(8).section()),
        () -> assertEquals("KNUKL", legs.get(8).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(8).pathTerminator()),

        () -> assertEquals("I17R", legs.get(9).section()),
        () -> assertEquals("RATOY", legs.get(9).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.DF, legs.get(9).pathTerminator()),

        () -> assertEquals("I17R", legs.get(10).section()),
        () -> assertEquals("SACRO", legs.get(10).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(11).section()),
        () -> assertEquals("TACOT", legs.get(11).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(12).section()),
        () -> assertEquals("DALTY", legs.get(12).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(13).section()),
        () -> assertEquals("ELLAN", legs.get(13).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(14).section()),
        () -> assertEquals("GLOSI", legs.get(14).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(15).section()),
        () -> assertEquals("MINCO", legs.get(15).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(16).section()),
        () -> assertEquals("RW17R", legs.get(16).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("KMCO", legs.get(17).section()),
        () -> assertEquals("KMCO", legs.get(17).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(18, legs.size())
    );
  }

  /**
   * Test for a SID departure without a common portion.
   */
  @Test
  void testAPF_No_Common_Portion() {
    String route = "KATL.PLMMR2.SPA";

    Fix spa = fix("SPA", 35.033625, -81.92701111111111);

    RouteExpander expander = newExpander(
        singletonList(spa),
        emptyList(),
        singletonList(Airports.KATL()),
        Collections.singletonList(PLMMR2.INSTANCE)
    );

    ExpandedRoute expandedRoute = expander.apply(route, "RW09", null).get();

    RouteSummary routeSummary = expandedRoute.routeSummary().orElseThrow(AssertionError::new);

    assertAll(
        "Check expanded SID summary statistics.",
        () -> assertEquals("KATL", routeSummary.departureAirport()),
        () -> assertEquals(Optional.of("RW09"), routeSummary.departureRunway()),

        () -> assertEquals(Optional.of("PLMMR2"), routeSummary.sid()),
        () -> assertEquals(Optional.of("SPA"), routeSummary.sidExitFix())
    );

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("KATL", legs.get(0).section()),
        () -> assertEquals("KATL", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("PLMMR2", legs.get(1).section()),
        () -> assertEquals(PathTerminator.VA, legs.get(1).pathTerminator()),

        () -> assertEquals("PLMMR2", legs.get(2).section()),
        () -> assertEquals("LIDAS", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("PLMMR2", legs.get(3).section()),
        () -> assertEquals("ERWIN", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("PLMMR2", legs.get(4).section()),
        () -> assertEquals("PLMMR", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("PLMMR2", legs.get(5).section()),
        () -> assertEquals("TENSE", legs.get(5).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("PLMMR2", legs.get(6).section()),
        () -> assertEquals("NWANT", legs.get(6).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("PLMMR2", legs.get(7).section()),
        () -> assertEquals("SPA", legs.get(7).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(8, legs.size())
    );
  }

  /**
   * JIIMS3 STAR into KPHL has no common portion (and also has runway transitions serving multiple runways).
   */
  @Test
  void testAPF_STAR_No_Common_Portion1() {
    String route = "BRIGS.JIIMS3.KPHL";

    Fix spa = fix("BRIGS", 39.52353333333333, -74.13879722222222);

    RouteExpander expander = newExpander(
        singletonList(spa),
        emptyList(),
        singletonList(Airports.KPHL()),
        Collections.singletonList(JIIMS3.INSTANCE)
    );

    ExpandedRoute expandedRoute = expander.apply(route, null, "RW35").get();

    RouteSummary routeSummary = expandedRoute.routeSummary().orElseThrow(AssertionError::new);

    assertAll(
        "Check expanded STAR summary statistics.",
        () -> assertEquals("KPHL", routeSummary.arrivalAirport()),
        () -> assertEquals(Optional.of("RW35"), routeSummary.arrivalRunway()),

        () -> assertEquals(Optional.of("JIIMS3"), routeSummary.star()),
        () -> assertEquals(Optional.of("BRIGS"), routeSummary.starEntryFix())
    );

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("JIIMS3", legs.get(0).section()),
        () -> assertEquals("BRIGS", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("JIIMS3", legs.get(1).section()),
        () -> assertEquals("IROKT", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("JIIMS3", legs.get(2).section()),
        () -> assertEquals("JIIMS", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("JIIMS3", legs.get(3).section()),
        () -> assertEquals("SNEDE", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(3).pathTerminator()),

        () -> assertEquals("JIIMS3", legs.get(4).section()),
        () -> assertEquals("SNEDE", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.FM, legs.get(4).pathTerminator()),

        () -> assertEquals("KPHL", legs.get(5).section()),
        () -> assertEquals("KPHL", legs.get(5).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(6, legs.size())
    );
  }

  /**
   * JIIMS3 STAR into KPHL has no common portion (and also has runway transitions serving multiple runways).
   */
  @Test
  void testAPF_STAR_No_Common_Portion2() {
    String route = "SWL.JIIMS3.KPHL";

    Fix swl = fix("SWL", 38.05659444444444, -75.46390000000001);

    RouteExpander expander = newExpander(
        singletonList(swl),
        emptyList(),
        singletonList(Airports.KPHL()),
        Collections.singletonList(JIIMS3.INSTANCE)
    );

    ExpandedRoute expandedRoute = expander.apply(route, null, "RW35").get();

    RouteSummary routeSummary = expandedRoute.routeSummary().orElseThrow(AssertionError::new);

    assertAll(
        "Check expanded STAR summary statistics.",
        () -> assertEquals("KPHL", routeSummary.arrivalAirport()),
        () -> assertEquals(Optional.of("RW35"), routeSummary.arrivalRunway()),

        () -> assertEquals(Optional.of("JIIMS3"), routeSummary.star()),
        () -> assertEquals(Optional.of("SWL"), routeSummary.starEntryFix())
    );

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("JIIMS3", legs.get(0).section()),
        () -> assertEquals("SWL", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("JIIMS3", legs.get(1).section()),
        () -> assertEquals("RADDS", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("JIIMS3", legs.get(2).section()),
        () -> assertEquals("WNSTN", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("JIIMS3", legs.get(3).section()),
        () -> assertEquals("HEKMN", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("JIIMS3", legs.get(4).section()),
        () -> assertEquals("JIIMS", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("JIIMS3", legs.get(5).section()),
        () -> assertEquals("SNEDE", legs.get(5).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(5).pathTerminator()),

        () -> assertEquals("JIIMS3", legs.get(6).section()),
        () -> assertEquals("SNEDE", legs.get(6).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.FM, legs.get(6).pathTerminator()),

        () -> assertEquals("KPHL", legs.get(7).section()),
        () -> assertEquals("KPHL", legs.get(7).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(8, legs.size())
    );
  }

  /**
   * TDP-5731, TDP-5562
   */
  @Test
  void testStarExpansionExtraLeg() {
    String route = "KMCO./.SBY057030..JIIMS.JIIMS3.KPHL/1243";

    Fix sby = fix("SBY", 38.34500555555556, -75.510575);
    Fix jiims = fix("JIIMS", 39.53767222222222, -74.96714444444444);

    RouteExpander expander = newExpander(
        asList(sby, jiims),
        emptyList(),
        asList(Airports.KPHL(), Airports.KMCO()),
        Collections.singletonList(JIIMS3.INSTANCE)
    );

    ExpandedRoute expandedRoute = expander.apply(route, null, null).get();

    RouteSummary routeSummary = expandedRoute.routeSummary().orElseThrow(AssertionError::new);

    assertAll(
        "Check expanded STAR summary statistics.",
        () -> assertEquals("KMCO", routeSummary.departureAirport()),
        () -> assertEquals(Optional.empty(), routeSummary.departureRunway()),

        () -> assertEquals("KPHL", routeSummary.arrivalAirport()),
        () -> assertEquals(Optional.empty(), routeSummary.arrivalRunway()),

        () -> assertEquals(Optional.of("JIIMS3"), routeSummary.star()),
        () -> assertEquals(Optional.of("JIIMS"), routeSummary.starEntryFix())
    );

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("KMCO", legs.get(0).section()),
        () -> assertEquals("KMCO", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("SBY057030", legs.get(1).section()),
        () -> assertEquals("SBY057030", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("JIIMS3", legs.get(2).section()),
        () -> assertEquals("JIIMS", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.DF, legs.get(2).pathTerminator()),

        () -> assertEquals("KPHL", legs.get(3).section()),
        () -> assertEquals("KPHL", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(4, legs.size())
    );
  }

  /**
   * TDP-5731, TDP-5562
   */
  @Test
  void testSidExpansionExtraLeg() {
    String route = "KSEA.SUMMA2.SUMMA..JINMO";

    Fix summa = fix("SUMMA", 46.61785833333333, -121.98832222222222);
    Fix jinmo = fix("JINMO", 46.37138888888889, -122.12527777777777);

    RouteExpander expander = newExpander(
        asList(summa, jinmo),
        emptyList(),
        singletonList(Airports.KSEA()),
        Collections.singletonList(SUMMA2.INSTANCE)
    );

    ExpandedRoute expandedRoute = expander.apply(route, null, null).get();

    RouteSummary routeSummary = expandedRoute.routeSummary().orElseThrow(AssertionError::new);

    assertAll(
        "Check expanded STAR summary statistics.",
        () -> assertEquals("KSEA", routeSummary.departureAirport()),
        () -> assertEquals(Optional.empty(), routeSummary.departureRunway()),

        () -> assertEquals(Optional.of("SUMMA2"), routeSummary.sid()),
        () -> assertEquals(Optional.of("SUMMA"), routeSummary.sidExitFix())
    );

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("KSEA", legs.get(0).section()),
        () -> assertEquals("KSEA", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("SUMMA2", legs.get(1).section()),
        () -> assertEquals("SUMMA", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, legs.get(1).pathTerminator()),

        () -> assertEquals("JINMO", legs.get(2).section()),
        () -> assertEquals("JINMO", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(2).pathTerminator()),

        () -> assertEquals(3, legs.size())
    );
  }

  /**
   * TDP-5708
   */
  @Test
  void testStarIncorrectlyUsedAsAirwayEntryAndExitFix() {
    String route = "SAP.UG521.CZM.UB881.CUN.UM219.MYDIA";

    Fix sap = fix("SAP", 15.4636, -87.91696111111112);
    Fix czm = fix("CZM", 20.507472222222223, -86.912);
    Fix cun = fix("CUN", 21.025108333333332, -86.85871666666667);
    Fix mydia = fix("MYDIA", 24.04066388888889, -86.15824166666667);

    RouteExpander expander = newExpander(
        asList(sap, czm, cun, mydia),
        asList(Airways.UG521(), Airways.UB881(), Airways.UM219()),
        emptyList(),
        asList(CZM.INSTANCE, CUN.INSTANCE)
    );

    ExpandedRoute expandedRoute = expander.apply(route, null, null).get();

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("UG521", legs.get(0).section()),
        () -> assertEquals(ElementType.AIRWAY, legs.get(0).elementType()),
        () -> assertEquals("SAP", legs.get(0).associatedFix().get().fixIdentifier()),

        () -> assertEquals("UG521", legs.get(1).section()),
        () -> assertEquals(ElementType.AIRWAY, legs.get(1).elementType()),
        () -> assertEquals("SAP", legs.get(0).associatedFix().get().fixIdentifier()),

        () -> assertEquals("UG521", legs.get(2).section()),
        () -> assertEquals(ElementType.AIRWAY, legs.get(2).elementType()),
        () -> assertEquals("KIRAP", legs.get(2).associatedFix().get().fixIdentifier()),

        () -> assertEquals("UG521", legs.get(3).section()),
        () -> assertEquals(ElementType.AIRWAY, legs.get(3).elementType()),
        () -> assertEquals("AMIDA", legs.get(3).associatedFix().get().fixIdentifier()),

        () -> assertEquals("UG521", legs.get(4).section()),
        () -> assertEquals(ElementType.AIRWAY, legs.get(4).elementType()),
        () -> assertEquals("ITPIG", legs.get(4).associatedFix().get().fixIdentifier()),

        () -> assertEquals("UB881", legs.get(5).section()),
        () -> assertEquals(ElementType.AIRWAY, legs.get(5).elementType()),
        () -> assertEquals("CZM", legs.get(5).associatedFix().get().fixIdentifier()),

        () -> assertEquals("UB881", legs.get(6).section()),
        () -> assertEquals(ElementType.AIRWAY, legs.get(6).elementType()),
        () -> assertEquals("LIDEK", legs.get(6).associatedFix().get().fixIdentifier()),

        () -> assertEquals("UM219", legs.get(7).section()),
        () -> assertEquals(ElementType.AIRWAY, legs.get(7).elementType()),
        () -> assertEquals("CUN", legs.get(7).associatedFix().get().fixIdentifier()),

        () -> assertEquals("UM219", legs.get(8).section()),
        () -> assertEquals(ElementType.AIRWAY, legs.get(8).elementType()),
        () -> assertEquals("OMSUK", legs.get(8).associatedFix().get().fixIdentifier()),

        () -> assertEquals("UM219", legs.get(9).section()),
        () -> assertEquals(ElementType.AIRWAY, legs.get(9).elementType()),
        () -> assertEquals("ROTGI", legs.get(9).associatedFix().get().fixIdentifier()),

        () -> assertEquals("UM219", legs.get(10).section()),
        () -> assertEquals(ElementType.AIRWAY, legs.get(10).elementType()),
        () -> assertEquals("XOPGI", legs.get(10).associatedFix().get().fixIdentifier()),

        () -> assertEquals("UM219", legs.get(11).section()),
        () -> assertEquals(ElementType.AIRWAY, legs.get(11).elementType()),
        () -> assertEquals("RAKAR", legs.get(11).associatedFix().get().fixIdentifier()),

        () -> assertEquals("UM219", legs.get(12).section()),
        () -> assertEquals(ElementType.AIRWAY, legs.get(12).elementType()),
        () -> assertEquals("ALPUK", legs.get(12).associatedFix().get().fixIdentifier()),

        () -> assertEquals("UM219", legs.get(13).section()),
        () -> assertEquals(ElementType.AIRWAY, legs.get(13).elementType()),
        () -> assertEquals("MYDIA", legs.get(13).associatedFix().get().fixIdentifier())
    );
  }


  private RouteExpander newExpander(
      Collection<Fix> fixes,
      Collection<Airway> airways,
      Collection<Airport> airports,
      Collection<Procedure> procedures
  ) {
    return RouteExpander.inMemoryBuilder(airports, procedures, airways, fixes).build();
  }

  private RouteExpander newSurlyExpander(
      Collection<Fix> fixes,
      Collection<Airway> airways,
      Collection<Airport> airports,
      Collection<Procedure> procedures
  ) {
    return RouteExpander.inMemoryBuilder(airports, procedures, airways, fixes)
        .sectionResolver(RouteTokenResolver::surly)
        .build();
  }
}
