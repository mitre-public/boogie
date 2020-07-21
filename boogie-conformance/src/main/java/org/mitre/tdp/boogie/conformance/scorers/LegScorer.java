package org.mitre.tdp.boogie.conformance.scorers;

import java.util.Optional;

import org.mitre.caasd.commons.Distance;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.Scorer;
import org.mitre.tdp.boogie.conformance.alg.assemble.ConsecutiveLegs;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

/**
 * Top level class for leg scoring, providing access to both the previous and the subsequent legs as
 * declared in the procedure/airway definition.
 *
 * <p>The leg scorer provides two primary functions:
 * 1) It generates a score representing how well the aircraft is flying the current leg.
 * 2) It generates a score representing the likelihood of a transition to another leg.
 */
public interface LegScorer extends Scorer<ConformablePoint, ConsecutiveLegs> {

  Logger LOG = LoggerFactory.getLogger(LegScorer.class);

  ConsecutiveLegs scorerLeg();

  /**
   * Returns the score in the range [0,1] of the point to the decorated {@link #scorerLeg()}.
   */
  double scoreAgainstLeg(ConformablePoint point);

  @Override
  default Optional<Double> score(ConformablePoint point) {
    if (scorerLeg().current().type().hasRequiredFields(scorerLeg().current())) {
      return Optional.of(scoreAgainstLeg(point));
    }
//    LOG.info("Missing required fields - skipping score for leg {}", scorerLeg().current());
    return Optional.empty();
  }

  @Override
  default double transitionScore(ConsecutiveLegs consecutiveLegs) {
    return sharedLegs(consecutiveLegs) ? 1.0 : endpointDistanceScore(consecutiveLegs);
  }

  /**
   * Returns true when the {@link ConsecutiveLegs} represented by this scorer overlap with those provided.
   */
  default boolean sharedLegs(ConsecutiveLegs consecutiveLegs) {
    Optional<Leg> myCurrent = Optional.of(scorerLeg().current());
    Optional<Leg> theirCurrent = Optional.of(consecutiveLegs.current());

    return scorerLeg().next().equals(theirCurrent)
        || scorerLeg().next().equals(consecutiveLegs.next())
        || myCurrent.equals(consecutiveLegs.next())
        || scorerLeg().current().equals(consecutiveLegs.current())
        || myCurrent.equals(consecutiveLegs.previous())
        || scorerLeg().previous().equals(theirCurrent)
        || scorerLeg().previous().equals(consecutiveLegs.previous());
  }

  /**
   * The max distance to consider for transitions between legs.
   */
  default Distance maxDistance() {
    return Distance.ofNauticalMiles(100.0);
  }

  /**
   * Returns the endpoint distance adjusted score between the wrapped legs and the provided legs.
   */
  default double endpointDistanceScore(ConsecutiveLegs consecutiveLegs) {
    Optional<Fix> thisOptFix = Optional.ofNullable(scorerLeg().current().pathTerminator());
    Optional<Fix> thatOptFix = Optional.ofNullable(consecutiveLegs.current().pathTerminator());

    if (thisOptFix.isPresent() && thatOptFix.isPresent()) {
      Fix thisFix = thisOptFix.get();
      Fix thatFix = thatOptFix.get();

      double distanceNm = thisFix.distanceInNmTo(thatFix);
      double maxDistanceNm = maxDistance().inNauticalMiles();
      return distanceNm > maxDistanceNm ? 0.0 : 1.0 - (distanceNm / maxDistanceNm);
    } else {
      return 0.0;
    }
  }
}
