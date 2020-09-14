package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

public class CaScorer extends AltitudeTerminationScorer {

  public CaScorer() {
    super(simpleLogistic(5., 8.), simpleLogistic(100.0, 250.0));
  }
}
