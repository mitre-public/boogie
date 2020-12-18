package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

import com.google.common.collect.Lists;

/**
 * Extension of the {@link ScoringStrategy} for use compositionally with the composable {@link OnLegScoringRule}s.
 */
public final class RuleBasedScoringStrategy implements ScoringStrategy {

  /**
   * The reduce collection of {@link OnLegScoringRule}s to use to configure the {@link OnLegScorer}s of {@link FlyableLeg}s.
   */
  private final OnLegScoringRule onLegScoringRule;
  /**
   * The reduced collection of {@link LegTransitionScorer}s provided via the builder.
   */
  private final LegTransitionScorer legTransitionScorer;

  private RuleBasedScoringStrategy(
      OnLegScoringRule onLegScoringRule,
      LegTransitionScorer legTransitionScorer
  ) {
    this.onLegScoringRule = onLegScoringRule;
    this.legTransitionScorer = legTransitionScorer;
  }

  @Override
  public OnLegScorer onLegScorerFor(FlyableLeg flyableLeg) {
    return onLegScoringRule.onLegScorerFor(flyableLeg).orElseThrow(IllegalStateException::new);
  }

  @Override
  public LegTransitionScorer legTransitionScorer(FlyableLeg flyableLeg) {
    return legTransitionScorer;
  }

  public static class Builder {
    private List<OnLegScoringRule> onLegScoringRules = new ArrayList<>();
    private List<LegTransitionScorer> legTransitionScorers = new ArrayList<>();

    public Builder setOnLegScoringRules(List<OnLegScoringRule> onLegScoringRules) {
      this.onLegScoringRules = onLegScoringRules;
      return this;
    }

    public Builder setOnLegScorers(OnLegScoringRule... onLegScoringRules) {
      return setOnLegScoringRules(Lists.newArrayList(onLegScoringRules));
    }

    public Builder addOnLegScoringRules(List<OnLegScoringRule> onLegScoringRules) {
      this.onLegScoringRules.addAll(onLegScoringRules);
      return this;
    }

    public Builder addOnLegScoringRules(OnLegScoringRule... onLegScoringRules) {
      return addOnLegScoringRules(Lists.newArrayList(onLegScoringRules));
    }

    public Builder setLegTransitionScorers(List<LegTransitionScorer> legTransitionScorers) {
      this.legTransitionScorers = legTransitionScorers;
      return this;
    }

    public Builder setLegTransitionScorers(LegTransitionScorer... legTransitionScorers) {
      return setLegTransitionScorers(Lists.newArrayList(legTransitionScorers));
    }

    public Builder addLegTransitionScorers(List<LegTransitionScorer> legTransitionScorers) {
      this.legTransitionScorers.addAll(legTransitionScorers);
      return this;
    }

    public Builder addLegTransitionScorers(LegTransitionScorer... legTransitionScorers) {
      return addLegTransitionScorers(Lists.newArrayList(legTransitionScorers));
    }

    public RuleBasedScoringStrategy build() {
      return new RuleBasedScoringStrategy(
          onLegScoringRules.stream().reduce(leg -> Optional.empty(), OnLegScoringRule::orElse),
          legTransitionScorers.stream().reduce((l1, l2) -> Optional.empty(), LegTransitionScorer::orElseTry)
      );
    }
  }
}
