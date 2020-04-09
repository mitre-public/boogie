package org.mitre.tdp.boogie.conformance.scorers.impl;

import java.util.function.Function;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.caasd.commons.Spherical.angleDifference;
import static org.mitre.tdp.boogie.conformance.scorers.impl.MissingRequiredFieldException.supplier;
import static org.mitre.tdp.boogie.conformance.scorers.impl.WeightFunctions.simpleLogistic;

/**
 * This is the default conformance scorer for {@link LegType#CF} legs.
 */
public class CfScorer implements LegScorer {

  private final ConsecutiveLegs legs;

  CfScorer(ConsecutiveLegs legs) {
    this.legs = legs;
  }

  @Override
  public ConsecutiveLegs scorerLeg() {
    return legs;
  }

  @Override
  public double score(ConformablePoint that) {
    Function<Double, Double> wfn = simpleLogistic(5.0, 15.0);

    Fix navaid = scorerLeg().current().recommendedNavaid().orElseThrow(supplier("Recommended Navaid"));

    // both of these are bearings - magnetic
    double courseToFix = scorerLeg().current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));

    // convert the true course to the point to a magnetic one for comparison against the boundary/fix radials
    MagneticVariation localVariation = navaid.magneticVariation();
    double pointCourse = localVariation.trueToMagnetic(that.trueCourse().orElseThrow(supplier("Point Course")));
    return wfn.apply(abs(angleDifference(pointCourse, courseToFix)));
  }
}
