package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * Simple implementation of a scorer for a Vector to Manual Termination leg - which generally just means "fly this vector
 * (specified in the outbound_magnetic_course) until ATC tells you to stop and turn to the next leg".
 */
public final class VmScorer implements OnLegScorer {

  private final Function<Double, Double> courseWeight;

  public VmScorer() {
    this(simpleLogistic(15., 30.));
  }

  public VmScorer(Function<Double, Double> courseWeight) {
    this.courseWeight = courseWeight;
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint point, FlyableLeg legTriple) {
    double pointCourse = point.trueCourse().orElseThrow(IllegalStateException::new);
    double vmCourse = legTriple.current().outboundMagneticCourse().orElseThrow(IllegalStateException::new);

    MagneticVariation localVariation = MagneticVariationResolver.getInstance().magneticVariation(point, legTriple);

    double courseScore = courseWeight.apply(abs(Spherical.angleDifference(vmCourse, localVariation.trueToMagnetic(pointCourse))));
    return courseScore;
  }
}
