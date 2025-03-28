package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.BiFunction;
import java.util.function.UnaryOperator;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

/**
 * Manually configured version of a TF feature vector scoring function. This function is applied to {@link TfDelegator} legs
 * and is used to convert TF derived features into likelihoods for Viterbi to consume.
 */
public final class TfFeatureScorer implements ViterbiFeatureVectorScorer {

  private final UnaryOperator<Double> offTrackWeight;
  private final UnaryOperator<Double> offCourseWeight;
  private final BiFunction<Double, Double, Double> prePostPenalizer;

  public TfFeatureScorer() {
    this(
        simpleLogistic(.5, 1.),
        simpleLogistic(90., 175.),
        prePostPenalizer(2, 2)
    );
  }

  public TfFeatureScorer(
      UnaryOperator<Double> offTrackWeight,
      UnaryOperator<Double> offCourseWeight
  ) {
    this(offTrackWeight, offCourseWeight, prePostPenalizer(2, 2));
  }

  public TfFeatureScorer(
      UnaryOperator<Double> offTrackWeight,
      UnaryOperator<Double> offCourseWeight,
      BiFunction<Double, Double, Double> prePostPenalizer
  ) {
    this.offTrackWeight = requireNonNull(offTrackWeight);
    this.offCourseWeight = requireNonNull(offCourseWeight);
    this.prePostPenalizer = requireNonNull(prePostPenalizer);
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(TfFeatureExtractor.LEG_TYPE));

    double offTrackDistance = viterbiFeatureVector.featureValue(TfFeatureExtractor.OFF_TRACK_DISTANCE);
    double prePostDistance = viterbiFeatureVector.featureValue(TfFeatureExtractor.PRE_POST_DISTANCE);
    double courseDifference = viterbiFeatureVector.featureValue(TfFeatureExtractor.DEGREES_OFF_COURSE);

    return offCourseWeight.apply(courseDifference) * offTrackWeight.apply(abs(prePostPenalizer.apply(offTrackDistance, prePostDistance)));
  }

  public static BiFunction<Double, Double, Double> prePostPenalizer(int prePenalty, int postPenalty) {
    return (otd, ppd) -> (ppd > 0. ? postPenalty : ppd < 0 ? prePenalty : 1) * otd;
  }
}
