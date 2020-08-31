package org.mitre.tdp.boogie.conformance.alg.evaluate;

import java.util.Optional;

import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.conformance.alg.assemble.LegPair;

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
}
