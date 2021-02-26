package org.mitre.tdp.boogie.conformance.alg.assign.score.impl;

import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.MissingRequiredFieldException.supplier;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.impl.WeightFunctions.simpleLogistic;

import java.util.Optional;
import java.util.function.Function;

import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.PathTerm;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;
import org.mitre.tdp.boogie.conformance.alg.assign.score.OnLegScorer;

/**
 * This is the default conformance evaluator for {@link PathTerm#TF} legs.
 */
public class TfScorer implements OnLegScorer {

  /**
   * The weight function to use when weighting the on leg score by cross track distance from the leg.
   */
  private final Function<Double, Double> offTrackDistanceWeight;

  /**
   * Weight function to use when scoring the heading delta between the leg and the track.
   */
  private final Function<Double, Double> headingWeight;

  /**
   * Function to translate {@link SegmentDistances} into a distance to use for scoring.
   * <p>
   * This is pluggable for cases where a penalty should be applied to points based on
   * SegmentDistance attributes.
   */
  private final Function<SegmentDistances, Double> distanceExtractor;

  public TfScorer() {
    this(simpleLogistic(.5, 1.), simpleLogistic(90., 175.));
  }

  public TfScorer(
      Function<Double, Double> offTrackDistanceWeight,
      Function<Double, Double> headingWeight) {
    this(offTrackDistanceWeight, headingWeight, penalizePrePost(2, 2));
  }

  public TfScorer(
      Function<Double, Double> offTrackDistanceWeight,
      Function<Double, Double> headingWeight,
      Function<SegmentDistances, Double> distanceExtractor) {
    this.offTrackDistanceWeight = offTrackDistanceWeight;
    this.headingWeight = headingWeight;
    this.distanceExtractor = distanceExtractor;
  }

  @Override
  public Optional<Double> score(ConformablePoint point, FlyableLeg legTriple) {
    return legTriple.previous().isPresent()
        && legTriple.previous().map(Leg::pathTerminator).isPresent()
        && legTriple.previous().map(Leg::pathTerminator).map(Fix::latLong).isPresent()
        ? Optional.of(scoreAgainstLeg(point, legTriple)) : Optional.empty();
  }

  @Override
  public double scoreAgainstLeg(ConformablePoint point, FlyableLeg legTriple) {
    Fix startFix = legTriple.previous().map(Leg::pathTerminator)
        .orElseThrow(supplier("pathTerminator of from leg"));

    Fix endFix = Optional.ofNullable(legTriple.current().pathTerminator())
        .orElseThrow(supplier("pathTerminator of to leg"));

    double distanceToScore = abs(distanceExtractor.apply(SegmentDistances.of(startFix, endFix, point)));

    double distanceWeight = offTrackDistanceWeight.apply(distanceToScore);
    double headingWeight = headingWeight(point, startFix, endFix);

    return distanceWeight * headingWeight;
  }

  double headingWeight(ConformablePoint conformablePoint, Fix startFix, Fix endFix) {
    double legCourse = startFix.courseInDegrees(endFix);

    double angleDifference = abs(Spherical.angleDifference(conformablePoint.trueCourse().orElseThrow(supplier("required course")), legCourse));
    return headingWeight.apply(angleDifference);
  }

  public static class SegmentDistances {
    /** Cross-track distance (negative on left side of leg, positive on right) */
    final double ctd;
    /** Pre-/post-distance (negative before start, positive after end) */
    final double ppd;
    /** Off-track distance (aka "segment distance", with sign of ctd) */
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

  /**
   * Returns a distance extractor that penalizes points preceding the start fix or past the end fix by
   * applying the specified multiplicative penalty.
   */
  public static Function<SegmentDistances, Double> penalizePrePost(int prePenalty, int postPenalty) {
    return x -> (x.ppd > 0. ? postPenalty : x.ppd < 0 ? prePenalty : 1) * x.otd;
  }

  /**
   * Returns a distance extractor with no pre/post penalty
   */
  public static Function<SegmentDistances, Double> extractSegmentDistance() {
    return x -> x.otd;
  }


}
