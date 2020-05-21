package org.mitre.tdp.boogie.conformance.alg.evaluate;

import java.util.Optional;

import org.mitre.caasd.commons.Distance;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.Scorer;
import org.mitre.tdp.boogie.conformance.alg.assign.LegAssigner;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.impl.OffTrackScorer;

/**
 * A conformance evaluator takes a {@link LegAssigner} which can be used to find the leg the aircraft is
 * flying at the time of the point and uses it to decide as a simple y/n whether the aircraft is conforming
 * to the assigned leg.
 */
@FunctionalInterface
public interface ConformanceEvaluator {

  /**
   * Y/N to whether the current point is conforming to its assigned leg.
   */
  boolean conforming(ConformablePoint point, ConsecutiveLegs consecutiveLegs);

  /**
   * Returns the off track distance of the {@link ConformablePoint} from the consecutive legs if it can be
   * computed for them.
   *
   * <p>This is generally useful across all methods of estimating conformance - even those implementing the
   * {@link PrecomputedEvaluator} interface - therefore we choose to make this static for more robust if less
   * flexible access.
   */
  static Optional<Distance> offTrackDistance(ConformablePoint point, ConsecutiveLegs consecutiveLegs) {
    Scorer<ConformablePoint, ConsecutiveLegs> scorer = consecutiveLegs.scorer();
    return Optional.of(scorer)
        .filter(s -> s instanceof OffTrackScorer)
        .map(s -> ((OffTrackScorer) s).offTrackDistance(point));
  }
}
