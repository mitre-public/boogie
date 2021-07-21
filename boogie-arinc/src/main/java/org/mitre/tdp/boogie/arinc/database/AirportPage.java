package org.mitre.tdp.boogie.arinc.database;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;

/**
 * Represents a page of structured ARINC 424 data related to an {@link ArincAirport}.
 */
final class AirportPage {

  private final ArincAirport airport;
  private final Map<String, RunwayPage> runwayPages;
  private final Map<String, List<ArincProcedureLeg>> procedureLegs;
  private final Map<String, ArincWaypoint> waypoints;
  private final Map<String, ArincNdbNavaid> ndbNavaids;
  private final Map<String, ArincVhfNavaid> vhfNavaids;

  AirportPage(
      ArincAirport airport,
      Map<String, RunwayPage> runwayPages,
      Map<String, List<ArincProcedureLeg>> procedureLegs,
      Map<String, ArincWaypoint> waypoints,
      Map<String, ArincNdbNavaid> ndbNavaids,
      Map<String, ArincVhfNavaid> vhfNavaids
  ) {
    this.airport = requireNonNull(airport);
    this.runwayPages = runwayPages;
    this.procedureLegs = procedureLegs;
    this.waypoints = waypoints;
    this.ndbNavaids = ndbNavaids;
    this.vhfNavaids = vhfNavaids;
  }

  public ArincAirport airport() {
    return airport;
  }

  public Collection<ArincRunway> runways() {
    return runwayPages.values().stream().map(RunwayPage::runway).collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public Optional<ArincRunway> runway(String identifier) {
    return Optional.ofNullable(runwayPages.get(identifier)).map(RunwayPage::runway);
  }

  public Optional<ArincLocalizerGlideSlope> primaryLocalizerGlideSlopeForRunway(String runwayId) {
    return Optional.ofNullable(runwayPages.get(runwayId)).flatMap(RunwayPage::primaryLocalizerGlideSlope);
  }

  public Optional<ArincLocalizerGlideSlope> secondaryLocalizerGlideSlopeForRunway(String runwayId) {
    return Optional.ofNullable(runwayPages.get(runwayId)).flatMap(RunwayPage::secondaryLocalizerGlideSlope);
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
