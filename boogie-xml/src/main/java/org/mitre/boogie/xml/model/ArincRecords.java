package org.mitre.boogie.xml.model;

import java.util.HashSet;
import java.util.Set;

public final class ArincRecords {
  Set<ArincWaypoint> waypoints = new HashSet<>();

  public ArincRecords(){}

  public void addWaypoint(ArincWaypoint waypoint) {
    waypoints.add(waypoint);
  }
  public Set<ArincWaypoint> waypoints() {
    return waypoints;
  }
}
