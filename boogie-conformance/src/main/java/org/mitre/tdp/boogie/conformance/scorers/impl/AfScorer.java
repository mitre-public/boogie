package org.mitre.tdp.boogie.conformance.scorers.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.conformance.scorers.impl.MissingRequiredFieldException.supplier;
import static org.mitre.tdp.boogie.conformance.scorers.impl.WeightFunctions.simpleLogistic;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.LegScorer;
import org.mitre.tdp.boogie.conformance.scorers.RadialAngles;

/**
 * This is the default conformance scorer for {@link PathTerm#AF} legs.
 */
public class AfScorer implements LegScorer {

  private final ConsecutiveLegs legs;

  AfScorer(ConsecutiveLegs legs) {
    checkArgument(legs.current().type().equals(PathTerm.AF), "Incorrect to leg in AF leg scorer.");
    this.legs = legs;
  }

  @Override
  public ConsecutiveLegs scorerLeg() {
    return legs;
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint point) {
    Function<Double, Double> wfn = simpleLogistic(1.0, 2.0);

    Fix navaid = scorerLeg().current().recommendedNavaid().orElseThrow(supplier("Recommended Navaid"));
    double radius = scorerLeg().current().rho().orElseThrow(supplier("Rho"));

    // both of these are bearings - magnetic
    double fixRadial = scorerLeg().current().theta().orElseThrow(supplier("Theta"));
    double boundaryRadial = scorerLeg().current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));
    TurnDirection turnDirection = scorerLeg().current().turnDirection().orElseThrow(supplier("Turn Direction"));

    // convert the true course to the point to a magnetic one for comparison against the boundary/fix radials
    MagneticVariation localVariation = navaid.magneticVariation();
    double pointRadial = localVariation.trueToMagnetic(navaid.courseInDegrees(point));
    double radialScore = RadialAngles.of(boundaryRadial, fixRadial, turnDirection).contains(pointRadial) ? 1.0 : 0.0;

    double pointDistance = navaid.distanceInNmTo(point);
    double distanceScore = wfn.apply(abs(pointDistance - radius));

    return distanceScore * radialScore;
  }
}
