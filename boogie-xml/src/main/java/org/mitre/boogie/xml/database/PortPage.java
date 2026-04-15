package org.mitre.boogie.xml.database;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

/**
 * A per-airport or per-heliport page of assembled terminal objects, providing identifier-based
 * lookups scoped to a single port.
 *
 * <p>Terminal records include: terminal waypoints, NDB navaids, runways, gates, helipads,
 * localizer/glideslopes, markers, and GNSS landing systems. Each is indexed by its
 * {@link org.mitre.boogie.xml.model.fields.ArincPointInfo#identifier() identifier} within the
 * parent port.
 *
 * @param <F> the client fix type
 * @param <R> the client runway type
 * @param <H> the client helipad type
 */
public final class PortPage<F, R, H> {

  private final F referencePoint;
  private final String identifier;
  private final String icaoCode;
  private final Map<String, F> terminalWaypoints;
  private final Map<String, F> ndbNavaids;
  private final Map<String, R> runways;
  private final Map<String, F> gates;
  private final Map<String, H> helipads;
  private final Map<String, F> localizerGlideSlopes;
  private final Map<String, F> markers;
  private final Map<String, F> gnssLandingSystems;

  private PortPage(Builder<F, R, H> builder) {
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

  public static <F, R, H> Builder<F, R, H> builder() {
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

  public Optional<R> runway(String identifier) {
    return Optional.ofNullable(runways.get(identifier));
  }

  public Collection<R> runways() {
    return Collections.unmodifiableCollection(runways.values());
  }

  public Optional<F> gate(String identifier) {
    return Optional.ofNullable(gates.get(identifier));
  }

  public Collection<F> gates() {
    return Collections.unmodifiableCollection(gates.values());
  }

  public Optional<H> helipad(String identifier) {
    return Optional.ofNullable(helipads.get(identifier));
  }

  public Collection<H> helipads() {
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

  public static final class Builder<F, R, H> {

    private F referencePoint;
    private String identifier;
    private String icaoCode;
    private final Map<String, F> terminalWaypoints = new HashMap<>();
    private final Map<String, F> ndbNavaids = new HashMap<>();
    private final Map<String, R> runways = new HashMap<>();
    private final Map<String, F> gates = new HashMap<>();
    private final Map<String, H> helipads = new HashMap<>();
    private final Map<String, F> localizerGlideSlopes = new HashMap<>();
    private final Map<String, F> markers = new HashMap<>();
    private final Map<String, F> gnssLandingSystems = new HashMap<>();

    private Builder() {
    }

    public Builder<F, R, H> referencePoint(F referencePoint) {
      this.referencePoint = referencePoint;
      return this;
    }

    public Builder<F, R, H> identifier(String identifier) {
      this.identifier = identifier;
      return this;
    }

    public Builder<F, R, H> icaoCode(String icaoCode) {
      this.icaoCode = icaoCode;
      return this;
    }

    public Builder<F, R, H> addTerminalWaypoint(String identifier, F fix) {
      this.terminalWaypoints.put(identifier, fix);
      return this;
    }

    public Builder<F, R, H> addNdbNavaid(String identifier, F fix) {
      this.ndbNavaids.put(identifier, fix);
      return this;
    }

    public Builder<F, R, H> addRunway(String identifier, R runway) {
      this.runways.put(identifier, runway);
      return this;
    }

    public Builder<F, R, H> addGate(String identifier, F fix) {
      this.gates.put(identifier, fix);
      return this;
    }

    public Builder<F, R, H> addHelipad(String identifier, H helipad) {
      this.helipads.put(identifier, helipad);
      return this;
    }

    public Builder<F, R, H> addLocalizerGlideSlope(String identifier, F fix) {
      this.localizerGlideSlopes.put(identifier, fix);
      return this;
    }

    public Builder<F, R, H> addMarker(String identifier, F fix) {
      this.markers.put(identifier, fix);
      return this;
    }

    public Builder<F, R, H> addGnssLandingSystem(String identifier, F fix) {
      this.gnssLandingSystems.put(identifier, fix);
      return this;
    }

    public PortPage<F, R, H> build() {
      return new PortPage<>(this);
    }
  }
}
