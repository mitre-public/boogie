package org.mitre.tdp.boogie.projections;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Rhumb;
import org.mitre.caasd.commons.Spherical;

/**
 * This class provides projections along a rhumb line.
 */
public final class RhumbLine {
  private RhumbLine() {}

  /**
   * Finds points along a rhumb line at the requested distance interval, including the start and excluding the end.
   * @param start the starting point of the projection
   * @param end the ending point of the projection
   * @param stepNm the finite, positive distance between samples in nautical miles
   * @return the list of points
   */
  public static List<LatLong> project(LatLong start, LatLong end, double stepNm) {
    checkArgument(Double.isFinite(stepNm) && stepNm > 0, "Step must be finite and greater than zero nautical miles.");
    boolean startAtPole = Math.abs(start.latitude()) == 90.0;
    boolean endAtPole = Math.abs(end.latitude()) == 90.0;
    if (startAtPole && endAtPole && start.latitude() == end.latitude()) {
      return List.of(start);
    }
    boolean sameMeridian = start.longitude() == end.longitude() || Math.abs(start.longitude() - end.longitude()) == 360.0;
    checkArgument(!startAtPole || !endAtPole || sameMeridian,
        "Opposite poles need a common longitude to define a rhumb meridian.");
    boolean meridian = sameMeridian || startAtPole || endAtPole;
    double rhumbDistance = meridian
        ? Math.toRadians(Math.abs(end.latitude() - start.latitude()))
        : Rhumb.rhumbDistance(start, end);
    double rhumbDistanceNM = Spherical.distanceInNM(rhumbDistance);
    checkArgument(rhumbDistanceNM / stepNm <= 1_000_000, "Projection exceeds the maximum number of points.");

    double rhumbAzimuth = meridian ? 0.0 : Rhumb.rhumbAzimuth(start, end);
    double longitude = startAtPole ? end.longitude() : start.longitude();

    List<LatLong> result = new ArrayList<>();
    result.add(start);

    // Distance conversion can put an exact step multiple a few ULPs above the endpoint.
    // Leave that endpoint to the next boundary sequence, avoiding a tiny spurious edge.
    // Include input-coordinate precision: subtracting nearby latitudes can lose many
    // distance ULPs even when the result differs from an exact step by only roundoff.
    double coordinateUlpDegrees = Math.max(
        Math.max(Math.ulp(start.latitude()), Math.ulp(end.latitude())),
        Math.max(Math.ulp(start.longitude()), Math.ulp(end.longitude())));
    double endpointToleranceNm = 8.0 * Math.max(Math.ulp(rhumbDistanceNM),
        Spherical.distanceInNM(Math.toRadians(coordinateUlpDegrees)));
    for (int sample = 1; ; sample++) {
      double distanceNm = sample * stepNm;
      if (distanceNm >= rhumbDistanceNM || rhumbDistanceNM - distanceNm <= endpointToleranceNm) {
        break;
      }
      // Mercator-based rhumb formulas are singular at a pole. A meridian instead has
      // linear latitude and constant longitude, including when a pole has another label.
      LatLong projection = meridian
          ? LatLong.of(start.latitude() + (end.latitude() - start.latitude()) * (distanceNm / rhumbDistanceNM), longitude)
          : Rhumb.rhumbEndPosition(start, rhumbAzimuth, Spherical.distanceInRadians(distanceNm));
      result.add(projection);
    }

    return result;
  }

  /**
   * This will estimate a 10NM projection along a rhumb line.
   * @param start the starting point of the projection
   * @param end the ending point of the projection
   * @return the starting point and projections until the end of the leg
   */
  public static List<LatLong> project10NM(LatLong start, LatLong end) {
    return project(start, end, 10.0);
  }
}
