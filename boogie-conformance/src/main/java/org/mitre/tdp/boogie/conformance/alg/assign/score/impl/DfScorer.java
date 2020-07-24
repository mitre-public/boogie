package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.caasd.commons.Spherical.angleDifference;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.MissingRequiredFieldException.supplier;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * This is the default conformance scorer for {@link PathTerm#DF}/{@link PathTerm#IF} legs.
 */
public class DfScorer implements OnLegScorer {

  @Override
  public double scoreAgainstLeg(ConformablePoint that, FlyableLeg legTriple) {
    Function<Double, Double> courseWeight = simpleLogistic(10.0, 20.0);
    Function<Double, Double> distanceWeight = simpleLogistic(20.0, 40.0);

    Fix pathTerminator = legTriple.current().pathTerminator();

    double distance = that.distanceInNmTo(pathTerminator);
    double courseBetween = that.courseInDegrees(pathTerminator);

    double angleDiff = angleDifference(that.trueCourse().orElseThrow(supplier("Point Course")), courseBetween);
    double wcrs = courseWeight.apply(abs(angleDiff));
    double wdst = distanceWeight.apply(distance);

    return wcrs * wdst;
  }
}
