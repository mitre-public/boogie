package org.mitre.tdp.boogie.conformance.alg.evaluate;

import java.util.Optional;

import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.conformance.alg.assemble.LegPair;
import org.mitre.tdp.boogie.conformance.alg.assign.score.RadialAngles;

/**
 * The max off track distance evaluator considers an aircraft to be non-conforming when it is outside
 * some buffer around the active leg its on via the {@link ConformanceEvaluator}.
 */
public interface MaxOffTrackDistanceEvaluator extends ConformanceEvaluator {

  /**
   * Returns the max off track distance the flight can be prior to being considered non-conforming.
   */
  default Distance maxOffTrackDistance() {
    return Distance.ofNauticalMiles(2.0);
  }

  @Override
  default Optional<Boolean> conforming(ConformablePoint point, LegPair legPair) {
    return offTrackDistance(point, legPair).map(maxOffTrackDistance()::isGreaterThan);
  }

  /**
   * Since all current notions of conformance are tied to the idea of off-track distance we provide the standard computation
   * as a top-level method for all evaluators.
   */
  static Optional<Distance> offTrackDistance(ConformablePoint point, LegPair legPair) {
    Fix previousTerminator = legPair.previous().pathTerminator();
    Fix currentTerminator = legPair.current().pathTerminator();

    if (legPair.current().type().isArc()) {
      return arcLegOffTrackDistance(point, legPair);
    }

    boolean isConcretePair = legPair.previous().type().isConcrete() && legPair.current().type().isConcrete();
    boolean hasDefinedEndpoints = previousTerminator.latLong() != null && currentTerminator.latLong() != null;

    if (isConcretePair && hasDefinedEndpoints) {
      double crossTrackDistance = Spherical.crossTrackDistanceNM(previousTerminator, currentTerminator, point);
      double alongTrackDistance = Spherical.alongTrackDistanceNM(previousTerminator, currentTerminator, point, crossTrackDistance);

      double legLength = previousTerminator.distanceInNmTo(currentTerminator);
      double modifiedOffTrackDistance = alongTrackDistance < 0.0 ? previousTerminator.distanceInNmTo(point) : alongTrackDistance > legLength ? currentTerminator.distanceInNmTo(point) : crossTrackDistance;

      return Optional.of(Distance.ofNauticalMiles(modifiedOffTrackDistance));
    }

    return Optional.empty();
  }

  /**
   * @return Distance from the supplied {@code point} to an arc leg.  If the
   *         point is before or after the angles included in the arc, returns
   *         the distance to the nearest arc endpoint.
   */
  static Optional<Distance> arcLegOffTrackDistance(ConformablePoint point, LegPair legPair) {
    Fix previousTerminator = legPair.previous().pathTerminator();
    Fix currentTerminator = legPair.current().pathTerminator();

    Fix centerFix = legPair.current().centerFix().orElseThrow(() -> new IllegalStateException("Center Fix required to compute off-track distance on arc"));
    TurnDirection turnDirection = legPair.current().turnDirection().orElseThrow(() -> new IllegalStateException("Turn direction required to compute off-track distance on arc"));
    double previousFixBearing = centerFix.courseInDegrees(previousTerminator);
    double currentFixBearing = centerFix.courseInDegrees(currentTerminator);
    double pointBearing = centerFix.courseInDegrees(point);

    if (RadialAngles.of(previousFixBearing, currentFixBearing, turnDirection).contains(pointBearing)){
      double radius = centerFix.distanceInNmTo(currentTerminator);
      return Optional.of(Distance.ofNauticalMiles(Math.abs(radius - centerFix.distanceInNmTo(point))));
    } else {
      double previousFixDistance = previousTerminator.distanceInNmTo(point);
      double currentFixDistance = currentTerminator.distanceInNmTo(point);
      return Optional.of(Distance.ofNauticalMiles(Math.min(previousFixDistance, currentFixDistance)));
    }
  }
}
