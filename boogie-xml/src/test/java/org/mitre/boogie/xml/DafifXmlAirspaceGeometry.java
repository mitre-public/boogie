package org.mitre.boogie.xml;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;
import java.util.Optional;

import org.mitre.boogie.xml.v23_4.generated.AirspaceSegment;
import org.mitre.boogie.xml.v23_4.generated.BoundaryVia;
import org.mitre.boogie.xml.v23_4.generated.RecordType;
import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.dafif.model.DafifBoundarySegment;
import org.mitre.tdp.boogie.dafif.model.DafifSuasSegment;
import org.mitre.tdp.boogie.dafif.model.enums.Shape;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Maps DAFIF edges to XML's outgoing boundary geometry, retaining each source endpoint.
 * Like the standard DAFIF assembler, this fixture omits complete point, annular, open or disconnected
 * airspaces; only coordinate gaps up to 0.1 NM are bridged. An empty result identifies an omission.
 */
final class DafifXmlAirspaceGeometry {
  private static final Logger LOG = LoggerFactory.getLogger(DafifXmlAirspaceGeometry.class);
  private static final double MAX_BRIDGE_NM = 0.1;

  private DafifXmlAirspaceGeometry() {}

  static List<AirspaceSegment> boundary(List<DafifBoundarySegment> source) {
    return convert(source.stream().sorted(Comparator.comparingInt(DafifBoundarySegment::segmentNumber))
        .map(segment -> new Edge(segment.boundaryIdentification(), segment.segmentNumber(), segment.cycleDate(),
            segment.shape(), point(segment.latitude1(), segment.longitude1()), point(segment.latitude2(), segment.longitude2()),
            point(segment.latitude0(), segment.longitude0()), segment.radius1(), segment.radius2(),
            segment.bearing1(), segment.bearing2())).toList());
  }

  static List<AirspaceSegment> suas(List<DafifSuasSegment> source) {
    return convert(source.stream().sorted(Comparator.comparingInt(DafifSuasSegment::segmentNumber))
        .map(segment -> new Edge(segment.suasIdentification() + "/" + segment.sector().orElse(""),
            segment.segmentNumber(), segment.cycleDate(), segment.shape(),
            point(segment.latitude1(), segment.longitude1()), point(segment.latitude2(), segment.longitude2()),
            point(segment.latitude0(), segment.longitude0()), segment.radius1(), segment.radius2(),
            segment.bearing1(), segment.bearing2())).toList());
  }

  private static List<AirspaceSegment> convert(List<Edge> source) {
    try {
      return geometry(source);
    } catch (UnsupportedGeometry exception) {
      LOG.warn("Omitting DAFIF XML airspace {}: {}", source.isEmpty() ? "without segments" : source.get(0).identifier(),
          exception.getMessage());
      return List.of();
    }
  }

  private static List<AirspaceSegment> geometry(List<Edge> source) {
    if (source.isEmpty()) {
      throw new UnsupportedGeometry("No boundary segments");
    }
    if (source.stream().anyMatch(edge -> edge.shape() == Shape.POINT)) {
      throw new UnsupportedGeometry("Point boundaries have no XML boundary geometry");
    }
    if (source.stream().anyMatch(edge -> edge.radius2().filter(radius -> radius > 0.0).isPresent())) {
      throw new UnsupportedGeometry("Annular boundaries require an interior exclusion");
    }
    if (source.stream().anyMatch(edge -> edge.shape() == Shape.CIRCLE)) {
      if (source.size() != 1) {
        throw new UnsupportedGeometry("Mixed or multiple circle boundaries");
      }
      Edge edge = source.get(0);
      AirspaceSegment circle = segment(edge, BoundaryVia.CIRCLE, null);
      LatLong center = edge.center().orElseThrow(() -> new UnsupportedGeometry("Circle has no center"));
      circle.setArcOriginLocation(CifpXmlPoints.location(center.latitude(), center.longitude()));
      circle.setArcDistance(BigDecimal.valueOf(edge.radius1().orElseThrow(() -> new UnsupportedGeometry("Circle has no radius"))));
      circle.setSequenceNumber(1);
      circle.setIsEndOfDescription(true);
      return List.of(circle);
    }

    List<AirspaceSegment> target = new ArrayList<>();
    LatLong first = source.get(0).start();
    LatLong previous = first;
    Edge previousEdge = source.get(0);
    for (Edge edge : source) {
      LatLong start = edge.start();
      bridge(target, previousEdge, previous, start);
      AirspaceSegment segment = segment(edge, via(edge.shape()), start);
      if (edge.shape() == Shape.CLOCKWISE_ARC || edge.shape() == Shape.COUNTERCLOCKWISE_ARC) {
        LatLong center = edge.center().orElseThrow(() -> new UnsupportedGeometry("Arc has no center"));
        segment.setArcOriginLocation(CifpXmlPoints.location(center.latitude(), center.longitude()));
        segment.setArcDistance(BigDecimal.valueOf(edge.radius1().orElseGet(() -> center.distanceInNM(start))));
        // XML arcBearing refers to the beginning of the outgoing arc, unlike the core endpoint-oriented model.
        segment.setArcBearing(BigDecimal.valueOf(edge.bearing1().orElseGet(() -> center.courseInDegrees(start))));
      }
      target.add(segment);
      previous = edge.end();
      previousEdge = edge;
    }
    bridge(target, previousEdge, previous, first);
    // Include the closing endpoint so the preceding source edge retains its complete outgoing geometry.
    target.add(segment(previousEdge, BoundaryVia.GREAT_CIRCLE, first));
    target.get(target.size() - 1).setIsEndOfDescription(true);
    for (int index = 0; index < target.size(); index++) {
      target.get(index).setSequenceNumber(index + 1);
    }
    return List.copyOf(target);
  }

  private static void bridge(List<AirspaceSegment> target, Edge source, LatLong previous, LatLong next) {
    if (!previous.equals(next)) {
      if (previous.distanceInNM(next) > MAX_BRIDGE_NM) {
        throw new UnsupportedGeometry("Open or disconnected boundary exceeds the 0.1 NM coordinate tolerance");
      }
      AirspaceSegment bridge = segment(source, BoundaryVia.GREAT_CIRCLE, previous);
      bridge.getNotes().add("DAFIF source-coordinate gap bridged within 0.1 NM");
      target.add(bridge);
    }
  }

  private static AirspaceSegment segment(Edge edge, BoundaryVia via, LatLong location) {
    AirspaceSegment target = new AirspaceSegment();
    target.setBoundaryVia(via);
    target.setRecordType(RecordType.STANDARD);
    if (edge.cycle() != null) {
      target.setCycleDate(String.format("%04d", edge.cycle() % 10000));
    }
    if (location != null) {
      target.setLocation(CifpXmlPoints.location(location.latitude(), location.longitude()));
    }
    target.getNotes().add("DAFIF SEG_NBR=" + edge.number() + "; SHAP=" + edge.shape().code());
    return target;
  }

  private static BoundaryVia via(Shape shape) {
    return switch (shape) {
      case GREAT_CIRCLE, GENERALIZED -> BoundaryVia.GREAT_CIRCLE;
      case RHUMB_LINE -> BoundaryVia.RHUMB_LINE;
      case CLOCKWISE_ARC -> BoundaryVia.CLOCKWISE_ARC;
      case COUNTERCLOCKWISE_ARC -> BoundaryVia.COUNTER_CLOCKWISE_ARC;
      case POINT, CIRCLE -> throw new UnsupportedGeometry("Unsupported edge shape " + shape);
    };
  }

  private static Optional<LatLong> point(Optional<Double> latitude, Optional<Double> longitude) {
    return latitude.flatMap(lat -> longitude.map(lon -> LatLong.of(lat, lon)));
  }

  private record Edge(String identifier, int number, Integer cycle, Shape shape,
                      Optional<LatLong> point1, Optional<LatLong> point2, Optional<LatLong> center,
                      Optional<Double> radius1, Optional<Double> radius2, Optional<Double> bearing1, Optional<Double> bearing2) {
    LatLong start() {
      return point1.or(() -> projected(bearing1)).orElseThrow(() -> new UnsupportedGeometry("Missing start coordinates"));
    }

    LatLong end() {
      return point2.or(() -> projected(bearing2)).orElseThrow(() -> new UnsupportedGeometry("Missing end coordinates"));
    }

    private Optional<LatLong> projected(Optional<Double> bearing) {
      return center.flatMap(point -> radius1.flatMap(radius -> bearing.map(degrees -> point.projectOut(degrees, radius))));
    }
  }

  private static final class UnsupportedGeometry extends RuntimeException {
    private UnsupportedGeometry(String message) {
      super(message);
    }
  }
}
