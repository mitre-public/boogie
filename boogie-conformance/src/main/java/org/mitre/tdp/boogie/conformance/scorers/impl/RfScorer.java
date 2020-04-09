package org.mitre.tdp.boogie.conformance.scorers.impl;

import static org.mitre.tdp.boogie.conformance.scorers.impl.MissingRequiredFieldException.supplier;
import static org.mitre.tdp.boogie.conformance.scorers.impl.WeightFunctions.simpleLogistic;

import java.util.Optional;

import org.mitre.caasd.commons.Distance;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.LegType;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;

/**
 * This is the default conformance scorer for {@link LegType#RF} legs.
 */
class RfScorer implements OffTrackScorer {

  private final ConsecutiveLegs legs;

  RfScorer(ConsecutiveLegs legs) {
    this.legs = legs;
  }

  @Override
  public ConsecutiveLegs scorerLeg() {
    return legs;
  }

  @Override
  public double weightFn(Distance distance) {
    return simpleLogistic(0.75, 1.25).apply(distance.inNauticalMiles());
  }

  @Override
  public Distance offTrackDistance(ConformablePoint point) {
    Fix exitFix = Optional.ofNullable(scorerLeg().current().pathTerminator()).orElseThrow(supplier("Path Terminator"));
    Fix centerFix = scorerLeg().current().centerFix().orElseThrow(supplier("Arc Center Fix"));

    TurnDirection turnDirection = scorerLeg().current().turnDirection().orElseThrow(supplier("Turn Direction"));

    double inboundTangentialTrack = scorerLeg().current().inboundMagneticCourse()
        .orElse(scorerLeg().previous(Leg::outboundMagneticCourse).orElseThrow(supplier("Inbound Tangential Course")));

    double outboundTangentialTrack = scorerLeg().current().outboundMagneticCourse()
        .orElse(scorerLeg().next(Leg::inboundMagneticCourse).orElseThrow(supplier("Outbound Tangential Course")));

    // TODO - figure out if this should preferentially be the magvar of the recommended navaid - since its actually published (in theory)
    MagneticVariation magneticVariation = scorerLeg().current().pathTerminator().magneticVariation();

    double pointTrueCourse = point.trueCourse().orElseThrow(supplier("Point Course"));
    double pointMagCourse = magneticVariation.trueToMagnetic(pointTrueCourse);



    return Distance.ofNauticalMiles(0.0);
  }
}