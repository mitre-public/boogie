package org.mitre.tdp.boogie.conformance.alg.assign.score;

import static java.util.Objects.requireNonNull;
import static org.apache.commons.math3.util.FastMath.abs;
import static org.mitre.tdp.boogie.conformance.alg.assign.score.MissingRequiredFieldException.supplier;

import java.util.List;
import java.util.stream.IntStream;

import org.apache.commons.math3.util.FastMath;
import org.mitre.caasd.commons.HasPosition;
import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Spherical;
import org.mitre.tdp.boogie.ConformablePoint;
import org.mitre.tdp.boogie.Fix;
import org.mitre.tdp.boogie.MagneticVariation;
import org.mitre.tdp.boogie.conformance.alg.assign.FlyableLeg;

/**
 * Utility class for calculating various distance-related measurements between aircraft positions and legs.
 * All distances are calculated in nautical miles (NM) unless otherwise specified.
 */
public final class LegDistance {
    private static final double PROJECTION_DISTANCE = 150.0;
    private static final double PROJECTION_STEP = 0.1;
    private static final int MAX_PROJECTIONS = 1000;

  private LegDistance() {}

  /**
   * This finds the distance before the track or 0.0 if it's not before it.
   * @param conformablePoint the point to check
   * @param flyableLeg our leg to use
   * @return the distance (more negative is more before the leg)
   */
  public static double deriveDistanceBeforeLeg(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    requireNonNull(conformablePoint, "conformablePoint cannot be null");
    requireNonNull(flyableLeg, "flyableLeg cannot be null");
    LatLong current = flyableLeg.current().associatedFix().map(Fix::latLong).orElseThrow(IllegalStateException::new);
    MagneticVariation navMagVar = MagneticVariationResolver.getInstance().magneticVariation(conformablePoint, flyableLeg);
    Double magCrs = flyableLeg.current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));
    Double trueCourse = navMagVar.magneticToTrue(magCrs);
    LatLong projection = current.projectOut(trueCourse, PROJECTION_DISTANCE);
    LatLong planeHere = conformablePoint.latLong();

    double ctd = Spherical.crossTrackDistanceNM(HasPosition.from(current), HasPosition.from(projection), HasPosition.from(planeHere));
    double atd = Spherical.alongTrackDistanceNM(HasPosition.from(current), HasPosition.from(projection), HasPosition.from(planeHere), ctd);

    return FastMath.min(atd, 0.0);
  }

  public static double deriveNearestToDmeTermination(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    requireNonNull(conformablePoint, "conformablePoint cannot be null");
    requireNonNull(flyableLeg, "flyableLeg cannot be null");

    LatLong spot = conformablePoint.latLong();
    LatLong navaid = getNavaidLatLong(flyableLeg);
    double airplaneCourse = getAirplaneCourse(conformablePoint);
    double dmeDistance = getDmeDistance(flyableLeg);
    double distanceToNavaid = spot.distanceInNM(navaid);

    List<LatLong> projections = generateProjections(spot, airplaneCourse);
    LatLong firstNearest = findFirstNearestPoint(projections, navaid, dmeDistance, distanceToNavaid);

    return Math.abs(dmeDistance - firstNearest.distanceInNM(navaid));
}

private static LatLong getNavaidLatLong(FlyableLeg flyableLeg) {
    return flyableLeg.current().recommendedNavaid()
        .orElseThrow(supplier("No recommended navaid available"))
        .latLong();
}

private static double getAirplaneCourse(ConformablePoint conformablePoint) {
    return conformablePoint.trueCourse()
        .orElseThrow(supplier("True course not available"));
}

private static double getDmeDistance(FlyableLeg flyableLeg) {
    return flyableLeg.current().routeDistance()
        .orElseThrow(supplier("DME distance not available"));
}

private static List<LatLong> generateProjections(LatLong start, double course) {
    return IntStream.rangeClosed(0, MAX_PROJECTIONS)
        .mapToObj(i -> start.projectOut(course, i * PROJECTION_STEP))
        .toList();
}

private static LatLong findFirstNearestPoint(List<LatLong> projections, LatLong navaid, double targetDistance, double initialDistance) {
    return projections.stream()
        .filter(point -> isDistanceThresholdCrossed(point, navaid, targetDistance, initialDistance))
        .findFirst()
        .orElse(projections.get(0));
}

  /**
   *  You are either far away getting closer to the DME or you are near it and getting further away
   * @param point where u is
   * @param navaid the navaid thing
   * @param targetDistance the dme distance we want to get to.
   * @param initialDistance how far we were to start ^^ see the opening line
   * @return magic boolean
   */
  private static boolean isDistanceThresholdCrossed(LatLong point, LatLong navaid, double targetDistance, double initialDistance) {
    double currentDistance = point.distanceInNM(navaid);
    return (initialDistance > targetDistance && currentDistance <= targetDistance) || (initialDistance <= targetDistance && currentDistance >= targetDistance);
}

  /**
   * This method finds how near to a radial from the leg's navaid you either are or will be if you continue on the current path.
   * @param conformablePoint location of airplane
   * @param flyableLeg the leg stuff
   * @return the distance in NM
   */
  public static double nearestToRadial(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    LatLong navaid = flyableLeg.current().recommendedNavaid().orElseThrow(supplier("No rec nav?")).latLong();
    double theta = flyableLeg.current().theta().orElseThrow(supplier("No theta?"));
    double aircraftTrueCrs = conformablePoint.trueCourse().orElseThrow(supplier("True Course"));

    LatLong radialEndPoint = navaid.projectOut(theta, PROJECTION_DISTANCE);
    List<LatLong> aircraftProjections = IntStream.rangeClosed(0, MAX_PROJECTIONS)
        .mapToObj(i -> conformablePoint.latLong().projectOut(aircraftTrueCrs, i * PROJECTION_STEP))
        .toList();

    return aircraftProjections.stream()
        .map(HasPosition::from)
        .mapToDouble(p -> Spherical.crossTrackDistanceNM(HasPosition.from(navaid), HasPosition.from(radialEndPoint), p))
        .map(FastMath::abs)
        .min()
        .orElseThrow(supplier("You don't be looking near by"));
  }

  /**
   * Your airplane is pointed one way and the leg says go another, whats the difference there?
   * @param conformablePoint point
   * @param flyableLeg leg
   * @return your number in degrees
   */
  public static double deriveDegreesOffCourse(ConformablePoint conformablePoint, FlyableLeg flyableLeg) {
    double pointCourse = conformablePoint.trueCourse().orElseThrow(supplier("True Course"));
    double expectedCourse = flyableLeg.current().outboundMagneticCourse().orElseThrow(supplier("Outbound Magnetic Course"));
    MagneticVariation navMagVar = MagneticVariationResolver.getInstance().magneticVariation(conformablePoint, flyableLeg);

    return abs(Spherical.angleDifference(navMagVar.magneticToTrue(expectedCourse), pointCourse));
  }

  /**
   * When a leg is fix originating and its some kind of course leg, (e.g, FD) then we want to see how far off center we are.
   * @param conformablePoint the point.
   * @param leg the leg
   * @return This is normalized to a positive number (aka for far off center you are does not matter about left/right)
   */
  public static double fixOriginatingCrossTrackDistance(ConformablePoint conformablePoint, FlyableLeg leg) {
    LatLong fix = leg.current().associatedFix().map(Fix::latLong).orElseThrow(IllegalStateException::new);
    MagneticVariation navMagVar = MagneticVariationResolver.getInstance().magneticVariation(conformablePoint, leg);
    double legTrueCourse = leg.current().outboundMagneticCourse().map(navMagVar::magneticToTrue).orElseThrow(supplier("Outbound Magnetic Course"));
    LatLong projection = fix.projectOut(legTrueCourse, PROJECTION_DISTANCE);
    double ctd = Spherical.crossTrackDistanceNM(HasPosition.from(fix), HasPosition.from(projection), HasPosition.from(conformablePoint.latLong()));
    return FastMath.abs(ctd);
  }
}