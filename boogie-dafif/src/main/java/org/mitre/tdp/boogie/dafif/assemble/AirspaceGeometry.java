package org.mitre.tdp.boogie.dafif.assemble;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.dafif.model.DafifBoundarySegment;
import org.mitre.tdp.boogie.dafif.model.DafifSuasSegment;
import org.mitre.tdp.boogie.dafif.model.enums.Shape;

/**
 * Shared conversion of DAFIF start/end segments into endpoint-oriented airspace sequences.
 */
final class AirspaceGeometry {

  /**
   * Allows source coordinate rounding differences without joining disjoint parts of an airspace.
   */
  private static final double MAX_BRIDGE_NM = 0.1;

  private AirspaceGeometry() {
  }

  static List<AirspaceSequence> convert(List<Segment> segments) {
    if (segments.isEmpty()) {
      throw new UnsupportedGeometry("No boundary segments");
    }
    if (segments.stream().anyMatch(segment -> segment.shape() == Shape.POINT)) {
      throw new UnsupportedGeometry("Point airspaces cannot be represented by Airspace");
    }
    if (segments.stream().anyMatch(segment -> segment.shape() == Shape.CIRCLE)) {
      if (segments.size() != 1) {
        throw new UnsupportedGeometry("Multiple rings cannot be represented by Airspace");
      }
      return List.of(circle(segments.get(0)));
    }

    List<AirspaceSequence> sequences = new ArrayList<>();
    LatLong first = segments.get(0).start();
    appendPoint(sequences, first);
    LatLong previous = first;
    for (Segment segment : segments) {
      LatLong start = segment.start();
      bridge(sequences, previous, start);
      Geometry geometry = geometry(segment.shape());
      AirspaceSequence.Standard.Builder builder = AirspaceSequence.builder(geometry, sequences.size());
      LatLong end = segment.end();
      builder.associatedFix(end);
      if (geometry == Geometry.CLOCKWISE_ARC || geometry == Geometry.COUNTER_CLOCKWISE_ARC) {
        LatLong center = segment.center().orElseThrow(() -> new UnsupportedGeometry("Arc has no center coordinates"));
        builder.centerFix(center)
            .arcRadius(segment.radius1().orElseGet(() -> center.distanceInNM(start)))
            .arcBearing(segment.bearing2().orElseGet(() -> center.courseInDegrees(end)));
      }
      sequences.add(builder.build());
      previous = end;
    }
    bridge(sequences, previous, first);
    return List.copyOf(sequences);
  }

  private static AirspaceSequence circle(Segment segment) {
    if (segment.radius2().filter(radius -> radius > 0.0).isPresent()) {
      throw new UnsupportedGeometry("Annular airspaces cannot be represented by Airspace");
    }
    return AirspaceSequence.builder(Geometry.CIRCLE, 0)
        .centerFix(segment.center().orElseThrow(() -> new UnsupportedGeometry("Circle has no center coordinates")))
        .arcRadius(segment.radius1().orElseThrow(() -> new UnsupportedGeometry("Circle has no radius")))
        .build();
  }

  private static Geometry geometry(Shape shape) {
    return switch (shape) {
      case GREAT_CIRCLE, GENERALIZED -> Geometry.GREAT_CIRCLE;
      case RHUMB_LINE -> Geometry.RHUMB_LINE;
      case COUNTERCLOCKWISE_ARC -> Geometry.COUNTER_CLOCKWISE_ARC;
      case CLOCKWISE_ARC -> Geometry.CLOCKWISE_ARC;
      case POINT, CIRCLE -> throw new UnsupportedGeometry("Unsupported DAFIF edge shape: " + shape);
    };
  }

  private static void bridge(List<AirspaceSequence> sequences, LatLong previous, LatLong next) {
    if (!previous.equals(next)) {
      if (previous.distanceInNM(next) > MAX_BRIDGE_NM) {
        throw new UnsupportedGeometry("Open or disconnected boundary exceeds the 0.1 NM source-coordinate tolerance");
      }
      appendPoint(sequences, next);
    }
  }

  private static void appendPoint(List<AirspaceSequence> sequences, LatLong point) {
    sequences.add(AirspaceSequence.builder(Geometry.GREAT_CIRCLE, sequences.size()).associatedFix(point).build());
  }

  static Segment from(DafifBoundarySegment segment) {
    return new Segment(segment.shape(), point(segment.latitude1(), segment.longitude1()),
        point(segment.latitude2(), segment.longitude2()), point(segment.latitude0(), segment.longitude0()),
        segment.radius1(), segment.radius2(), segment.bearing1(), segment.bearing2());
  }

  static Segment from(DafifSuasSegment segment) {
    return new Segment(segment.shape(), point(segment.latitude1(), segment.longitude1()),
        point(segment.latitude2(), segment.longitude2()), point(segment.latitude0(), segment.longitude0()),
        segment.radius1(), segment.radius2(), segment.bearing1(), segment.bearing2());
  }

  private static Optional<LatLong> point(Optional<Double> latitude, Optional<Double> longitude) {
    return latitude.flatMap(lat -> longitude.map(lon -> LatLong.of(lat, lon)));
  }

  record Segment(Shape shape, Optional<LatLong> point1, Optional<LatLong> point2, Optional<LatLong> center,
                 Optional<Double> radius1, Optional<Double> radius2, Optional<Double> bearing1, Optional<Double> bearing2) {

    LatLong start() {
      return point1.or(() -> projected(bearing1))
          .orElseThrow(() -> new UnsupportedGeometry("Segment has no starting coordinates or center/radius/bearing"));
    }

    LatLong end() {
      return point2.or(() -> projected(bearing2))
          .orElseThrow(() -> new UnsupportedGeometry("Segment has no ending coordinates or center/radius/bearing"));
    }

    private Optional<LatLong> projected(Optional<Double> bearing) {
      return center.flatMap(point -> radius1.flatMap(radius -> bearing.map(degrees -> point.projectOut(degrees, radius))));
    }
  }

  /**
   * Only this exception is handled as a standard-assembly omission. Custom strategy errors propagate.
   */
  static final class UnsupportedGeometry extends UnsupportedOperationException {

    UnsupportedGeometry(String message) {
      super(message);
    }
  }
}
