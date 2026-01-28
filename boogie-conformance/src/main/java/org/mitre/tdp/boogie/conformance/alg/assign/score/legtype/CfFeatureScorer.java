package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

public final class CfFeatureScorer implements ViterbiFeatureVectorScorer {

  private final UnaryOperator<Double> offCourseWeight;
  private final UnaryOperator<Double> offTrackWeight;

  public CfFeatureScorer() {
    this(
        simpleLogistic(5.0, 10.0),
        simpleLogistic(0.5, 1.0)
    );
  }

  public CfFeatureScorer(
      UnaryOperator<Double> offCourseWeight,
      UnaryOperator<Double> offTrackWeight
  ) {
    this.offCourseWeight = requireNonNull(offCourseWeight);
    this.offTrackWeight = requireNonNull(offTrackWeight);
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(CfFeatureExtractor.LEG_TYPE));

    double courseOffset = viterbiFeatureVector.featureValue(CfFeatureExtractor.DEGREES_OFF_COURSE);
    double distanceOffset = viterbiFeatureVector.featureValue(CfFeatureExtractor.PROJECTED_DISTANCE_OFFSET);

    return offCourseWeight.apply(courseOffset) * offTrackWeight.apply(distanceOffset);
  }
}
