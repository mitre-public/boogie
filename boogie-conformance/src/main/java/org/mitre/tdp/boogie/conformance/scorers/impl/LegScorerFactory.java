package org.mitre.tdp.boogie.conformance.scorers.impl;

import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;

public class LegScorerFactory {

  public static LegScorer forLegs(ConsecutiveLegs legs) {
    switch (legs.current().type()) {
      case TF:
        return new TfScorer(legs);
      case DF:
      case IF:
        return new DfIfScorer(legs);
      case AF:
        return new AfScorer(legs);
      case CF:
        return new CfScorer(legs);
      case RF:
        return new RfScorer(legs);
      default:
        return new DefaultScorer(legs);
    }
  }
}
