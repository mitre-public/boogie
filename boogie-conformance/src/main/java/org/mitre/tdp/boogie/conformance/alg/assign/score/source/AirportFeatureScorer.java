package org.mitre.tdp.boogie.conformance.alg.assign.score.source;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

import com.google.common.annotations.Beta;

@Beta
public final class AirportFeatureScorer implements ViterbiFeatureVectorScorer {
  private final UnaryOperator<Double> distanceFromAirportWeight;

  public  AirportFeatureScorer() {
    this(simpleLogistic(1, 5));
  }

  public AirportFeatureScorer(UnaryOperator<Double> distanceFromAirportWeight) {
    this.distanceFromAirportWeight = distanceFromAirportWeight;
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(AirportFeatureExtractor.CLASS_TYPE));
    double distance = viterbiFeatureVector.featureValue(AirportFeatureExtractor.DISTANCE_FROM);
    return distanceFromAirportWeight.apply(distance);
  }
}
