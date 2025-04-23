package org.mitre.tdp.boogie.alg.chooser;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
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
import org.mitre.tdp.boogie.alg.facade.ExpandedRoute;
import org.mitre.tdp.boogie.alg.facade.ExpandedRouteLeg;
import org.mitre.tdp.boogie.alg.facade.FluentRouteExpander;

import com.google.common.collect.Lists;

/**
 * Explicit integration-y test for the {@link GraphicalRouteChooser} when applied through the {@link FluentRouteExpander} top
 * level class.
 */
class TestGraphicalRouteExpander {
  static FluentRouteExpander wsssExpander() {
    Airport wsss = MockObjects.airport("WSSS", 0.0, 6.0);

    Fix bv413 = Fix.builder().fixIdentifier("BV413").latLong(LatLong.of(5.0, 5.0)).build();
    Fix bv414 = Fix.builder().fixIdentifier("BV414").latLong(LatLong.of(5.0, 6.0)).build();
    Fix lebat = Fix.builder().fixIdentifier("LEBAT").latLong(LatLong.of(5.0, 7.0)).build();

    Leg l10 = Leg.ifBuilder(bv413, 0).build();
    Leg l11 = Leg.dfBuilder(bv414, 1).build();
    Leg l12 = Leg.dfBuilder(lebat, 2).build();

    Fix paspu = Fix.builder().fixIdentifier("PASPU").latLong(LatLong.of(0.0, 2.0)).build();
    Fix pu = Fix.builder().fixIdentifier("PU").latLong(LatLong.of(0.0, 3.0)).build();
    Fix sj = Fix.builder().fixIdentifier("SJ").latLong(LatLong.of(0.0, 4.0)).build();
    Fix palga = Fix.builder().fixIdentifier("PALGA").latLong(LatLong.of(0.0, 5.0)).build();

    Leg l2 = Leg.ifBuilder(paspu, 0).build();
    Leg l3 = Leg.tfBuilder(pu, 1).build();
    Leg l4 = Leg.tfBuilder(sj, 2).build();
    Leg l5 = Leg.tfBuilder(palga, 3).build();

    List<Leg> starLegs = List.of(l2, l3, l4, l5);
    List<Leg> sidLegs = List.of(l10, l11, l12);

    Transition sidTrans = Transition.builder()
        .transitionIdentifier("ALL")
        .transitionType(TransitionType.COMMON)
        .legs(sidLegs)
        .build();
    Transition starTrans = Transition.builder()
        .transitionIdentifier("RW02C")
        .transitionType(TransitionType.RUNWAY)
        .legs(starLegs)
        .build();

    Procedure sid = Procedure.builder()
        .procedureIdentifier("LEBA2A")
        .procedureType(ProcedureType.SID)
        .transitions(List.of(sidTrans))
        .airportIdentifier("OTHER")
        .requiredNavigationEquipage(RequiredNavigationEquipage.RNAV)
        .build();

    Procedure star = Procedure.builder()
        .procedureIdentifier("LEBA2A")
        .procedureType(ProcedureType.STAR)
        .transitions(List.of(starTrans))
        .airportIdentifier("WSSS")
        .requiredNavigationEquipage(RequiredNavigationEquipage.RNAV)
        .build();

    return FluentRouteExpander.inMemoryBuilder(List.of(wsss), List.of(sid, star), List.of(), List.of(paspu)).build();
  }

  @Test
  void sameIdentTest() {
    String route = "PASPU.LEBA2A.WSSS";
    FluentRouteExpander expander = wsssExpander();
    ExpandedRoute expandedRoute = expander.apply(route, null, "RW02C").orElseThrow();

    assertAll("the same name but clearly different routes",
        () -> assertEquals(5, expandedRoute.legs().size())
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