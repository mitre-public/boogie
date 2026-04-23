package org.mitre.tdp.boogie.util;

import static com.google.common.base.Preconditions.checkNotNull;

import java.util.List;
import java.util.function.Function;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.Leg;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.TurnDirection;

/**
 * This class provides a simple estimate of the distance of a list of legs.
 * It relies on fixes in the list, so this might be viewed as the shortest length the list can be. This might be useful for longer paths where most of the route is fix based.
 */
public final class LegListDistanceEstimate implements Function<List<Leg>, Double> {

  /**
   * Returns the estimated distance of the provided list of legs. This distance is wrong, but useful for a quick estimate.
   * @param legs the function argument
   * @return the estimated distance in nautical miles.
   */
  @Override
  public Double apply(List<Leg> legs) {
    checkNotNull(legs);

    List<Leg> filteredLegs = legs.stream().filter(l -> l.associatedFix().isPresent()).toList();

    if (filteredLegs.size() < 2) {
      return 0.0;
    }

    return Streams.pairwise(filteredLegs)
        .mapToDouble(p -> distance(p.first(), p.second()))
        .sum();
  }

  private static double distance(Leg previous, Leg current) {
    return switch (current.pathTerminator()) {
      case AF -> afDistance(current);
      case RF -> rfDistance(previous, current);
      default -> simpleDistance(previous, current);
    };
  }

  private static Double simpleDistance(Leg previous, Leg next) {
    return previous.associatedFix().orElseThrow().latLong().distanceInNM(next.associatedFix().orElseThrow().latLong());
  }

  private static Double afDistance(Leg leg) {
    MagneticVariation magvar = leg.recommendedNavaid().flatMap(Fix::magneticVariation).orElseThrow(IllegalStateException::new);
    Double outboundTrue = leg.outboundMagneticCourse().map(magvar::magneticToTrue).orElseThrow(IllegalStateException::new);
    Double thetaTrue = leg.theta().map(magvar::magneticToTrue).orElseThrow(IllegalStateException::new);
    Double rho = leg.rho().orElseThrow(IllegalStateException::new);
    TurnDirection td = leg.turnDirection().orElseThrow();
    Double arcAngle = ArcAngleCalculator.from(outboundTrue, thetaTrue, td);
    return Spherical.arcLength(rho, arcAngle);
  }

  private static Double rfDistance(Leg previous, Leg leg) {
    LatLong centerFix = leg.centerFix().orElseThrow(() -> new IllegalStateException("Center fix required to compute RF leg distance")).latLong();
    LatLong incomingFix = previous.associatedFix().orElseThrow(() -> new IllegalStateException("Incoming fix required to compute RF leg distance")).latLong();
    LatLong outgoingFix = leg.associatedFix().orElseThrow(() -> new IllegalStateException("Outgoing fix required to compute RF leg distance")).latLong();
    TurnDirection td = leg.turnDirection().orElseThrow();
    Double arcAngle = ArcAngleCalculator.from(centerFix.courseInDegrees(incomingFix), centerFix.courseInDegrees(outgoingFix), td);
    Double radius = centerFix.distanceInNM(outgoingFix);
    return Spherical.arcLength(arcAngle, radius);
  }
}
