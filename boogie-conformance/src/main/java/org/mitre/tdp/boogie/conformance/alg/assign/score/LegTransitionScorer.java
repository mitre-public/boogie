package org.mitre.tdp.boogie.conformance.alg.assign.score;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;

@FunctionalInterface
public interface LegTransitionScorer {

  double transitionScore(ConformablePoint point, FlyableLeg currentLeg, FlyableLeg nextLeg);

  static LegTransitionScorer allowAll() {
    return (point, currentLeg, nextLeg) -> 0.99;
  }
}
