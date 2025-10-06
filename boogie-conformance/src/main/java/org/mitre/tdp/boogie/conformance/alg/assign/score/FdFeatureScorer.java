package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static com.google.common.base.Preconditions.checkArgument;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;


public final class FdFeatureScorer implements ViterbiFeatureVectorScorer {

  private final Function<Double, Double> offCourseWeight;
  private final Function<Double, Double> nearestDmeWight;
  private final Function<Double, Double> beforeFixWeight;
  private final Function<Double, Double> distanceOffCenterWeight;

  public FdFeatureScorer() {
    this(
        simpleLogistic(5, 15),
        simpleLogistic(.5, 2),
        (d) -> d < 0.0 ? 0.0 : 1.0,
        simpleLogistic(.5, 2)
    );
  }


  public FdFeatureScorer(Function<Double, Double> offCourseWeight, Function<Double, Double> nearestDmeWight, Function<Double, Double> beforeFixWeight, Function<Double, Double> distanceOffCenterWeight) {
    this.offCourseWeight = offCourseWeight;
    this.nearestDmeWight = nearestDmeWight;
    this.beforeFixWeight = beforeFixWeight;
    this.distanceOffCenterWeight = distanceOffCenterWeight;
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(FdFeatureExtractor.LEG_TYPE));

    double degreesOffCourse = viterbiFeatureVector.featureValue(FdFeatureExtractor.DEGREES_OFF_COURSE);
    double currentBestToDme = viterbiFeatureVector.featureValue(FdFeatureExtractor.NEAREST_DME);
    double distanceBefore = viterbiFeatureVector.featureValue(FdFeatureExtractor.DISTANCE_BEFORE_FIX);
    double distanceOffCenter = viterbiFeatureVector.featureValue(FdFeatureExtractor.DISTANCE_OFF_CENTER);

    return offCourseWeight.apply(degreesOffCourse)
        * nearestDmeWight.apply(currentBestToDme)
        * beforeFixWeight.apply(distanceBefore)
        * distanceOffCenterWeight.apply(distanceOffCenter);
  }
}
