package org.mitre.boogie.xml.database;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A per-airport or per-heliport page of assembled terminal fix objects, providing identifier-based
 * lookups scoped to a single port.
 *
 * <p>Terminal fixes include: terminal waypoints, NDB navaids, runways, gates, helipads,
 * localizer/glideslopes, markers, and GNSS landing systems. Each is indexed by its
 * {@link org.mitre.boogie.xml.model.fields.ArincPointInfo#identifier() identifier} within the
 * parent port.
 *
 * @param <F> the client fix type
 */
public final class PortPage<F> {

  private final F referencePoint;
  private final String identifier;
  private final String icaoCode;
  private final Map<String, F> terminalWaypoints;
  private final Map<String, F> ndbNavaids;
  private final Map<String, F> runways;
  private final Map<String, F> gates;
  private final Map<String, F> helipads;
  private final Map<String, F> localizerGlideSlopes;
  private final Map<String, F> markers;
  private final Map<String, F> gnssLandingSystems;

  private PortPage(Builder<F> builder) {
    this.referencePoint = requireNonNull(builder.referencePoint);
    this.identifier = requireNonNull(builder.identifier);
    this.icaoCode = requireNonNull(builder.icaoCode);
    this.terminalWaypoints = Map.copyOf(builder.terminalWaypoints);
    this.ndbNavaids = Map.copyOf(builder.ndbNavaids);
    this.runways = Map.copyOf(builder.runways);
    this.gates = Map.copyOf(builder.gates);
    this.helipads = Map.copyOf(builder.helipads);
    this.localizerGlideSlopes = Map.copyOf(builder.localizerGlideSlopes);
    this.markers = Map.copyOf(builder.markers);
    this.gnssLandingSystems = Map.copyOf(builder.gnssLandingSystems);
  }

  public static <F> Builder<F> builder() {
    return new Builder<>();
  }

  public F referencePoint() {
    return referencePoint;
  }

  public String identifier() {
    return identifier;
  }

  public String icaoCode() {
    return icaoCode;
  }

  public Optional<F> terminalWaypoint(String identifier) {
    return Optional.ofNullable(terminalWaypoints.get(identifier));
  }

  public Collection<F> terminalWaypoints() {
    return Collections.unmodifiableCollection(terminalWaypoints.values());
  }

  public Optional<F> ndbNavaid(String identifier) {
    return Optional.ofNullable(ndbNavaids.get(identifier));
  }

  public Collection<F> ndbNavaids() {
    return Collections.unmodifiableCollection(ndbNavaids.values());
  }

  public Optional<F> runway(String identifier) {
    return Optional.ofNullable(runways.get(identifier));
  }

  public Collection<F> runways() {
    return Collections.unmodifiableCollection(runways.values());
  }

  public Optional<F> gate(String identifier) {
    return Optional.ofNullable(gates.get(identifier));
  }

  public Collection<F> gates() {
    return Collections.unmodifiableCollection(gates.values());
  }

  public Optional<F> helipad(String identifier) {
    return Optional.ofNullable(helipads.get(identifier));
  }

  public Collection<F> helipads() {
    return Collections.unmodifiableCollection(helipads.values());
  }

  public Optional<F> localizerGlideSlope(String identifier) {
    return Optional.ofNullable(localizerGlideSlopes.get(identifier));
  }

  public Collection<F> localizerGlideSlopes() {
    return Collections.unmodifiableCollection(localizerGlideSlopes.values());
  }

  public Optional<F> marker(String identifier) {
    return Optional.ofNullable(markers.get(identifier));
  }

  public Collection<F> markers() {
    return Collections.unmodifiableCollection(markers.values());
  }

  public Optional<F> gnssLandingSystem(String identifier) {
    return Optional.ofNullable(gnssLandingSystems.get(identifier));
  }

  public Collection<F> gnssLandingSystems() {
    return Collections.unmodifiableCollection(gnssLandingSystems.values());
  }

  public static final class Builder<F> {

    private F referencePoint;
    private String identifier;
    private String icaoCode;
    private final Map<String, F> terminalWaypoints = new HashMap<>();
    private final Map<String, F> ndbNavaids = new HashMap<>();
    private final Map<String, F> runways = new HashMap<>();
    private final Map<String, F> gates = new HashMap<>();
    private final Map<String, F> helipads = new HashMap<>();
    private final Map<String, F> localizerGlideSlopes = new HashMap<>();
    private final Map<String, F> markers = new HashMap<>();
    private final Map<String, F> gnssLandingSystems = new HashMap<>();

    private Builder() {
    }

    public Builder<F> referencePoint(F referencePoint) {
      this.referencePoint = referencePoint;
      return this;
    }

    public Builder<F> identifier(String identifier) {
      this.identifier = identifier;
      return this;
    }

    public Builder<F> icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }

    public Builder<F> addTerminalWaypoint(String identifier, F fix) {
      this.terminalWaypoints.put(identifier, fix);
      return this;
    }

    public Builder<F> addNdbNavaid(String identifier, F fix) {
      this.ndbNavaids.put(identifier, fix);
      return this;
    }

    public Builder<F> addRunway(String identifier, F fix) {
      this.runways.put(identifier, fix);
      return this;
    }

    public Builder<F> addGate(String identifier, F fix) {
      this.gates.put(identifier, fix);
      return this;
    }

    public Builder<F> addHelipad(String identifier, F fix) {
      this.helipads.put(identifier, fix);
      return this;
    }

    public Builder<F> addLocalizerGlideSlope(String identifier, F fix) {
      this.localizerGlideSlopes.put(identifier, fix);
      return this;
    }

    public Builder<F> addMarker(String identifier, F fix) {
      this.markers.put(identifier, fix);
      return this;
    }

    public Builder<F> addGnssLandingSystem(String identifier, F fix) {
      this.gnssLandingSystems.put(identifier, fix);
      return this;
    }

    public PortPage<F> build() {
      return new PortPage<>(this);
    }
  }
}
