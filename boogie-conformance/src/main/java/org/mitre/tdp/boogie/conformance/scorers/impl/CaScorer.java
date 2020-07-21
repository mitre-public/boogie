package org.mitre.tdp.boogie.conformance.scorers.impl;

import static org.mitre.tdp.boogie.conformance.scorers.impl.WeightFunctions.simpleLogistic;

import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;

public class CaScorer implements AltitudeTerminationScorer {

  private final ConsecutiveLegs legs;

  public CaScorer(ConsecutiveLegs legs) {
    this.legs = legs;
  }

  @Override
  public ConsecutiveLegs scorerLeg() {
    return legs;
  }

  @Override
  public Double offCourseWeight(Double courseDelta) {
    return simpleLogistic(20.0, 40.0).apply(courseDelta);
  }

  @Override
  public Double feetToTargetAltitudeWeight(Double altitudeDelta) {
    return simpleLogistic(100.0, 250.0).apply(altitudeDelta);
  }
}
