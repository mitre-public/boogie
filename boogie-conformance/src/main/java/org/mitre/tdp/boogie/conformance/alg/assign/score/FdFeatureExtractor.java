package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.function.Supplier;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;


public final class FdFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {

  /**
   * Singleton instance of an extractor for shared use.
   */
  public static final FdFeatureExtractor INSTANCE = new FdFeatureExtractor();

  /**
   * Feature who's presence indicates the ViterbiFeatureVector is associated with an FD leg.
   */
  public static final String LEG_TYPE = "IsFd";
  /**
   * Feature representing the course delta between the FD declared course and the actual heading of the aircraft.
   */
  public static final String DEGREES_OFF_COURSE = "DegreesOffCourse";
  /**
   * The feature representing how far you are after the terminating distance in the leg.
   */
  public static final String NEAREST_DME = "NearestDme";

  /**
   * Feature representing how far before the FD's fix this is. Since the leg includes a true course checking this works.
   */
  public static final String DISTANCE_BEFORE_FIX = "DistanceBeforeFix";

  /**
   * Feature representing how far off center the airplane is from the center of the route.
   */
  public static final String DISTANCE_OFF_CENTER = "DistanceOffCenter";


  private FdFeatureExtractor() {
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(LEG_TYPE, (c, l) -> 1.)
        .addFeatureExtractor(DEGREES_OFF_COURSE, this::deriveDegreesOffCourseFeature)
        .addFeatureExtractor(NEAREST_DME, this::nearestDme)
        .addFeatureExtractor(DISTANCE_BEFORE_FIX, this::deriveBeforeTrackDistance)
        .addFeatureExtractor(DISTANCE_OFF_CENTER, this::deriveDistanceOffCenter)
        .build();
  }

  private double deriveDegreesOffCourseFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return LegDistance.deriveDegreesOffCourse(conformablePoint, flyableLeg);
  }

  private double nearestDme(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return LegDistance.deriveNearestToDmeTermination(conformablePoint, flyableLeg);
  }

  /**
   * This calculates the along track distance for the point to the fd's start to a 25 mile projection.
   *
   * @param conformablePoint to test
   * @param flyableLeg       our fd leg
   * @return the distance before fix. Negative for before or if its after the fix 0;
   */
  private double deriveBeforeTrackDistance(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return LegDistance.deriveDistanceBeforeLeg(conformablePoint, flyableLeg);

  }

  private double deriveDistanceOffCenter(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return LegDistance.fixOriginatingCrossTrackDistance(conformablePoint, flyableLeg);
  }
}
