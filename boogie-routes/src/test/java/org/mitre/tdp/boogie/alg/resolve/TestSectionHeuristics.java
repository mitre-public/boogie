package org.mitre.tdp.boogie.alg.resolve;

import static org.junit.jupiter.api.Assertions.assertFalse;
import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mitre.tdp.boogie.alg.resolve.SectionHeuristics.airport;
import static org.mitre.tdp.boogie.alg.resolve.SectionHeuristics.airway;
import static org.mitre.tdp.boogie.alg.resolve.SectionHeuristics.latLon;
import static org.mitre.tdp.boogie.alg.resolve.SectionHeuristics.tailored;
import static org.mitre.tdp.boogie.alg.resolve.SectionHeuristics.waypoint;

import java.util.Arrays;
import java.util.List;

import org.junit.jupiter.api.Test;

public class TestSectionHeuristics {

  private static final List<String> airports = Arrays.asList("KATL", "KBDL", "JX57", "HNL");
  private static final List<String> airways = Arrays.asList("J121", "Q5", "V65", "AA23", "4V45");
  private static final List<String> waypoints = Arrays.asList("SHERL", "LOWGN", "YYT", "KATL");
  private static final List<String> latlons = Arrays.asList("5300N/14000W", "5600N/15000W", "5700N/16000W", "5700N/27000W");
  private static final List<String> tailoreds = Arrays.asList("HTO354018", "ODI301025", "BDF351058", "BNA046035");

  @Test
  public void testAirportMatching() {
    assertTrue(airports.get(0).matches(airport().pattern()));
    assertTrue(airports.get(1).matches(airport().pattern()));
    assertFalse(airports.get(2).matches(airport().pattern()));
    assertFalse(airports.get(3).matches(airport().pattern()));
  }

  @Test
  public void testAirwayMatching() {
    assertTrue(airways.get(0).matches(airway().pattern()));
    assertTrue(airways.get(1).matches(airway().pattern()));
    assertTrue(airways.get(2).matches(airway().pattern()));
    assertFalse(airways.get(3).matches(airway().pattern()));
    assertFalse(airways.get(4).matches(airway().pattern()));
  }

  @Test
  public void testWaypointMatching() {
    assertTrue(waypoints.get(0).matches(waypoint().pattern()));
    assertTrue(waypoints.get(1).matches(waypoint().pattern()));
    assertFalse(waypoints.get(2).matches(waypoint().pattern()));
    assertFalse(waypoints.get(3).matches(waypoint().pattern()));
  }

  @Test
  public void testLatLonMatching() {
    assertTrue(latlons.get(0).matches(latLon().pattern()));
    assertTrue(latlons.get(1).matches(latLon().pattern()));
    assertTrue(latlons.get(2).matches(latLon().pattern()));
    assertFalse(latlons.get(3).matches(latLon().pattern()));
  }

  @Test
  public void testTailoredMatching() {
    assertTrue(tailoreds.get(0).matches(tailored().pattern()));
    assertTrue(tailoreds.get(1).matches(tailored().pattern()));
    assertTrue(tailoreds.get(2).matches(tailored().pattern()));
    assertTrue(tailoreds.get(3).matches(tailored().pattern()));
  }
}
