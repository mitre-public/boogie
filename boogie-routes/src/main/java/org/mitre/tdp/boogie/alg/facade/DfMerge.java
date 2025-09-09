package org.mitre.tdp.boogie.alg.facade;

import java.util.function.BiFunction;

import org.mitre.tdp.boogie.Leg;

final class DfMerge implements BiFunction<ExpandedRouteLeg, ExpandedRouteLeg, ExpandedRouteLeg> {
  DfMerge() {}
  @Override
  public ExpandedRouteLeg apply(ExpandedRouteLeg c, ExpandedRouteLeg n) {
    int sequence = c.sequenceNumber() + ((c.sequenceNumber() + n.sequenceNumber()) / 2);
    Leg df = Leg.dfBuilder(n.associatedFix().orElseThrow(), sequence)
        .recommendedNavaid(n.recommendedNavaid().orElse(null))
        .rho(n.rho().orElse(null))
        .theta(n.theta().orElse(null))
        .rnp(n.rnp().orElse(null))
        .altitudeConstraint(n.altitudeConstraint())
        .speedConstraint(n.speedConstraint())
        .turnDirection(n.turnDirection().orElse(null))
        .isFlyOverFix(n.isFlyOverFix())
        .isPublishedHoldingFix(n.isPublishedHoldingFix())
        .build();
    return new ExpandedRouteLeg(n.section(), n.elementType(), n.wildcards(), df);
  }
}
