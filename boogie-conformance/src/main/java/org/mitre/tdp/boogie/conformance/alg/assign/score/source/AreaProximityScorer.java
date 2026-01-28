package org.mitre.tdp.boogie.conformance.alg.assign.score.source;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

import com.google.common.annotations.Beta;

@Beta
public class AreaProximityScorer implements ViterbiFeatureVectorScorer {
  private final UnaryOperator<Double> distanceRatioWeight;

  public AreaProximityScorer(){
    this(simpleLogistic(3, 6));
  }


  public AreaProximityScorer(UnaryOperator<Double> distanceRatioWeight) {
    this.distanceRatioWeight = distanceRatioWeight;
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(AreaProximityFeatureExtractor.CLASS_TYPE));
    double distance = viterbiFeatureVector.featureValue(AreaProximityFeatureExtractor.DISTANCE_RATIO);
    return distanceRatioWeight.apply(distance);
  }
}
