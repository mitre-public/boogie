package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * Simple implementation of a scorer for a Vector to Manual Termination leg - which generally just means "fly this vector
 * (specified in the outbound_magnetic_course) until ATC tells you to stop and turn to the next leg".
 */
public final class VmScorer implements OnLegScorer {

  /**
   * Additional stand-in weighting factor until we do a full intercept computation and can give the score the :axe: post
   * intercept with the CF radial.
   */
  private static final Function<Double, Double> NEXT_FIX_DISTANCE_WEIGHT = simpleLogistic(10., 20.);

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

    double distToNextFix = point.distanceInNmTo(legTriple.next().map(Leg::pathTerminator).orElseThrow(IllegalStateException::new));

    MagneticVariation localVariation = MagneticVariationResolver.getInstance().magneticVariation(point, legTriple);

    double courseScore = courseWeight.apply(abs(Spherical.angleDifference(vmCourse, localVariation.trueToMagnetic(pointCourse))));
    double distScore = NEXT_FIX_DISTANCE_WEIGHT.apply(distToNextFix);

    return courseScore * distScore;
  }

  @Override
  public Optional<Double> score(ConformablePoint point, FlyableLeg legTriple) {
    if (PathTerm.VM.hasRequiredFields(legTriple.current()) && legTriple.next().filter(this::supportedNextLeg).isPresent()) {
      return Optional.of(scoreAgainstLeg(point, legTriple));
    }
    return MinValueScorer.getInstance().score(point, legTriple);
  }

  private boolean supportedNextLeg(Leg leg) {
    return leg.pathTerminator() != null && leg.pathTerminator().latLong() != null;
  }
}
