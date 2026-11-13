package org.mitre.tdp.boogie.airspace;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;

import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.airspace.BoundaryRetraceIdentifier.Retrace;

/**
 * Identifies boundary retraces and removes excursions within a caller's length limit.
 * Apply this resolver to ordered source sequences before building an index with {@link AirspaceIndexFactory}.
 * Sequences are analyzed in sequence-number order, and the last sequence closes to the first. Duplicate
 * sequence numbers are rejected with an {@link IllegalArgumentException}; the input list order is preserved.
 *
 * <p>Both excursion edges must be great circles or both must be rhumb lines. An exact A-B-A retrace,
 * where the two A fixes have exactly equal coordinates, is resolved by removing the first A and B
 * records, retaining the returning A record and its outgoing geometry, parameters, and source metadata.
 * A great-circle backtrack, where the return from B overshoots A or stops short of it along the same
 * line (see {@link BoundaryRetraceIdentifier}), is resolved by removing only the B record, so the
 * boundary runs from A straight to C along that line. Neither repair changes the enclosed area.
 * Zero-length edges, ambiguous pole-to-pole rhumb lines, antipodal great circles, arcs, and mixed
 * geometries are left for normal compilation validation.
 *
 * <p>{@link #identify(Object)} uses {@link BoundaryRetraceIdentifier} to report retraces of any length.
 * {@link #resolve(Object, List)} repairs the first supplied finding within the limit in traversal order.
 * {@link #apply(List)} repeats identification and resolution until no further repair is possible, including newly exposed nested
 * retraces. Each successful pass removes one or two records. Source lists and records are never mutated;
 * the factory still validates the resulting boundary.
 */
public final class BoundaryRetraceResolver extends IssueResolver<List<AirspaceSequence>, Retrace> {
  /**
   * Uses {@link BoundaryRetraceIdentifier#DEFAULT_COLLINEAR_TOLERANCE_NM} to recognize backtracks.
   * @param maximumLengthNm finite, positive maximum one-way excursion length to remove, in nautical miles
   */
  public BoundaryRetraceResolver(double maximumLengthNm) {
    this(new BoundaryRetraceIdentifier(), maximumLengthNm);
  }

  /**
   * @param maximumLengthNm finite, positive maximum one-way excursion length to remove, in nautical miles
   * @param collinearToleranceNm finite, nonnegative distance within which a fix counts as lying on a
   *     great-circle segment, in nautical miles
   */
  public BoundaryRetraceResolver(double maximumLengthNm, double collinearToleranceNm) {
    this(new BoundaryRetraceIdentifier(collinearToleranceNm), maximumLengthNm);
  }

  private BoundaryRetraceResolver(BoundaryRetraceIdentifier identifier, double maximumLengthNm) {
    super(identifier, repairPolicy(identifier, maximumLengthNm));
  }

  private static BiFunction<List<AirspaceSequence>, List<Retrace>, List<AirspaceSequence>> repairPolicy(BoundaryRetraceIdentifier identifier, double maximumLengthNm) {
    checkArgument(Double.isFinite(maximumLengthNm) && maximumLengthNm > 0.0, "Maximum retrace length must be finite and positive");
    return (sequences, issues) -> repair(sequences, issues, identifier, maximumLengthNm);
  }

  private static List<AirspaceSequence> repair(List<AirspaceSequence> sequences, List<Retrace> issues, BoundaryRetraceIdentifier identifier, double maximumLengthNm) {
    Set<Retrace> selected = Set.copyOf(issues);
    List<Retrace> eligible = identifier.apply(sequences).stream()
        .filter(selected::contains)
        .filter(retrace -> retrace.lengthNm() <= maximumLengthNm)
        .toList();
    // Exact retraces first, keeping their established repair; backtracks only once none remain.
    return eligible.stream().filter(retrace -> retrace.exact(sequences)).findFirst()
        .or(() -> eligible.stream().findFirst())
        .map(retrace -> retrace.exact(sequences)
            ? Set.of(retrace.outgoingSequenceNumber(), retrace.returningSequenceNumber())
            : Set.of(retrace.returningSequenceNumber()))
        .map(sequenceNumbers -> sequences.stream().filter(sequence -> !sequenceNumbers.contains(sequence.sequenceNumber())).toList())
        .orElse(sequences);
  }

  @Override
  public List<AirspaceSequence> apply(List<AirspaceSequence> sequences) {
    requireNonNull(sequences, "sequences");
    List<AirspaceSequence> current = sequences;
    // Each successful repair removes at least one record, so an unchanged size means no repair was possible.
    for (List<AirspaceSequence> next = super.apply(current); next.size() < current.size(); next = super.apply(current)) {
      current = next;
    }
    return current;
  }
}
