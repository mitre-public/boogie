package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

public final class FmFeatureScorer implements ViterbiFeatureVectorScorer {

  private final UnaryOperator<Double> offCourseWeight;
  private final UnaryOperator<Double> distanceFromFixWeight;
  private final UnaryOperator<Double> beforeFixWeight;

  public FmFeatureScorer() {
    this(
        simpleLogistic(15., 30.),
        simpleLogistic(15., 25.),
        (d) -> d < 0.0 ? 0.0 : 1.0
    );
  }

  public FmFeatureScorer(UnaryOperator<Double> offCourseWeight, UnaryOperator<Double> distanceFromFixWeight, UnaryOperator<Double> beforeFixWeight) {
    this.offCourseWeight = offCourseWeight;
    this.distanceFromFixWeight = distanceFromFixWeight;
    this.beforeFixWeight = beforeFixWeight;
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(FmFeatureExtractor.LEG_TYPE));

    double degreesOffCourse = viterbiFeatureVector.featureValue(FmFeatureExtractor.DEGREES_OFF_COURSE);
    double distanceToNextFix = viterbiFeatureVector.featureValue(FmFeatureExtractor.DISTANCE_FROM_FIX);
    double distanceBefore = viterbiFeatureVector.featureValue(FmFeatureExtractor.DISTANCE_BEFORE_FIX);

    return offCourseWeight.apply(degreesOffCourse) * distanceFromFixWeight.apply(distanceToNextFix) * beforeFixWeight.apply(distanceBefore);
  }
}
