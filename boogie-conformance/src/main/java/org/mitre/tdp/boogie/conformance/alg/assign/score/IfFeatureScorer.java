package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

public final class IfFeatureScorer implements ViterbiFeatureVectorScorer {

  private final UnaryOperator<Double> offTrackWeight;

  public IfFeatureScorer() {
    this(simpleLogistic(0.25, 0.5));
  }

  public IfFeatureScorer(UnaryOperator<Double> offTrackWeight) {
    this.offTrackWeight = requireNonNull(offTrackWeight);
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(IfFeatureExtractor.LEG_TYPE));
    double distanceFromFix = viterbiFeatureVector.featureValue(IfFeatureExtractor.DISTANCE_FROM_FIX);
    return offTrackWeight.apply(distanceFromFix);
  }
}
