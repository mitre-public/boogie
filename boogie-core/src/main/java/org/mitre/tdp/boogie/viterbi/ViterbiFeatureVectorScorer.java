package org.mitre.tdp.boogie.viterbi;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;

import java.util.function.Function;

/**
 * Extension interface for a Viterbi scoring function which operates directly on {@link ViterbiFeatureVector}s to convert them
 * into likelihood estimates.
 */
@FunctionalInterface
public interface ViterbiFeatureVectorScorer extends Function<ViterbiFeatureVector, Double> {

  /**
   * Returns a new {@link ViterbiFeatureVectorScorer} representing the weighted combination of two other scorers.
   *
   * i.e. newScore = (scorer1 * weight1) + (scorer2 * weight2)
   *
   * Compositional scorers can be useful when the underlying distribution is relatively bi-modal (e.g. when there is a split
   * between nominal and off-nominal behavior).
   */
  static ViterbiFeatureVectorScorer weightedCombinationOf(
      ViterbiFeatureVectorScorer scorer1,
      double weight1,
      ViterbiFeatureVectorScorer scorer2,
      double weight2
  ) {
    checkArgument(weight1 + weight2 == 1., "Sum of weights in combination must be 1.");
    requireNonNull(scorer1);
    requireNonNull(scorer2);
    return viterbiFeatureVector -> (weight1 * scorer1.apply(viterbiFeatureVector)) + (weight2 * scorer2.apply(viterbiFeatureVector));
  }
}
