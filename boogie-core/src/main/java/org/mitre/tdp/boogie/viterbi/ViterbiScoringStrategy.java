package org.mitre.tdp.boogie.viterbi;

import java.util.function.BiFunction;

/**
 * Interface representing a strategy for scoring stages and states within the {@link ViterbiTagger}.
 *
 * On initialization instead of normal {@link BiFunction}s a more formal {@link ViterbiScoringStrategy} may be supplied to the
 * {@link ViterbiTagger}. The formalization of state and transition scorers as a strategy object allows us to layer interesting
 * tooling on top of the scoring process.
 *
 * Extension interfaces include:
 *
 * 1) {@link FeatureBasedViterbiScoringStrategy} - for scoring functions for stage/state combinations which can be broken into a
 * feature extraction to feature normalization/combination pair of distinct stages.
 *
 * 2) {@link RuleBasedViterbiScoringStrategy} - which uses predicates to delegate stage/state combinations to a fixed set of
 * functions capable of converting the combination into a likelihood.
 */
public interface ViterbiScoringStrategy<Stage, State> {

  /**
   * Returns the likelihood given the provided stage, that the state is the correct association.
   */
  Double scoreStateGivenStage(Stage stage, State state);
}
