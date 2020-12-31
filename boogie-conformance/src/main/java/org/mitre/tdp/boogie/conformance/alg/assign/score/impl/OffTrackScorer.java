package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static com.google.common.base.Preconditions.checkArgument;

import org.mitre.caasd.commons.Distance;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * Interface for scoring metrics which determine the score based on the distance the aircraft is off of a
 * pre-defined ground path.
 */
public interface OffTrackScorer extends OnLegScorer {

  /**
   * Returns a weight function which will transform the distance to a score in the range [0,1].
   */
  double weightFn(Distance offTrackDistance);

  /**
   * Returns a {@link Distance} object representing the distance the conformable point is off of the over-ground
   * path represented by the given {@link FlyableLeg}
   */
  Distance offTrackDistance(ConformablePoint point, FlyableLeg legTriple);

  @Override
  default double scoreAgainstLeg(ConformablePoint point, FlyableLeg legTriple) {
    double score = weightFn(offTrackDistance(point, legTriple));
    checkArgument(0.0 <= score && score <= 1.0, "Score must be in range [0, 1]");
    return score;
  }
}
