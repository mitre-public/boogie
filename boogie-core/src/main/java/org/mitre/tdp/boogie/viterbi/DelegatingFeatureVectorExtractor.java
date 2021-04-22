package org.mitre.tdp.boogie.viterbi;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * A delegating feature extractor wraps a collection of {@link ViterbiFeatureVectorExtractor}s alongside predicates for determining
 * when a stage/state combination should use that particular extractor.
 *
 * This is convenient when you have a collection of models (e.g. one per leg type AF/RF/TF/CF/VA/etc.) and the one which should be
 * used for a given stage/state combination depends on features of that stage/state (e.g. when provided a point and a TF leg we
 * should probably be using the TF-based feature extractor and likelihood model to assign a likelihood instead of the RF/VA/etc.).
 *
 * For one-size-fits all cases something like this isn't necessary and the {@link FeatureBasedViterbiScoringStrategy} can be built
 * with a single configured static feature extractor (regardless of the input stage/state combinations).
 *
 * (think using this explicitly in) {@link FeatureBasedViterbiScoringStrategy#featureExtractor(Object, Object)}
 */
public final class DelegatingFeatureVectorExtractor<Stage, State> implements BiFunction<Stage, State, ViterbiFeatureVectorExtractor<Stage, State>> {

  /**
   * The ordered list of feature extractors to apply to the underlying state/stages - the first extractor who's delegation method
   * returns "true" will be used to extract the feature vector from the stage/state combination.
   *
   * If there are no matches at the end an {@link IllegalStateException} will be thrown.
   */
  private final List<DelegatableFeatureExtractor<Stage, State>> featureExtractors;

  private DelegatingFeatureVectorExtractor(Builder<Stage, State> builder) {
    this.featureExtractors = builder.extractors;
  }

  @Override
  public ViterbiFeatureVectorExtractor<Stage, State> apply(Stage stage, State state) {
    return featureExtractors.stream()
        .filter(extractor -> extractor.test(stage, state))
        .findFirst().orElseThrow(IllegalStateException::new).get();
  }

  public static <Stage, State> Builder<Stage, State> newBuilder() {
    return new Builder<>();
  }

  /**
   * Builder class for adding delegatable {@link ViterbiFeatureVectorExtractor}s to the configuration.
   */
  public static final class Builder<Stage, State> {
    private final List<DelegatableFeatureExtractor<Stage, State>> extractors = new ArrayList<>();

    /**
     * Adds a new feature extractor with the supplied delegation method only dependent on the stage.
     */
    public final Builder<Stage, State> addStageDelegatedFeatureExtractor(
        Predicate<Stage> stageBasedDelegator,
        ViterbiFeatureVectorExtractor<Stage, State> extractor
    ) {
      requireNonNull(stageBasedDelegator);
      return addFeatureExtractor((stage, state) -> stageBasedDelegator.test(stage), extractor);
    }

    /**
     * Adds a new feature extractor with the supplied delegation method only dependent on the state.
     */
    public final Builder<Stage, State> addStateDelegatedFeatureExtractor(
        Predicate<State> stateBasedDelegator,
        ViterbiFeatureVectorExtractor<Stage, State> extractor
    ) {
      requireNonNull(stateBasedDelegator);
      return addFeatureExtractor((stage, state) -> stateBasedDelegator.test(state), extractor);
    }

    /**
     * Adds a new {@link ViterbiFeatureVectorExtractor} with the given feature name and extraction function.
     */
    public final Builder<Stage, State> addFeatureExtractor(
        BiPredicate<Stage, State> delegator,
        ViterbiFeatureVectorExtractor<Stage, State> extractor
    ) {
      this.extractors.add(new DelegatableFeatureExtractor<>(delegator, extractor));
      return this;
    }

    public final DelegatingFeatureVectorExtractor<Stage, State> build() {
      return new DelegatingFeatureVectorExtractor<>(this);
    }
  }

  /**
   * Wrapper class representing holding a {@link ViterbiFeatureVectorExtractor} function as well as a delegation function.
   */
  private static final class DelegatableFeatureExtractor<Stage, State> implements BiPredicate<Stage, State>, Supplier<ViterbiFeatureVectorExtractor<Stage, State>> {

    /**
     * Returns true when the stage/state combination should be handed off to the configured extractor.
     */
    private final BiPredicate<Stage, State> matches;
    private final ViterbiFeatureVectorExtractor<Stage, State> extractor;

    private DelegatableFeatureExtractor(
        BiPredicate<Stage, State> matches,
        ViterbiFeatureVectorExtractor<Stage, State> extractor
    ) {
      this.matches = requireNonNull(matches);
      this.extractor = requireNonNull(extractor);
    }

    @Override
    public boolean test(Stage stage, State state) {
      return matches.test(stage, state);
    }

    @Override
    public ViterbiFeatureVectorExtractor<Stage, State> get() {
      return extractor;
    }
  }
}
