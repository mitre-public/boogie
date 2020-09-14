package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * The default scorer provides a score which won't positively influence the overall final score, but also allows one to always
 * transition to it if necessary.
 */
public class MinValueScorer implements OnLegScorer {

  public static final MinValueScorer INSTANCE = new MinValueScorer();

  public static final double MIN_SCORE = 1e-5;

  private MinValueScorer() {
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint that, FlyableLeg legTriple) {
    return MIN_SCORE;
  }

  public static MinValueScorer getInstance() {
    return INSTANCE;
  }
}
