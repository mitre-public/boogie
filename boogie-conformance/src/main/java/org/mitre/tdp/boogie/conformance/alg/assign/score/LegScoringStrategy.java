package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.function.Function;

import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assemble.GraphicalLegReducer;
import org.mitre.tdp.boogie.conformance.alg.assign.ScoreBasedRouteResolver;

/**
 * A leg scoring strategy represents a shared methodology for scoring collections of {@link FlyableLeg}s such that when those
 * legs are fed into an operator such as the {@link ScoreBasedRouteResolver} their scores work synergistically to achieve the
 * desired outcome.
 *
 * e.g. A common usage for this is to feed a set of legs into the {@link GraphicalLegReducer} to reduce the set of legs based
 * on their shared vertices then stream those legs out via {@link GraphicalLegReducer#flyableLegStream()} and apply the provided
 * {@link LegScoringStrategy} before reading them into something like the {@link ScoreBasedRouteResolver}.
 */
public interface LegScoringStrategy extends Function<FlyableLeg, FlyableLeg> {

  /**
   * Returns the {@link OnLegScorer} which should be used in the scoring of the given {@link FlyableLeg}.
   */
  OnLegScorer onLegScorerFor(FlyableLeg flyableLeg);

  /**
   * Returns the {@link LegTransitionScorer} to use when scoring transitions between this leg and connected legs.
   */
  LegTransitionScorer legTransitionScorer(FlyableLeg flyableLeg);

  @Override
  default FlyableLeg apply(FlyableLeg flyableLeg) {
    return flyableLeg.setOnLegScorer(onLegScorerFor(flyableLeg)).setLegTransitionScorer(legTransitionScorer(flyableLeg));
  }
}
