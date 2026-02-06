package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

public final class DfFeatureScorer implements ViterbiFeatureVectorScorer {

  private final UnaryOperator<Double> offTrackWeight;

  public DfFeatureScorer() {
    this(simpleLogistic(0.5, 1.0));
  }

  public DfFeatureScorer(UnaryOperator<Double> offTrackWeight) {
    this.offTrackWeight = requireNonNull(offTrackWeight);
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(DfFeatureExtractor.LEG_TYPE));
    double distanceOffset = viterbiFeatureVector.featureValue(CfFeatureExtractor.PROJECTED_DISTANCE_OFFSET);
    return offTrackWeight.apply(distanceOffset);
  }
}
