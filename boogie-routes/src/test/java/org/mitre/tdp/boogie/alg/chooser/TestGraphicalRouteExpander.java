package org.mitre.tdp.boogie.alg.chooser;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mitre.tdp.boogie.Airports.KDEN;
import static org.mitre.tdp.boogie.MockObjects.fix;

import java.util.Collection;
import java.util.Map;
import java.util.function.Function;
import java.util.stream.Collectors;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.CONNR5;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;
import org.mitre.tdp.boogie.alg.ExpandedRoute;
import org.mitre.tdp.boogie.alg.ExpandedRouteLeg;
import org.mitre.tdp.boogie.alg.RouteExpander;

import com.google.common.collect.Lists;

/**
 * Explicit integration-y test for the {@link GraphBasedRouteChooser} when applied through the {@link RouteExpander} top
 * level class.
 */
class TestGraphicalRouteExpander {

  @Test
  void testSubsequentDFToTFConverterIsAppliedWithinExpansion() {
    String route = "KDEN.CONNR5.DBL..OTHER";

    RouteExpander expander = newExpander(
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

  private RouteExpander newExpander(
      Collection<? extends Fix> fixes,
      Collection<? extends Airway> airways,
      Collection<? extends Airport> airports,
      Collection<? extends Procedure> procedures
  ) {
    return RouteExpander.inMemoryBuilder(airports, procedures, airways, fixes).build();
  }
}