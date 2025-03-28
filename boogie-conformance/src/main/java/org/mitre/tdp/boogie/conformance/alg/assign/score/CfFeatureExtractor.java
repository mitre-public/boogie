package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.caasd.commons.Spherical.angleDifference;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException.supplier;

import java.util.function.Supplier;

import org.mitre.caasd.commons.HasPosition;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;

public final class CfFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {
  /**
   * Singleton instance of the extractor for shared use.
   */
  public static final CfFeatureExtractor INSTANCE = new CfFeatureExtractor();

  /**
   * Feature who's presence indicates the {@link ViterbiFeatureVector} is associated with an CF leg.
   */
  public static final String LEG_TYPE = "IsCf";
  /**
   * Feature representing the distance from the fix the aircraft is at the projected crossing point based on the current course
   * of the aircraft and its current distance to the fix.
   */
  public static final String PROJECTED_DISTANCE_OFFSET = "ProjectedDistanceOffset";
  /**
   * Feature indicating the delta (in degrees) between the leg's declared course and the flights actual course.
   */
  public static final String DEGREES_OFF_COURSE = "DegreesOffCourse";

  private CfFeatureExtractor() {
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(LEG_TYPE, (c, f) -> 1.)
        .addFeatureExtractor(DEGREES_OFF_COURSE, this::deriveDegreesOffCourse)
        .addFeatureExtractor(PROJECTED_DISTANCE_OFFSET, this::deriveProjectedDistanceOffset)
        .build();
  }

  /**
   * Computes the projected offset of the aircraft from the direct-to fix given the current distance of the aircraft from the
   * fix and the current course of the aircraft.
   */
  double deriveProjectedDistanceOffset(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    Fix pathTerminator = flyableLeg.current().associatedFix().orElseThrow(supplier("Associated Fix"));

    double distance = conformablePoint.distanceInNmTo(pathTerminator);
    double pointCourse = conformablePoint.trueCourse().orElseThrow(supplier("Point Course"));

    HasPosition projectedPosition = conformablePoint.projectOut(pointCourse, distance);
    return projectedPosition.distanceInNmTo(pathTerminator);
  }

  double deriveDegreesOffCourse(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    // both of these are bearings - magnetic
    double courseToFix = flyableLeg.current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));

    // convert the true course to the point to a magnetic one for comparison against the boundary/fix radials
    MagneticVariation localVariation = MagneticVariationResolver.getInstance().magneticVariation(conformablePoint, flyableLeg);

    double trueCourse = conformablePoint.trueCourse().orElseThrow(supplier("Point Course"));
    double magneticCourse = localVariation.trueToMagnetic(trueCourse);

    return abs(angleDifference(magneticCourse, courseToFix));
  }
}
