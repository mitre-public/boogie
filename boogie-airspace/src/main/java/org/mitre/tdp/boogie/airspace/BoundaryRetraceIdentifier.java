package org.mitre.tdp.boogie.airspace;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.function.Function;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Rhumb;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.util.Streams;

import com.google.common.geometry.S2EdgeUtil;
import com.google.common.geometry.S2Point;

/**
 * Identifies boundary retraces, A-B-C excursions that turn fully around at B and come back along the
 * outgoing line, without modifying source records.
 * Sequences are analyzed in sequence-number order; triples include the closure from the last sequence to the first.
 * Duplicate sequence numbers are rejected with an {@link IllegalArgumentException}.
 *
 * <p>Both excursion edges must use the same line geometry. Two forms are reported:
 * <ul>
 *   <li>an exact retrace, where C has exactly the coordinates of A (great circles or rhumb lines); and</li>
 *   <li>a great-circle backtrack, where the return overshoots A along the same line (A lies on segment
 *       B-C) or stops short of it (C lies on segment A-B), each within the collinearity tolerance.</li>
 * </ul>
 * Use {@link Retrace#exact(List)} to tell the forms apart. Findings include retraces of any length.
 * Zero-length edges, ambiguous opposite-pole rhumb lines, antipodal great circles, arcs, mixed
 * geometries, and missing fixes are ignored. {@link BoundaryRetraceResolver} supplies a repair policy
 * with a caller's length limit.
 */
public final class BoundaryRetraceIdentifier implements Function<List<AirspaceSequence>, List<BoundaryRetraceIdentifier.Retrace>> {
  /**
   * Default distance, in nautical miles (about 1.85 m), within which a point counts as lying on a great-circle
   * segment. Source coordinates published to 0.01 arcsecond are rounded by up to about 0.3 m.
   */
  public static final double DEFAULT_COLLINEAR_TOLERANCE_NM = 0.001;

  /**
   * Source sequence numbers and the one-way length of the doubled-back excursion: A to B for an exact retrace
   * or an overshoot, B to C for an undershoot.
   */
  public record Retrace(int outgoingSequenceNumber, int returningSequenceNumber, int resumedSequenceNumber, double lengthNm) {
    /**
     * @param sequences the boundary in which this retrace was identified
     * @return true when the resumed fix has exactly the outgoing fix's coordinates
     */
    public boolean exact(List<? extends AirspaceSequence> sequences) {
      requireNonNull(sequences, "sequences");
      Optional<LatLong> start = fix(sequences, outgoingSequenceNumber);
      return start.isPresent() && start.equals(fix(sequences, resumedSequenceNumber));
    }

    private static Optional<LatLong> fix(List<? extends AirspaceSequence> sequences, int sequenceNumber) {
      return sequences.stream()
          .filter(sequence -> sequence.sequenceNumber() == sequenceNumber)
          .findFirst()
          .flatMap(AirspaceSequence::associatedFix);
    }
  }

  private final double collinearToleranceRadians;

  /** Uses {@link #DEFAULT_COLLINEAR_TOLERANCE_NM}. */
  public BoundaryRetraceIdentifier() {
    this(DEFAULT_COLLINEAR_TOLERANCE_NM);
  }

  /**
   * @param collinearToleranceNm finite, nonnegative distance within which a fix counts as lying on a
   *     great-circle segment, in nautical miles; zero reports only exact retraces and exactly collinear backtracks
   */
  public BoundaryRetraceIdentifier(double collinearToleranceNm) {
    checkArgument(Double.isFinite(collinearToleranceNm) && collinearToleranceNm >= 0.0, "Collinear tolerance must be finite and nonnegative");
    this.collinearToleranceRadians = collinearToleranceNm / Spherical.EARTH_RADIUS_NM;
  }

  @Override
  public List<Retrace> apply(List<AirspaceSequence> sequences) {
    requireNonNull(sequences, "sequences");
    List<AirspaceSequence> ordered = BoundaryCompiler.traversalOrder(sequences);
    List<Retrace> found = Streams.triplesCircular(ordered, this::retrace)
        .flatMap(Optional::stream)
        .toList();
    // The legs around an exact A-B-A also read as a backtrack (B-A-next often heads back through B);
    // those are consequences of the exact retrace and disappear once it is repaired.
    Set<Integer> exactRecords = found.stream()
        .filter(retrace -> retrace.exact(ordered))
        .flatMap(retrace -> Stream.of(retrace.outgoingSequenceNumber(), retrace.returningSequenceNumber(), retrace.resumedSequenceNumber()))
        .collect(Collectors.toSet());
    return found.stream()
        .filter(retrace -> retrace.exact(ordered)
            || Stream.of(retrace.outgoingSequenceNumber(), retrace.returningSequenceNumber(), retrace.resumedSequenceNumber()).noneMatch(exactRecords::contains))
        .toList();
  }

  private Optional<Retrace> retrace(AirspaceSequence outgoing, AirspaceSequence returning, AirspaceSequence resumed) {
    if (!sameLineGeometry(outgoing, returning)) {
      return Optional.empty();
    }
    Optional<LatLong> start = outgoing.associatedFix();
    Optional<LatLong> turn = returning.associatedFix();
    Optional<LatLong> end = resumed.associatedFix();
    if (start.isEmpty() || turn.isEmpty() || end.isEmpty() || !uniquePath(start.get(), turn.get(), outgoing.geometry())) {
      return Optional.empty();
    }
    boolean found = start.equals(end)
        || (outgoing.geometry() == Geometry.GREAT_CIRCLE && backtracks(start.get(), turn.get(), end.get()));
    if (!found) {
      return Optional.empty();
    }
    double lengthNm = outgoing.geometry() == Geometry.GREAT_CIRCLE
        // The doubled-back part is the shorter leg: the outgoing leg of an overshoot, the return of an undershoot.
        ? Math.min(start.get().distanceInNM(turn.get()), start.equals(end) ? Double.POSITIVE_INFINITY : turn.get().distanceInNM(end.get()))
        : rhumbLengthNm(start.get(), turn.get());
    return Optional.of(new Retrace(outgoing.sequenceNumber(), returning.sequenceNumber(), resumed.sequenceNumber(), lengthNm));
  }

  /**
   * True when the great-circle return from B passes through A (overshoot) or stops on the A-B segment (undershoot).
   * Both legs must be longer than the tolerance: a fix within the tolerance of B lies "on" any segment from B,
   * so its direction cannot show a reversal.
   */
  private boolean backtracks(LatLong start, LatLong turn, LatLong end) {
    S2Point a = BoundaryCompiler.point(start);
    S2Point b = BoundaryCompiler.point(turn);
    S2Point c = BoundaryCompiler.point(end);
    double outAngle = a.angle(b);
    double returnAngle = b.angle(c);
    if (Math.min(outAngle, returnAngle) <= collinearToleranceRadians || Math.PI - returnAngle <= 1e-12) {
      return false;
    }
    return returnAngle >= outAngle
        ? S2EdgeUtil.getDistance(a, b, c).radians() <= collinearToleranceRadians
        : S2EdgeUtil.getDistance(c, a, b).radians() <= collinearToleranceRadians;
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
    // Match LatLong.distanceInNM; Spherical.distanceInNM(radians) assumes exactly 60 NM per degree.
    return radians * Spherical.EARTH_RADIUS_NM;
  }

  private static boolean sameMeridian(LatLong start, LatLong turn) {
    return start.longitude() == turn.longitude() || Math.abs(start.longitude() - turn.longitude()) == 360.0;
  }

  private static boolean isPole(LatLong position) {
    return Math.abs(position.latitude()) == 90.0;
  }
}
