package org.mitre.tdp.boogie.conformance.alg.assign.transition;

import java.util.function.BiFunction;

import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * This is used to impact the weights of edges between legs. So a higher value makes the edge more likely than others.
 */
public final class StandardTransitionScorer implements BiFunction<FlyableLeg, FlyableLeg, Double> {
  private final LegTransitionScoringStrategy sameLeg = LegTransitionScoringStrategy.sameLeg();
  private final LegTransitionScoringStrategy inSequence = LegTransitionScoringStrategy.legsInSequence();
  private final LegTransitionScoringStrategy noCigar = LegTransitionScoringStrategy.closeButDifferentRoute();
  private final LegTransitionScoringStrategy linearScore = LegTransitionScoringStrategy.linearDistance();
  private static final Double LOW_SCORE = .05;

  @Override
  public Double apply(FlyableLeg a, FlyableLeg b) {
    return sameLeg.score(a, b)
        .or(() -> inSequence.score(a, b))
        .or(() -> noCigar.score(a, b))
        .or(() -> linearScore.score(a, b))
        .orElse(LOW_SCORE);
  }
}
