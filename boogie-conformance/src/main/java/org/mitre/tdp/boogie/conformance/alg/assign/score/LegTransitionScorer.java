package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.Optional;

import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * Returns a score representing the likelihood of a given transition between {@link FlyableLeg}s. Optionally empty if the
 * given scoring function doesn't apply to the provided pair of flyable legs.
 */
@FunctionalInterface
public interface LegTransitionScorer {

  Optional<Double> transitionScore(FlyableLeg currentLeg, FlyableLeg nextLeg);

  default LegTransitionScorer orElseTry(LegTransitionScorer legTransitionScorer) {
    return (l1, l2) -> {
      Optional<Double> score = transitionScore(l1, l2);
      return score.isPresent() ? score : legTransitionScorer.transitionScore(l1, l2);
    };
  }
}
