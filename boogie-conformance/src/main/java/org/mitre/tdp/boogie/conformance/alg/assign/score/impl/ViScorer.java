package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.PathTerm.CF;
import static org.mitre.tdp.boogie.PathTerm.VI;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * Generally speaking VI legs are used in departures in 1 of two primary combinations:
 *
 * 1) VI:CF - where the outbound magnetic course of the CF leg is the course to the fix (see {@link CfScorer}).
 * 2) VI:IF:TF - where the IF:TF pair specify the segment the VI is to intercept.
 *
 * The problem here is... the IF:TF pairing isn't captured within the FlyableLeg - so we can't actually draw the segment given
 * the current formulation of a {@link FlyableLeg}. As such for now we settle for VI:CF combination support.
 */
public final class ViScorer implements OnLegScorer {

  /**
   * Additional stand-in weighting factor until we do a full intercept computation and can give the score the :axe: post
   * intercept with the CF radial.
   */
  private static final Function<Double, Double> CF_DISTANCE_WEIGHT = simpleLogistic(10., 20.);

  private final Function<Double, Double> courseWeight;

  public ViScorer() {
    this(simpleLogistic(15., 30.));
  }

  public ViScorer(Function<Double, Double> courseWeight) {
    this.courseWeight = courseWeight;
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint point, FlyableLeg legTriple) {
    if (legTriple.next().filter(leg -> leg.type().equals(CF)).isPresent()) {
      // TODO - compute the expected intersection and score based on that
      double courseToCfFix = legTriple.next().flatMap(Leg::outboundMagneticCourse).orElseThrow(IllegalStateException::new);
      double pointCourse = point.trueCourse().orElseThrow(IllegalStateException::new);

      double viCourse = legTriple.current().outboundMagneticCourse().orElseThrow(IllegalStateException::new);
      double distToCf = point.distanceInNmTo(legTriple.next().map(Leg::pathTerminator).orElseThrow(IllegalStateException::new));

      MagneticVariation localVariation = MagneticVariationResolver.getInstance().magneticVariation(point, legTriple);

      double courseScore = courseWeight.apply(abs(Spherical.angleDifference(viCourse, localVariation.trueToMagnetic(pointCourse))));
      double distScore = CF_DISTANCE_WEIGHT.apply(distToCf);

      return courseScore * distScore;
    } else {
      // TODO - Other cases (VI:IF:TF), etc.
      return MinValueScorer.getInstance().scoreAgainstLeg(point, legTriple);
    }
  }

  @Override
  public Optional<Double> score(ConformablePoint point, FlyableLeg legTriple) {
    if (VI.hasRequiredFields(legTriple.current()) && legTriple.next().filter(this::supportedNextLeg).isPresent()) {
      return Optional.of(scoreAgainstLeg(point, legTriple));
    }
    return Optional.empty();
  }

  /**
   * If we have time later on it would be nice to expand this to VI:IF:TF combinations as well.
   */
  private boolean supportedNextLeg(Leg leg) {
    return leg.type().equals(CF) && CF.hasRequiredFields(leg);
  }
}
