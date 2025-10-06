package org.mitre.tdp.boogie.estimates;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mitre.tdp.boogie.Airports.KATL;
import static org.mitre.tdp.boogie.Airports.KDEN;
import static org.mitre.tdp.boogie.MockObjects.fix;
import static org.mitre.tdp.boogie.PathTerminator.CF;
import static org.mitre.tdp.boogie.PathTerminator.VI;

import java.util.*;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.*;
import org.mitre.tdp.boogie.alg.facade.ExpandedRoute;
import org.mitre.tdp.boogie.alg.facade.ExpandedRouteLeg;
import org.mitre.tdp.boogie.alg.facade.FluentRouteExpander;
import org.mitre.tdp.boogie.alg.facade.InarticulateRouteExpander;
import org.mitre.tdp.boogie.pathtermination.AirportLegVisitor;
import org.mitre.tdp.boogie.pathtermination.LegPathEstimate;
import org.mitre.tdp.boogie.pathtermination.PathAndTermination;

import com.google.common.collect.Range;

public class LegPathEstimateTests {
  static LegPathEstimate estimator = LegPathEstimate.standard();

  private Leg ruwnayLeg(Airport airport, String runwayIdent) {
    Runway rw10 = airport.runways().stream().filter(i -> i.runwayIdentifier().equals(runwayIdent)).findFirst().orElseThrow(AssertionError::new);
    Fix rw10Fix = Fix.builder().fixIdentifier(runwayIdent).latLong(rw10.origin()).build();
    return Leg.ifBuilder(rw10Fix, 10)
        .altitudeConstraint(Range.atLeast(rw10.elevation().map(Distance::inFeet).orElseThrow()))
        .build();
  }

  /**
   * Test for a SID departure without a common portion.
   */
  @Test
  void testWithCILeg() {
    String route = "KATL.PLMMR2.SPA";

    Fix spa = fix("SPA", 35.033625, -81.92701111111111);

    InarticulateRouteExpander expander = newExpander(
        singletonList(spa),
        emptyList(),
        singletonList(Airports.KATL()),
        Collections.singletonList(PLMMR2.INSTANCE)
    );

    ExpandedRoute expandedRoute = expander.apply(route, "RW10", null).get();

    List<Leg> withRunway = expandedRoute.legs().stream()
        .map(ExpandedRouteLeg::leg)
        .map(l -> AirportLegVisitor.getAirport(l)
            .filter(a -> a.airportIdentifier().equals("KATL"))
            .filter(a -> a.runways().stream().anyMatch(r -> r.runwayIdentifier().equals("RW10")))
            .map(a -> ruwnayLeg(a, "RW10")).
            orElse(l))
        .toList();

    Map<Leg, PathAndTermination> pts = estimator.estimateAll(withRunway);
    PathAndTermination viLeg = pts.get(withRunway.get(1));
    PathAndTermination cfLeg = pts.get(withRunway.get(2));

    assertAll(
        () -> assertEquals(VI, withRunway.get(1).pathTerminator()),
        () -> assertEquals(CF, withRunway.get(2).pathTerminator()),
        () -> assertEquals(LatLong.of(33.6203, -84.4479), viLeg.startOfLeg()),
        () -> assertEquals(LatLong.of(33.62026170715851, -84.40034378603421), viLeg.endOfLeg()),
        () -> assertNotEquals(viLeg.endOfLeg(), cfLeg.startOfLeg(), "Close but each estimate is off by a bit"),
        () -> assertEquals(LatLong.of(33.62002598042024, -84.39852711789585), cfLeg.startOfLeg(), "Its counted back by the route distance"),
        () -> assertEquals(withRunway.get(2).associatedFix().get().latLong(), cfLeg.endOfLeg())
    );
  }

  @Test
  void testIntoStar() {
    String route = "DRSDN.HOBTT2.KATL";

    InarticulateRouteExpander expander = newExpander(
        singletonList(fix("DRSDN", 33.06475, -86.183083)),
        emptyList(),
        singletonList(KATL()),
        singletonList(HOBTT2.INSTANCE));

    ExpandedRoute expandedRoute = expander.apply(route, null, "RW27R").orElseThrow(IllegalStateException::new);

    List<Leg> withRunway = expandedRoute.legs().stream()
        .map(ExpandedRouteLeg::leg)
        .map(l -> AirportLegVisitor.getAirport(l)
            .filter(a -> a.airportIdentifier().equals("KATL"))
            .filter(a -> a.runways().stream().anyMatch(r -> r.runwayIdentifier().equals("RW27R")))
            .map(a -> ruwnayLeg(a, "RW27R")).
            orElse(l))
        .toList();

    Map<Leg, PathAndTermination> pts = estimator.estimateAll(withRunway);
    PathAndTermination fmLeg = pts.get(withRunway.get(12));

    assertAll(
        () -> assertEquals(fmLeg.startOfLeg(), fmLeg.endOfLeg())
    );
  }

  @Test
  void vaVMTest() {
    String route = "KDEN.CONNR5.DBL";

    InarticulateRouteExpander expander = newExpander(
        singletonList(fix("DBL", 39.439344444444444, -106.89468055555557)),
        emptyList(),
        singletonList(KDEN()),
        singletonList(CONNR5.INSTANCE));

    ExpandedRoute expandedRoute = expander.apply(route, "RW08", null).get();
    List<Leg> withRunway = expandedRoute.legs().stream()
        .map(ExpandedRouteLeg::leg)
        .map(l -> AirportLegVisitor.getAirport(l)
            .filter(a -> a.airportIdentifier().equals("KDEN"))
            .filter(a -> a.runways().stream().anyMatch(r -> r.runwayIdentifier().equals("RW08")))
            .map(a -> ruwnayLeg(a, "RW08")).
            orElse(l))
        .toList();

    Map<Leg, PathAndTermination> pts = estimator.estimateAll(withRunway);
    PathAndTermination vaLeg = pts.get(withRunway.get(1));
    PathAndTermination vmLeg = pts.get(withRunway.get(2));

    assertAll(
        () -> assertEquals("RW08", withRunway.get(0).associatedFix().get().fixIdentifier(), "its the runway"),
        () -> assertEquals(withRunway.get(0).associatedFix().get().latLong(), vaLeg.startOfLeg(), "matches the runway"),
        () -> assertEquals(1.1649, vaLeg.lengthNm(), .1, "its measured from start to end"),
        () -> assertEquals(vmLeg.startOfLeg(), vmLeg.endOfLeg(), "we are not adding length for these")
    );
  }

  @Test
  void vmToApproach() {
    String route = "LBV.COSTR3.KMCO";

    Fix lbv = fix("LBV", 26.828186111111112, -81.3914388888889);

    InarticulateRouteExpander expander = newExpander(
        singletonList(lbv),
        emptyList(),
        singletonList(Airports.KMCO()),
        List.of(COSTR3_VM.INSTANCE, KMCO_I17L.I17L, KMCO_I17R.I17R)
    );

    ExpandedRoute expandedRoute = expander.apply(route, null, "RW17R", RequiredNavigationEquipage.CONV).get();
    List<ExpandedRouteLeg> legs = expandedRoute.legs();

    Map<Leg, PathAndTermination> pathAndTermination = estimator.estimateAll(legs.stream().map(ExpandedRouteLeg::leg).collect(Collectors.toList()));

    assertAll(
        () -> assertTrue(pathAndTermination.isEmpty(), "lots of legs missing data")
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

}
