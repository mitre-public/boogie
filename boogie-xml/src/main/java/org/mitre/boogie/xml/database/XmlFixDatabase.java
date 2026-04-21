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
public final class XmlFixDatabase<F> {

  private final Map<String, F> fixesByReferenceId;
  private final Map<IdentKey, F> waypoints;
  private final Map<IdentKey, F> ndbNavaids;
  private final Map<IdentKey, F> vhfNavaids;
  private final Map<IdentKey, F> airports;
  private final Map<IdentKey, F> heliports;

  // Terminal-scoped indexes — keyed by (portIdentifier, icaoCode, fixIdentifier)
  private final Map<TerminalKey, F> terminalWaypoints;
  private final Map<TerminalKey, F> terminalNdbNavaids;
  private final Map<TerminalKey, F> runways;
  private final Map<TerminalKey, F> gates;
  private final Map<TerminalKey, F> terminalHelipads;
  private final Map<TerminalKey, F> localizerGlideSlopes;
  private final Map<TerminalKey, F> markers;
  private final Map<TerminalKey, F> gnssLandingSystems;

  XmlFixDatabase(Map<String, F> fixesByReferenceId,
                 Map<IdentKey, F> waypoints,
                 Map<IdentKey, F> ndbNavaids,
                 Map<IdentKey, F> vhfNavaids,
                 Map<IdentKey, F> airports,
                 Map<IdentKey, F> heliports,
                 Map<TerminalKey, F> terminalWaypoints,
                 Map<TerminalKey, F> terminalNdbNavaids,
                 Map<TerminalKey, F> runways,
                 Map<TerminalKey, F> gates,
                 Map<TerminalKey, F> terminalHelipads,
                 Map<TerminalKey, F> localizerGlideSlopes,
                 Map<TerminalKey, F> markers,
                 Map<TerminalKey, F> gnssLandingSystems) {
    this.fixesByReferenceId = requireNonNull(fixesByReferenceId);
    this.waypoints = requireNonNull(waypoints);
    this.ndbNavaids = requireNonNull(ndbNavaids);
    this.vhfNavaids = requireNonNull(vhfNavaids);
    this.airports = requireNonNull(airports);
    this.heliports = requireNonNull(heliports);
    this.terminalWaypoints = requireNonNull(terminalWaypoints);
    this.terminalNdbNavaids = requireNonNull(terminalNdbNavaids);
    this.runways = requireNonNull(runways);
    this.gates = requireNonNull(gates);
    this.terminalHelipads = requireNonNull(terminalHelipads);
    this.localizerGlideSlopes = requireNonNull(localizerGlideSlopes);
    this.markers = requireNonNull(markers);
    this.gnssLandingSystems = requireNonNull(gnssLandingSystems);
  }

  /**
   * Create a new builder for incremental construction of a {@link XmlFixDatabase}.
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

  // ---------------------------------------------------------------------------
  // Terminal-scoped lookups — keyed by (portIdentifier, icaoCode, fixIdentifier)
  // ---------------------------------------------------------------------------

  /**
   * Look up a terminal waypoint by port identifier, ICAO code, and fix identifier.
   */
  public Optional<F> terminalWaypoint(String portIdentifier, String icaoCode, String fixIdentifier) {
    return Optional.ofNullable(terminalWaypoints.get(new TerminalKey(portIdentifier, icaoCode, fixIdentifier)));
  }

  /**
   * Look up a terminal NDB navaid by port identifier, ICAO code, and fix identifier.
   */
  public Optional<F> terminalNdbNavaid(String portIdentifier, String icaoCode, String fixIdentifier) {
    return Optional.ofNullable(terminalNdbNavaids.get(new TerminalKey(portIdentifier, icaoCode, fixIdentifier)));
  }

  /**
   * Look up a runway by port identifier, ICAO code, and runway identifier.
   */
  public Optional<F> runway(String portIdentifier, String icaoCode, String runwayIdentifier) {
    return Optional.ofNullable(runways.get(new TerminalKey(portIdentifier, icaoCode, runwayIdentifier)));
  }

  /**
   * Look up a gate by port identifier, ICAO code, and gate identifier.
   */
  public Optional<F> gate(String portIdentifier, String icaoCode, String gateIdentifier) {
    return Optional.ofNullable(gates.get(new TerminalKey(portIdentifier, icaoCode, gateIdentifier)));
  }

  /**
   * Look up a helipad by port identifier, ICAO code, and helipad identifier.
   */
  public Optional<F> terminalHelipad(String portIdentifier, String icaoCode, String helipadIdentifier) {
    return Optional.ofNullable(terminalHelipads.get(new TerminalKey(portIdentifier, icaoCode, helipadIdentifier)));
  }

  /**
   * Look up a localizer/glideslope by port identifier, ICAO code, and fix identifier.
   */
  public Optional<F> localizerGlideSlope(String portIdentifier, String icaoCode, String fixIdentifier) {
    return Optional.ofNullable(localizerGlideSlopes.get(new TerminalKey(portIdentifier, icaoCode, fixIdentifier)));
  }

  /**
   * Look up a marker by port identifier, ICAO code, and fix identifier.
   */
  public Optional<F> marker(String portIdentifier, String icaoCode, String fixIdentifier) {
    return Optional.ofNullable(markers.get(new TerminalKey(portIdentifier, icaoCode, fixIdentifier)));
  }

  /**
   * Look up a GNSS landing system by port identifier, ICAO code, and fix identifier.
   */
  public Optional<F> gnssLandingSystem(String portIdentifier, String icaoCode, String fixIdentifier) {
    return Optional.ofNullable(gnssLandingSystems.get(new TerminalKey(portIdentifier, icaoCode, fixIdentifier)));
  }

  record IdentKey(String identifier, String icaoCode) {
  }

  record TerminalKey(String portIdentifier, String icaoCode, String fixIdentifier) {
  }

  /**
   * Builder for incrementally constructing a {@link XmlFixDatabase} during streaming unmarshalling.
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

    private final Map<TerminalKey, F> terminalWaypoints = new HashMap<>();
    private final Map<TerminalKey, F> terminalNdbNavaids = new HashMap<>();
    private final Map<TerminalKey, F> runways = new HashMap<>();
    private final Map<TerminalKey, F> gates = new HashMap<>();
    private final Map<TerminalKey, F> terminalHelipads = new HashMap<>();
    private final Map<TerminalKey, F> localizerGlideSlopes = new HashMap<>();
    private final Map<TerminalKey, F> markers = new HashMap<>();
    private final Map<TerminalKey, F> gnssLandingSystems = new HashMap<>();

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

    public void indexTerminalWaypoint(String portIdentifier, String icaoCode, String fixIdentifier, F fix) {
      terminalWaypoints.put(new TerminalKey(portIdentifier, icaoCode, fixIdentifier), fix);
    }

    public void indexTerminalNdbNavaid(String portIdentifier, String icaoCode, String fixIdentifier, F fix) {
      terminalNdbNavaids.put(new TerminalKey(portIdentifier, icaoCode, fixIdentifier), fix);
    }

    public void indexRunway(String portIdentifier, String icaoCode, String runwayIdentifier, F fix) {
      runways.put(new TerminalKey(portIdentifier, icaoCode, runwayIdentifier), fix);
    }

    public void indexGate(String portIdentifier, String icaoCode, String gateIdentifier, F fix) {
      gates.put(new TerminalKey(portIdentifier, icaoCode, gateIdentifier), fix);
    }

    public void indexTerminalHelipad(String portIdentifier, String icaoCode, String helipadIdentifier, F fix) {
      terminalHelipads.put(new TerminalKey(portIdentifier, icaoCode, helipadIdentifier), fix);
    }

    public void indexLocalizerGlideSlope(String portIdentifier, String icaoCode, String fixIdentifier, F fix) {
      localizerGlideSlopes.put(new TerminalKey(portIdentifier, icaoCode, fixIdentifier), fix);
    }

    public void indexMarker(String portIdentifier, String icaoCode, String fixIdentifier, F fix) {
      markers.put(new TerminalKey(portIdentifier, icaoCode, fixIdentifier), fix);
    }

    public void indexGnssLandingSystem(String portIdentifier, String icaoCode, String fixIdentifier, F fix) {
      gnssLandingSystems.put(new TerminalKey(portIdentifier, icaoCode, fixIdentifier), fix);
    }

    public XmlFixDatabase<F> build() {
      return new XmlFixDatabase<>(index, waypoints, ndbNavaids, vhfNavaids, airports, heliports,
          terminalWaypoints, terminalNdbNavaids, runways, gates, terminalHelipads,
          localizerGlideSlopes, markers, gnssLandingSystems);
    }
  }
}
