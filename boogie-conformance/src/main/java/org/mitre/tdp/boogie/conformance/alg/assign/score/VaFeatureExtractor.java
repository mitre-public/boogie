package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.caasd.commons.Spherical.angleDifference;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException.supplier;

import java.util.function.Supplier;

import org.mitre.tdp.boogie.AltitudeLimit;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;

public final class VaFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {

  public static final VaFeatureExtractor INSTANCE = new VaFeatureExtractor();
  /**
   * Feature who's presence indicates the {@link ViterbiFeatureVector} is associated with an CA leg.
   */
  public static final String LEG_TYPE = "IsVa";
  /**
   * Feature indicating the feet past the target altitude the flight is:
   *
   * 1) For "AT OR BELOW" - positive indicates the flight is that far above the target altitude
   * 2) For "AT OR ABOVE" - positive indicates the flight is that far below the target altitude
   */
  public static final String FEET_PAST_TARGET_ALTITUDE = "FeetPastTargetAltitude";
  /**
   * Feature indicating the delta (in degrees) between the leg's declared course and the flights actual course.
   */
  public static final String DEGREES_OFF_COURSE = "DegreesOffCourse";

  private VaFeatureExtractor() {
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(LEG_TYPE, (c, f) -> 1.)
        .addFeatureExtractor(FEET_PAST_TARGET_ALTITUDE, this::deriveFeetPastTargetAltitudeFeature)
        .addFeatureExtractor(DEGREES_OFF_COURSE, this::deriveDegreesOffCourseFeature)
        .build();
  }

  /**
   * Takes the provided point as well as the {@link FlyableLeg} and computes the distance past the target altitude based on
   * the listed target altitude and type in the leg.
   */
  double deriveFeetPastTargetAltitudeFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    double targetAltitude = flyableLeg.current().altitudeConstraint().flatMap(AltitudeLimit::altitudeLimit).orElseThrow(supplier("Target Altitude"));
    double pressureAltitude = conformablePoint.pressureAltitude().orElseThrow(supplier("Pressure Altitude"));
    return pressureAltitude - targetAltitude;
  }

  double deriveDegreesOffCourseFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    double outboundCourse = flyableLeg.current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));
    MagneticVariation localVariation = MagneticVariationResolver.getInstance().magneticVariation(conformablePoint, flyableLeg);

    double pointCourseTrue = conformablePoint.trueCourse().orElseThrow(supplier("True Course"));
    return abs(angleDifference(localVariation.trueToMagnetic(pointCourseTrue), outboundCourse));
  }
}
