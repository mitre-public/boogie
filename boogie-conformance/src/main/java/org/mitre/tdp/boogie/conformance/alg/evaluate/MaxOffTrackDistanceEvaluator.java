package org.mitre.tdp.boogie.conformance.alg.evaluate;

import java.util.Optional;

import org.mitre.caasd.commons.Distance;
import org.mitre.tdp.boogie.conformance.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.LegAssigner;
import org.mitre.tdp.boogie.conformance.model.ConsecutiveLegs;

/**
 * The max off track distance evaluator considers an aircraft to be non-conforming when it is outside
 * some buffer around the active leg its on via the {@link LegAssigner}.
 */
public interface MaxOffTrackDistanceEvaluator extends ConformanceEvaluator {

  /**
   * Returns the max off track distance the flight can be prior to being considered non-conforming.
   */
  default Distance maxOffTrackDistance() {
    return Distance.ofNauticalMiles(2.0);
  }

  @Override
  default boolean conforming(ConformablePoint point, ConsecutiveLegs consecutiveLegs) {
    Optional<Distance> offTrackDistance = ConformanceEvaluator.offTrackDistance(point, consecutiveLegs);
    return !offTrackDistance.isPresent() || maxOffTrackDistance().isGreaterThan(offTrackDistance.get());
  }
}
