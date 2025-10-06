package org.mitre.tdp.boogie.conformance.alg.assign.score;

import java.util.function.Supplier;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;

public final class VrFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {
  /**
   * Singleton instance of an extractor for shared use.
   */
  public static final VrFeatureExtractor INSTANCE = new VrFeatureExtractor();

  /**
   * Feature who's presence indicates the ViterbiFeatureVector is associated with an FD leg.
   */
  public static final String LEG_TYPE = "IsVr";
  /**
   * Feature representing the course delta between the FD declared course and the actual heading of the aircraft.
   */
  public static final String DEGREES_OFF_COURSE = "DegreesOffCourse";
  /**
   * The feature representing how far you are after the terminating distance in the leg.
   */
  public static final String NEAREST_YOU_GOT = "NearestYouGot";

  private VrFeatureExtractor() {
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(LEG_TYPE, (c, l) -> 1.)
        .addFeatureExtractor(DEGREES_OFF_COURSE, this::deriveDegreesOffCourseFeature)
        .addFeatureExtractor(NEAREST_YOU_GOT, this::nearestYouGot)
        .build();
  }

  private double deriveDegreesOffCourseFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return LegDistance.deriveDegreesOffCourse(conformablePoint, flyableLeg);
  }

  private double nearestYouGot(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return LegDistance.nearestToRadial(conformablePoint, flyableLeg);
  }
}
