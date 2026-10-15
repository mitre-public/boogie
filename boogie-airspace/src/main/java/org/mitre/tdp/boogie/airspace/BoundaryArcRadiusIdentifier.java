package org.mitre.tdp.boogie.airspace;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Stream;

import org.mitre.caasd.commons.LatLong;
import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.Geometry;
import org.mitre.tdp.boogie.util.Streams;

/**
 * Identifies arcs whose endpoint distances from their center differ beyond a caller's tolerance.
 * Inputs must be in traversal order; each arc runs from its own associated fix to the next sequence's
 * associated fix, including across the boundary closure. Two-sequence boundaries are supported.
 *
 * <p>The comparison uses the same coordinate-derived radii as boundary projection. Published arc
 * radius and bearing fields are not used. Small differences can reflect rounded source coordinates;
 * a mismatch does not by itself establish a crossing or identify which source coordinate is wrong.
 * Circles and lines are ignored, and missing arc coordinates are left for compilation validation.
 *
 * <p>This identifier never modifies the input. Use {@link IssueResolver#of(Function, java.util.function.BiFunction)}
 * to combine it with a chosen repair or reporting policy for the factory's sequence processor.
 * {@link BoundaryArcRadiusResolver} provides an opt-in policy that moves the center to equalize the radii.
 */
public final class BoundaryArcRadiusIdentifier implements Function<List<AirspaceSequence>, List<BoundaryArcRadiusIdentifier.Mismatch>> {
  private final double toleranceNm;

  /**
   * @param toleranceNm finite, nonnegative maximum accepted difference between endpoint radii, in nautical miles
   */
  public BoundaryArcRadiusIdentifier(double toleranceNm) {
    checkArgument(Double.isFinite(toleranceNm) && toleranceNm >= 0.0, "Radius tolerance must be finite and nonnegative");
    this.toleranceNm = toleranceNm;
  }

  /** Source sequence numbers and the measured distances from the arc center to its endpoints. */
  public record Mismatch(int arcSequenceNumber, int endingSequenceNumber, double startRadiusNm, double endRadiusNm) {
    /** @return absolute difference between the endpoint radii, in nautical miles */
    public double differenceNm() {
      return Math.abs(startRadiusNm - endRadiusNm);
    }
  }

  @Override
  public List<Mismatch> apply(List<AirspaceSequence> sequences) {
    requireNonNull(sequences, "sequences");
    List<AirspaceSequence> closed = Stream.concat(sequences.stream(), sequences.stream().limit(1)).toList();
    return Streams.pairwise(closed)
        .flatMap(pair -> radii(pair.first(), pair.second()).stream())
        .filter(mismatch -> mismatch.differenceNm() > toleranceNm)
        .toList();
  }

  private static Optional<Mismatch> radii(AirspaceSequence current, AirspaceSequence next) {
    return Optional.of(current)
        .filter(BoundaryArcRadiusIdentifier::isArc)
        .flatMap(AirspaceSequence::centerFix)
        .flatMap(center -> radii(current, next, center));
  }

  private static Optional<Mismatch> radii(AirspaceSequence current, AirspaceSequence next, LatLong center) {
    return current.associatedFix()
        .flatMap(start -> next.associatedFix()
            .map(end -> new Mismatch(current.sequenceNumber(), next.sequenceNumber(),
                center.distanceInNM(start), center.distanceInNM(end))));
  }

  private static boolean isArc(AirspaceSequence sequence) {
    return sequence.geometry() == Geometry.CLOCKWISE_ARC || sequence.geometry() == Geometry.COUNTER_CLOCKWISE_ARC;
  }
}
