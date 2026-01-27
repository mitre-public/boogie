package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException.supplier;

import java.util.function.Supplier;

import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;

public final class VmFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {

  public static final VmFeatureExtractor INSTANCE = new VmFeatureExtractor();

  /**
   * Feature who's presence indicates the {@link ViterbiFeatureVector} is associated with an VM leg.
   */
  public static final String LEG_TYPE = "IsVm";
  /**
   * Feature representing the course delta between the VI declared heading and the actual heading of the aircraft.
   */
  public static final String DEGREES_OFF_COURSE = "DegreesOffCourse";
  /**
   * Feature representing the distance to the fix following the VI leg.
   */
  public static final String DISTANCE_TO_NEXT_FIX = "DistanceToNextFix";

  private VmFeatureExtractor() {
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(LEG_TYPE, (c, l) -> 1.)
        .addFeatureExtractor(DEGREES_OFF_COURSE, this::deriveDegreesOffCourseFeature)
        .addFeatureExtractor(DISTANCE_TO_NEXT_FIX, this::deriveDistanceToNextFixFeature)
        .build();
  }

  double deriveDegreesOffCourseFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    double pointCourse = conformablePoint.trueCourse().orElseThrow(supplier("True Course"));
    double expectedCourse = flyableLeg.current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));

    MagneticVariation localVariation = MagneticVariationResolver.getInstance().magneticVariation(conformablePoint, flyableLeg);
    return abs(Spherical.angleDifference(expectedCourse, localVariation.trueToMagnetic(pointCourse)));
  }

  double deriveDistanceToNextFixFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return conformablePoint.distanceInNmTo(flyableLeg.next().flatMap(Leg::associatedFix).orElseThrow(supplier("Next Path Terminator")));
  }
}
