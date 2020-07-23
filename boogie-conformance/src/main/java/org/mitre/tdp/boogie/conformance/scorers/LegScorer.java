package org.mitre.tdp.boogie.conformance.scorers;

import java.util.Optional;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.Scorer;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;

/**
 * Top level class for leg scoring, providing access to both the previous and the subsequent legs as
 * declared in the procedure/airway definition.
 *
 * <p>The leg scorer provides two primary functions:
 * 1) It generates a score representing how well the aircraft is flying the current leg.
 * 2) It generates a score representing the likelihood of a transition to another leg.
 */
public interface LegScorer extends Scorer<ConformablePoint, ConsecutiveLegs> {

  ConsecutiveLegs scorerLeg();

  /**
   * Returns the score in the range [0,1] of the point to the decorated {@link #scorerLeg()}.
   */
  double scoreAgainstLeg(ConformablePoint point);

  @Override
  default Optional<Double> score(ConformablePoint point) {
    if (scorerLeg().current().type().hasRequiredFields(scorerLeg().current())) {
      return Optional.of(scoreAgainstLeg(point));
    }
    return Optional.empty();
  }
}
