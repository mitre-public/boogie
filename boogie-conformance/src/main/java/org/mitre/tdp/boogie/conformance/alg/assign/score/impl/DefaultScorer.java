package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * The default scorer provides a score which won't positively influence the overall final score, but also allows one to always
 * transition to it if necessary.
 */
public class DefaultScorer implements OnLegScorer {

  public static final double DEFAULT_PROBABILITY = 0.01;

  @Override
  public double scoreAgainstLeg(ConformablePoint that, FlyableLeg legTriple) {
    return DEFAULT_PROBABILITY;
  }
}
