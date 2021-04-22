package org.mitre.tdp.boogie.viterbi;

import static java.util.Objects.requireNonNull;

import java.util.ArrayList;
import java.util.List;
import java.util.function.BiFunction;
import java.util.function.BiPredicate;
import java.util.function.Predicate;
import java.util.function.Supplier;

/**
 * Simple class for building up a rules-based strategy for scoring with the {@link ViterbiTagger}.
 */
public final class RuleBasedViterbiScoringStrategy<Stage, State> implements ViterbiScoringStrategy<Stage, State> {

  /**
   * The list of all configured scorers and their associated delegation functions wrapped as {@link DelegatableScorer}s.
   */
  private final List<DelegatableScorer<Stage, State>> scorers;

  private RuleBasedViterbiScoringStrategy(Builder<Stage, State> builder) {
    this.scorers = builder.scorers;
  }

  @Override
  public Double scoreStateGivenStage(Stage stage, State state) {
    DelegatableScorer<Stage, State> scorer = scorers.stream()
        .filter(s -> s.test(stage, state))
        .findFirst()
        .orElseThrow(IllegalStateException::new);

    double score = scorer.get().apply(stage, state);
    return score;
  }

  public static <Stage, State> RuleBasedViterbiScoringStrategy.Builder<Stage, State> newBuilder() {
    return new RuleBasedViterbiScoringStrategy.Builder<>();
  }

  public static final class Builder<Stage, State> {
    private final List<DelegatableScorer<Stage, State>> scorers = new ArrayList<>();

    /**
     * Adds a new feature scorer with the supplied delegation method only dependent on the stage.
     */
    public final Builder<Stage, State> addStageDelegatedScorer(
        Predicate<Stage> stageBasedDelegator,
        BiFunction<Stage, State, Double> scorer
    ) {
      requireNonNull(stageBasedDelegator);
      return addFeatureScorer((stage, state) -> stageBasedDelegator.test(stage), scorer);
    }

    /**
     * Adds a new feature scorer with the supplied delegation method only dependent on the state.
     */
    public final Builder<Stage, State> addStateDelegatedScorer(
        Predicate<State> stateBasedDelegator,
        BiFunction<Stage, State, Double> scorer
    ) {
      requireNonNull(stateBasedDelegator);
      return addFeatureScorer((stage, state) -> stateBasedDelegator.test(state), scorer);
    }

    /**
     * Adds a new {@link ViterbiFeatureVectorScorer} with the given feature name and extraction function.
     */
    public final Builder<Stage, State> addFeatureScorer(
        BiPredicate<Stage, State> delegator,
        BiFunction<Stage, State, Double> scorer
    ) {
      this.scorers.add(new DelegatableScorer<>(delegator, scorer));
      return this;
    }

    public final RuleBasedViterbiScoringStrategy<Stage, State> build() {
      return new RuleBasedViterbiScoringStrategy<>(this);
    }
  }

  private static final class DelegatableScorer<Stage, State> implements BiPredicate<Stage, State>, Supplier<BiFunction<Stage, State, Double>> {

    private final BiPredicate<Stage, State> matches;
    private final BiFunction<Stage, State, Double> scorer;

    private DelegatableScorer(
        BiPredicate<Stage, State> matches,
        BiFunction<Stage, State, Double> scorer
    ) {
      this.matches = requireNonNull(matches);
      this.scorer = requireNonNull(scorer);
    }

    @Override
    public boolean test(Stage stage, State state) {
      return matches.test(stage, state);
    }

    @Override
    public BiFunction<Stage, State, Double> get() {
      return scorer;
    }
  }
}
