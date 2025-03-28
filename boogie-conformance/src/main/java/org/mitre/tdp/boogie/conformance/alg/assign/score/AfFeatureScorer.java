package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

public final class AfFeatureScorer implements ViterbiFeatureVectorScorer {

  private final UnaryOperator<Double> offTrackWeight;

  public AfFeatureScorer() {
    this(simpleLogistic(0.5, 1.0));
  }

  public AfFeatureScorer(UnaryOperator<Double> offTrackWeight) {
    this.offTrackWeight = requireNonNull(offTrackWeight);
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(AfFeatureExtractor.LEG_TYPE));

    double offTrackDistance = viterbiFeatureVector.featureValue(AfFeatureExtractor.OFF_TRACK_DISTANCE);
    double radialAngle = viterbiFeatureVector.featureValue(AfFeatureExtractor.IN_RADIAL_ANGLE);

    return offTrackWeight.apply(offTrackDistance) * radialAngle;
  }
}
