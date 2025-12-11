package org.mitre.tdp.boogie.arinc.database;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;

/**
 * This class holds the supporting objects that are common between airports and heliports.
 */
final class SupportingPage {
  private final Map<String, List<ArincProcedureLeg>> procedureLegs;
  private final Map<String, ArincWaypoint> waypoints;
  private final Map<String, ArincNdbNavaid> ndbNavaids;
  private final Map<String, ArincVhfNavaid> vhfNavaids;

  SupportingPage(Map<String, List<ArincProcedureLeg>> procedureLegs, Map<String, ArincWaypoint> waypoints, Map<String, ArincNdbNavaid> ndbNavaids, Map<String, ArincVhfNavaid> vhfNavaids) {
    this.procedureLegs = procedureLegs;
    this.waypoints = waypoints;
    this.ndbNavaids = ndbNavaids;
    this.vhfNavaids = vhfNavaids;
  }

  public Collection<String> procedureNames() {
    return procedureLegs.keySet();
  }

  public Collection<ArincProcedureLeg> procedureLegs() {
    return procedureLegs.values().stream().flatMap(Collection::stream).collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public Collection<ArincProcedureLeg> procedureLegs(String procedure) {
    return procedureLegs.get(procedure);
  }

  public Optional<ArincWaypoint> waypoint(String waypoint) {
    return Optional.ofNullable(waypoints.get(waypoint));
  }

  public Collection<ArincWaypoint> waypoints() {
    return waypoints.values();
  }

  public Optional<ArincNdbNavaid> ndbNavaid(String identifier) {
    return Optional.ofNullable(ndbNavaids.get(identifier));
  }

  public Collection<ArincNdbNavaid> ndbNavaids() {
    return ndbNavaids.values();
  }

  public Optional<ArincVhfNavaid> vhfNavaid(String identifier) {
    return Optional.ofNullable(vhfNavaids.get(identifier));
  }

  public Collection<ArincVhfNavaid> vhfNavaids() {
    return vhfNavaids.values();
  }
}
