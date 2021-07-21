package org.mitre.tdp.boogie.arinc.database;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.Optional;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincLocalizerGlideSlope;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincRunway;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;

import com.google.common.collect.Multimap;

/**
 * The TerminalAreaDatabase represents an indexed collection of records based on an airport identifier.
 * <br>
 * This database contains methods for doing the collection of expected terminal area lookups one might want to make in a downstream
 * application.
 * <br>
 * This implementation is probably not yet complete from a user perspective - if there is a feature that should be added feel free
 * to add it to this implementation and submit a PR.
 */
public final class TerminalAreaDatabase {

  /**
   * Multimap of {Identifier, ICAO Region} for airports.
   */
  private final Multimap<Pair<String, String>, AirportPage> airportLookup;

  TerminalAreaDatabase(Multimap<Pair<String, String>, AirportPage> airportLookup) {
    this.airportLookup = requireNonNull(airportLookup);
    this.addIdentifierOnlyIndices();
  }

  public Optional<ArincAirport> airport(String identifier) {
    return highlander(airportLookup.get(Pair.of(identifier, null))).map(AirportPage::airport);
  }

  public Optional<ArincAirport> airport(String identifier, String icaoRegion) {
    return highlander(airportLookup.get(Pair.of(identifier, icaoRegion))).map(AirportPage::airport);
  }

  public Optional<ArincRunway> runwayAt(String airport, String runwayId) {
    return highlander(airportLookup.get(Pair.of(airport, null))).flatMap(page -> page.runway(runwayId));
  }

  public Optional<ArincRunway> runwayAt(String airport, String icaoRegion, String runwayId) {
    return highlander(airportLookup.get(Pair.of(airport, icaoRegion))).flatMap(page -> page.runway(runwayId));
  }

  public Collection<ArincRunway> runwaysAt(String airport) {
    return highlander(airportLookup.get(Pair.of(airport, null))).map(AirportPage::runways).orElse(Collections.emptySet());
  }

  public Collection<ArincRunway> runwaysAt(String airport, String icaoRegion) {
    return highlander(airportLookup.get(Pair.of(airport, icaoRegion))).map(AirportPage::runways).orElse(Collections.emptySet());
  }

  public Optional<ArincLocalizerGlideSlope> primaryLocalizerGlideSlopeAt(String airport, String runway) {
    return highlander(airportLookup.get(Pair.of(airport, null))).flatMap(page -> page.primaryLocalizerGlideSlopeForRunway(runway));
  }

  public Optional<ArincLocalizerGlideSlope> primaryLocalizerGlideSlopeAt(String airport, String icaoRegion, String runway) {
    return highlander(airportLookup.get(Pair.of(airport, icaoRegion))).flatMap(page -> page.primaryLocalizerGlideSlopeForRunway(runway));
  }

  public Optional<ArincLocalizerGlideSlope> secondaryLocalizerGlideSlopeAt(String airport, String runway) {
    return highlander(airportLookup.get(Pair.of(airport, null))).flatMap(page -> page.secondaryLocalizerGlideSlopeForRunway(runway));
  }

  public Optional<ArincLocalizerGlideSlope> secondaryLocalizerGlideSlopeAt(String airport, String icaoRegion, String runway) {
    return highlander(airportLookup.get(Pair.of(airport, icaoRegion))).flatMap(page -> page.secondaryLocalizerGlideSlopeForRunway(runway));
  }

  public Optional<ArincWaypoint> waypointAt(String airport, String waypoint) {
    return highlander(airportLookup.get(Pair.of(airport, null))).flatMap(page -> page.waypoint(waypoint));
  }

  public Optional<ArincWaypoint> waypointAt(String airport, String icaoRegion, String waypoint) {
    return highlander(airportLookup.get(Pair.of(airport, icaoRegion))).flatMap(page -> page.waypoint(waypoint));
  }

  public Collection<ArincWaypoint> waypointsAt(String airport) {
    return highlander(airportLookup.get(Pair.of(airport, null))).map(AirportPage::waypoints).orElse(Collections.emptySet());
  }

  public Collection<ArincWaypoint> waypointsAt(String airport, String icaoRegion) {
    return highlander(airportLookup.get(Pair.of(airport, icaoRegion))).map(AirportPage::waypoints).orElse(Collections.emptySet());
  }

  public Collection<ArincNdbNavaid> ndbNavaidsAt(String airport) {
    return highlander(airportLookup.get(Pair.of(airport, null))).map(AirportPage::ndbNavaids).orElse(Collections.emptySet());
  }

  public Collection<ArincNdbNavaid> ndbNavaidsAt(String airport, String icaoRegion) {
    return highlander(airportLookup.get(Pair.of(airport, icaoRegion))).map(AirportPage::ndbNavaids).orElse(Collections.emptySet());
  }

  public Collection<ArincProcedureLeg> allProcedureLegsAt(String airport) {
    return highlander(airportLookup.get(Pair.of(airport, null))).map(AirportPage::procedureLegs).orElse(Collections.emptySet());
  }

  public Collection<ArincProcedureLeg> allProcedureLegsAt(String airport, String icaoRegion) {
    return highlander(airportLookup.get(Pair.of(airport, icaoRegion))).map(AirportPage::procedureLegs).orElse(Collections.emptySet());
  }

  public Collection<ArincProcedureLeg> legsForProcedure(String airport, String procedure) {
    return highlander(airportLookup.get(Pair.of(airport, null))).map(page -> page.procedureLegs(procedure)).orElse(Collections.emptySet());
  }

  public Collection<ArincProcedureLeg> legsForProcedure(String airport, String icaoRegion, String procedure) {
    return highlander(airportLookup.get(Pair.of(airport, icaoRegion))).map(page -> page.procedureLegs(procedure)).orElse(Collections.emptySet());
  }

  /**
   * "There can only be one"
   * <br>
   * https://www.youtube.com/watch?v=_J3VeogFUOs
   */
  private <T> Optional<T> highlander(Collection<T> col) {
    return col.size() == 1 ? Optional.of(col.iterator().next()) : Optional.empty();
  }

  private void addIdentifierOnlyIndices() {
    this.airportLookup.entries().forEach(entry -> {
      Pair<String, String> oldKey = entry.getKey();
      Pair<String, String> newKey = Pair.of(oldKey.first(), null);
      this.airportLookup.put(newKey, entry.getValue());
    });
  }
}
