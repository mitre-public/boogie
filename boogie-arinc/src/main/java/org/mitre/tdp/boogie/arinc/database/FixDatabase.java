package org.mitre.tdp.boogie.arinc.database;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Optional;
import java.util.Set;

import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Sets;

public final class FixDatabase {

  /**
   * The indexed collection of fix-like objects maintained by the database.
   */
  private final LinkedHashMultimap<ArincKey, Object> fixLookup;

  FixDatabase(LinkedHashMultimap<ArincKey, Object> fixLookup) {
    this.fixLookup = requireNonNull(fixLookup);

    fixLookup.entries().forEach(entry -> {
      ArincKey oldKey = entry.getKey();
      ArincKey newKey = new ArincKey(oldKey.identifier(), null, oldKey.section(), oldKey.subsection());
      fixLookup.put(newKey, entry.getValue());
    });
  }

  public Optional<ArincWaypoint> waypoint(String identifier) {
    return Optional.empty();
  }

  public Collection<ArincWaypoint> waypoints(String identifier) {
    Set<ArincWaypoint> enroute = (Set<ArincWaypoint>) (Set) fixLookup.get(new ArincKey(identifier, null, SectionCode.E, "A"));
    Set<ArincWaypoint> terminal = (Set<ArincWaypoint>) (Set) fixLookup.get(new ArincKey(identifier, null, SectionCode.P, "N"));
    return Sets.union(enroute, terminal);
  }

  public Optional<ArincWaypoint> waypoint(String identifier, String icaoRegion) {
    return Optional.empty();
  }

  public Optional<ArincAirport> airport(String identifier, String icaoRegion) {
    return Optional.empty();
  }

  public Optional<ArincNdbNavaid> ndbNavaid(String identifier, String icaoRegion) {
    return Optional.empty();
  }

  public Optional<ArincVhfNavaid> vhfNavaid(String identifier, String icaoRegion) {
    return Optional.empty();
  }
}
