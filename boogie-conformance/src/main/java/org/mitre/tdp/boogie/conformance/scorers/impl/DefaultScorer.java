package org.mitre.tdp.boogie.conformance.scorers.impl;

import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;

/**
 * The default scorer provides a score which won't positively influence the overall final
 * score, but also allows one to always transition to it if necessary.
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
  public double score(ConformablePoint that) {
    return 0.0;
  }
}
