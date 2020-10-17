package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.mitre.caasd.commons.Pair;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

import com.google.common.base.Preconditions;

public class CompositeWeightedScorer implements OnLegScorer {

  /**
   * The collection of weighted scorers to combine.
   */
  private final List<Pair<OnLegScorer, Double>> weightedScorers;

  public CompositeWeightedScorer(List<Pair<OnLegScorer, Double>> weightedScorers) {
    Preconditions.checkArgument(weightedScorers.stream().mapToDouble(Pair::second).sum() == 1., "Scorer weight modifiers must sum to 1.0");
    this.weightedScorers = weightedScorers;
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint point, FlyableLeg legTriple) {
    return weightedScorers.stream().mapToDouble(pair -> pair.second() * pair.first().scoreAgainstLeg(point, legTriple)).sum();
  }

  @Override
  public Optional<Double> score(ConformablePoint point, FlyableLeg legTriple) {
    List<Optional<Double>> optionalScores = weightedScorers.stream()
        .map(pair -> pair.first().score(point, legTriple).map(score -> score * pair.second()))
        .collect(Collectors.toList());

    return optionalScores.stream().allMatch(Optional::isPresent)
        ? Optional.of(optionalScores.stream().mapToDouble(Optional::get).sum()) : Optional.empty();
  }

  /**
   * Composes the varargs set of weighted scorers and serves them as a composite scorer.
   */
  public static CompositeWeightedScorer compose(Pair<OnLegScorer, Double>... weightedScorers) {
    return new CompositeWeightedScorer(Arrays.asList(weightedScorers));
  }
}
