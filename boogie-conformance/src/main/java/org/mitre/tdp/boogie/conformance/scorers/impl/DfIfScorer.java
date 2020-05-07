package org.mitre.tdp.boogie.conformance.scorers.impl;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.caasd.commons.Spherical.angleDifference;
import static org.mitre.tdp.boogie.conformance.scorers.impl.MissingRequiredFieldException.supplier;
import static org.mitre.tdp.boogie.conformance.scorers.impl.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;

/**
 * This is the default conformance scorer for {@link LegType#DF} legs.
 */
class DfIfScorer implements LegScorer {

  private final ConsecutiveLegs legs;

  DfIfScorer(ConsecutiveLegs legs) {
    this.legs = legs;
  }

  @Override
  public ConsecutiveLegs scorerLeg() {
    return legs;
  }

  @Override
  public double score(ConformablePoint that) {
    Function<Double, Double> courseWeight = simpleLogistic(5.0, 15.0);
    Function<Double, Double> distanceWeight = simpleLogistic(15.0, 40.0);

    Fix pathTerminator = scorerLeg().current().pathTerminator();

    double distance = that.distanceInNmTo(pathTerminator);
    double courseBetween = that.courseInDegrees(pathTerminator);

    double angleDiff = angleDifference(that.trueCourse().orElseThrow(supplier("Point Course")), courseBetween);
    double wcrs = courseWeight.apply(abs(angleDiff));
    double wdst = distanceWeight.apply(distance);

    return wcrs * wdst;
  }
}
