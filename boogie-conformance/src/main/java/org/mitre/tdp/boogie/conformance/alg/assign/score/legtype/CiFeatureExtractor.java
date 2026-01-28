package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException.supplier;

import java.util.function.Supplier;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;


public final class CiFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {

  /**
   * Singleton instance of an extractor for shared use.
   */
  public static final CiFeatureExtractor INSTANCE = new CiFeatureExtractor();

  /**
   * Feature whose presence indicates the ViterbiFeatureVector is associated with an CI leg.
   */
  public static final String LEG_TYPE = "IsCi";
  /**
   * Feature representing the course delta between the CI declared heading and the actual heading of the aircraft.
   */
  public static final String DEGREES_OFF_COURSE = "DegreesOffCourse";
  /**
   * Feature representing the distance to the fix following the CI leg.
   */
  public static final String DISTANCE_TO_NEXT_FIX = "DistanceToNextFix";

  private CiFeatureExtractor() {
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(LEG_TYPE, (c, l) -> 1.)
        .addFeatureExtractor(DEGREES_OFF_COURSE, this::deriveDegreesOffCourseFeature)
        .addFeatureExtractor(DISTANCE_TO_NEXT_FIX, this::deriveDistanceToNextFixFeature)
        .build();
  }

  private double deriveDegreesOffCourseFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return LegDistance.deriveDegreesOffCourse(conformablePoint, flyableLeg);
  }

  private double deriveDistanceToNextFixFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return flyableLeg.next().orElseThrow(IllegalStateException::new).associatedFix().map(conformablePoint::distanceInNmTo).orElseThrow(supplier("Next Path Terminator"));
  }
}
