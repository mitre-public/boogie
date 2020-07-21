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
        return new DfScorer(legs);
      case AF:
        return new AfScorer(legs);
      case CF:
        return new CfScorer(legs);
      case RF:
        return new RfScorer(legs);
      case VA:
        return new VaScorer(legs);
      case CA:
      case FA:
        // similar scoring functions, for FA we could check alignment from fix but probably un-necessary
        return new CaScorer(legs);
      default:
        return new DefaultScorer(legs);
    }
  }
}
