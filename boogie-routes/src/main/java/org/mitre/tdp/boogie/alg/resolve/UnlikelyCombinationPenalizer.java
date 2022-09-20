package org.mitre.tdp.boogie.alg.resolve;

import java.util.List;
import java.util.function.UnaryOperator;
import java.util.stream.Collectors;

/**
 * This class adds a penalty to a weight. This is needed for various reasons, like discouraging
 * odd combinations that lat/long wise seem ok but operationally make no sense (e.g., fling on an
 * airway then going onto a SID vs. the next airway).
 */
public final class UnlikelyCombinationPenalizer implements UnaryOperator<List<LinkedLegs>> {
  private final Double penalty;

  public UnlikelyCombinationPenalizer(Double penalty) {
    this.penalty = penalty;
  }

  @Override
  public List<LinkedLegs> apply(List<LinkedLegs> linkedLegsList) {
    return linkedLegsList.stream()
        .map(leg -> new LinkedLegs(leg.source(), leg.target(), leg.linkWeight() + penalty))
        .collect(Collectors.toList());
  }
}
