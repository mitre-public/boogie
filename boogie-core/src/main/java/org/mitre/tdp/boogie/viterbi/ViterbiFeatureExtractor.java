package org.mitre.tdp.boogie.viterbi;

import static java.util.Objects.requireNonNull;

import java.util.function.BiFunction;

import org.mitre.caasd.commons.Pair;

/**
 * Simple injectable class for extracting a named feature from a state/stage combination.
 *
 * For features which are only a function of either the state or the stage (and as such don't need to be recomputed multiple
 * times across either of those dimensions) it's recommended that the {@link #featureComputer} be memoized.
 *
 * This is most easily accomplished by wrapping the function with {@link ViterbiTagger#memoize(BiFunction)}.
 */
public final class ViterbiFeatureExtractor<Stage, State> implements BiFunction<Stage, State, Pair<String, Double>> {

  /**
   * The string name of the feature.
   *
   * Package-private so there can be some checking (so we don't have duplicate extractors for the same feature) within the higher
   * level feature extraction API.
   */
  private final String featureName;
  /**
   * The function to apply to extract the feature value.
   */
  private final BiFunction<Stage, State, Double> featureComputer;

  public ViterbiFeatureExtractor(String featureName, BiFunction<Stage, State, Double> featureComputer) {
    this.featureName = requireNonNull(featureName);
    this.featureComputer = requireNonNull(featureComputer);
  }

  String featureName() {
    return featureName;
  }

  @Override
  public Pair<String, Double> apply(Stage stage, State state) {
    return Pair.of(featureName, featureComputer.apply(requireNonNull(stage), requireNonNull(state)));
  }
}
