package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import org.mitre.tdp.boogie.PathTerm;

/**
 * This is the default conformance evaluator for {@link PathTerm#VA} legs.
 */
public class VaScorer extends AltitudeTerminationScorer {

  public VaScorer() {
    super(simpleLogistic(8., 15.), simpleLogistic(100.0, 250.0));
  }
}
