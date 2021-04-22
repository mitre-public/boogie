package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static com.google.common.base.Preconditions.checkArgument;
import static java.util.Objects.requireNonNull;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.WeightFunctions.simpleLogistic;

import java.util.function.Function;

import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorScorer;

public final class CaFeatureScorer implements ViterbiFeatureVectorScorer {
  /**
   * Returns a weighted score based on the abs value of the delta between the target course specified in the leg and the course
   * of the flight.
   *
   * VA/CA legs have no internal magvar which can be used to compare the point true course to the specified magnetic heading in
   * a direct fashion - so we recommend adding some fudge factor here as variations can be up to 15 degrees in some areas.
   */
  private final Function<Double, Double> offCourseWeight;
  /**
   * Returns a weighted score based on the signed distance to the target altitude in feet. The input to this method is always
   * taken to be the point altitude - the target altitude from the leg.
   */
  private final Function<Double, Double> pastTargetAltitudeWeight;

  public CaFeatureScorer() {
    this(
        simpleLogistic(5., 8.),
        simpleLogistic(100., 250.)
    );
  }

  public CaFeatureScorer(
      Function<Double, Double> offCourseWeight,
      Function<Double, Double> pastTargetAltitudeWeight
  ) {
    this.offCourseWeight = requireNonNull(offCourseWeight);
    this.pastTargetAltitudeWeight = requireNonNull(pastTargetAltitudeWeight);
  }

  @Override
  public Double apply(ViterbiFeatureVector viterbiFeatureVector) {
    checkArgument(viterbiFeatureVector.containsFeature(CaFeatureExtractor.LEG_TYPE));

    double degreesOffCourse = viterbiFeatureVector.featureValue(CaFeatureExtractor.DEGREES_OFF_COURSE);
    double feetPastTargetAltitude = viterbiFeatureVector.featureValue(CaFeatureExtractor.FEET_PAST_TARGET_ALTITUDE);

    return offCourseWeight.apply(degreesOffCourse) * pastTargetAltitudeWeight.apply(feetPastTargetAltitude);
  }
}
