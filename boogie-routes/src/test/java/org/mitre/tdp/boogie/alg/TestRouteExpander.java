package org.mitre.tdp.boogie.alg;

import static java.util.Collections.emptyList;
import static java.util.Collections.singletonList;
import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertNotEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.test.Airports.KATL;
import static org.mitre.tdp.boogie.test.Airports.KDEN;
import static org.mitre.tdp.boogie.test.MockObjects.fix;

import java.util.Arrays;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.Airport;
import org.mitre.tdp.boogie.Airway;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.Transition;
import org.mitre.tdp.boogie.alg.resolve.GraphableLeg;
import org.mitre.tdp.boogie.alg.resolve.RunwayPredictor;
import org.mitre.tdp.boogie.alg.split.Wildcard;
import org.mitre.tdp.boogie.test.Airports;
import org.mitre.tdp.boogie.test.Airways;
import org.mitre.tdp.boogie.test.CONNR5;
import org.mitre.tdp.boogie.test.HOBTT2;

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
        CONNR5.build().transitions());

    RunwayTransitionAppender appender = new RunwayTransitionAppender(
        () -> Optional.of("RW16R"),
        RunwayPredictor.noop()
    );

    ExpandedRoute expandedRoute = expander.apply(route).map(appender).get();

    List<GraphableLeg> legs = expandedRoute.mergedLegs();

    assertAll(
        () -> assertEquals("KDEN", legs.get(0).split().value()),
        () -> assertEquals("KDEN", legs.get(0).leg().pathTerminator().identifier()),

        () -> assertEquals("CONNR5", legs.get(1).split().value()),
        () -> assertEquals(PathTerm.VI, legs.get(1).leg().type()),

        () -> assertEquals("CONNR5", legs.get(2).split().value()),
        () -> assertEquals("GOROC", legs.get(2).leg().pathTerminator().identifier()),

        () -> assertEquals("CONNR5", legs.get(3).split().value()),
        () -> assertEquals("HURDL", legs.get(3).leg().pathTerminator().identifier()),

        () -> assertEquals("CONNR5", legs.get(4).split().value()),
        () -> assertEquals("HAWPE", legs.get(4).leg().pathTerminator().identifier()),

        () -> assertEquals("CONNR5", legs.get(5).split().value()),
        () -> assertEquals("TUNNN", legs.get(5).leg().pathTerminator().identifier()),

        () -> assertEquals("CONNR5", legs.get(6).split().value()),
        () -> assertEquals("TAVRN", legs.get(6).leg().pathTerminator().identifier()),

        () -> assertEquals("CONNR5", legs.get(7).split().value()),
        () -> assertEquals("VONNN", legs.get(7).leg().pathTerminator().identifier()),

        () -> assertEquals("CONNR5", legs.get(8).split().value()),
        () -> assertEquals("TEEBO", legs.get(8).leg().pathTerminator().identifier()),

        () -> assertEquals("CONNR5", legs.get(9).split().value()),
        () -> assertEquals("CONNR", legs.get(9).leg().pathTerminator().identifier()),

        () -> assertEquals("CONNR5", legs.get(10).split().value()),
        () -> assertEquals("CONNR", legs.get(10).leg().pathTerminator().identifier()),

        () -> assertEquals("CONNR5", legs.get(11).split().value()),
        () -> assertEquals("BULDG", legs.get(11).leg().pathTerminator().identifier()),

        () -> assertEquals("CONNR5", legs.get(12).split().value()),
        () -> assertEquals("DBL", legs.get(12).leg().pathTerminator().identifier()),

        () -> assertEquals(13, legs.size())
    );
  }

  @Test
  void testFPA() {
    String route = "DRSDN.HOBTT2.KATL";

    RouteExpander expander = newExpander(
        singletonList(fix("DRSDN", 33.06475, -86.183083)),
        emptyList(),
        singletonList(KATL()),
        HOBTT2.build().transitions());

    RunwayTransitionAppender appender = new RunwayTransitionAppender(
        RunwayPredictor.noop(),
        () -> Optional.of("RW26B")
    );

    ExpandedRoute expandedRoute = expander.apply(route).map(appender).get();

    List<GraphableLeg> legs = expandedRoute.mergedLegs();

    assertAll(
        () -> assertEquals("DRSDN", legs.get(0).split().value()),
        () -> assertEquals("DRSDN", legs.get(0).leg().pathTerminator().identifier()),

        () -> assertEquals("HOBTT2", legs.get(1).split().value()),
        () -> assertEquals("SMAWG", legs.get(1).leg().pathTerminator().identifier()),

        () -> assertEquals("HOBTT2", legs.get(2).split().value()),
        () -> assertEquals("HOBTT", legs.get(2).leg().pathTerminator().identifier()),

        () -> assertEquals("HOBTT2", legs.get(3).split().value()),
        () -> assertEquals("HOBTT", legs.get(3).leg().pathTerminator().identifier()),

        () -> assertEquals("HOBTT2", legs.get(4).split().value()),
        () -> assertEquals("ENSLL", legs.get(4).leg().pathTerminator().identifier()),

        () -> assertEquals("HOBTT2", legs.get(5).split().value()),
        () -> assertEquals("ENSLL", legs.get(5).leg().pathTerminator().identifier()),

        () -> assertEquals("HOBTT2", legs.get(6).split().value()),
        () -> assertEquals("RAIIN", legs.get(6).leg().pathTerminator().identifier()),

        () -> assertEquals("HOBTT2", legs.get(7).split().value()),
        () -> assertEquals("KLOWD", legs.get(7).leg().pathTerminator().identifier()),

        () -> assertEquals("HOBTT2", legs.get(8).split().value()),
        () -> assertEquals("SWEPT", legs.get(8).leg().pathTerminator().identifier()),

        () -> assertEquals("HOBTT2", legs.get(9).split().value()),
        () -> assertEquals("KYMMY", legs.get(9).leg().pathTerminator().identifier()),

        () -> assertEquals("HOBTT2", legs.get(10).split().value()),
        () -> assertEquals("KEAVY", legs.get(10).leg().pathTerminator().identifier()),

        () -> assertEquals("HOBTT2", legs.get(11).split().value()),
        () -> assertEquals("KEAVY", legs.get(11).leg().pathTerminator().identifier()),

        () -> assertEquals("KATL", legs.get(12).split().value()),
        () -> assertEquals("KATL", legs.get(12).leg().pathTerminator().identifier()),

        () -> assertEquals(13, legs.size())
    );
  }

  @Test
  void testFPA_NoProcedureMatch() {
    String route = "DRSDN.HOBTT3.KATL";

    RouteExpander expander = newExpander(
        singletonList(fix("DRSDN", 33.06475, -86.183083)),
        emptyList(),
        singletonList(KATL()),
        HOBTT2.build().transitions());

    ExpandedRoute expandedRoute = expander.apply(route).get();

    List<GraphableLeg> legs = expandedRoute.mergedLegs();

    assertAll(
        () -> assertEquals("DRSDN", legs.get(0).split().value()),
        () -> assertEquals("DRSDN", legs.get(0).leg().pathTerminator().identifier()),

        () -> assertEquals("KATL", legs.get(1).split().value()),
        () -> assertEquals("KATL", legs.get(1).leg().pathTerminator().identifier()),

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

    List<GraphableLeg> legs = expandedRoute.mergedLegs();

    assertAll(
        () -> assertEquals("JMACK", legs.get(0).split().value()),
        () -> assertEquals("JMACK", legs.get(0).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(1).split().value()),
        () -> assertEquals("BARTL", legs.get(1).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(2).split().value()),
        () -> assertEquals("ISO", legs.get(2).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(3).split().value()),
        () -> assertEquals("WEAVR", legs.get(3).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(4).split().value()),
        () -> assertEquals("ORF", legs.get(4).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(5).split().value()),
        () -> assertEquals("SAWED", legs.get(5).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(6).split().value()),
        () -> assertEquals("KALDA", legs.get(6).leg().pathTerminator().identifier())
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

    List<GraphableLeg> legs = expandedRoute.mergedLegs();

    assertAll(
        () -> assertEquals("KALDA", legs.get(0).split().value()),
        () -> assertEquals("KALDA", legs.get(0).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(1).split().value()),
        () -> assertEquals("SAWED", legs.get(1).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(2).split().value()),
        () -> assertEquals("ORF", legs.get(2).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(3).split().value()),
        () -> assertEquals("WEAVR", legs.get(3).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(4).split().value()),
        () -> assertEquals("ISO", legs.get(4).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(5).split().value()),
        () -> assertEquals("BARTL", legs.get(5).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(6).split().value()),
        () -> assertEquals("JMACK", legs.get(6).leg().pathTerminator().identifier())
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

    List<GraphableLeg> legs = expandedRoute.mergedLegs();

    assertAll(
        () -> assertEquals("MILIE", legs.get(0).split().value()),
        () -> assertEquals("MILIE", legs.get(0).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(1).split().value()),
        () -> assertEquals("CHS", legs.get(1).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(2).split().value()),
        () -> assertEquals("JMACK", legs.get(2).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(3).split().value()),
        () -> assertEquals("BARTL", legs.get(3).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(4).split().value()),
        () -> assertEquals("ISO", legs.get(4).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(5).split().value()),
        () -> assertEquals("WEAVR", legs.get(5).leg().pathTerminator().identifier()),

        () -> assertEquals("J121", legs.get(6).split().value()),
        () -> assertEquals("ORF", legs.get(6).leg().pathTerminator().identifier())
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

    List<GraphableLeg> legs = expandedRoute.mergedLegs();

    assertAll(
        () -> assertEquals("MILIE", legs.get(0).split().value()),
        () -> assertEquals("MILIE", legs.get(0).leg().pathTerminator().identifier()),

        () -> assertEquals("5300N/14000W", legs.get(1).split().value()),
        () -> assertEquals("5300N/14000W", legs.get(1).leg().pathTerminator().identifier()),
        () -> assertEquals(PathTerm.DF, legs.get(1).leg().type()),
        () -> assertEquals(LatLong.of(53.0, -140.0), legs.get(1).leg().pathTerminator().latLong()),

        () -> assertEquals("BARTL", legs.get(2).split().value()),
        () -> assertEquals("BARTL", legs.get(2).leg().pathTerminator().identifier())
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

    List<GraphableLeg> legs = expandedRoute.mergedLegs();

    assertAll(
        () -> assertEquals("KDEN", legs.get(0).split().value()),
        () -> assertEquals("KDEN", legs.get(0).leg().pathTerminator().identifier()),

        () -> assertEquals("2200N/12000W", legs.get(1).split().value()),
        () -> assertTrue(Wildcard.TAILORED.test(legs.get(1).split().wildcards())),
        () -> assertEquals("2200N/12000W", legs.get(1).leg().pathTerminator().identifier()),
        () -> assertEquals(PathTerm.IF, legs.get(1).leg().type()),

        () -> assertEquals("BARTL", legs.get(2).split().value()),
        () -> assertEquals("BARTL", legs.get(2).leg().pathTerminator().identifier())
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

    List<GraphableLeg> legs = expandedRoute.mergedLegs();

    assertAll(
        () -> assertEquals("KDEN", legs.get(0).split().value()),
        () -> assertEquals("KDEN", legs.get(0).leg().pathTerminator().identifier()),

        () -> assertEquals("BARTL031018", legs.get(1).split().value()),
        () -> assertEquals("BARTL031018", legs.get(1).leg().pathTerminator().identifier()),
        () -> assertEquals(PathTerm.IF, legs.get(0).leg().type()),
        () -> assertNotEquals(bartl.latLong(), legs.get(1).leg().pathTerminator().latLong()),

        () -> assertEquals("BARTL", legs.get(2).split().value()),
        () -> assertEquals("BARTL", legs.get(2).leg().pathTerminator().identifier())
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

    List<GraphableLeg> legs = expandedRoute.mergedLegs();

    assertAll(
        () -> assertEquals("BARTL", legs.get(0).split().value()),
        () -> assertEquals("BARTL", legs.get(0).leg().pathTerminator().identifier()),

        () -> assertEquals("BARTL125045", legs.get(1).split().value()),
        () -> assertEquals("BARTL125045", legs.get(1).leg().pathTerminator().identifier()),
        () -> assertEquals(PathTerm.DF, legs.get(1).leg().type()),
        () -> assertNotEquals(bartl.latLong(), legs.get(1).leg().pathTerminator().latLong()),

        () -> assertEquals("BARTL", legs.get(2).split().value()),
        () -> assertEquals("BARTL", legs.get(2).leg().pathTerminator().identifier())

    );
  }

  private RouteExpander newExpander(
      Collection<? extends Fix> fixes,
      Collection<? extends Airway> airways,
      Collection<? extends Airport> airports,
      Collection<? extends Transition> transitions
  ) {
    return RouteExpanderFactory.newFactory(fixes, airways, airports, transitions).newExpander();
  }
}
