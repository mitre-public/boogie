package org.mitre.tdp.boogie.conformance.scorers.impl;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;

/**
 * The default scorer provides a score which won't positively influence the overall final score, but also allows one to always
 * transition to it if necessary.
 */
public class DefaultScorer implements LegScorer {

  private final ConsecutiveLegs legs;

  public DefaultScorer(ConsecutiveLegs legs) {
    this.legs = legs;
  }

  @Override
  public ConsecutiveLegs scorerLeg() {
    return legs;
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint that) {
    return 0.0;
  }
}
