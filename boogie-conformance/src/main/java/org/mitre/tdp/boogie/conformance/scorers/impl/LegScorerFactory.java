package org.mitre.tdp.boogie.conformance.scorers.impl;

import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;

public class LegScorerFactory {

  /**
   * Returns a {@link LegScorer} based on the type of the {@link ConsecutiveLegs#current()}.
   */
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
