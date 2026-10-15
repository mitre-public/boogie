package org.mitre.tdp.boogie.airspace;

import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Rhumb;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.util.Streams;

/**
 * Identifies exact A-B-A retraces in a boundary without modifying its source records.
 * Inputs must be in traversal order; triples include the closure from the last sequence to the first.
 *
 * <p>Both excursion edges must use the same line geometry, and the two A fixes must have exactly equal
 * coordinates. Findings include retraces of any length. Zero-length edges, ambiguous opposite-pole
 * rhumb lines, antipodal great circles, arcs, mixed geometries, and missing fixes are ignored.
 * {@link BoundaryRetraceResolver} supplies a repair policy with a caller's length limit.
 */
public final class BoundaryRetraceIdentifier implements Function<List<AirspaceSequence>, List<BoundaryRetraceIdentifier.Retrace>> {
  /** Source sequence numbers and the one-way length of an exact same-path excursion. */
  public record Retrace(int outgoingSequenceNumber, int returningSequenceNumber, int resumedSequenceNumber, double lengthNm) {}

  @Override
  public List<Retrace> apply(List<AirspaceSequence> sequences) {
    requireNonNull(sequences, "sequences");
    return Streams.triplesCircular(sequences, BoundaryRetraceIdentifier::retrace)
        .flatMap(Optional::stream)
        .toList();
  }

  private static Optional<Retrace> retrace(AirspaceSequence outgoing, AirspaceSequence returning, AirspaceSequence resumed) {
    return Optional.of(outgoing)
        .filter(sequence -> sameLineGeometry(sequence, returning))
        .flatMap(AirspaceSequence::associatedFix)
        .filter(start -> resumed.associatedFix().filter(start::equals).isPresent())
        .flatMap(start -> returning.associatedFix()
            .filter(turn -> uniquePath(start, turn, outgoing.geometry()))
            .map(turn -> new Retrace(outgoing.sequenceNumber(), returning.sequenceNumber(), resumed.sequenceNumber(),
                outgoing.geometry() == Geometry.GREAT_CIRCLE ? start.distanceInNM(turn) : rhumbLengthNm(start, turn))));
  }

  private static boolean sameLineGeometry(AirspaceSequence outgoing, AirspaceSequence returning) {
    return outgoing.geometry() == returning.geometry()
        && (outgoing.geometry() == Geometry.GREAT_CIRCLE || outgoing.geometry() == Geometry.RHUMB_LINE);
  }

  private static boolean uniquePath(LatLong start, LatLong turn, Geometry geometry) {
    double angle = BoundaryCompiler.point(start).angle(BoundaryCompiler.point(turn));
    return angle > 0.0 && switch (geometry) {
      case GREAT_CIRCLE -> Math.PI - angle > 1e-12;
      case RHUMB_LINE -> !isPole(start) || !isPole(turn) || sameMeridian(start, turn);
      default -> false;
    };
  }

  private static double rhumbLengthNm(LatLong start, LatLong turn) {
    double radians = Optional.of(start)
        .filter(position -> sameMeridian(position, turn) || isPole(position) || isPole(turn))
        .map(position -> Math.toRadians(Math.abs(position.latitude() - turn.latitude())))
        .orElseGet(() -> Rhumb.rhumbDistance(start, turn));
    return Spherical.distanceInNM(radians);
  }

  private static boolean sameMeridian(LatLong start, LatLong turn) {
    return start.longitude() == turn.longitude() || Math.abs(start.longitude() - turn.longitude()) == 360.0;
  }

  private static boolean isPole(LatLong position) {
    return Math.abs(position.latitude()) == 90.0;
  }
}
