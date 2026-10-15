package org.mitre.tdp.boogie.airspace;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.List;
import java.util.Set;
import java.util.function.BiFunction;
import java.util.stream.Stream;

import org.mitre.tdp.boogie.AirspaceSequence;
import org.mitre.tdp.boogie.airspace.BoundaryRetraceIdentifier.Retrace;

/**
 * Identifies exact A-B-A boundary retraces and removes excursions within a caller's length limit.
 * Apply this resolver to ordered source sequences before building an index with {@link AirspaceIndexFactory}.
 * Inputs must be in traversal order with unique sequence numbers; the last sequence closes to the first.
 *
 * <p>Both excursion edges must be great circles or both must be rhumb lines, and the two A fixes must
 * have exactly equal coordinates. Zero-length edges, ambiguous pole-to-pole rhumb lines, antipodal
 * great circles, arcs, and mixed geometries are left for normal compilation validation. Resolution
 * removes the first A and B records, retaining the returning A record and its outgoing geometry,
 * parameters, and source metadata.
 *
 * <p>{@link #identify(Object)} uses {@link BoundaryRetraceIdentifier} to report retraces of any length.
 * {@link #resolve(Object, List)} repairs the first supplied finding within the limit in traversal order.
 * {@link #apply(List)} repeats identification and resolution until no further repair is possible, including newly exposed nested
 * retraces. Each successful pass removes two records. Source lists and records are never mutated;
 * the factory still validates the resulting boundary.
 */
public final class BoundaryRetraceResolver extends IssueResolver<List<AirspaceSequence>, Retrace> {
  /**
   * @param maximumLengthNm finite, positive maximum one-way excursion length to remove, in nautical miles
   */
  public BoundaryRetraceResolver(double maximumLengthNm) {
    this(new BoundaryRetraceIdentifier(), maximumLengthNm);
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
    return identifier.apply(sequences).stream()
        .filter(selected::contains)
        .filter(retrace -> retrace.lengthNm() <= maximumLengthNm)
        .findFirst()
        .map(retrace -> Set.of(retrace.outgoingSequenceNumber(), retrace.returningSequenceNumber()))
        .map(sequenceNumbers -> sequences.stream().filter(sequence -> !sequenceNumbers.contains(sequence.sequenceNumber())).toList())
        .orElse(sequences);
  }

  @Override
  public List<AirspaceSequence> apply(List<AirspaceSequence> sequences) {
    requireNonNull(sequences, "sequences");
    return Stream.iterate(repairPass(sequences), pass -> repairPass(pass.after()))
        .dropWhile(RepairPass::changed)
        .findFirst().orElseThrow()
        .after();
  }

  private RepairPass repairPass(List<AirspaceSequence> sequences) {
    return new RepairPass(sequences, super.apply(sequences));
  }

  private record RepairPass(List<AirspaceSequence> before, List<AirspaceSequence> after) {
    private boolean changed() {
      return before != after;
    }
  }
}
