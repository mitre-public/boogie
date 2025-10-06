package org.mitre.tdp.boogie.conformance.alg.assign.score;


import java.util.function.Supplier;

import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;


public final class VdFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {
  public static final VdFeatureExtractor INSTANCE = new VdFeatureExtractor();
  /**
   * Feature who's presence indicates the ViterbiFeatureVector is associated with an VD leg.
   */
  public static final String LEG_TYPE = "IsVd";
  /**
   * Feature representing the course delta between the VD declared heading and the actual heading of the aircraft.
   */
  public static final String DEGREES_OFF_COURSE = "DegreesOffCourse";
  /**
   * Feature representing the distance to or from the navaid's DME.
   */
  public static final String CURRENT_BEST_DME = "CurrentBestDme";

  private VdFeatureExtractor() {}

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(LEG_TYPE, (c, l) -> 1.)
        .addFeatureExtractor(DEGREES_OFF_COURSE, this::deriveDegreesOffCourseFeature)
        .addFeatureExtractor(CURRENT_BEST_DME, this::nmToDME)
        .build();
  }

  private double deriveDegreesOffCourseFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return LegDistance.deriveDegreesOffCourse(conformablePoint, flyableLeg);
  }

  private double nmToDME(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return LegDistance.deriveNearestToDmeTermination(conformablePoint, flyableLeg);
  }
}

