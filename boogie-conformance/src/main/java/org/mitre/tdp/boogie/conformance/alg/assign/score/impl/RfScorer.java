package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.conformance.alg.evaluate.LegPair;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;
import org.mitre.tdp.boogie.conformance.alg.evaluate.MaxOffTrackDistanceEvaluator;

/**
 * This is the default conformance scorer for {@link PathTerm#RF} legs.
 */
public class RfScorer implements OnLegScorer {

  private final Function<Double, Double> offTrackWeight;

  public RfScorer() {
    this.offTrackWeight = simpleLogistic(0.25, 0.5);
  }

  public RfScorer(Function<Double, Double> offTrackWeight) {
    this.offTrackWeight = offTrackWeight;
  }

  @Override
  public Optional<Double> score(ConformablePoint point, FlyableLeg legTriple) {
    return legTriple.previous()
        .flatMap(previous -> {
          LegPair asLegPair = new LegPair(previous, legTriple.current());
          return MaxOffTrackDistanceEvaluator.offTrackDistance(point, asLegPair);
        })
        .map(Distance::inNauticalMiles)
        .map(offTrackWeight);
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint point, FlyableLeg legTriple) {
    throw new UnsupportedOperationException();
  }

  double tangentToRadial(Double tangentialTrueBearing, TurnDirection turnDirection) {
    return Spherical.mod(tangentialTrueBearing + (turnDirection.isLeft() ? 90 : -90), 360.);
  }

  double radialToTangent(Double radialTrueBearing, TurnDirection turnDirection) {
    return Spherical.mod(radialTrueBearing - (turnDirection.isLeft() ? 90. : -90.), 360.);
  }
}