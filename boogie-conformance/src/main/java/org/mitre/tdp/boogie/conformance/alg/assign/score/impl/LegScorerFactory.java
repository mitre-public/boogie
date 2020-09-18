package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.mitre.caasd.commons.Pair.of;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.CompositeWeightedScorer.compose;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * Simple leg scorer factory for generating leg scorers based off of the {@link PathTerm} of a leg.
 */
public class LegScorerFactory {

  /**
   * Returns a {@link OnLegScorer} based on the type of the {@link FlyableLeg#current()}.
   */
  public static OnLegScorer forLegType(PathTerm pathTerm) {
    switch (pathTerm) {
      case TF:
        TfScorer onTfScorer = new TfScorer();
        TfScorer offTfScorer = new TfScorer(simpleLogistic(5., 10.));
        return compose(of(onTfScorer, .9), of(offTfScorer, .1));
      case DF:
        DfScorer onDfScorer = new DfScorer();
        DfScorer offDfScorer = new DfScorer(simpleLogistic(10., 20.));
        return compose(of(onDfScorer, .9), of(offDfScorer, .1));
      case AF:
        AfScorer onAfScorer = new AfScorer();
        AfScorer offAfScorer = new AfScorer(simpleLogistic(1., 2.));
        return compose(of(onAfScorer, .9), of(offAfScorer, .1));
      case CF:
        CfScorer onCfScorer = new CfScorer();
        CfScorer offCfScorer = new CfScorer(
            simpleLogistic(10., 20.),
            simpleLogistic(10., 20.));
        return compose(of(onCfScorer, .9), of(offCfScorer, .1));
      case RF:
        RfScorer onRfScorer = new RfScorer();
        RfScorer offRfScorer = new RfScorer(simpleLogistic(1., 2.));
        return compose(of(onRfScorer, .9), of(offRfScorer, .1));
      case VA:
        VaScorer onVaScorer = new VaScorer();
        AltitudeTerminationScorer offVaScorer = new AltitudeTerminationScorer(
            simpleLogistic(15., 30.),
            simpleLogistic(100.0, 250.0));
        return compose(of(onVaScorer, .9), of(offVaScorer, .1));
      case CA:
      case FA:
        CaScorer onCaScorer = new CaScorer();
        AltitudeTerminationScorer offCaScorer = new AltitudeTerminationScorer(
            simpleLogistic(10., 20.),
            simpleLogistic(100.0, 250.0));
        return compose(of(onCaScorer, .9), of(offCaScorer, .1));
      case IF:
        IfScorer onIfScorer = new IfScorer();
        IfScorer offIfScorer = new IfScorer(simpleLogistic(1., 2.));
        return compose(of(onIfScorer, .9), of(offIfScorer, .1));
      default:
        return UnscoreableLegScorer.getInstance();
    }
  }
}
