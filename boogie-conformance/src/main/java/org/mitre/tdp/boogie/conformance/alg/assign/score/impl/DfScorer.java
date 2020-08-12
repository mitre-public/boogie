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
 * This is the default conformance scorer for {@link PathTerm#DF} legs.
 */
public class DfScorer implements OnLegScorer {

  private final Function<Double, Double> courseWeight;
  private final Function<Double, Double> distanceWeight;

  public DfScorer() {
    this.courseWeight = simpleLogistic(10.0, 20.0);
    this.distanceWeight = simpleLogistic(20.0, 40.0);
  }

  public DfScorer(Function<Double, Double> courseWeight, Function<Double, Double> distanceWeight) {
    this.courseWeight = courseWeight;
    this.distanceWeight = distanceWeight;
  }

  public Function<Double, Double> courseWeight() {
    return courseWeight;
  }

  public Function<Double, Double> distanceWeight() {
    return distanceWeight;
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint that, FlyableLeg legTriple) {
    Fix pathTerminator = legTriple.current().pathTerminator();

    double distance = that.distanceInNmTo(pathTerminator);
    double courseBetween = that.courseInDegrees(pathTerminator);

    double angleDiff = angleDifference(that.trueCourse().orElseThrow(supplier("Point Course")), courseBetween);
    double wcrs = courseWeight().apply(abs(angleDiff));
    double wdst = distanceWeight().apply(distance);

    return wcrs * wdst;
  }
}
