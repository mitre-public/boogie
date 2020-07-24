package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.Optional;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;

/**
 * Top level class for leg scoring, providing access to both the previous and the subsequent legs as declared in the
 * procedure/airway definition.
 *
 * <p>The leg scorer provides two primary functions:
 * 1) It generates a score representing how well the aircraft is flying the current leg.
 * 2) It generates a score representing the likelihood of a transition to another leg.
 */
@FunctionalInterface
public interface OnLegScorer {

  /**
   * Returns the score in the range [0,1] of the point to the decorated {@link FlyableLeg}.
   */
  double scoreAgainstLeg(ConformablePoint point, FlyableLeg legTriple);

  default Optional<Double> score(ConformablePoint point, FlyableLeg legTriple) {
    if (legTriple.current().type().hasRequiredFields(legTriple.current())) {
      return Optional.of(scoreAgainstLeg(point, legTriple));
    }
    return Optional.empty();
  }
}
