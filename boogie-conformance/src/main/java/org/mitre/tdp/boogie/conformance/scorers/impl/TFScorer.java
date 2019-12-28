package org.mitre.tdp.boogie.conformance.scorers.impl;

import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.Scorable;
import org.mitre.tdp.boogie.conformance.scorers.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;

/**
 * This is the default conformance evaluator for {@link LegType#TF} legs.
 */
class TFScorer implements LegScorer {

  private final ConsecutiveLegs legs;

  TFScorer(ConsecutiveLegs legs) {
    this.legs = legs;
  }

  @Override
  public double score(ConformablePoint that) {
    return 0;
  }

  @Override
  public double transitionScore(Scorable<ConformablePoint> scorable) {
    return 0;
  }
}
