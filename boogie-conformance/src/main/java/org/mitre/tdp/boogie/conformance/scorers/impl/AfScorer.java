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
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;

/**
 * This is the default conformance scorer for {@link LegType#AF} legs.
 */
class AfScorer implements LegScorer {

  private final ConsecutiveLegs legs;

  AfScorer(ConsecutiveLegs legs) {
    checkArgument(legs.current().type().equals(LegType.AF), "Incorrect to leg in AF leg scorer.");
    this.legs = legs;
  }

  @Override
  public ConsecutiveLegs scorerLeg() {
    return legs;
  }

  @Override
  public double score(ConformablePoint that) {
    Function<Double, Double> wfn = simpleLogistic(1.0, 2.0);

    Fix navaid = scorerLeg().current().recommendedNavaid().orElseThrow(supplier("Recommended Navaid"));
    double radius = scorerLeg().current().rho().orElseThrow(supplier("Rho"));

    // both of these are bearings - magnetic
    double fixRadial = scorerLeg().current().theta().orElseThrow(supplier("Theta"));
    double boundaryRadial = scorerLeg().current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));
    TurnDirection turnDirection = scorerLeg().current().turnDirection().orElseThrow(supplier("Turn Direction"));

    // convert the true course to the point to a magnetic one for comparison against the boundary/fix radials
    MagneticVariation localVariation = navaid.magneticVariation();
    double pointRadial = localVariation.trueToMagnetic(navaid.courseInDegrees(that));

    double pointDistance = navaid.distanceInNmTo(that);

    double distanceScore = wfn.apply(abs(pointDistance - radius));
    double radialScore = between(pointRadial, boundaryRadial, fixRadial, turnDirection) ? 1.0 : 0.0;

    return distanceScore * radialScore;
  }
}
