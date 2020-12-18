package org.mitre.tdp.boogie.conformance.alg.assign.score;

import org.mitre.tdp.boogie.conformance.alg.assign.AssignmentAlgorithm;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * A leg scoring strategy represents a shared methodology for scoring collections of {@link FlyableLeg}s within the context of
 * an {@link AssignmentAlgorithm}. These scores inform a Hidden Markov Model which is then used to decide the optimal set of
 * flyable leg assignments for a given collection of points.
 */
public interface ScoringStrategy {

  /**
   * Returns the {@link OnLegScorer} which should be used in the scoring of the given {@link FlyableLeg}.
   */
  OnLegScorer onLegScorerFor(FlyableLeg flyableLeg);

  /**
   * Returns the {@link LegTransitionScorer} to use when scoring transitions between this leg and connected legs.
   */
  LegTransitionScorer legTransitionScorer(FlyableLeg flyableLeg);
}
