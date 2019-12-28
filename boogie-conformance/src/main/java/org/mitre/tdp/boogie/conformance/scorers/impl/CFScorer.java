package org.mitre.tdp.boogie.conformance.scorers.impl;

import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.Scorable;
import org.mitre.tdp.boogie.conformance.scorers.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;

/**
 * This is the default conformance scorer for {@link LegType#RF} legs.
 */
public class CFScorer implements LegScorer {

  private final ConsecutiveLegs legs;

  CFScorer(ConsecutiveLegs legs) {
    this.legs = legs;
  }

  @Override
  public double score(ConformablePoint that) {
    return 0;
  }

  @Override
  public double transitionScore(Scorable<ConformablePoint> l2) {
    return 0;
  }
}
