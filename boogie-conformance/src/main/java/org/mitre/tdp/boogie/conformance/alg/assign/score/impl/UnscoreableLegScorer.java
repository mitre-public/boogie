package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * A scorer implementation for use when the method of scoring a leg is unknown.
 */
public class UnscoreableLegScorer implements OnLegScorer {

  private static final UnscoreableLegScorer INSTANCE = new UnscoreableLegScorer();

  private UnscoreableLegScorer() {
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint point, FlyableLeg legTriple) {
    return 1e-10;
  }

  public static UnscoreableLegScorer getInstance() {
    return INSTANCE;
  }
}
