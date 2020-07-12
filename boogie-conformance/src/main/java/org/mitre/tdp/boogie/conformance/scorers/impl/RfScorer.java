package org.mitre.tdp.boogie.conformance.scorers.impl;

import static org.mitre.tdp.boogie.conformance.scorers.impl.MissingRequiredFieldException.supplier;
import static org.mitre.tdp.boogie.conformance.scorers.impl.WeightFunctions.simpleLogistic;

import java.util.Optional;

import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;
import org.mitre.tdp.boogie.conformance.scorers.RadialAngles;

/**
 * This is the default conformance scorer for {@link PathTerm#RF} legs.
 */
public class RfScorer implements OffTrackScorer {

  private final ConsecutiveLegs legs;

  RfScorer(ConsecutiveLegs legs) {
    this.legs = legs;
  }

  @Override
  public ConsecutiveLegs scorerLeg() {
    return legs;
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint point) {
    Fix centerFix = scorerLeg().current().centerFix().orElseThrow(supplier("Arc Center Fix"));

    MagneticVariation magneticVariation = scorerLeg().current().pathTerminator().magneticVariation();
    TurnDirection turnDirection = scorerLeg().current().turnDirection().orElseThrow(supplier("Turn Direction"));

    double inboundTangentialMagBearing = scorerLeg().previous(Leg::outboundMagneticCourse).orElseThrow(supplier("Inbound Tangential Course"));
    double inboundTrueRadial = Spherical.mod(magneticVariation.magneticToTrue(inboundTangentialMagBearing) - 90.0, 360.0);

    double outboundTangentialMagBearing = scorerLeg().current().outboundMagneticCourse().orElseThrow(supplier("Outbound Tangential Course"));
    double outboundTrueRadial = Spherical.mod(magneticVariation.magneticToTrue(outboundTangentialMagBearing) - 90.0, 360.0);

    double pointRadialTrue = magneticVariation.trueToMagnetic(centerFix.courseInDegrees(point));
    return OffTrackScorer.super.scoreAgainstLeg(point) * (RadialAngles.of(inboundTrueRadial, outboundTrueRadial, turnDirection).contains(pointRadialTrue) ? 1.0 : 0.0);
  }

  @Override
  public double weightFn(Distance distance) {
    return simpleLogistic(0.75, 1.25).apply(distance.inNauticalMiles());
  }

  @Override
  public Distance offTrackDistance(ConformablePoint point) {
    Fix exitFix = Optional.ofNullable(scorerLeg().current().pathTerminator()).orElseThrow(supplier("Path Terminator"));
    Fix centerFix = scorerLeg().current().centerFix().orElseThrow(supplier("Arc Center Fix"));

    double radius = centerFix.distanceInNmTo(exitFix);
    return Distance.ofNauticalMiles(radius);
  }
}