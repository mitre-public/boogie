package org.mitre.tdp.boogie.airspace;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.Comparator;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
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
    intersect(start, end, 0.0, 0.0, false);
  }

  /**
   * Intersects a leg whose observations may lack altitude. When both altitudes are present, clips
   * against the altitude bands using linear interpolation and emits spans with
   * {@link AirspaceSpan#altitudeChecked()} set to true. Unknown bands produce no definite 3D spans.
   * Stationary lateral positions still permit vertical entries/exits. Endpoint altitudes and bands
   * must use the same caller-resolved reference, in feet. When either altitude is null, emits lateral occupancy with
   * {@link AirspaceSpan#altitudeChecked()} set to false, including airspaces with unknown bands.
   * A single observed altitude does not establish an altitude profile for the leg.
   *
   * @param start starting geographic position
   * @param startAltitudeFeet starting altitude in feet using the bands' reference, or null if missing
   * @param end ending geographic position
   * @param endAltitudeFeet ending altitude in feet using the bands' reference, or null if missing
   * @throws IllegalArgumentException if positions or present altitudes are invalid, or the endpoints are antipodal
   * @throws IllegalStateException if the consumer recursively invokes this query
   */
  public void intersect(LatLong start, Double startAltitudeFeet, LatLong end, Double endAltitudeFeet) {
    if ((startAltitudeFeet != null && !Double.isFinite(startAltitudeFeet)) || (endAltitudeFeet != null && !Double.isFinite(endAltitudeFeet))) {
      throw new IllegalArgumentException("Present endpoint altitudes must be finite");
    }
    if (startAltitudeFeet != null && endAltitudeFeet != null) {
      intersect(start, end, startAltitudeFeet, endAltitudeFeet, true);
    } else {
      intersect(start, end);
    }
  }

  private void intersect(LatLong start, LatLong end, double startAltitude, double endAltitude, boolean vertical) {
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
      for (S2Shape shape : shapes) {
        candidates.add(entries.get(shape));
      }
      candidates.sort(Comparator.comparingInt(AirspaceIndex.Entry::ordinal));
      S2Polyline line = length > 0.0 ? new S2Polyline(List.of(a, b)) : null;
      for (AirspaceIndex.Entry<K> entry : candidates) {
        double lower = 0.0;
        double upper = 1.0;
        if (vertical) {
          var interval = entry.altitudeBand().clip(startAltitude, endAltitude);
          if (interval.isEmpty()) {
            continue;
          }
          lower = interval.get().startFraction();
          upper = interval.get().endFraction();
        }
        if (length == 0.0) {
          emit(entry.key(), lower, upper, vertical);
        } else {
          for (S2Polyline piece : entry.polygon().intersectWithPolyline(line)) {
            if (piece.numVertices() < 2) {
              continue;
            }
            double enter = Math.max(lower, Math.min(1.0, a.angle(piece.vertex(0)) / length));
            double exit = Math.min(upper, Math.min(1.0, a.angle(piece.vertex(piece.numVertices() - 1)) / length));
            emit(entry.key(), enter, exit, vertical);
          }
        }
      }
    } finally {
      shapes.clear();
      candidates.clear();
      querying = false;
    }
  }

  private void emit(K key, double enter, double exit, boolean altitudeChecked) {
    if (enter < exit) {
      output.accept(new AirspaceSpan<>(key, enter, exit, altitudeChecked));
    }
  }
}
