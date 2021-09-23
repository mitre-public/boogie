package org.mitre.tdp.boogie.alg;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.*;
import static org.mitre.tdp.boogie.Airports.KDEN;
import static org.mitre.tdp.boogie.MockObjects.fix;

import java.util.Collection;

import org.junit.jupiter.api.Test;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.CONNR5;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.Procedure;

import com.google.common.collect.Lists;

class DirectToFixPostProcessorTest {

  @Test
  void apply() {
    String route = "KDEN.CONNR5.DBL..OTHER";

    RouteExpander expander = newExpander(
        Lists.newArrayList(fix("DBL", 39.439344444444444, -106.89468055555557), fix("OTHER", 39.439344444444444, -106.89478055555557)),
        emptyList(),
        singletonList(KDEN()),
        singletonList(CONNR5.INSTANCE));

    ExpandedRoute expandedRoute = expander.apply(route, "RW16R", null).orElseThrow(IllegalStateException::new);
    ExpandedRoute alteredRoute = expandedRoute.postProcess(DirectToFixPostProcessor.INSTANCE);

    assertAll("Before and After the processor",
        () -> assertEquals(PathTerminator.DF, expandedRoute.legs().get(expandedRoute.legs().size() - 1).pathTerminator()),
        () -> assertEquals(PathTerminator.TF, alteredRoute.legs().get(alteredRoute.legs().size() - 1).pathTerminator()));
  }

  @Test
  void applyWithoutCondition() {
    String route = "KDEN.CONNR5.DBL";

    RouteExpander expander = newExpander(
        Lists.newArrayList(fix("DBL", 39.439344444444444, -106.89468055555557), fix("OTHER", 39.439344444444444, -106.89478055555557)),
        emptyList(),
        singletonList(KDEN()),
        singletonList(CONNR5.INSTANCE));

    ExpandedRoute expandedRoute = expander.apply(route, "RW16R", null).orElseThrow(IllegalStateException::new);
    ExpandedRoute alteredRoute = expandedRoute.postProcess(DirectToFixPostProcessor.INSTANCE);

    assertEquals(expandedRoute, alteredRoute);
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