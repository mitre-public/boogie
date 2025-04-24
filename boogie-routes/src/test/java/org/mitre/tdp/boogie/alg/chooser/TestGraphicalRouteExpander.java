package org.mitre.tdp.boogie.alg.chooser;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mitre.tdp.boogie.Airports.KDEN;
import static org.mitre.tdp.boogie.MockObjects.*;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.*;
import org.mitre.tdp.boogie.alg.chooser.graph.Expanders;
import org.mitre.tdp.boogie.alg.facade.ExpandedRoute;
import org.mitre.tdp.boogie.alg.facade.ExpandedRouteLeg;
import org.mitre.tdp.boogie.alg.facade.FluentRouteExpander;

import com.google.common.collect.Lists;

/**
 * Explicit integration-y test for the {@link GraphicalRouteChooser} when applied through the {@link FluentRouteExpander} top
 * level class.
 */
class TestGraphicalRouteExpander {

  @Test
  void sameIdentNoTransitions() {
    FluentRouteExpander expander = Expanders.wsssExpander();

    String route = "PASPU.LEBA2A.WSSS";
    ExpandedRoute expandedRoute = expander.apply(route, null, "RW02C").orElseThrow();

    Map<String, Leg> legsByFix = expandedRoute.legs().stream()
        .filter(leg -> leg.associatedFix().isPresent())
        .collect(Collectors.toMap(leg -> leg.associatedFix().orElseThrow(IllegalStateException::new).fixIdentifier(), Function.identity()));

    String route1 = "OTHER.LEBA2A.LEBAT";
    ExpandedRoute expandedRoute1 = expander.apply(route1, "RW22", null).orElseThrow();

    Map<String, Leg> legsByFix1 = expandedRoute1.legs().stream()
        .filter(leg -> leg.associatedFix().isPresent())
        .collect(Collectors.toMap(leg -> leg.associatedFix().orElseThrow(IllegalStateException::new).fixIdentifier(), Function.identity()));

    assertAll("This used to resolve LEBA2A as a SID common into the STAR runway in case one ... oops",
        () -> assertEquals(5, expandedRoute.legs().size(), "Would be 3 legs if the routes were built up wrong"),
        () -> assertEquals(4, expandedRoute1.legs().size(), "Same 3 leg issue here if the route was wrong"),
        () -> assertNotNull(legsByFix.get("PU"), "gets skipped if went to the star from the sid by mistake"),
        () -> assertNotNull(legsByFix.get("SJ"), "gets skipped if went to the tar from teh sid by mistake"),
        () -> assertNotNull(legsByFix1.get("LEBAT")),
        () -> assertNotNull(legsByFix1.get("BV414"), "would get skipped if it tried to jump to the star")
    );
  }

  @Test
  void testSubsequentDFToTFConverterIsAppliedWithinExpansion() {
    String route = "KDEN.CONNR5.DBL..OTHER";

    FluentRouteExpander expander = newExpander(
        Lists.newArrayList(fix("DBL", 39.439344444444444, -106.89468055555557), fix("OTHER", 39.439344444444444, -106.89478055555557)),
        emptyList(),
        singletonList(KDEN()),
        singletonList(CONNR5.INSTANCE));

    ExpandedRoute expandedRoute = expander.apply(route, "RW25R", null).orElseThrow(IllegalStateException::new);

    Map<String, ExpandedRouteLeg> legsByFix = expandedRoute.legs().stream()
        .filter(leg -> leg.associatedFix().isPresent())
        .collect(Collectors.toMap(leg -> leg.associatedFix().orElseThrow(IllegalStateException::new).fixIdentifier(), Function.identity()));

    assertAll("Before and After the processor",
        () -> assertEquals(PathTerminator.TF, legsByFix.get("OTHER").pathTerminator(), "Filed direct leg should be TF."),
        () -> assertEquals(PathTerminator.DF, legsByFix.get("WRIPS").pathTerminator(), "DF filed after VA as part of SID should be unchanged."));
  }

  private static FluentRouteExpander newExpander(
      Collection<? extends Fix> fixes,
      Collection<? extends Airway> airways,
      Collection<? extends Airport> airports,
      Collection<? extends Procedure> procedures
  ) {
    return FluentRouteExpander.inMemoryBuilder(airports, procedures, airways, fixes).build();
  }
}