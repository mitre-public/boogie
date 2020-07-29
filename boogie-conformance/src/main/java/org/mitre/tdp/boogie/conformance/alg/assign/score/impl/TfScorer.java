package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.MissingRequiredFieldException.supplier;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import java.util.Optional;

import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assemble.FlyableLeg;

/**
 * This is the default conformance evaluator for {@link PathTerm#TF} legs.
 */
public class TfScorer implements OffTrackScorer {

  @Override
  public double weightFn(Distance distance) {
    return simpleLogistic(5.0, 10.0).apply(distance.inNauticalMiles());
  }

  @Override
  public Optional<Double> score(ConformablePoint point, FlyableLeg legTriple) {
    return legTriple.previous().isPresent()
        && legTriple.previous().map(Leg::pathTerminator).isPresent()
        && legTriple.previous().map(Leg::pathTerminator).map(Fix::latLong).isPresent()
        ? OffTrackScorer.super.score(point, legTriple) : Optional.empty();
  }

  @Override
  public Distance offTrackDistance(ConformablePoint point, FlyableLeg legTriple) {
    Fix startFix = legTriple.previous().map(Leg::pathTerminator)
        .orElseThrow(supplier("pathTerminator of from leg"));

    Fix endFix = Optional.ofNullable(legTriple.current().pathTerminator())
        .orElseThrow(supplier("pathTerminator of to leg"));

    return Distance.ofNauticalMiles(abs(endpointModifiedCrossTrackDistance(startFix, endFix, point)));
  }

  /**
   * Returns the endpoint-modified cross track distance between the segment defined by the given start and end fix and the
   * provided point position. This value is the normal cross-track distance when the point falls between the start and end
   * points laterally - otherwise it is the distance from the nearest endpoint rather than the CTD of the extended segment.
   *
   * Note - this maintains the appropriate sign post the end of the segment. If off to the left the endpoint modified dist
   * will still be negative as expected.
   */
  public static double endpointModifiedCrossTrackDistance(HasPosition startFix, HasPosition endFix, HasPosition point) {
    double ctd = Spherical.crossTrackDistanceNM(startFix, endFix, point);
    double atd = Spherical.alongTrackDistanceNM(startFix, endFix, point, ctd);

    double pathLength = startFix.distanceInNmTo(endFix);

    boolean alongSegment = atd > 0.0 && atd < pathLength;
    double minEndpointDistance = Math.min(point.distanceInNmTo(startFix), point.distanceInNmTo(endFix));

    return alongSegment ? ctd : (ctd < 0 ? -1.0 * minEndpointDistance : minEndpointDistance);
  }
}
