package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.Optional;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;

@FunctionalInterface
public interface LegTransitionScorer {

  double transitionScore(FlyableLeg currentLeg, FlyableLeg nextLeg);

  static LegTransitionScorer allowAll() {
    return (currentLeg, nextLeg) -> {
      Optional<Object> currentSourceObject = currentLeg.getSourceObject();
      Optional<Object> nextSourceObject = nextLeg.getSourceObject();
      if (currentSourceObject.equals(nextSourceObject) && nextLeg.previous().map(p -> p.equals(currentLeg.current())).orElse(false)) {
        return 0.99;
      } else if (currentSourceObject.equals(nextSourceObject)) {
        return 0.5;
      }
      return 0.1;
    };
  }
}
