package org.mitre.tdp.boogie.airspace;

import static java.util.Objects.requireNonNull;

import java.util.Collection;
import java.util.Collections;
import java.util.HashSet;
import java.util.IdentityHashMap;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;

import org.mitre.tdp.boogie.AltitudeLimits;

import org.mitre.tdp.boogie.Airspace;

import com.google.common.geometry.S2Polygon;
import com.google.common.geometry.S2Shape;
import com.google.common.geometry.S2ShapeIndex;

/**
 * Validates source definitions, resolves altitude limits, and compiles an immutable airspace index.
 * Each application creates an independent snapshot. Results follow the input collection's iteration
 * order by airspace, then travel order within each airspace. Use an ordered collection when this matters.
 *
 * <p>All mutable compilation state is local to {@link #apply(Collection)}, so a factory can be reused.
 * Callers apply any boundary repair policies to source airspaces before building an index.
 *
 * @param <K> caller's airspace key type
 */
public final class AirspaceIndexFactory<K> implements Function<Collection<AirspaceDefinition<K>>, AirspaceIndex<K>> {
  /**
   * The spacing of samples along great-circle and rhumb-line edges, in nautical miles.
   */
  private final double lineStepNm;
  /**
   * The bearing increment between samples along arcs and circles, in degrees.
   */
  private final double arcStepDegrees;

  /**
   * Uses sampling steps of 10 nautical miles for lines and 10 degrees for arcs/circles.
   */
  public AirspaceIndexFactory() {
    this(10.0, 10.0);
  }

  /**
   * Stores boundary sampling steps, which are validated when {@link #apply(Collection)} is called.
   * Smaller steps produce more vertices; these are spacing settings, not a geometric error bound.
   *
   * @param lineStepNm finite, positive spacing for great-circle and rhumb lines, in nautical miles
   * @param arcStepDegrees bearing increment for arcs/circles, greater than zero and less than 180 degrees
   */
  public AirspaceIndexFactory(double lineStepNm, double arcStepDegrees) {
    this.lineStepNm = lineStepNm;
    this.arcStepDegrees = arcStepDegrees;
  }

  /**
   * Creates a snapshot with compiled boundaries, resolved altitude bands, and prepared spatial lookup.
   * Source collections and airspaces are not retained by the resulting index.
   *
   * <p>An explicit altitude band overrides the source's limits. Otherwise, the band is built from the source's
   * {@link Airspace#altitudeLimits()}, each limit in its own reference (see {@link AltitudeBand#of(AltitudeLimits)}).
   *
   * @param definitions source airspaces with unique caller keys and optional altitude overrides
   * @return an independent index ready for queries
   * @throws IllegalArgumentException if sampling steps, altitude limits, or geometry are invalid, or keys are duplicated
   * @throws NullPointerException if the collection or any definition is null
   */
  @Override
  public AirspaceIndex<K> apply(Collection<AirspaceDefinition<K>> definitions) {
    requireNonNull(definitions, "definitions");
    if (!Double.isFinite(lineStepNm) || lineStepNm <= 0.0) {
      throw new IllegalArgumentException("lineStepNm must be finite and positive");
    }
    if (!Double.isFinite(arcStepDegrees) || arcStepDegrees <= 0.0 || arcStepDegrees >= 180.0) {
      throw new IllegalArgumentException("arcStepDegrees must be finite and between 0 and 180 degrees");
    }

    BoundaryCompiler compiler = new BoundaryCompiler(lineStepNm, arcStepDegrees);
    S2ShapeIndex index = new S2ShapeIndex();
    Map<S2Shape, AirspaceIndex.Entry<K>> entries = new IdentityHashMap<>();
    Set<K> keys = new HashSet<>();
    Set<K> unknownAltitudeKeys = new HashSet<>();
    for (AirspaceDefinition<K> definition : definitions) {
      requireNonNull(definition, "definition");
      K key = definition.key();
      if (!keys.add(key)) {
        throw new IllegalArgumentException("Duplicate airspace key: " + key);
      }
      AltitudeBand band = Optional.ofNullable(definition.altitudeBand())
          .orElseGet(() -> altitudeBand(definition.airspace()));
      S2Polygon polygon;
      try {
        polygon = compiler.apply(definition.airspace());
      } catch (IllegalArgumentException exception) {
        throw new IllegalArgumentException("Cannot compile airspace " + key + ": " + exception.getMessage(), exception);
      }
      S2Shape shape = polygon.shape();
      index.add(shape);
      entries.put(shape, new AirspaceIndex.Entry<>(key, polygon, band, entries.size()));
      if (band.status() == AltitudeBand.Status.UNKNOWN) {
        unknownAltitudeKeys.add(key);
      }
    }
    index.iterator(); // Build lazy spatial lookup before sharing the snapshot with worker queries.
    return new AirspaceIndex<>(index, Collections.unmodifiableMap(entries), Set.copyOf(unknownAltitudeKeys));
  }

  /**
   * Builds the band for an airspace without an explicit override, from its published limits.
   *
   * @param airspace the airspace
   * @return the band
   */
  private AltitudeBand altitudeBand(Airspace airspace) {
    return AltitudeBand.of(airspace.altitudeLimits());
  }
}
