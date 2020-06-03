package org.mitre.tdp.boogie.conformance.scorers.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.conformance.scorers.Angles.between;
import static org.mitre.tdp.boogie.conformance.scorers.impl.MissingRequiredFieldException.supplier;
import static org.mitre.tdp.boogie.conformance.scorers.impl.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.Scorable;
import org.mitre.tdp.boogie.conformance.scorers.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;

/**
 * This is the default conformance scorer for {@link LegType#AF} legs.
 */
class AFScorer implements LegScorer {

  private final ConsecutiveLegs legs;

  AFScorer(ConsecutiveLegs legs) {
    checkArgument(legs.to().type().equals(LegType.AF), "Incorrect to leg in AF leg scorer.");
    this.legs = legs;
  }

  @Override
  public double score(ConformablePoint that) {
    Function<Double, Double> wfn = simpleLogistic(1.0, 2.0);

    Fix navaid = legs.to().recommendedNavaid().orElseThrow(supplier("Recommended Navaid"));
    double radius = legs.to().rho().orElseThrow(supplier("Rho"));

    // both of these are bearings - magnetic
    double fixRadial = legs.to().theta().orElseThrow(supplier("Theta"));
    double boundaryRadial = legs.to().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));
    TurnDirection turnDirection = legs.to().turnDirection().orElseThrow(supplier("Turn Direction"));

    // convert the true course to the point to a magnetic one for comparison against the boundary/fix radials
    MagneticVariation localVariation = navaid.magneticVariation();
    double pointRadial = localVariation.trueToMagnetic(navaid.courseInDegrees(that));

    double pointDistance = navaid.distanceInNmTo(that);

    double distanceScore = wfn.apply(abs(pointDistance - radius));
    double radialScore = between(pointRadial, boundaryRadial, fixRadial, turnDirection) ? 1.0 : 0.0;

    return distanceScore * radialScore;
  }

  @Override
  public double transitionScore(Scorable<ConformablePoint> l2) {
    return 1.0;
  }
}
