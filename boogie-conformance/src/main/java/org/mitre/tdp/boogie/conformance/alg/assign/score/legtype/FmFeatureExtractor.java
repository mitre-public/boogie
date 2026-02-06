package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException.supplier;

import java.util.function.Supplier;

import org.apache.commons.math3.util.FastMath;
import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;

public final class FmFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {

  /**
   * Singleton instance of an extractor for shared use.
   */
  public static final FmFeatureExtractor INSTANCE = new FmFeatureExtractor();

  /**
   * Feature who's presence indicates the {@link ViterbiFeatureVector} is associated with an FM leg.
   */
  public static final String LEG_TYPE = "IsFm";
  /**
   * Feature representing the course delta between the FM declared course and the actual heading of the aircraft.
   */
  public static final String DEGREES_OFF_COURSE = "DegreesOffCourse";
  /**
   * Feature representing the distance to the FM's fix.
   */
  public static final String DISTANCE_FROM_FIX = "DistanceFromFix";

  /**
   * Feature representing how far before the FM's fix this is. Since the leg includes a true course checking this works.
   */
  public static final String DISTANCE_BEFORE_FIX = "DistanceBeforeFix";

  private FmFeatureExtractor() {
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(LEG_TYPE, (c, l) -> 1.)
        .addFeatureExtractor(DEGREES_OFF_COURSE, this::deriveDegreesOffCourseFeature)
        .addFeatureExtractor(DISTANCE_FROM_FIX, this::deriveDistanceToNextFixFeature)
        .addFeatureExtractor(DISTANCE_BEFORE_FIX, this::deriveBeforeTrackDistance)
        .build();
  }

  double deriveDegreesOffCourseFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    double pointCourse = conformablePoint.trueCourse().orElseThrow(supplier("True Course"));
    double expectedCourse = flyableLeg.current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));
    MagneticVariation navMagVar = MagneticVariationResolver.getInstance().magneticVariation(conformablePoint, flyableLeg);

    return abs(Spherical.angleDifference(navMagVar.magneticToTrue(expectedCourse), pointCourse));
  }

  double deriveDistanceToNextFixFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    return flyableLeg.current().associatedFix()
        .map(f -> conformablePoint.latLong().distanceInNM(f.latLong()))
        .orElseThrow(supplier("No Fix in current leg"));
  }

  /**
   * This calculates the along track distance for the point to the fm's start to a 25 mile projection.
   * @param conformablePoint to test
   * @param flyableLeg our fm leg
   * @return the distance before fix. Negative for before or if its after the fix 0;
   */
  double deriveBeforeTrackDistance(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    HasPosition current = flyableLeg.current().associatedFix().map(Fix::latLong).map(HasPosition::from).orElseThrow(supplier("No fix in FM"));
    MagneticVariation navMagVar = flyableLeg.current().recommendedNavaid()
        .orElseThrow(supplier("No Rec Nav in FM"))
        .magneticVariation()
        .orElseThrow(supplier("No magvar on navaid"));
    Double magCrs = flyableLeg.current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));
    Double trueCourse = navMagVar.magneticToTrue(magCrs);
    HasPosition projection = current.projectOut(trueCourse, 25.0);

    double ctd = Spherical.crossTrackDistanceNM(current, projection, conformablePoint);
    double atd = Spherical.alongTrackDistanceNM(current, projection, conformablePoint, ctd);

    return FastMath.min(atd, 0.0);

  }
}