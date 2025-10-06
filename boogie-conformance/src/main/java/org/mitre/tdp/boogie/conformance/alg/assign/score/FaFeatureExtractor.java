package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException.supplier;

import java.util.Optional;
import java.util.function.Supplier;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;

import com.google.common.collect.Range;

public final class FaFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {

  /**
   * Singleton instance of the extractor for shared use.
   */
  public static final FaFeatureExtractor INSTANCE = new FaFeatureExtractor();

  /**
   * Feature who's presence indicates the {@link org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector} is associated with an FA leg.
   */
  public static final String LEG_TYPE = "IsFa";
  /**
   * Feature indicating the feet past the target altitude the flight is:
   * 1) For "AT OR BELOW" - positive indicates the flight is that far above the target altitude
   * 2) For "AT OR ABOVE" - positive indicates the flight is that far below the target altitude
   */
  public static final String FEET_PAST_TARGET_ALTITUDE = "FeetPastTargetAltitude";
  /**
   * Feature indicating the delta (in degrees) between the leg's declared course and the flights actual course.
   */
  public static final String DEGREES_OFF_COURSE = "DegreesOffCourse";

  /**
   * Feature representing how far before the FA's fix this is. Since the leg includes a true course checking this works.
   */
  public static final String DISTANCE_BEFORE_FIX = "DistanceBeforeFix";

  /**
   * Feature representing how far off center the airplane is from the center of the route.
   */
  public static final String DISTANCE_OFF_CENTER = "DistanceOffCenter";

  private FaFeatureExtractor() {
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(LEG_TYPE, (c, f) -> 1.)
        .addFeatureExtractor(FEET_PAST_TARGET_ALTITUDE, this::deriveFeetPastTargetAltitudeFeature)
        .addFeatureExtractor(DEGREES_OFF_COURSE, this::deriveDegreesOffCourseFeature)
        .addFeatureExtractor(DISTANCE_BEFORE_FIX, this::deriveBeforeTrackDistanceNM)
        .addFeatureExtractor(DISTANCE_OFF_CENTER, this::deriveDistanceOffCenter)
        .build();
  }

  /**
   * Takes the provided point as well as the {@link FlyableLeg} and computes the distance past the target altitude based on
   * the listed target altitude and type in the leg.
   */
  double deriveFeetPastTargetAltitudeFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    double targetAltitude = Optional.of(flyableLeg.current().altitudeConstraint())
        .filter(Range::hasLowerBound)
        .map(Range::lowerEndpoint)
        .orElseThrow(supplier("Target Altitude"));
    double pressureAltitude = conformablePoint.pressureAltitude()
        .orElseThrow(supplier("Pressure Altitude"));
    return pressureAltitude - targetAltitude;
  }

  /**
   * Returns the degrees off course the point is from the course specified in the CA legs outbound magnetic course.
   */
  double deriveDegreesOffCourseFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return LegDistance.deriveDegreesOffCourse(conformablePoint, flyableLeg);
  }

  /**
      * This calculates the along track distance for the point to the fm's start to a 25 mile projection.
      * @param conformablePoint to test
   * @param flyableLeg our fm leg
   * @return the distance before fix. Negative for before or if its after the fix 0;
   */
  private double deriveBeforeTrackDistanceNM(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return LegDistance.deriveDistanceBeforeLeg(conformablePoint, flyableLeg);
  }

  private double deriveDistanceOffCenter(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return LegDistance.fixOriginatingCrossTrackDistance(conformablePoint, flyableLeg);
  }
}
