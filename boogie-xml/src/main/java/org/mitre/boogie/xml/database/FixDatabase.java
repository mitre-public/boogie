package org.mitre.boogie.xml.database;

import static java.util.Objects.requireNonNull;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * Indexed collection of pre-assembled fix objects supporting both XML reference ID lookups and
 * type-specific identifier + ICAO code lookups.
 *
 * <p>Legs in the XML schema reference fixes via IDREF elements which correspond to the {@code referenceId} attribute
 * on each point record. This database indexes every record that carries an
 * {@link org.mitre.boogie.xml.model.fields.ArincPointInfo} &mdash; top-level waypoints, NDB navaids, VHF navaids,
 * and airports together with all of their nested records (runways, gates, terminal waypoints, terminal NDBs, helipads,
 * localizer/glideslopes, markers, and GNSS landing systems).
 *
 * <p>The type-specific indexes (e.g. {@link #waypoint(String, String)}) provide lookups by identifier and ICAO code,
 * mirroring the query patterns of the ARINC {@code ArincFixDatabase} in a simplified form.
 *
 * <p><b>Note:</b> {@link Builder#build()} shares the backing maps with the built database. This means additions to
 * the builder after {@code build()} are visible through the built instance &mdash; enabling on-the-fly assembly
 * during streaming. This is safe in a single-threaded streaming context.
 *
 * @see FixDatabaseFactory
 */
public final class FixDatabase<F> {

  private final Map<String, F> fixesByReferenceId;
  private final Map<IdentKey, F> waypoints;
  private final Map<IdentKey, F> ndbNavaids;
  private final Map<IdentKey, F> vhfNavaids;
  private final Map<IdentKey, F> airports;
  private final Map<IdentKey, F> heliports;

  FixDatabase(Map<String, F> fixesByReferenceId,
      Map<IdentKey, F> waypoints,
      Map<IdentKey, F> ndbNavaids,
      Map<IdentKey, F> vhfNavaids,
      Map<IdentKey, F> airports,
      Map<IdentKey, F> heliports) {
    this.fixesByReferenceId = requireNonNull(fixesByReferenceId);
    this.waypoints = requireNonNull(waypoints);
    this.ndbNavaids = requireNonNull(ndbNavaids);
    this.vhfNavaids = requireNonNull(vhfNavaids);
    this.airports = requireNonNull(airports);
    this.heliports = requireNonNull(heliports);
  }

  /**
   * Create a new builder for incremental construction of a {@link FixDatabase}.
   */
  public static <F> Builder<F> builder() {
    return new Builder<>();
  }

  /**
   * Look up a pre-assembled fix by its XML reference ID.
   */
  public Optional<F> fix(String referenceId) {
    return Optional.ofNullable(fixesByReferenceId.get(referenceId));
  }

  /**
   * Look up a waypoint by identifier and ICAO code.
   */
  public Optional<F> waypoint(String identifier, String icaoCode) {
    return Optional.ofNullable(waypoints.get(new IdentKey(identifier, icaoCode)));
  }

  /**
   * Look up an NDB navaid by identifier and ICAO code.
   */
  public Optional<F> ndbNavaid(String identifier, String icaoCode) {
    return Optional.ofNullable(ndbNavaids.get(new IdentKey(identifier, icaoCode)));
  }

  /**
   * Look up a VHF navaid by identifier and ICAO code.
   */
  public Optional<F> vhfNavaid(String identifier, String icaoCode) {
    return Optional.ofNullable(vhfNavaids.get(new IdentKey(identifier, icaoCode)));
  }

  /**
   * Look up an airport by identifier and ICAO code.
   */
  public Optional<F> airport(String identifier, String icaoCode) {
    return Optional.ofNullable(airports.get(new IdentKey(identifier, icaoCode)));
  }

  /**
   * Look up a heliport by identifier and ICAO code.
   */
  public Optional<F> heliport(String identifier, String icaoCode) {
    return Optional.ofNullable(heliports.get(new IdentKey(identifier, icaoCode)));
  }

  record IdentKey(String identifier, String icaoCode) {
  }

  /**
   * Builder for incrementally constructing a {@link FixDatabase} during streaming unmarshalling.
   *
   * <p>The {@link #build()} method shares the backing maps with the built database rather than copying them.
   * This allows the builder to continue indexing records after build and have those additions visible through
   * the built instance &mdash; which is the mechanism that enables on-the-fly airway and procedure assembly
   * during streaming.
   */
  public static final class Builder<F> {

    private final Map<String, F> index = new HashMap<>();
    private final Map<IdentKey, F> waypoints = new HashMap<>();
    private final Map<IdentKey, F> ndbNavaids = new HashMap<>();
    private final Map<IdentKey, F> vhfNavaids = new HashMap<>();
    private final Map<IdentKey, F> airports = new HashMap<>();
    private final Map<IdentKey, F> heliports = new HashMap<>();

    private Builder() {
    }

    /**
     * Index a pre-assembled fix by its XML reference ID.
     */
    public void index(String referenceId, F fix) {
      index.put(referenceId, fix);
    }

    public void indexWaypoint(String identifier, String icaoCode, F fix) {
      waypoints.put(new IdentKey(identifier, icaoCode), fix);
    }

    public void indexNdbNavaid(String identifier, String icaoCode, F fix) {
      ndbNavaids.put(new IdentKey(identifier, icaoCode), fix);
    }

    public void indexVhfNavaid(String identifier, String icaoCode, F fix) {
      vhfNavaids.put(new IdentKey(identifier, icaoCode), fix);
    }

    public void indexAirport(String identifier, String icaoCode, F fix) {
      airports.put(new IdentKey(identifier, icaoCode), fix);
    }

    public void indexHeliport(String identifier, String icaoCode, F fix) {
      heliports.put(new IdentKey(identifier, icaoCode), fix);
    }

    public FixDatabase<F> build() {
      return new FixDatabase<>(index, waypoints, ndbNavaids, vhfNavaids, airports, heliports);
    }
  }
}
