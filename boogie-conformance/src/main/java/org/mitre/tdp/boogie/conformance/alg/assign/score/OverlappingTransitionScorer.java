package org.mitre.tdp.boogie.conformance.alg.assign.score;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;

public class OverlappingTransitionScorer implements LegTransitionScorer {

  @Override
  public double transitionScore(ConformablePoint stage, FlyableLeg currentLeg, FlyableLeg nextLeg) {
    return currentLeg.equals(nextLeg) || nextLeg.previous().filter(currentLeg.current()::equals).isPresent() && currentLeg.next().filter(nextLeg.current()::equals).isPresent() ? 1.0 : 0.0;
  }
}
