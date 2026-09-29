package org.mitre.tdp.boogie.airspace;

import static java.util.Objects.requireNonNull;

import java.util.Map;
import java.util.Set;
import java.util.function.Consumer;

import com.google.common.geometry.S2Polygon;
import com.google.common.geometry.S2Shape;
import com.google.common.geometry.S2ShapeIndex;

/**
 * Compiled spherical airspaces and a spatial index, created by {@link AirspaceIndexFactory}.
 * Reuse a snapshot for many observation pairs. Each boundary is one simple ring enclosing at
 * most a hemisphere, independent of winding order.
 *
 * <p>The index is immutable after construction. Each processing thread must use its own
 * {@link AirspaceQuery}; queries reuse mutable scratch state and must not be called recursively.
 *
 * @param <K> caller's airspace key type
 */
public final class AirspaceIndex<K> {
  private final S2ShapeIndex index;
  private final Map<S2Shape, Entry<K>> entries;
  private final Set<K> unknownAltitudeKeys;

  AirspaceIndex(S2ShapeIndex index, Map<S2Shape, Entry<K>> entries, Set<K> unknownAltitudeKeys) {
    this.index = requireNonNull(index, "index");
    this.entries = requireNonNull(entries, "entries");
    this.unknownAltitudeKeys = requireNonNull(unknownAltitudeKeys, "unknownAltitudeKeys");
  }

  /**
   * Counts the compiled airspaces in this snapshot.
   *
   * @return the number of unique airspace keys
   */
  public int size() {
    return entries.size();
  }

  /**
   * Identifies airspaces with unresolved vertical bands, including definitions missing both altitude bounds.
   * These airspaces remain available to lateral queries and are omitted when queries apply altitude clipping.
   *
   * @return an immutable set of keys with unknown altitude bands
   */
  public Set<K> unknownAltitudeKeys() {
    return unknownAltitudeKeys;
  }

  /**
   * Creates reusable query state for one processing thread, sharing this index's compiled geometry.
   *
   * @param output consumer receiving spans synchronously on every intersection call
   * @return a new query instance which must not be used concurrently or recursively
   * @throws NullPointerException if the consumer is null
   */
  public AirspaceQuery<K> newQuery(Consumer<AirspaceSpan<K>> output) {
    return new AirspaceQuery<>(index, entries, output);
  }

  record Entry<K>(K key, S2Polygon polygon, AltitudeBand altitudeBand, int ordinal) {}
}
