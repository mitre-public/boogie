package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.Optional;
import java.util.function.BiFunction;
import java.util.function.Predicate;

import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * Defines a "rule" for deciding when a given {@link FlyableLeg} should be configured with a particular {@link OnLegScorer}.
 */
@FunctionalInterface
public interface OnLegScoringRule {

  /**
   * Returns an optional {@link OnLegScorer} assuming the scorer applies to the provided {@link FlyableLeg}.
   */
  Optional<OnLegScorer> onLegScorerFor(FlyableLeg flyableLeg);

  /**
   * Composition option - for functional chaining of scoring rules.
   */
  default OnLegScoringRule orElse(OnLegScoringRule onLegScoringRule) {
    return flyableLeg -> {
      Optional<OnLegScorer> onLegScorer = onLegScorerFor(flyableLeg);
      return onLegScorer.isPresent() ? onLegScorer : onLegScoringRule.onLegScorerFor(flyableLeg);
    };
  }

  /**
   * Allows overriding of the existing {@link OnLegScorer} returned by this functional on the fly.
   */
  default OnLegScoringRule withModification(BiFunction<FlyableLeg, OnLegScorer, OnLegScorer> modifier) {
    return flyableLeg -> onLegScorerFor(flyableLeg).map(oldScorer -> modifier.apply(flyableLeg, oldScorer));
  }

  /**
   * Generates a new {@link OnLegScoringRule} which returns the provided scorer only when the provided predicate evaluates true.
   */
  static OnLegScoringRule newScoringRule(Predicate<FlyableLeg> pred, OnLegScorer scorer) {
    return flyableLeg -> pred.test(flyableLeg) ? Optional.of(scorer) : Optional.empty();
  }
}
