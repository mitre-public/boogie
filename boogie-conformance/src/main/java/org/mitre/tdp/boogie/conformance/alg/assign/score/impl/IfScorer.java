package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * The custom scorer to use when scoring IF leg types. These legs are essentially just start points for routes and as such
 * people don't actually fly IF legs - in reality the fly downstream legs which are defined by including the IF leg (think
 * something like a TF, without a previous fix the leg is ill-defined).
 *
 * Since we don't want to simple punt and give them a low score we give them fix-crossing scores and score them based on a
 * tight distance crossing window around the fix itself.
 */
public class IfScorer implements OnLegScorer {

  /**
   * The weighting function to use to score the distance to the IF leg path terminator in nm.
   */
  private final Function<Double, Double> distanceWeight;

  public IfScorer() {
    this.distanceWeight = simpleLogistic(0.25, 0.5);
  }

  public IfScorer(Function<Double, Double> distanceWeight) {
    this.distanceWeight = distanceWeight;
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint point, FlyableLeg legTriple) {
    double distanceBetween = point.distanceInNmTo(legTriple.current().pathTerminator());
    return distanceWeight.apply(distanceBetween);
  }
}
