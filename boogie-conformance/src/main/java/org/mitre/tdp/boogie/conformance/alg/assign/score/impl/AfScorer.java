package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static com.google.common.base.Preconditions.checkArgument;
import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.MissingRequiredFieldException.supplier;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;
import org.mitre.tdp.boogie.conformance.alg.assign.score.RadialAngles;

/**
 * This is the default conformance scorer for {@link PathTerm#AF} legs.
 */
public class AfScorer implements OnLegScorer {

  private final Function<Double, Double> offTrackWeight;

  public AfScorer() {
    this(simpleLogistic(0.5, 1.0));
  }

  public AfScorer(Function<Double, Double> offTrackWeight) {
    this.offTrackWeight = offTrackWeight;
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint point, FlyableLeg legTriple) {
    checkArgument(legTriple.current().type().equals(PathTerm.AF), "Incorrect to leg in AF leg scorer.");

    Fix navaid = legTriple.current().recommendedNavaid().orElseThrow(supplier("Recommended Navaid"));
    double radius = legTriple.current().rho().orElseThrow(supplier("Rho"));

    // both of these are bearings - magnetic
    double fixRadial = legTriple.current().theta().orElseThrow(supplier("Theta"));
    double boundaryRadial = legTriple.current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));
    TurnDirection turnDirection = legTriple.current().turnDirection().orElseThrow(supplier("Turn Direction"));

    // convert the true course to the point to a magnetic one for comparison against the boundary/fix radials
    MagneticVariation localVariation = navaid.magneticVariation();
    double pointRadial = localVariation.trueToMagnetic(navaid.courseInDegrees(point));
    double radialScore = RadialAngles.of(boundaryRadial, fixRadial, turnDirection).contains(pointRadial) ? 1.0 : 0.0;

    double pointDistance = navaid.distanceInNmTo(point);
    double distanceScore = offTrackWeight.apply(abs(pointDistance - radius));

    return distanceScore * radialScore;
  }
}
