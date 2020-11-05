package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.MissingRequiredFieldException.supplier;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import java.util.Optional;
import java.util.function.Function;

import org.apache.commons.math3.util.FastMath;
import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.RadialAngles;

/**
 * This is the default conformance scorer for {@link PathTerm#RF} legs.
 */
public class RfScorer implements OffTrackScorer {

  private final Function<Double, Double> offTrackWeight;

  public RfScorer() {
    this.offTrackWeight = simpleLogistic(0.25, 0.5);
  }

  public RfScorer(Function<Double, Double> offTrackWeight) {
    this.offTrackWeight = offTrackWeight;
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint point, FlyableLeg legTriple) {
    Fix centerFix = legTriple.current().centerFix().orElseThrow(supplier("Arc Center Fix"));

    MagneticVariation magneticVariation = legTriple.current().centerFix().map(Fix::magneticVariation)
        .orElseGet(() -> legTriple.current().pathTerminator().magneticVariation());
    TurnDirection turnDirection = legTriple.current().turnDirection().orElseThrow(supplier("Turn Direction"));

    double inboundTangentialMagBearing = legTriple
        // typically in the previous leg
        .previous(Leg::outboundMagneticCourse)
        // for certain leg combinations this is contained in theta (IF:RF, RF:RF, or RF:HX)
        .orElseGet(() -> legTriple.current().theta().orElseGet(() -> computeInboundTangentialCourse(legTriple)));
    double inboundTrueRadial = tangentToRadial(magneticVariation.magneticToTrue(inboundTangentialMagBearing), turnDirection);

    double outboundTangentialMagBearing = legTriple
        // typically in the next leg
        .next().flatMap(Leg::outboundMagneticCourse)
        // for certain leg combinations this is contained in current leg outbound (IF:RF, RF:RF, RF:HX, or RF is final leg)
        .orElse(legTriple.current().outboundMagneticCourse().orElseGet(() -> computeOutboundTangentialCourse(legTriple)));
    double outboundTrueRadial = tangentToRadial(magneticVariation.magneticToTrue(outboundTangentialMagBearing), turnDirection);

    double pointRadialTrue = centerFix.courseInDegrees(point);
    double radialWeight = (RadialAngles.of(inboundTrueRadial, outboundTrueRadial, turnDirection).contains(pointRadialTrue) ? 1. : 0.1);

    return OffTrackScorer.super.scoreAgainstLeg(point, legTriple) * radialWeight;
  }

  double tangentToRadial(Double tangentialTrueBearing, TurnDirection turnDirection) {
    return Spherical.mod(tangentialTrueBearing + (turnDirection.isLeft() ? 90 : -90), 360.);
  }

  double radialToTangent(Double radialTrueBearing, TurnDirection turnDirection) {
    return Spherical.mod(radialTrueBearing - (turnDirection.isLeft() ? 90. : -90.), 360.);
  }

  double computeInboundTangentialCourse(FlyableLeg flyableLeg) {
    Fix previousFix = flyableLeg.previous().map(Leg::pathTerminator).orElseThrow(supplier("Inbound Fix"));
    Fix centerFix = flyableLeg.current().centerFix().orElseThrow(supplier("Arc Center Fix"));
    TurnDirection turnDirection = flyableLeg.current().turnDirection().orElseThrow(supplier("Turn Direction"));
    return tangentToRadial(centerFix.courseInDegrees(previousFix), turnDirection);
  }

  double computeOutboundTangentialCourse(FlyableLeg flyableLeg) {
    Fix nextFix = flyableLeg.next().map(Leg::pathTerminator).orElseThrow(supplier("Outbound Fix"));
    Fix currentFix = Optional.ofNullable(flyableLeg.current().pathTerminator()).orElseThrow(supplier("Current Fix"));
    return currentFix.courseInDegrees(nextFix);
  }

  @Override
  public double weightFn(Distance distance) {
    return offTrackWeight.apply(distance.inNauticalMiles());
  }

  @Override
  public Distance offTrackDistance(ConformablePoint point, FlyableLeg legTriple) {
    Fix exitFix = Optional.ofNullable(legTriple.current().pathTerminator()).orElseThrow(supplier("Path Terminator"));
    Fix centerFix = legTriple.current().centerFix().orElseThrow(supplier("Arc Center Fix"));

    double radius = centerFix.distanceInNmTo(exitFix);
    return Distance.ofNauticalMiles(FastMath.abs(radius - centerFix.distanceInNmTo(point)));
  }
}