package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import org.mitre.tdp.boogie.PathTerm;

/**
 * This is the default conformance evaluator for {@link PathTerm#VA} legs.
 */
public class VaScorer implements AltitudeTerminationScorer {
  @Override
  public Double offCourseWeight(Double courseDelta) {
    // in theory for VA legs the provided course in the leg is a heading - since were comparing that heading to the course
    // of the aircraft instead of its heading we need to add a bit more slop than for a CA leg.
    return simpleLogistic(30.0, 50.0).apply(courseDelta);
  }

  @Override
  public Double feetToTargetAltitudeWeight(Double altitudeDelta) {
    return simpleLogistic(100.0, 250.0).apply(altitudeDelta);
  }
}
