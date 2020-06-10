package org.mitre.tdp.boogie.conformance.scorers.impl;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.conformance.scorers.impl.MissingRequiredFieldException.supplier;
import static org.mitre.tdp.boogie.conformance.scorers.impl.WeightFunctions.simpleLogistic;

import java.util.Optional;

import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;

/**
 * This is the default conformance evaluator for {@link PathTerm#TF} legs.
 */
class TfScorer implements OffTrackScorer {

  private final ConsecutiveLegs legs;

  TfScorer(ConsecutiveLegs legs) {
    this.legs = legs;
  }

  @Override
  public ConsecutiveLegs scorerLeg() {
    return legs;
  }

  @Override
  public double weightFn(Distance distance) {
    return simpleLogistic(1.0, 2.0).apply(distance.inNauticalMiles());
  }

  @Override
  public Distance offTrackDistance(ConformablePoint point) {
    Fix startFix = scorerLeg().previous().map(Leg::pathTerminator)
        .orElseThrow(supplier("pathTerminator of from leg"));

    Fix endFix = Optional.ofNullable(scorerLeg().current().pathTerminator())
        .orElseThrow(supplier("pathTerminator of to leg"));

    double ctd = Spherical.crossTrackDistanceNM(startFix, endFix, point);
    double offTrackDistanceNm = modifyWithEndpointDistances(startFix, endFix, point, ctd);
    return Distance.ofNauticalMiles(abs(offTrackDistanceNm));
  }

  /**
   * Checks whether the given point is either along the physical route segment (between the start
   * and end fixes) or whether its within the off-track range of the endpoints of the segments.
   */
  public double modifyWithEndpointDistances(Fix startFix, Fix endFix, ConformablePoint point, double ctd) {
    double atd = Spherical.alongTrackDistanceNM(startFix, endFix, point, ctd);
    double pathLength = startFix.distanceInNmTo(endFix);

    boolean alongSegment = atd > 0.0 && atd < pathLength;
    return alongSegment ? ctd : Math.min(point.distanceInNmTo(startFix), point.distanceInNmTo(endFix));
  }
}
