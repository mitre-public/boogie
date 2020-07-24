package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

public class CaScorer implements AltitudeTerminationScorer {

  @Override
  public Double offCourseWeight(Double courseDelta) {
    return simpleLogistic(20.0, 40.0).apply(courseDelta);
  }

  @Override
  public Double feetToTargetAltitudeWeight(Double altitudeDelta) {
    return simpleLogistic(100.0, 250.0).apply(altitudeDelta);
  }
}
