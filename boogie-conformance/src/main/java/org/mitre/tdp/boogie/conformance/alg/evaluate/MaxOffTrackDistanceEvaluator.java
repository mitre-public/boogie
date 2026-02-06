package org.mitre.tdp.boogie.conformance.alg.evaluate;

import java.util.Optional;
import java.util.function.BiPredicate;

import org.mitre.caasd.commons.Distance;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.PathTerminator;
import org.mitre.tdp.boogie.TurnDirection;
import org.mitre.tdp.boogie.conformance.alg.assign.score.legtype.RadialAngle;
import org.mitre.tdp.boogie.validate.PathTerminatorBasedLegValidator;

/**
 * The max off track distance evaluator considers an aircraft to be non-conforming when it is outside
 * some buffer around the active leg its on via the {@link ConformanceEvaluator}.
 */
public final class MaxOffTrackDistanceEvaluator implements ConformanceEvaluator, BiPredicate<ConformablePoint, LegPair> {

  private static final PathTerminatorBasedLegValidator validator = new PathTerminatorBasedLegValidator();

  /**
   * The max off track distance the flight can be prior to being considered non-conforming.
   */
  private final Distance maxOffTrackDistance;

  public MaxOffTrackDistanceEvaluator() {
    this(Distance.ofNauticalMiles(2.0));
  }

  public MaxOffTrackDistanceEvaluator(Distance maxOffTrackDistance) {
    this.maxOffTrackDistance = maxOffTrackDistance;
  }

  public Distance maxOffTrackDistance() {
    return maxOffTrackDistance;
  }

  @Override
  public boolean test(ConformablePoint conformablePoint, LegPair legPair) {
    return validator.test(legPair.previous()) && validator.test(legPair.current());
  }

  @Override
  public Optional<Boolean> conforming(ConformablePoint point, LegPair legPair) {
    return test(point, legPair) ? offTrackDistance(point, legPair).map(maxOffTrackDistance()::isGreaterThan) : Optional.empty();
  }

  /**
   * Since all current notions of conformance are tied to the idea of off-track distance we provide the standard computation
   * as a top-level method for all evaluators.
   * <br>
   * Note this method handles arcs appropriately and will handle leg pairs where the legs aren't both leg concrete leg types
   * (i.e. ending in a concrete path terminator).
   */
  public static Optional<Distance> offTrackDistance(ConformablePoint point, LegPair legPair) {
    Fix previousTerminator = legPair.previous().associatedFix().orElseThrow(IllegalStateException::new);
    Fix currentTerminator = legPair.current().associatedFix().orElseThrow(IllegalStateException::new);

    if (legPair.current().pathTerminator().isArc()) {
      return arcLegOffTrackDistance(point, legPair);
    }
    double crossTrackDistance = Spherical.crossTrackDistanceNM(previousTerminator, currentTerminator, point);
    double alongTrackDistance = Spherical.alongTrackDistanceNM(previousTerminator, currentTerminator, point, crossTrackDistance);

    double legLength = previousTerminator.distanceInNmTo(currentTerminator);

    double modifiedOffTrackDistance;

    if (alongTrackDistance < 0.) {
      modifiedOffTrackDistance = previousTerminator.distanceInNmTo(point);
    } else if (alongTrackDistance > legLength) {
      modifiedOffTrackDistance = currentTerminator.distanceInNmTo(point);
    } else {
      modifiedOffTrackDistance = crossTrackDistance;
    }

    return Optional.of(Distance.ofNauticalMiles(modifiedOffTrackDistance));
  }

  /**
   * @return Distance from the supplied {@code point} to an arc leg.  If the
   * point is before or after the angles included in the arc, returns
   * the distance to the nearest arc endpoint.
   */
  static Optional<Distance> arcLegOffTrackDistance(ConformablePoint point, LegPair legPair) {
    Fix previousTerminator = legPair.previous().associatedFix().orElseThrow(IllegalStateException::new);
    Fix currentTerminator = legPair.current().associatedFix().orElseThrow(IllegalStateException::new);

    Fix centerFix = legPair.current().pathTerminator().equals(PathTerminator.RF) ?
        legPair.current().centerFix().orElseThrow(() -> new IllegalStateException("Center Fix required to compute off-track distance on RF leg")) :
        legPair.current().recommendedNavaid().orElseThrow(() -> new IllegalStateException("Recommended Navaid required to compute off-track distance on AF leg"));

    TurnDirection turnDirection = legPair.current().turnDirection().orElseThrow(() -> new IllegalStateException("Turn direction required to compute off-track distance on arc"));

    double previousFixBearing = centerFix.courseInDegrees(previousTerminator);
    double currentFixBearing = centerFix.courseInDegrees(currentTerminator);
    double pointBearing = centerFix.courseInDegrees(point);

    if (new RadialAngle(previousFixBearing, currentFixBearing, turnDirection).contains(pointBearing)) {
      double radius = centerFix.distanceInNmTo(currentTerminator);
      return Optional.of(Distance.ofNauticalMiles(Math.abs(radius - centerFix.distanceInNmTo(point))));
    } else {
      double previousFixDistance = previousTerminator.distanceInNmTo(point);
      double currentFixDistance = currentTerminator.distanceInNmTo(point);
      return Optional.of(Distance.ofNauticalMiles(Math.min(previousFixDistance, currentFixDistance)));
    }
  }
}
