package org.mitre.tdp.boogie.conformance.alg.assign.score.legtype;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException.supplier;

import java.util.Arrays;
import java.util.List;
import java.util.function.Supplier;

import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.Pair;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVector;
import org.mitre.tdp.boogie.viterbi.ViterbiFeatureVectorExtractor;

public final class TfFeatureExtractor implements Supplier<ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg>> {

  public static final TfFeatureExtractor INSTANCE = new TfFeatureExtractor();
  /**
   * Feature who's presence indicates the {@link ViterbiFeatureVector} is associated with an TF leg.
   */
  public static final String LEG_TYPE = "IsTf";
  /**
   * Returns the degrees off course from the TF the aircraft is when the
   */
  public static final String DEGREES_OFF_COURSE = "DegreesOffCourse";
  /**
   * The distance the flight is off the TF leg.
   */
  public static final String OFF_TRACK_DISTANCE = "OffTrackDistance";
  /**
   * The signed distance pre/post the end of the leg (if the CTD doesn't fall between the endpoints of the segment).
   */
  public static final String PRE_POST_DISTANCE = "PrePostDistance";

  private TfFeatureExtractor() {
  }

  @Override
  public ViterbiFeatureVectorExtractor<ConformablePoint, FlyableLeg> get() {
    return ViterbiFeatureVectorExtractor.<ConformablePoint, FlyableLeg>newBuilder()
        .addFeatureExtractor(LEG_TYPE, (c, f) -> 1.)
        .addFeatureExtractor(DEGREES_OFF_COURSE, this::deriveDegreesOffCourseFeature)
        .addMultiFeatureExtractor(this::deriveDistanceFeatures, OFF_TRACK_DISTANCE, PRE_POST_DISTANCE)
        .build();
  }

  double deriveDegreesOffCourseFeature(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    Fix startFix = flyableLeg.previous().flatMap(Leg::associatedFix)
        .orElseThrow(supplier("pathTerminator of from leg"));

    Fix endFix = flyableLeg.current().associatedFix()
        .orElseThrow(supplier("pathTerminator of to leg"));

    double legCourse = startFix.courseInDegrees(endFix);
    return abs(Spherical.angleDifference(conformablePoint.trueCourse().orElseThrow(supplier("required course")), legCourse));
  }

  /**
   * Leverages the {@link SegmentDistances} object to compute the collection of cross/along/off track distances between the
   * point and the {@link FlyableLeg} then returns those as a list of computed named features.
   */
  List<Pair<String, Double>> deriveDistanceFeatures(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    Fix startFix = flyableLeg.previous().flatMap(Leg::associatedFix)
        .orElseThrow(supplier("pathTerminator of from leg"));

    Fix endFix = flyableLeg.current().associatedFix()
        .orElseThrow(supplier("pathTerminator of to leg"));

    SegmentDistances segmentDistances = SegmentDistances.of(startFix, endFix, conformablePoint);
    return Arrays.asList(
        Pair.of(OFF_TRACK_DISTANCE, segmentDistances.otd),
        Pair.of(PRE_POST_DISTANCE, segmentDistances.ppd)
    );
  }

  /**
   * Container class for computation of point-to-point segment distance.
   */
  public static class SegmentDistances {
    /**
     * Cross-track distance (negative on left side of leg, positive on right)
     */
    final double ctd;
    /**
     * Pre-/post-distance (negative before start, positive after end)
     */
    final double ppd;
    /**
     * Off-track distance (aka "segment distance", with sign of ctd)
     */
    final double otd;

    SegmentDistances(double ctd, double ppd, double otd) {
      this.ctd = ctd;
      this.ppd = ppd;
      this.otd = otd;
    }

    public static SegmentDistances of(HasPosition startFix, HasPosition endFix, HasPosition point) {
      double ctd = Spherical.crossTrackDistanceNM(startFix, endFix, point);
      double atd = Spherical.alongTrackDistanceNM(startFix, endFix, point, ctd);

      double pathLength = startFix.distanceInNmTo(endFix);
      boolean beforeSegment = atd < 0.0;
      boolean afterSegment = atd > pathLength;

      if (beforeSegment) {
        return new SegmentDistances(ctd, atd, Math.signum(ctd) * point.distanceInNmTo(startFix));
      } else if (afterSegment) {
        return new SegmentDistances(ctd, atd - pathLength, Math.signum(ctd) * point.distanceInNmTo(endFix));
      } else {
        return new SegmentDistances(ctd, 0., ctd);
      }
    }
  }
}
