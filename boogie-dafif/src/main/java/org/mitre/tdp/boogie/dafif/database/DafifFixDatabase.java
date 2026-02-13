package org.mitre.tdp.boogie.dafif.database;

import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.dafif.model.DafifNavaid;
import org.mitre.tdp.boogie.dafif.model.DafifWaypoint;

public final class DafifFixDatabase {
  private static final Function<DafifWaypoint, NavaidKey> TO_NAV_KEY = sn -> {
    String ident = sn.navaidIdentifier().orElseThrow(() -> new IllegalArgumentException("Not a secret navaid: ".concat(sn.toString())));
    Integer navType = sn.navaidType().orElseThrow(() -> new IllegalArgumentException("Not a secret navaid: ".concat(sn.toString())));
    String navCountry = sn.navaidCountryCode().orElseThrow(() -> new IllegalArgumentException("Not a secret navaid: ".concat(sn.toString())));
    Integer navKeyCode = sn.navaidKeyCode().orElseThrow(() -> new IllegalArgumentException("Not a secret navaid: ".concat(sn.toString())));
    return new NavaidKey(ident, navType, navCountry, navKeyCode);
  };
  private final Map<WaypointKey, DafifWaypoint> waypoints;
  private final Map<NavaidKey, DafifNavaid> navaids;

  public DafifFixDatabase(Map<WaypointKey, DafifWaypoint> waypoints, Map<NavaidKey, DafifNavaid> navaids) {
    this.waypoints = waypoints;
    this.navaids = navaids;
  }

  public Optional<DafifWaypoint> waypoint(String waypointIdentifier, String country) {
    return Optional.ofNullable(waypoints.get(new WaypointKey(waypointIdentifier, country)));
  }

  public Optional<DafifNavaid> navaidFor(DafifWaypoint waypoint) {
    return Optional.ofNullable(waypoint)
        .filter(DafifWaypoint::waypointPointNavaidFlag)
        .map(TO_NAV_KEY)
        .map(navaids::get);
  }

  public Optional<DafifNavaid> navaid(String naviadIdentifier, String country, Integer type, Integer keyCode) {
    return Optional.ofNullable(navaids.get(new NavaidKey(naviadIdentifier, type, country, keyCode)));
  }
}
