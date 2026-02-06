package org.mitre.tdp.boogie.conformance.alg.assign.transition;

import java.util.function.BiFunction;

import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

import com.google.common.annotations.Beta;

/**
 * This is used to impact the weights of edges between legs. So a higher value makes the edge more likely than others.
 */
@Beta
public final class StandardTransitionScorer implements BiFunction<FlyableLeg, FlyableLeg, Double> {
  private final LegTransitionScoringStrategy sameLeg = LegTransitionScoringStrategy.sameLeg();
  private final LegTransitionScoringStrategy inSequence = LegTransitionScoringStrategy.legsInSequence();
  private final LegTransitionScoringStrategy sameFixDifferentRoute = LegTransitionScoringStrategy.sameFixDifferentRoute();
  private final LegTransitionScoringStrategy nearbyFixesDifferentRoute = LegTransitionScoringStrategy.linearDistanceBetweenFix();
  private static final Double LOW_SCORE = .05;

  @Override
  public Double apply(FlyableLeg a, FlyableLeg b) {
    return sameLeg.score(a, b)
        .or(() -> inSequence.score(a, b))
        .or(() -> sameFixDifferentRoute.score(a, b))
        .or(() -> nearbyFixesDifferentRoute.score(a, b))
        .orElse(LOW_SCORE);
  }
}
