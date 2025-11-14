package org.mitre.tdp.boogie.alg.chooser;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.Airports.KDEN;
import static org.mitre.tdp.boogie.MockObjects.fix;

import java.util.Collection;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.CONNR5;
import org.mitre.tdp.boogie.CategoryAndType;
import org.mitre.tdp.boogie.CategoryOrType;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.chooser.graph.Expanders;
import org.mitre.tdp.boogie.alg.facade.ExpandedRoute;
import org.mitre.tdp.boogie.alg.facade.ExpandedRouteLeg;
import org.mitre.tdp.boogie.alg.facade.FluentRouteExpander;
import org.mitre.tdp.boogie.alg.facade.RouteDetails;
import org.mitre.tdp.boogie.alg.resolve.ElementType;

import com.google.common.collect.Lists;

/**
 * Explicit integration-y test for the {@link GraphicalRouteChooser} when applied through the {@link FluentRouteExpander} top
 * level class.
 */
class TestGraphicalRouteExpander {

  @Test
  void sameIdentDeparture() {
    FluentRouteExpander expander = Expanders.OTHER_DEPARTURE;
    String route = "OTHER.LEBA2A.LEBAT";
    ExpandedRoute expandedRoute = expander.apply(route, "RW88", null).orElseThrow();

    assertAll("There should only be a sid here",
        () -> assertTrue(expandedRoute.legs().stream().noneMatch(i -> ElementType.STAR.equals(i.elementType()))),
        () -> assertEquals(4, expandedRoute.legs().size())
    );
  }

  @Test
  void sameIdentNoTransitions() {
    FluentRouteExpander expander = Expanders.WSSS_ARRIVAL;

    String route = "PASPU.LEBA2A.WSSS";
    ExpandedRoute expandedRoute = expander.apply(route, null, "RW02C").orElseThrow();

    Map<String, Leg> legsByFix = expandedRoute.legs().stream()
        .filter(leg -> leg.associatedFix().isPresent())
        .collect(Collectors.toMap(leg -> leg.associatedFix().orElseThrow(IllegalStateException::new).fixIdentifier(), Function.identity()));

    assertAll("This used to resolve LEBA2A as a SID common into the STAR runway in case one ... oops",
        () -> assertEquals(5, expandedRoute.legs().size(), "Would be 3 legs if the routes were built up wrong"),
        () -> assertNotNull(legsByFix.get("PU"), "gets skipped if went to the star from the sid by mistake"),
        () -> assertNotNull(legsByFix.get("SJ"), "gets skipped if went to the tar from teh sid by mistake"),
        () -> assertTrue(expandedRoute.legs().stream().noneMatch(l -> l.elementType().equals(ElementType.SID)))
    );
  }

  @Test
  void testSubsequentDFToTFConverterIsAppliedWithinExpansion2() {
    String route = "KDEN.CONNR5.DBL..OTHER";

    FluentRouteExpander expander = newExpander(
        List.of(fix("DBL", 39.439344444444444, -106.89468055555557), fix("OTHER", 39.439344444444444, -106.89478055555557)),
        emptyList(),
        singletonList(KDEN()),
        singletonList(CONNR5.INSTANCE));

    RouteDetails jetDetails = RouteDetails.builder()
        .categoryOrTypes(new CategoryAndType(CategoryOrType.JET, CategoryOrType.CAT_C))
        .departureRunway("RW25R")
        .build();

    ExpandedRoute expandedRoute = expander.expand(route, jetDetails).orElseThrow(AssertionError::new);

    Map<String, ExpandedRouteLeg> legsByFix = expandedRoute.legs().stream()
        .filter(leg -> leg.associatedFix().isPresent())
        .collect(Collectors.toMap(leg -> leg.associatedFix().orElseThrow(IllegalStateException::new).fixIdentifier(), Function.identity()));

    RouteDetails copterDetails = RouteDetails.builder()
        .categoryOrTypes(new CategoryAndType(CategoryOrType.CAT_H, CategoryOrType.CAT_H))
        .departureRunway("RW17L")
        .build();

    ExpandedRoute copterRoute = expander.expand(route, copterDetails).orElseThrow(AssertionError::new);

    RouteDetails brokeDetails = RouteDetails.builder()
        .categoryOrTypes(new CategoryAndType(CategoryOrType.CAT_H, CategoryOrType.CAT_H))
        .departureRunway("RW25R")
        .build(); //aka the procedure only has a common for us ... but we do our best
    Optional<ExpandedRoute> broke = expander.expand(route, brokeDetails);

    assertAll("Before and After the processor",
        () -> assertEquals(PathTerminator.TF, legsByFix.get("OTHER").pathTerminator(), "Filed direct leg should be TF."),
        () -> assertEquals(PathTerminator.DF, legsByFix.get("WRIPS").pathTerminator(), "DF filed after VA as part of SID should be unchanged."),
        () -> assertEquals(50.0, copterRoute.legs().get(copterRoute.legs().size() - 2).leg().speedConstraint().upperEndpoint(), "Just to prove we kept the copter leg"),
        () -> assertTrue(broke.isPresent(), "It is going to grab what it can from the common no enroute here to get at least something")
    );
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