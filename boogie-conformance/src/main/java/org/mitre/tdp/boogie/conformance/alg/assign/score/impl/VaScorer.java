package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.PathTerm;

/**
 * This is the default conformance evaluator for {@link PathTerm#VA} legs.
 */
public class VaScorer extends AltitudeTerminationScorer {

  public VaScorer() {
    this(simpleLogistic(15., 30.), simpleLogistic(100.0, 250.0));
  }

  public VaScorer(Function<Double, Double> offCourseWeight, Function<Double, Double> pastTargetAltitudeWeight) {
    super(offCourseWeight, pastTargetAltitudeWeight);
  }
}
