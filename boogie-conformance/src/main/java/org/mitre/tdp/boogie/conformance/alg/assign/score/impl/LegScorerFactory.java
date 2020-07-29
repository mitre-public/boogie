package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

public class LegScorerFactory {

  /**
   * Returns a {@link OnLegScorer} based on the type of the {@link FlyableLeg#current()}.
   */
  public static OnLegScorer forLegType(PathTerm pathTerm) {
    switch (pathTerm) {
      case TF:
        return new TfScorer();
      case DF:
        return new DfScorer();
      case AF:
        return new AfScorer();
      case CF:
        return new CfScorer();
      case RF:
        return new RfScorer();
      case VA:
        return new VaScorer();
      case CA:
      case FA:
        // similar scoring functions, for FA we could check alignment from fix but probably un-necessary
        return new CaScorer();
      default:
        return new MinValueScorer();
    }
  }
}
