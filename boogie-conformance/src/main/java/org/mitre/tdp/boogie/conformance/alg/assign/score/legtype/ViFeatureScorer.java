package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

public final class ViFeatureScorer implements ViterbiFeatureVectorScorer {

  private final UnaryOperator<Double> offCourseWeight;
  private final UnaryOperator<Double> nextFixDistanceWeight;

  public ViFeatureScorer() {
    this(
        simpleLogistic(15., 30.),
        simpleLogistic(10., 20.)
    );
  }

  public ViFeatureScorer(
      UnaryOperator<Double> offCourseWeight,
      UnaryOperator<Double> nextFixDistanceWeight
  ) {
    this.offCourseWeight = requireNonNull(offCourseWeight);
    this.nextFixDistanceWeight = requireNonNull(nextFixDistanceWeight);
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(ViFeatureExtractor.LEG_TYPE));

    double degreesOffCourse = viterbiFeatureVector.featureValue(ViFeatureExtractor.DEGREES_OFF_COURSE);
    double distanceToNextFix = viterbiFeatureVector.featureValue(ViFeatureExtractor.DISTANCE_TO_NEXT_FIX);

    return offCourseWeight.apply(degreesOffCourse) * nextFixDistanceWeight.apply(distanceToNextFix);
  }
}
