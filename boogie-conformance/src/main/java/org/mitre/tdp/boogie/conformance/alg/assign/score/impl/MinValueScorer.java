package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import java.util.Optional;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * The default scorer provides a score which won't positively influence the overall final score, but also allows one to always
 * transition to it if necessary.
 */
public class MinValueScorer implements OnLegScorer {

  public static final MinValueScorer INSTANCE = new MinValueScorer();

  public static final double MIN_SCORE = 1e-10;

  private MinValueScorer() {
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint that, FlyableLeg legTriple) {
    return MIN_SCORE;
  }

  @Override
  public Optional<Double> score(ConformablePoint that, FlyableLeg legTriple) {
    return Optional.of(scoreAgainstLeg(that, legTriple));
  }

  public static MinValueScorer getInstance() {
    return INSTANCE;
  }
}
