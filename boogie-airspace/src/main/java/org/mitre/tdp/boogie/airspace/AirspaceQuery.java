package org.mitre.tdp.boogie.airspace;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.function.Consumer;

import org.mitre.caasd.commons.LatLong;

import com.google.common.geometry.S2ContainsPointQuery;
import com.google.common.geometry.S2EdgeQuery;
import com.google.common.geometry.S2Point;
import com.google.common.geometry.S2Polyline;
import com.google.common.geometry.S2Shape;
import com.google.common.geometry.S2ShapeIndex;

/**
 * Reusable query state for one processing thread. Results are emitted in the factory input's iteration order,
 * then in travel order for each airspace. Isolated boundary touches have zero duration and are
 * omitted; exact boundary ownership follows S2's semi-open polygon convention. No global ordering
 * between overlapping airspaces is implied. Inputs must describe an unambiguous shortest
 * great-circle leg (antipodal endpoints are rejected).
 *
 * <p>Each call sends spans synchronously to the consumer supplied when this query was created.
 *
 * @param <K> caller's airspace key type
 */
public final class AirspaceQuery<K> {
  private final S2ContainsPointQuery points;
  private final S2EdgeQuery edges;
  private final Map<S2Shape, AirspaceIndex.Entry<K>> entries;
  private final Consumer<AirspaceSpan<K>> output;
  private final Set<S2Shape> shapes = new HashSet<>();
  private final List<AirspaceIndex.Entry<K>> candidates = new ArrayList<>();
  private boolean querying;

  AirspaceQuery(S2ShapeIndex index, Map<S2Shape, AirspaceIndex.Entry<K>> entries, Consumer<AirspaceSpan<K>> output) {
    this.output = requireNonNull(output, "output");
    this.points = new S2ContainsPointQuery(index);
    this.edges = new S2EdgeQuery(index);
    this.entries = entries;
  }

  /**
   * Emits lateral occupancy, regardless of whether altitude bounds are known. A stationary position inside an airspace
   * occupies the full interval. Exceptions from the consumer propagate to the caller; the query can be reused afterwards.
   * Emitted spans have {@link AirspaceSpan#altitudeChecked()} set to false.
   *
   * @param start starting geographic position
   * @param end ending geographic position
   * @throws IllegalArgumentException if positions are invalid or the endpoints are antipodal
   * @throws IllegalStateException if the consumer recursively invokes this query
   */
  public void intersect(LatLong start, LatLong end) {
    intersect(start, null, end, null, false);
  }

  /**
   * Intersects a leg whose observations may lack altitude, with altitudes above mean sea level only.
   *
   * <p>Equivalent to {@link #intersect(LatLong, VerticalPosition, LatLong, VerticalPosition)} with
   * {@link VerticalPosition#msl(Double)} at each end: limits measured from the terrain or in pressure altitude cannot be
   * evaluated, so bands with such a limit are emitted with {@link AirspaceSpan#altitudeChecked()} set to false.
   *
   * @param start starting geographic position
   * @param startAltitudeFeet starting altitude in feet above mean sea level, or null if missing
   * @param end ending geographic position
   * @param endAltitudeFeet ending altitude in feet above mean sea level, or null if missing
   * @throws IllegalArgumentException if positions are invalid, endpoints are antipodal, or present altitudes are nonfinite
   * @throws IllegalStateException if the consumer recursively invokes this query
   */
  public void intersect(LatLong start, Double startAltitudeFeet, LatLong end, Double endAltitudeFeet) {
    intersect(start, VerticalPosition.msl(startAltitudeFeet), end, VerticalPosition.msl(endAltitudeFeet));
  }

  /**
   * Intersects a leg whose observations report some or all of their altitude, pressure altitude, and terrain.
   *
   * <p>When both observations report an altitude (see {@link VerticalPosition#hasAltitude()}), each airspace's band is
   * clipped with each limit in its own reference (see {@link AltitudeBand#clipEvaluable(VerticalPosition, VerticalPosition)}):
   * <ul>
   *   <li>If every limit can be evaluated at both ends, the spans are definite, with
   *       {@link AirspaceSpan#altitudeChecked()} set to true.</li>
   *   <li>If some limit cannot be evaluated, such as one measured from the terrain without terrain at both ends, the
   *       limits that can be evaluated still clip the leg and can rule it out; what remains is emitted with
   *       {@link AirspaceSpan#altitudeChecked()} set to false.</li>
   *   <li>Bands with {@link AltitudeBand.Status#UNKNOWN} limits produce no spans.</li>
   * </ul>
   * Stationary lateral positions still permit vertical entries and exits.
   *
   * <p>When either observation reports no altitude, a single observed altitude does not establish a vertical profile
   * for the leg, so every airspace, including those with unknown limits, is emitted as lateral occupancy with
   * {@link AirspaceSpan#altitudeChecked()} set to false.
   *
   * @param start starting geographic position
   * @param startVertical what the starting observation reports vertically
   * @param end ending geographic position
   * @param endVertical what the ending observation reports vertically
   * @throws IllegalArgumentException if positions are invalid or the endpoints are antipodal
   * @throws NullPointerException if either vertical position is null
   * @throws IllegalStateException if the consumer recursively invokes this query
   */
  public void intersect(LatLong start, VerticalPosition startVertical, LatLong end, VerticalPosition endVertical) {
    requireNonNull(startVertical, "startVertical");
    requireNonNull(endVertical, "endVertical");
    intersect(start, startVertical, end, endVertical, startVertical.hasAltitude() && endVertical.hasAltitude());
  }

  /**
   * Finds the candidate airspaces along a leg and emits each one's spans.
   *
   * @param start starting geographic position
   * @param startVertical what the starting observation reports vertically, or null for a lateral query
   * @param end ending geographic position
   * @param endVertical what the ending observation reports vertically, or null for a lateral query
   * @param vertical whether to clip by altitude limits, which requires both vertical positions
   */
  private void intersect(LatLong start, VerticalPosition startVertical, LatLong end, VerticalPosition endVertical, boolean vertical) {
    S2Point a = BoundaryCompiler.point(requireNonNull(start, "start"));
    S2Point b = BoundaryCompiler.point(requireNonNull(end, "end"));
    double length = a.angle(b);
    if (Math.PI - length <= 1e-12) {
      throw new IllegalArgumentException("Antipodal endpoints do not define a unique trajectory leg");
    }
    if (querying) {
      throw new IllegalStateException("AirspaceQuery cannot be called recursively");
    }
    querying = true;
    try {
      shapes.clear();
      candidates.clear();
      points.getContainingShapes(a).forEach(shapes::add);
      points.getContainingShapes(b).forEach(shapes::add);
      if (length > 0.0) {
        shapes.addAll(edges.getCandidates(a, b).keySet());
      }
      shapes.stream().map(entries::get).forEach(candidates::add);
      candidates.sort(Comparator.comparingInt(AirspaceIndex.Entry::ordinal));
      S2Polyline line = length > 0.0 ? new S2Polyline(List.of(a, b)) : null;
      for (AirspaceIndex.Entry<K> entry : candidates) {
        Optional<Extent> extent = vertical ? verticalExtent(entry.altitudeBand(), startVertical, endVertical) : Optional.of(Extent.LATERAL);
        extent.ifPresent(within -> emitPieces(entry, within, a, line, length));
      }
    } finally {
      shapes.clear();
      candidates.clear();
      querying = false;
    }
  }

  /**
   * The part of a leg an airspace's altitude limits allow, as fractions of the leg.
   *
   * @param lower the first allowed fraction
   * @param upper the last allowed fraction
   * @param checked whether every limit was evaluated, so the extent is definite
   */
  private record Extent(double lower, double upper, boolean checked) {

    /**
     * * The whole leg, unchecked: used when the leg has no vertical profile.
     */
    static final Extent LATERAL = new Extent(0.0, 1.0, false);
  }

  /**
   * Finds the part of a leg an airspace's altitude limits allow.
   *
   * @param band the airspace's altitude band
   * @param start what the leg's starting observation reports vertically
   * @param end what the leg's ending observation reports vertically
   * @return the allowed extent, checked only if every limit could be evaluated; empty if the band's limits are unknown
   *     or a limit that can be evaluated excludes the whole leg
   */
  private static Optional<Extent> verticalExtent(AltitudeBand band, VerticalPosition start, VerticalPosition end) {
    if (band.status() == AltitudeBand.Status.UNKNOWN) {
      return Optional.empty();
    }
    boolean checked = band.checkable(start) && band.checkable(end);
    return band.clipEvaluable(start, end)
        .map(interval -> new Extent(interval.startFraction(), interval.endFraction(), checked));
  }

  /**
   * Emits an airspace's spans along a leg: its lateral pieces, each clipped to the vertical extent.
   *
   * @param entry the airspace
   * @param extent the part of the leg its altitude limits allow
   * @param start the leg's starting point
   * @param line the leg, or null if it has no length
   * @param length the leg's length in radians
   */
  private void emitPieces(AirspaceIndex.Entry<K> entry, Extent extent, S2Point start, S2Polyline line, double length) {
    if (line == null) {
      emit(entry.key(), extent.lower(), extent.upper(), extent.checked());
      return;
    }
    entry.polygon().intersectWithPolyline(line).stream()
        .filter(piece -> piece.numVertices() >= 2)
        .forEach(piece -> emit(entry.key(),
            Math.max(extent.lower(), Math.min(1.0, start.angle(piece.vertex(0)) / length)),
            Math.min(extent.upper(), Math.min(1.0, start.angle(piece.vertex(piece.numVertices() - 1)) / length)),
            extent.checked()));
  }

  /**
   * Sends a span to the consumer if it has positive length.
   *
   * @param key the airspace's key
   * @param enter the span's first fraction
   * @param exit the span's last fraction
   * @param altitudeChecked whether the span is definite in altitude
   */
  private void emit(K key, double enter, double exit, boolean altitudeChecked) {
    if (enter < exit) {
      output.accept(new AirspaceSpan<>(key, enter, exit, altitudeChecked));
    }
  }

}
