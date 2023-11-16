package org.mitre.tdp.boogie.arinc.database;

import static java.util.Objects.requireNonNull;

import java.util.Arrays;
import java.util.Collection;
import java.util.LinkedHashSet;
import java.util.Optional;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.arinc.model.ArincAirport;
import org.mitre.tdp.boogie.arinc.model.ArincAirwayLeg;
import org.mitre.tdp.boogie.arinc.model.ArincHoldingPattern;
import org.mitre.tdp.boogie.arinc.model.ArincNdbNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincProcedureLeg;
import org.mitre.tdp.boogie.arinc.model.ArincVhfNavaid;
import org.mitre.tdp.boogie.arinc.model.ArincWaypoint;
import org.mitre.tdp.boogie.arinc.v18.field.SectionCode;

import com.google.common.collect.LinkedHashMultimap;
import com.google.common.collect.Streams;

/**
 * The FixDatabase is meant to provide simple key-value access to all ARINC 424 record types which can be uniquely identified
 * via a combination of:
 * <br>
 * 1. An Identifier (JMACK, KATL)
 * 2. An ICAO Region (K2, K6)
 * 3. A SectionCode (P, D)
 * 4. A SubSectionCode (N, B)
 * <br>
 * This database provides named + typed access to the different types of data from different sections of the database based on
 * provided identifier or identifier + ICAO region information.
 * <br>
 * The primary goal of a database like this is to aid in looking up references to location information in other ARINC record
 * types such as {@link ArincAirwayLeg}s or {@link ArincProcedureLeg}s.
 * <br>
 * One important thing to note is there are data models which cover multiple sections of the database - namely Waypoints + NDB
 * Navaids. In each case there are enroute and terminal sections of each type - and therefore there are both joint and disparate
 * query methods provided within the database API.
 */
public final class FixDatabase {

  /**
   * The indexed collection of fix-like objects maintained by the database.
   */
  private final LinkedHashMultimap<ArincKey, Object> fixLookup;

  FixDatabase(LinkedHashMultimap<ArincKey, Object> fixLookup) {
    this.fixLookup = requireNonNull(fixLookup);
    this.addIdentifierOnlyIndices();
  }

  public Optional<ArincWaypoint> waypoint(String identifier) {
    return highlander(waypoints(identifier));
  }

  public Optional<ArincWaypoint> terminalWaypoint(String identifier) {
    return castingLookup(
        ArincWaypoint.class,
        new ArincKey(identifier, null, SectionCode.P, "C")
    );
  }

  public Optional<ArincWaypoint> enrouteWaypoint(String identifier) {
    return castingLookup(
        ArincWaypoint.class,
        new ArincKey(identifier, null, SectionCode.E, "A")
    );
  }

  public Collection<ArincWaypoint> waypoints(String... identifiers) {
    return Arrays.stream(identifiers)
        .flatMap(identifier -> castingLookup(ArincWaypoint.class, new ArincKey(identifier, null, SectionCode.E, "A"))
            .or(() -> castingLookup(ArincWaypoint.class, new ArincKey(identifier, null, SectionCode.P, "C")))
            .stream())
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public Optional<ArincWaypoint> waypoint(String identifier, String icaoRegion) {
    return highlander(Stream.of(
        terminalWaypoint(identifier, icaoRegion),
        enrouteWaypoint(identifier, icaoRegion)
    ).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet()));
  }

  public Collection<ArincWaypoint> waypoints(String identifier) {
    return Streams.concat(multiCastLookup(ArincWaypoint.class, new ArincKey(identifier, null, SectionCode.E, "A")),
            multiCastLookup(ArincWaypoint.class, new ArincKey(identifier, null, SectionCode.P, "C")))
        .collect(Collectors.toSet());

  }

  public Optional<ArincWaypoint> terminalWaypoint(String identifier, String icaoRegion) {
    return castingLookup(
        ArincWaypoint.class,
        new ArincKey(identifier, icaoRegion, SectionCode.P, "C")
    );
  }

  public Optional<ArincWaypoint> enrouteWaypoint(String identifier, String icaoRegion) {
    return castingLookup(
        ArincWaypoint.class,
        new ArincKey(identifier, icaoRegion, SectionCode.E, "A")
    );
  }

  public Optional<ArincAirport> airport(String identifier) {
    return highlander(airports(identifier));
  }

  public Collection<ArincAirport> airports(String... identifiers) {
    return Arrays.stream(identifiers)
        .flatMap(identifier -> castingLookup(ArincAirport.class, new ArincKey(identifier, null, SectionCode.P, "A")).stream())
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public Optional<ArincAirport> airport(String identifier, String icaoRegion) {
    return castingLookup(
        ArincAirport.class,
        new ArincKey(identifier, icaoRegion, SectionCode.P, "A")
    );
  }

  public Optional<ArincNdbNavaid> ndbNavaid(String identifier) {
    return highlander(ndbNavaids(identifier));
  }

  public Optional<ArincNdbNavaid> terminalNdbNavaid(String identifier) {
    return castingLookup(
        ArincNdbNavaid.class,
        new ArincKey(identifier, null, SectionCode.P, "N")
    );
  }

  public Optional<ArincNdbNavaid> enrouteNdbNavaid(String identifier) {
    return castingLookup(
        ArincNdbNavaid.class,
        new ArincKey(identifier, null, SectionCode.D, "B")
    );
  }

  public Collection<ArincNdbNavaid> ndbNavaids(String... identifiers) {
    return Arrays.stream(identifiers)
        .flatMap(identifier -> castingLookup(ArincNdbNavaid.class, new ArincKey(identifier, null, SectionCode.P, "N"))
            .or(() -> castingLookup(ArincNdbNavaid.class, new ArincKey(identifier, null, SectionCode.D, "B")))
            .stream())
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public Optional<ArincNdbNavaid> ndbNavaid(String identifier, String icaoRegion) {
    return highlander(Stream.of(
        terminalNdbNavaid(identifier, icaoRegion),
        enrouteNdbNavaid(identifier, icaoRegion)
    ).filter(Optional::isPresent).map(Optional::get).collect(Collectors.toSet()));
  }

  public Optional<ArincNdbNavaid> terminalNdbNavaid(String identifier, String icaoRegion) {
    return castingLookup(
        ArincNdbNavaid.class,
        new ArincKey(identifier, icaoRegion, SectionCode.P, "N")
    );
  }

  public Optional<ArincNdbNavaid> enrouteNdbNavaid(String identifier, String icaoRegion) {
    return castingLookup(
        ArincNdbNavaid.class,
        new ArincKey(identifier, icaoRegion, SectionCode.D, "B")
    );
  }

  public Optional<ArincVhfNavaid> vhfNavaid(String identifier) {
    return highlander(vhfNavaids(identifier));
  }

  public Collection<ArincVhfNavaid> vhfNavaids(String... identifiers) {
    return Arrays.stream(identifiers)
        .flatMap(identifier -> castingLookup(ArincVhfNavaid.class, new ArincKey(identifier, null, SectionCode.D, null)).stream())
        .collect(Collectors.toCollection(LinkedHashSet::new));
  }

  public Optional<ArincVhfNavaid> vhfNavaid(String identifier, String icaoRegion) {
    return castingLookup(
        ArincVhfNavaid.class,
        new ArincKey(identifier, icaoRegion, SectionCode.D, null)
    );
  }

  /**
   * Returns the collection of holding fixes at that fixIdent/regionCode.
   *
   * @param fixIdentifier holding fix identifier
   * @param fixIcaoRegion holding fix region code
   * @return A collection of holds or empty collection to go with that key
   */
  public Collection<ArincHoldingPattern> enrouteHolds(String fixIdentifier, String fixIcaoRegion) {
    return castingLookups(
        ArincHoldingPattern.class,
        new ArincKey(fixIdentifier, fixIcaoRegion, SectionCode.E, "P")
    );
  }

  public <T> Optional<T> castingLookup(Class<T> clz, ArincKey key) {
    return fixLookup.get(key).stream().filter(clz::isInstance).map(clz::cast).findFirst();
  }

  public <T> Stream<T> multiCastLookup(Class<T> clz, ArincKey key) {
    return fixLookup.get(key).stream().filter(clz::isInstance).map(clz::cast);
  }

  /**
   * Not a huge fan of exposing this in the public API but it is a fairly convenient query method and allows for some of the
   * more common non-specific queries of the API.
   * <br>
   * As this class does internal class filtering it won't throw a {@link ClassCastException}, so it's at least somewhat safe.
   */
  public <T> Collection<T> castingLookups(Class<T> clz, ArincKey... keys) {
    return Arrays.stream(keys).map(fixLookup::get).flatMap(Collection::stream)
        .filter(o -> clz.isAssignableFrom(o.getClass())).map(clz::cast).collect(Collectors.toCollection(LinkedHashSet::new));
  }

  /**
   * "There can only be one"
   * <br>
   * https://www.youtube.com/watch?v=_J3VeogFUOs
   */
  private <T> Optional<T> highlander(Collection<T> col) {
    return col.size() == 1 ? Optional.of(col.iterator().next()) : Optional.empty();
  }

  /**
   * Adds a secondary collection of indices for the records to the {@link #fixLookup} map. This is performing the indexing by
   * <i>identifier only</i> reformatting the baseline inserted {@link ArincKey}s to remove the ICAO region.
   */
  private void addIdentifierOnlyIndices() {
    this.fixLookup.entries().forEach(entry -> {
      ArincKey oldKey = entry.getKey();
      ArincKey newKey = new ArincKey(oldKey.identifier(), null, oldKey.section(), oldKey.subsection());
      this.fixLookup.put(newKey, entry.getValue());
    });
  }
}
