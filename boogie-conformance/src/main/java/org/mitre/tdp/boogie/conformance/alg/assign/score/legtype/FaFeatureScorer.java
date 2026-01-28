package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

public final class FaFeatureScorer implements ViterbiFeatureVectorScorer {
  /**
   * Returns a weighted score based on the abs value of the delta between the target course specified in the leg and the course
   * of the flight.
   */
  private final Function<Double, Double> offCourseWeight;
  /**
   * Returns a weighted score based on the signed distance to the target altitude in feet. The input to this method is always
   * taken to be the point altitude - the target altitude from the leg.
   */
  private final Function<Double, Double> pastTargetAltitudeWeight;
  private final Function<Double, Double> beforeFixWeight;
  private final Function<Double, Double> offCenterWeight;

  public FaFeatureScorer() {
    this(
        simpleLogistic(5., 8.),
        simpleLogistic(100., 250.),
        (d) -> d < 0.0 ? 0.0 : 1.0,
        simpleLogistic(.5, 2)
    );
  }


  public FaFeatureScorer(
      Function<Double, Double> offCourseWeight,
      Function<Double, Double> pastTargetAltitudeWeight,
      Function<Double, Double> beforeFixWeight,
      Function<Double, Double> offCenterWeight
  ) {
    this.offCourseWeight = requireNonNull(offCourseWeight);
    this.pastTargetAltitudeWeight = requireNonNull(pastTargetAltitudeWeight);
    this.beforeFixWeight = requireNonNull(beforeFixWeight);
    this.offCenterWeight = offCenterWeight;
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(FaFeatureExtractor.LEG_TYPE));

    double degreesOffCourse = viterbiFeatureVector.featureValue(FaFeatureExtractor.DEGREES_OFF_COURSE);
    double feetPastTargetAltitude = viterbiFeatureVector.featureValue(FaFeatureExtractor.FEET_PAST_TARGET_ALTITUDE);
    double beforeFix = viterbiFeatureVector.featureValue(FaFeatureExtractor.DISTANCE_BEFORE_FIX);
    double distanceOffCenter = viterbiFeatureVector.featureValue(FaFeatureExtractor.DISTANCE_OFF_CENTER);

    return offCourseWeight.apply(degreesOffCourse)
        * pastTargetAltitudeWeight.apply(feetPastTargetAltitude)
        * beforeFixWeight.apply(beforeFix)
        * offCenterWeight.apply(distanceOffCenter);
  }
}