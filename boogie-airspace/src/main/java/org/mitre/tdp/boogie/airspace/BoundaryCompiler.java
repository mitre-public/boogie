package org.mitre.tdp.boogie.airspace;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.Comparator;
import java.util.List;
import java.util.function.Function;
import java.util.stream.IntStream;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.Airspace;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.projections.BoundaryProjection;
import org.mitre.tdp.boogie.util.Streams;

import com.google.common.geometry.S2Error;
import com.google.common.geometry.S2LatLng;
import com.google.common.geometry.S2Loop;
import com.google.common.geometry.S2Point;
import com.google.common.geometry.S2Polygon;

/** Compiles a single closed boundary; no S2 types cross the module's public API. */
final class BoundaryCompiler implements Function<Airspace, S2Polygon> {
  private final double lineStepNm;
  private final double arcStepDegrees;

  BoundaryCompiler(double lineStepNm, double arcStepDegrees) {
    this.lineStepNm = lineStepNm;
    this.arcStepDegrees = arcStepDegrees;
  }

  @Override
  public S2Polygon apply(Airspace airspace) {
    List<? extends AirspaceSequence> sequences = orderedSequences(airspace);
    return polygon(openRing(projectedVertices(sequences)));
  }

  private static List<? extends AirspaceSequence> orderedSequences(Airspace airspace) {
    List<? extends AirspaceSequence> sequences = airspace.sequences().stream()
        .sorted(Comparator.comparingInt(AirspaceSequence::sequenceNumber))
        .toList();
    checkArgument(!sequences.isEmpty(), "Airspace boundary is empty");
    Streams.pairwise(sequences).forEach(pair -> checkArgument(
        pair.first().sequenceNumber() != pair.second().sequenceNumber(),
        "Duplicate boundary sequence number: %s", pair.second().sequenceNumber()));
    return sequences;
  }

  private List<S2Point> projectedVertices(List<? extends AirspaceSequence> sequences) {
    return IntStream.range(0, sequences.size())
        .mapToObj(i -> project(sequences.get(i), sequences.get((i + 1) % sequences.size()), sequences.size()))
        .flatMap(List::stream)
        .map(BoundaryCompiler::point)
        .toList();
  }

  private List<LatLong> project(AirspaceSequence current, AirspaceSequence next, int sequenceCount) {
    validateGeometry(current, next, sequenceCount);
    return BoundaryProjection.project(current, next, lineStepNm, arcStepDegrees);
  }

  private static void validateGeometry(AirspaceSequence current, AirspaceSequence next, int sequenceCount) {
    switch (current.geometry()) {
      case CIRCLE -> {
        checkArgument(sequenceCount == 1, "A circle must be the only boundary sequence");
        double radius = current.arcRadius().orElse(0.0);
        checkArgument(Double.isFinite(radius) && radius > 0.0 && radius < Math.PI * Spherical.EARTH_RADIUS_NM / 2.0, "Circle radius must be finite, positive, and enclose less than a hemisphere");
      }
      case GREAT_CIRCLE -> {
        S2Point start = point(current.associatedFix().orElseThrow(() -> new IllegalArgumentException("Missing starting fix")));
        S2Point end = point(next.associatedFix().orElseThrow(() -> new IllegalArgumentException("Missing ending fix")));
        checkArgument(Math.PI - start.angle(end) > 1e-12, "Antipodal boundary endpoints do not define a unique great-circle edge");
      }
      default -> {}
    }
  }

  private static List<S2Point> openRing(List<S2Point> samples) {
    // Retain nonadjacent repeats so polygon validation can reject invalid boundaries.
    List<S2Point> vertices = IntStream.range(0, samples.size())
        .filter(i -> i == 0 || !samples.get(i - 1).equalsPoint(samples.get(i)))
        .mapToObj(samples::get)
        .toList();
    if (vertices.size() > 1 && vertices.get(0).equalsPoint(vertices.get(vertices.size() - 1))) {
      return vertices.subList(0, vertices.size() - 1);
    }
    return vertices;
  }

  private static S2Polygon polygon(List<S2Point> vertices) {
    checkArgument(vertices.size() >= 3, "Airspace boundary needs at least three distinct vertices");
    S2Loop loop = new S2Loop(vertices);
    S2Error error = new S2Error();
    if (loop.findValidationError(error)) {
      throw new IllegalArgumentException("Invalid airspace boundary: " + error.text());
    }
    // Source datasets use both orientations. An airspace is the smaller enclosed region.
    loop.normalize();
    S2Polygon polygon = new S2Polygon(loop);
    checkArgument(polygon.getArea() > 0.0, "Airspace boundary has no area");
    polygon.index().iterator(); // Build lazy geometry once, before worker queries.
    return polygon;
  }

  static S2Point point(LatLong position) {
    double latitude = position.latitude();
    double longitude = position.longitude();
    if (!Double.isFinite(latitude) || !Double.isFinite(longitude)
        || latitude < -90.0 || latitude > 90.0 || longitude < -180.0 || longitude > 180.0) {
      throw new IllegalArgumentException("Expected finite latitude/longitude in degrees within geographic bounds");
    }
    // Canonicalize the equivalent representations of the date line and poles.
    if (Math.abs(latitude) == 90.0) {
      longitude = 0.0;
    } else if (longitude == 180.0) {
      longitude = -180.0;
    }
    return S2LatLng.fromDegrees(latitude, longitude).toPoint();
  }
}
