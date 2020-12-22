package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.caasd.commons.Spherical.angleDifference;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.MissingRequiredFieldException.supplier;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * This is the default conformance scorer for {@link PathTerm#CF} legs.
 */
public class CfScorer implements OnLegScorer {

  private final Function<Double, Double> courseWeight;
  private final DfScorer dfScorer;

  public CfScorer() {
    this(simpleLogistic(5.0, 10.0), simpleLogistic(0.5, 1.0));
  }

  public CfScorer(Function<Double, Double> courseWeight, Function<Double, Double> distanceWeight) {
    this.courseWeight = courseWeight;
    this.dfScorer = new DfScorer(distanceWeight);
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint that, FlyableLeg legTriple) {
    Fix pathTerminator = legTriple.current().pathTerminator();

    Fix navaid = legTriple.current().recommendedNavaid().orElseThrow(supplier("Recommended Navaid"));

    // both of these are bearings - magnetic
    double courseToFix = legTriple.current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));

    // convert the true course to the point to a magnetic one for comparison against the boundary/fix radials
    MagneticVariation localVariation = MagneticVariationResolver.getInstance().magneticVariation(that, legTriple);

    if (localVariation == null) {
      throw new RuntimeException("No magnetic variation for navaid: " + navaid);
    }

    double trueCourse = that.trueCourse().orElseThrow(supplier("Point Course"));
    double magneticCourse = localVariation.trueToMagnetic(trueCourse);

    Double wcrs = courseWeight.apply(abs(angleDifference(magneticCourse, courseToFix)));
    return wcrs * dfScorer.scoreAgainstLeg(that, legTriple);
  }
}
