package org.mitre.tdp.boogie.conformance.scorers.impl;

import org.mitre.tdp.boogie.conformance.scorers.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;

public class LegScorerFactory {

  public static LegScorer forLegs(ConsecutiveLegs legs) {
    switch (legs.to().type()) {
      case TF:
        return new TFScorer(legs);
      case DF:
        return new DFScorer(legs);
      case AF:
        return new AFScorer(legs);
      case CF:
        return new CFScorer(legs);
      case RF:
        return new RFScorer(legs);
      case IF:
        return new IFScorer(legs);
      default:
        return new DefaultScorer();
    }
  }
}
