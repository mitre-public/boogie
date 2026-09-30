package org.mitre.tdp.boogie.airspace;

import static java.util.Objects.requireNonNull;

import org.mitre.tdp.boogie.Airspace;

/**
 * Source data for one indexed airspace. The factory resolves altitude limits and compiles the boundary.
 *
 * @param <K> caller's airspace key type
 * @param key stable caller identity, including source and layer when appropriate
 * @param airspace source boundary and altitude limits
 * @param altitudeBand explicit altitude override, or null to use {@link Airspace#altitudeLimit()}
 */
public record AirspaceDefinition<K>(K key, Airspace airspace, AltitudeBand altitudeBand) {
  /**
   * Holds source data without compiling or interpreting it.
   *
   * @param key nonnull caller identity
   * @param airspace nonnull source airspace
   * @param altitudeBand explicit altitude override, or null to use the source limits
   */
  public AirspaceDefinition {
    requireNonNull(key, "key");
    requireNonNull(airspace, "airspace");
  }

  /**
   * Holds an airspace whose altitude limits will be taken from the source.
   *
   * @param key nonnull caller identity
   * @param airspace nonnull source airspace
   */
  public AirspaceDefinition(K key, Airspace airspace) {
    this(key, airspace, null);
  }
}
