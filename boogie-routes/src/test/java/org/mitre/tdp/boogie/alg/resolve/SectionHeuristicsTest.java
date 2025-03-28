package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertAll;
import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.resolve.SectionHeuristics.ICAO_AIRPORT;
import static org.mitre.tdp.boogie.alg.resolve.SectionHeuristics.DOMESTIC_AIRWAY;
import static org.mitre.tdp.boogie.alg.resolve.SectionHeuristics.FAA_LATLON;
import static org.mitre.tdp.boogie.alg.resolve.SectionHeuristics.TAILORED;
import static org.mitre.tdp.boogie.alg.resolve.SectionHeuristics.WAYPOINT;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

class SectionHeuristicsTest {

  private static final List<String> airports = Arrays.asList("KATL", "KBDL", "JX57", "HNL");

  @Test
  void testAirportMatching() {
    assertAll(
        () -> assertTrue(airports.get(0).matches(ICAO_AIRPORT.pattern())),
        () -> assertTrue(airports.get(1).matches(ICAO_AIRPORT.pattern())),
        () -> assertFalse(airports.get(2).matches(ICAO_AIRPORT.pattern())),
        () -> assertFalse(airports.get(3).matches(ICAO_AIRPORT.pattern()))
    );
  }

  private static final List<String> airways = Arrays.asList("J121", "Q5", "V65", "AA23", "4V45");

  @Test
  void testAirwayMatching() {
    assertAll(
        () -> assertTrue(airways.get(0).matches(DOMESTIC_AIRWAY.pattern())),
        () -> assertTrue(airways.get(1).matches(DOMESTIC_AIRWAY.pattern())),
        () -> assertTrue(airways.get(2).matches(DOMESTIC_AIRWAY.pattern())),
        () -> assertFalse(airways.get(3).matches(DOMESTIC_AIRWAY.pattern())),
        () -> assertFalse(airways.get(4).matches(DOMESTIC_AIRWAY.pattern()))
    );
  }

  private static final List<String> waypoints = Arrays.asList("SHERL", "LOWGN", "YYT", "KATL");

  @Test
  void testWaypointMatching() {
    assertAll(
        () -> assertTrue(waypoints.get(0).matches(WAYPOINT.pattern())),
        () -> assertTrue(waypoints.get(1).matches(WAYPOINT.pattern())),
        () -> assertFalse(waypoints.get(2).matches(WAYPOINT.pattern())),
        () -> assertFalse(waypoints.get(3).matches(WAYPOINT.pattern()))
    );
  }

  private static final List<String> latlons = Arrays.asList("5300N/14000W", "5600N/15000W", "5700N/16000W", "5700N/27000W");

  @Test
  void testLatLonMatching() {
    assertAll(
        () -> assertTrue(latlons.get(0).matches(FAA_LATLON.pattern())),
        () -> assertTrue(latlons.get(1).matches(FAA_LATLON.pattern())),
        () -> assertTrue(latlons.get(2).matches(FAA_LATLON.pattern())),
        () -> assertFalse(latlons.get(3).matches(FAA_LATLON.pattern()))
    );
  }

  private static final List<String> tailoreds = Arrays.asList("HTO354018", "ODI301025", "BDF351058", "BNA046035");

  @Test
  void testTailoredMatching() {
    assertAll(
        () -> assertTrue(tailoreds.get(0).matches(TAILORED.pattern())),
        () -> assertTrue(tailoreds.get(1).matches(TAILORED.pattern())),
        () -> assertTrue(tailoreds.get(2).matches(TAILORED.pattern())),
        () -> assertTrue(tailoreds.get(3).matches(TAILORED.pattern()))
    );
  }
}