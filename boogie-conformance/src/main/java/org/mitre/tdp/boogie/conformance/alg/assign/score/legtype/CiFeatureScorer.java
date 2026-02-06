package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;


public final class CiFeatureScorer implements ViterbiFeatureVectorScorer {

  private final Function<Double, Double> offCourseWeight;
  private final Function<Double, Double> nextFixDistanceWeight;

  public CiFeatureScorer() {
    this(
        simpleLogistic(5, 15),
        simpleLogistic(10., 20.)
    );
  }

  public CiFeatureScorer(
      Function<Double, Double> offCourseWeight,
      Function<Double, Double> nextFixDistanceWeight
  ) {
    this.offCourseWeight = requireNonNull(offCourseWeight);
    this.nextFixDistanceWeight = requireNonNull(nextFixDistanceWeight);
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(CiFeatureExtractor.LEG_TYPE));

    double degreesOffCourse = viterbiFeatureVector.featureValue(CiFeatureExtractor.DEGREES_OFF_COURSE);
    double distanceToNextFix = viterbiFeatureVector.featureValue(CiFeatureExtractor.DISTANCE_TO_NEXT_FIX);

    return offCourseWeight.apply(degreesOffCourse) * nextFixDistanceWeight.apply(distanceToNextFix);
  }
}
