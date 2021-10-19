package org.mitre.tdp.boogie.alg;

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

import java.util.Arrays;
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
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.HOBTT2;
import org.mitre.tdp.boogie.JIIMS3;
import org.mitre.tdp.boogie.KMCO_I17R;
import org.mitre.tdp.boogie.PLMMR2;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.RequiredNavigationEquipage;
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
  void testExitsEarlyWithNoResolvedElements() {
    String route = "KDEN.CONNR5.DBL";

    RouteExpander expander = newExpander(emptyList(), emptyList(), emptyList(), emptyList());
    assertFalse(expander.apply(route).isPresent());
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
        () -> assertEquals("DRSDN", legs.get(0).section()),
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
        Arrays.asList(
            fix("JMACK", 33.98850277777778, -78.96658333333333),
            fix("KALDA", 37.84195833333334, -75.62648333333333)),
        singletonList(Airways.J121()),
        emptyList(),
        emptyList());

    ExpandedRoute expandedRoute = expander.apply(route).get();

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("JMACK", legs.get(0).section()),
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
        Arrays.asList(
            fix("JMACK", 33.98850277777778, -78.96658333333333),
            fix("KALDA", 37.84195833333334, -75.62648333333333)),
        singletonList(Airways.J121()),
        emptyList(),
        emptyList());

    ExpandedRoute expandedRoute = expander.apply(route).get();

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("KALDA", legs.get(0).section()),
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
        Arrays.asList(
            fix("MILIE", 31.328622222222222, -81.17371944444444),
            fix("BARTL", 34.303177777777776, -78.65149444444445),
            fix("ORF", 36.89190555555555, -76.20032777777779)),
        singletonList(Airways.J121()),
        emptyList(),
        emptyList());

    ExpandedRoute expandedRoute = expander.apply(route).get();

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("MILIE", legs.get(0).section()),
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
        Arrays.asList(
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
        () -> assertEquals(PathTerminator.DF, legs.get(1).pathTerminator()),
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
        () -> assertEquals("BARTL", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null))
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
        () -> assertEquals(PathTerminator.DF, legs.get(1).pathTerminator()),
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
        () -> assertEquals("RSW", legs.get(0).section()),
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
  void testFPApchA() {
    String route = "RSW.COSTR3.KMCO";

    Fix rsw = fix("RSW", 26.529875, -81.77576666666667);

    RouteExpander expander = newExpander(
        singletonList(rsw),
        emptyList(),
        singletonList(Airports.KMCO()),
        Arrays.asList(COSTR3.INSTANCE, KMCO_I17R.I17R)
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
        () -> assertEquals("RSW", legs.get(0).section()),
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

        () -> assertEquals("I17R", legs.get(10).section()),
        () -> assertEquals("RATOY", legs.get(10).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(11).section()),
        () -> assertEquals("SACRO", legs.get(11).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(12).section()),
        () -> assertEquals("TACOT", legs.get(12).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(13).section()),
        () -> assertEquals("DALTY", legs.get(13).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(14).section()),
        () -> assertEquals("ELLAN", legs.get(14).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(15).section()),
        () -> assertEquals("GLOSI", legs.get(15).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(16).section()),
        () -> assertEquals("MINCO", legs.get(16).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(17).section()),
        () -> assertEquals("RW17R", legs.get(17).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("KMCO", legs.get(18).section()),
        () -> assertEquals("KMCO", legs.get(18).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(19, legs.size())
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
        () -> assertEquals("BRIGS", legs.get(0).section()),
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

    Fix spa = fix("SWL", 38.05659444444444, -75.46390000000001);

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
        () -> assertEquals(Optional.of("SWL"), routeSummary.starEntryFix())
    );

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("SWL", legs.get(0).section()),
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

  private RouteExpander newExpander(
      Collection<? extends Fix> fixes,
      Collection<? extends Airway> airways,
      Collection<? extends Airport> airports,
      Collection<? extends Procedure> procedures
  ) {
    return RouteExpanderFactory.newGraphicalRouteExpander(fixes, airways, airports, procedures);
  }
}
