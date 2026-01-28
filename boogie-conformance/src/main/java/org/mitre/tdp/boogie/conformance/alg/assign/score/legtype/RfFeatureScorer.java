package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

public final class RfFeatureScorer implements ViterbiFeatureVectorScorer {

  private final UnaryOperator<Double> offTrackWeight;

  public RfFeatureScorer() {
    this(simpleLogistic(0.25, 0.5));
  }

  public RfFeatureScorer(UnaryOperator<Double> offTrackWeight) {
    this.offTrackWeight = requireNonNull(offTrackWeight);
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(RfFeatureExtractor.LEG_TYPE));

    double offTrackDistance = viterbiFeatureVector.featureValue(RfFeatureExtractor.OFF_TRACK_DISTANCE);
    return offTrackWeight.apply(offTrackDistance);
  }
}
