package org.mitre.tdp.boogie.viterbi;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Conceptual sibling of {@link DelegatingFeatureVectorExtractor} but for the {@link ViterbiFeatureVector} scoring process.
 */
public final class DelegatingFeatureVectorScorer<STAGE, STATE> implements BiFunction<STAGE, STATE, ViterbiFeatureVectorScorer> {

  /**
   * The ordered list of feature scorers to apply to the underlying state/stages - the first scorer who's delegation method
   * returns "true" will be used to score the feature vector given the stage/state combination.
   *
   * If there are no matches at the end an {@link IllegalStateException} will be thrown.
   */
  private final List<DelegateableFeatureVectorScorer<STAGE, STATE>> featureScorers;

  public DelegatingFeatureVectorScorer(Builder<STAGE, STATE> builder) {
    this.featureScorers = builder.scorers;
  }

  @Override
  public ViterbiFeatureVectorScorer apply(STAGE stage, STATE state) {
    return featureScorers.stream()
        .filter(scorer -> scorer.test(stage, state))
        .findFirst().orElseThrow(IllegalStateException::new).get();
  }

  public static <Stage, State> Builder<Stage, State> newBuilder() {
    return new Builder<>();
  }

  /**
   * Builder class for adding delegatable {@link ViterbiFeatureVectorExtractor}s to the configuration.
   */
  public static final class Builder<Stage, State> {
    private final List<DelegateableFeatureVectorScorer<Stage, State>> scorers = new ArrayList<>();

    /**
     * Adds a new feature scorer with the supplied delegation method only dependent on the stage.
     */
    public final Builder<Stage, State> addStageDelegatedFeatureScorer(
        Predicate<Stage> stageBasedDelegator,
        ViterbiFeatureVectorScorer scorer
    ) {
      requireNonNull(stageBasedDelegator);
      return addFeatureScorer((stage, state) -> stageBasedDelegator.test(stage), scorer);
    }

    /**
     * Adds a new feature scorer with the supplied delegation method only dependent on the state.
     */
    public final Builder<Stage, State> addStateDelegatedFeatureScorer(
        Predicate<State> stateBasedDelegator,
        ViterbiFeatureVectorScorer scorer
    ) {
      requireNonNull(stateBasedDelegator);
      return addFeatureScorer((stage, state) -> stateBasedDelegator.test(state), scorer);
    }

    /**
     * Adds a new {@link ViterbiFeatureVectorScorer} with the given feature name and extraction function.
     */
    public final Builder<Stage, State> addFeatureScorer(
        BiPredicate<Stage, State> delegator,
        ViterbiFeatureVectorScorer scorer
    ) {
      this.scorers.add(new DelegateableFeatureVectorScorer<>(delegator, scorer));
      return this;
    }

    public final DelegatingFeatureVectorScorer<Stage, State> build() {
      return new DelegatingFeatureVectorScorer<>(this);
    }
  }

  private static final class DelegateableFeatureVectorScorer<Stage, State> implements BiPredicate<Stage, State>, Supplier<ViterbiFeatureVectorScorer> {

    private final BiPredicate<Stage, State> matches;
    private final ViterbiFeatureVectorScorer scorer;

    public DelegateableFeatureVectorScorer(
        BiPredicate<Stage, State> matches,
        ViterbiFeatureVectorScorer scorer
    ) {
      this.matches = requireNonNull(matches);
      this.scorer = requireNonNull(scorer);
    }

    @Override
    public boolean test(Stage stage, State state) {
      return matches.test(stage, state);
    }

    @Override
    public ViterbiFeatureVectorScorer get() {
      return scorer;
    }
  }
}
