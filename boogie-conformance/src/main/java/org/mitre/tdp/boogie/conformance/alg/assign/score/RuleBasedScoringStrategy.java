package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.List;
import java.util.Optional;

import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;

/**
 * Extension of the {@link LegScoringStrategy} for use compositionally with the composable {@link OnLegScoringRule}s.
 */
public interface RuleBasedScoringStrategy extends LegScoringStrategy {

  /**
   * The collection of {@link OnLegScoringRule}s to use to configure the {@link OnLegScorer}s of {@link FlyableLeg}s. The list
   * is expected to be provided in priority order for configuration.
   */
  List<OnLegScoringRule> onLegScoringRules();

  /**
   * The collection of {@link LegTransitionScorer}s.
   */
  List<LegTransitionScorer> legTransitionScorers();

  @Override
  default OnLegScorer onLegScorerFor(FlyableLeg flyableLeg) {
    return onLegScoringRules().stream().reduce(leg -> Optional.empty(), OnLegScoringRule::orElse).onLegScorerFor(flyableLeg).orElseThrow(IllegalStateException::new);
  }

  @Override
  default LegTransitionScorer legTransitionScorer(FlyableLeg flyableLeg) {
    return legTransitionScorers().stream().reduce((l1, l2) -> Optional.empty(), LegTransitionScorer::orElseTry);
  }
}
