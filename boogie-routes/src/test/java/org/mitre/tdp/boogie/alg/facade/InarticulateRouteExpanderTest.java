package org.mitre.tdp.boogie.alg.facade;

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

import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.*;
import org.mitre.tdp.boogie.alg.resolve.RouteTokenResolver;
import org.mitre.tdp.boogie.alg.split.Wildcard;

/**
 * Route inflation tests (the full package) used to test specific component expansions.
 *
 * <p>Test names are abbreviated based on the composed infrastructure elements:
 * <ol>
 *   <li>A - Airport</li>
 *   <li>W - Airway</li>
 *   <li>F - Fix</li>
 *   <li>L - LatLon</li>
 *   <li>P - Procedure</li>
 *   <li>T - Tailored Waypoint</li>
 * </ol>
 * <p>e.g. TestAPF would indicate a test for Airport.Procedure.Fix one of the more common composite route elements.
 */
class InarticulateRouteExpanderTest {

  @Test
  void noCommonOrNext() {
    String route = "WSSS.CHA1C.ANITO";
    Fix anito = fix("ANITO", -0.2833, 104.8667);
    InarticulateRouteExpander expander = newExpander(
        List.of(anito),
        emptyList(),
        List.of(Airports.WSSS()),
        List.of(CHA1C_NO_COMMON.INSTANCE)
    );
    ExpandedRoute expandedRoute = expander.apply(route, "RW02", null).orElseThrow();
    List<ExpandedRouteLeg> legs = expandedRoute.legs();
    ExpandedRoute two = expander.apply(route, "RW03", null).orElseThrow();
    List<ExpandedRouteLeg> legs2 = two.legs();
    assertAll(
        () -> assertEquals(4, legs.size(), "Should be: WSSS -> VA -> VM -> WSSS -> FIX -> FIX"),
        () -> assertEquals(4, legs2.size(), "Same thing but with an FM leg")
    );
  }


  @Test
  void testNoWeight() {
    String route = "WSSS.CHA1C.ANITO";
    Fix anito = fix("ANITO", -0.2833, 104.8667);
    InarticulateRouteExpander expander = newExpander(
        List.of(anito),
        emptyList(),
        List.of(Airports.WSSS()),
        List.of(CHA1C_WSSS_PARTIAL.INSTANCE)
    );
    ExpandedRoute expandedRoute = expander.apply(route, "RW02", null).orElseThrow();
    List<ExpandedRouteLeg> legs = expandedRoute.legs();
    ExpandedRoute two = expander.apply(route, "RW03", null).orElseThrow();
    List<ExpandedRouteLeg> legs2 = two.legs();
    assertAll(
        () -> assertEquals(7, legs.size(), "Should be: WSSS -> VA -> VM -> WSSS -> FIX -> FIX -> ANITO-AGAIN"),
        () -> assertEquals(7, legs2.size(), "Same thing but with an FM leg")
    );
  }

  /**
   * Now we added a costr3 modck that has a duplicate LBV transition in it
   */
  @Test
  void testMultipleStarTrans() {
    String route = "LBV.COSTR3.KMCO";

    Fix lbv = fix("LBV", 26.828186111111112, -81.3914388888889);

    InarticulateRouteExpander expander = newExpander(
        singletonList(lbv),
        emptyList(),
        singletonList(Airports.KMCO()),
        singletonList(COSTR3_DUP.INSTANCE));

    ExpandedRoute expandedRoute = expander.apply(route).get();
    List<ExpandedRouteLeg> legs = expandedRoute.legs();
    assertAll(
        () -> assertEquals(13, legs.size(), "this makes sure we can expand an easy route without caring about category or type")
    );
  }

  //this is disabled until we get the weights on transitions working better
  @Disabled
  @Test
  void testMultipleApproachTransition() {
    Fix ot511 = fix("OT511", 18.1294, -15.8593);
    String route = "OT511..GQNO";
    InarticulateRouteExpander expander = newExpander(
        singletonList(ot511),
        emptyList(),
        singletonList(Airports.GQNO()),
        singletonList(GQNO_D34Y.INSTANCE)
    );
    RouteDetails routeDetails = RouteDetails.builder()
        .arrivalRunway("RW34")
        .equipagePreference(RequiredNavigationEquipage.CONV)
        .build();
    ExpandedRoute expandedRoute = expander.expand(route, routeDetails).orElseThrow();
    assertAll(
        () -> assertEquals("OT", expandedRoute.legs().get(0).associatedFix().get().fixIdentifier())
    );
  }

  @Test
  void testOddComboDoesNotHappen() {
    Fix start = fix("START", 33.3336, -84.7909);
    Fix end = fix("ENDDD", 33.5496, -84.1357);

    String route = "KATL..START..ZALLE.J000.GGOLF..ENDDD..KMCO"; //these are also on the sid

    InarticulateRouteExpander expander = newExpander(
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

    InarticulateRouteExpander expander = newExpander(emptyList(), emptyList(), emptyList(), emptyList());
    assertFalse(expander.apply(route).isPresent());
  }

  @Test
  void surlyDropTest() {
    String route = "KDEN.CONNR5.DBL";

    InarticulateRouteExpander expander = newSurlyExpander(
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
  void testFPA_NoProcedureMatch() {
    String route = "DRSDN.HOBTT3.KATL";

    InarticulateRouteExpander expander = newExpander(
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
  void testLongLatLong() {
    String route = "MILIE..0530N13946E..BARTL";
    InarticulateRouteExpander expander = newExpander(
        asList(
            fix("MILIE", 31.328622222222222, -81.17371944444444),
            fix("BARTL", 34.303177777777776, -78.65149444444445)),
        emptyList(),
        emptyList(),
        emptyList());

    ExpandedRoute expandedRoute = expander.apply(route).get();
    assertEquals(LatLong.of(5.5, 139.76666666666668), expandedRoute.legs().get(1).associatedFix().get().latLong());
  }

  @Test
  void testALF() {
    String route = "KDEN./.2200N/12000W..BARTL";

    InarticulateRouteExpander expander = newExpander(
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
        () -> assertEquals(LatLong.of(22., -120.), legs.get(1).associatedFix().map(Fix::latLong).orElse(null)),
        () -> assertEquals(PathTerminator.IF, legs.get(1).pathTerminator()),

        () -> assertEquals("BARTL", legs.get(2).section()),
        () -> assertEquals("BARTL", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null))
    );
  }

  @Test
  void testATF() {
    String route = "KDEN./.BARTL031018..BARTL";

    Fix bartl = fix("BARTL", 34.303177777777776, -78.65149444444445);

    InarticulateRouteExpander expander = newExpander(
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
  @Disabled("Jury is out on how we want this to work - having FixTerminationLeg w/ a true hashCode means this just returns BARTL, "
      + "we could rewrite and still capture the spirit of the test but do we want to write a new test that checks for identity mattering?")
  void testFTF() {
    String route = "BARTL..BARTL125045..BARTL";

    Fix bartl = fix("BARTL", 34.303177777777776, -78.65149444444445);

    InarticulateRouteExpander expander = newExpander(
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

  @Test
  void joinApproachAt() {
    Fix mmcap = fix("MMCAP", 33.6341, -84.0600);
    Fix youyu = fix("YOUYU", 33.6339, -83.9911);
    Fix david = fix("DAVID", 33.6858, -83.9001);

    InarticulateRouteExpander expander = newExpander(
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

    InarticulateRouteExpander expander = newExpander(
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
        () -> assertEquals("RSW", legs.get(0).section()),
        () -> assertEquals("RSW", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(1).section()),
        () -> assertEquals("RSW", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, legs.get(1).pathTerminator()),

        () -> assertEquals("COSTR3", legs.get(2).section()),
        () -> assertEquals("DOWNN", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(3).section()),
        () -> assertEquals("MOANS", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(4).section()),
        () -> assertEquals("COSTR", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(4).pathTerminator()),

        () -> assertEquals("COSTR3", legs.get(5).section()),
        () -> assertEquals("COSTR", legs.get(5).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, legs.get(5).pathTerminator()),

        () -> assertEquals("COSTR3", legs.get(10).section()),
        () -> assertEquals("KNUKL", legs.get(10).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(10).pathTerminator(), "For now because we get rid of the FM in the gluer until that is changed."),

        () -> assertEquals("I17R", legs.get(11).section()),
        () -> assertEquals("RATOY", legs.get(11).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.DF, legs.get(11).pathTerminator()),

        () -> assertEquals("I17R", legs.get(12).section()),
        () -> assertEquals("RATOY", legs.get(12).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, legs.get(12).pathTerminator()),

        () -> assertEquals("I17R", legs.get(13).section()),
        () -> assertEquals("SACRO", legs.get(13).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.CF, legs.get(13).pathTerminator()),

        () -> assertEquals("I17R", legs.get(14).section()),
        () -> assertEquals("SACRO", legs.get(14).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, legs.get(14).pathTerminator()),

        () -> assertEquals("I17R", legs.get(15).section()),
        () -> assertEquals("TACOT", legs.get(15).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(16).section()),
        () -> assertEquals("DALTY", legs.get(16).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(17).section()),
        () -> assertEquals("ELLAN", legs.get(17).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(18).section()),
        () -> assertEquals("GLOSI", legs.get(18).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(19).section()),
        () -> assertEquals("MINCO", legs.get(19).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(20).section()),
        () -> assertEquals("RW17R", legs.get(20).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("KMCO", legs.get(21).section()),
        () -> assertEquals("KMCO", legs.get(21).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(22, legs.size())
    );
  }

  @Test
  void testFPApchASurly() {
    String route = "RSW.COSTR3.KMCO";

    Fix rsw = fix("RSW", 26.529875, -81.77576666666667);

    InarticulateRouteExpander expander = newSurlyExpander(
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
        () -> assertEquals("RSW", legs.get(0).section()),
        () -> assertEquals("RSW", legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(1).section()),
        () -> assertEquals("RSW", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, legs.get(1).pathTerminator()),

        () -> assertEquals("COSTR3", legs.get(2).section()),
        () -> assertEquals("DOWNN", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(3).section()),
        () -> assertEquals("MOANS", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("COSTR3", legs.get(4).section()),
        () -> assertEquals("COSTR", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(4).pathTerminator()),

        () -> assertEquals("COSTR3", legs.get(5).section()),
        () -> assertEquals("COSTR", legs.get(5).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, legs.get(5).pathTerminator()),

        () -> assertEquals("COSTR3", legs.get(10).section()),
        () -> assertEquals("KNUKL", legs.get(10).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(10).pathTerminator(), "For now because we get rid of the FM in the gluer until that is changed."),

        () -> assertEquals("I17R", legs.get(11).section()),
        () -> assertEquals("RATOY", legs.get(11).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.DF, legs.get(11).pathTerminator()),

        () -> assertEquals("I17R", legs.get(12).section()),
        () -> assertEquals("RATOY", legs.get(12).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, legs.get(12).pathTerminator()),

        () -> assertEquals("I17R", legs.get(13).section()),
        () -> assertEquals("SACRO", legs.get(13).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.CF, legs.get(13).pathTerminator()),

        () -> assertEquals("I17R", legs.get(14).section()),
        () -> assertEquals("SACRO", legs.get(14).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.IF, legs.get(14).pathTerminator()),

        () -> assertEquals("I17R", legs.get(15).section()),
        () -> assertEquals("TACOT", legs.get(15).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(16).section()),
        () -> assertEquals("DALTY", legs.get(16).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(17).section()),
        () -> assertEquals("ELLAN", legs.get(17).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(18).section()),
        () -> assertEquals("GLOSI", legs.get(18).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(19).section()),
        () -> assertEquals("MINCO", legs.get(19).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("I17R", legs.get(20).section()),
        () -> assertEquals("RW17R", legs.get(20).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("KMCO", legs.get(21).section()),
        () -> assertEquals("KMCO", legs.get(21).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(22, legs.size())
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

    InarticulateRouteExpander expander = newExpander(
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

        () -> assertEquals("JIIMS", legs.get(2).section(), "This is the fix in the procedure"),
        () -> assertEquals("JIIMS", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.DF, legs.get(2).pathTerminator()),

        () -> assertEquals("JIIMS3", legs.get(3).section(), "This is the fix in the procedure"),
        () -> assertEquals("JIIMS", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.TF, legs.get(3).pathTerminator(), "last leg in the transition but we found it"),

        () -> assertEquals("KPHL", legs.get(4).section()),
        () -> assertEquals("KPHL", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals(5, legs.size())
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

    InarticulateRouteExpander expander = newExpander(
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

        () -> assertEquals("SUMMA", legs.get(2).section()),
        () -> assertEquals("SUMMA", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.DF, legs.get(2).pathTerminator()),

        () -> assertEquals("JINMO", legs.get(3).section()),
        () -> assertEquals("JINMO", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.DF, legs.get(3).pathTerminator()),

        () -> assertEquals(4, legs.size())
    );
  }

  @Test
  void testFAF() {
    String route = "JMACK.J121.KALDA";

    InarticulateRouteExpander expander = newExpander(
        asList(
            fix("JMACK", 33.98850277777778, -78.96658333333333),
            fix("KALDA", 37.84195833333334, -75.62648333333333)),
        singletonList(Airways.J121()),
        emptyList(),
        emptyList());

    ExpandedRoute expandedRoute = expander.apply(route).get();

    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    assertAll(
        () -> assertEquals("JMACK", legs.get(0).section()),
        () -> assertEquals("JMACK",  legs.get(0).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(1).section()),
        () -> assertEquals("JMACK", legs.get(1).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(2).section()),
        () -> assertEquals("BARTL", legs.get(2).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(3).section()),
        () -> assertEquals("ISO", legs.get(3).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(4).section()),
        () -> assertEquals("WEAVR", legs.get(4).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(5).section()),
        () -> assertEquals("ORF", legs.get(5).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(6).section()),
        () -> assertEquals("SAWED", legs.get(6).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("J121", legs.get(7).section()),
        () -> assertEquals("KALDA", legs.get(7).associatedFix().map(Fix::fixIdentifier).orElse(null)),

        () -> assertEquals("KALDA", legs.get(8).section()),
        () -> assertEquals("KALDA", legs.get(8).associatedFix().map(Fix::fixIdentifier).orElse(null)),
        () -> assertEquals(PathTerminator.DF, legs.get(8).pathTerminator()),

        () -> assertEquals(9, legs.size())
    );
  }

  private InarticulateRouteExpander newExpander(
      Collection<Fix> fixes,
      Collection<Airway> airways,
      Collection<Airport> airports,
      Collection<Procedure> procedures
  ) {
    return InarticulateRouteExpander.inMemoryBuilder(airports, procedures, airways, fixes).build();
  }

  private InarticulateRouteExpander newSurlyExpander(
      Collection<Fix> fixes,
      Collection<Airway> airways,
      Collection<Airport> airports,
      Collection<Procedure> procedures
  ) {
    return InarticulateRouteExpander.inMemoryBuilder(airports, procedures, airways, fixes)
        .routeTokenResolver(RouteTokenResolver::surly)
        .build();
  }
}
