package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;


public final class FcFeatureScorer implements ViterbiFeatureVectorScorer {

  private final Function<Double, Double> offCourseWeight;
  private final Function<Double, Double> distanceBeyond;
  private final Function<Double, Double> beforeFixWeight;
  private final Function<Double, Double> distanceOffCenterWeight;

  public FcFeatureScorer() {
    this(
        simpleLogistic(5, 15),
        (d) -> d > 0.0 ? 0.0 : 1.0,
        (d) -> d < 0.0 ? 0.0 : 1.0,
        simpleLogistic(.5, 2)
    );
  }

  public FcFeatureScorer(Function<Double, Double> offCourseWeight, Function<Double, Double> distanceBeyond, Function<Double, Double> beforeFixWeight, Function<Double, Double> distanceOffCenterWeight) {
    this.offCourseWeight = offCourseWeight;
    this.distanceBeyond = distanceBeyond;
    this.beforeFixWeight = beforeFixWeight;
    this.distanceOffCenterWeight = distanceOffCenterWeight;
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(FcFeatureExtractor.LEG_TYPE));

    double degreesOffCourse = viterbiFeatureVector.featureValue(FcFeatureExtractor.DEGREES_OFF_COURSE);
    double distanceBeyondTerm = viterbiFeatureVector.featureValue(FcFeatureExtractor.DISTANCE_BEYOND_TERM_DIST);
    double distanceBefore = viterbiFeatureVector.featureValue(FcFeatureExtractor.DISTANCE_BEFORE_FIX);
    double distanceOffCenter = viterbiFeatureVector.featureValue(FcFeatureExtractor.DISTANCE_OFF_CENTER);

    return offCourseWeight.apply(degreesOffCourse)
        * distanceBeyond.apply(distanceBeyondTerm)
        * beforeFixWeight.apply(distanceBefore)
        * distanceOffCenterWeight.apply(distanceOffCenter);
  }
}
