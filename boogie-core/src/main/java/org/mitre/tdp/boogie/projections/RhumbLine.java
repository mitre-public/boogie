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
    double rhumbDistance = Rhumb.rhumbDistance(start, end);
    double rhumbDistanceNM = Spherical.distanceInNM(rhumbDistance);
    checkArgument(rhumbDistanceNM / stepNm <= 1_000_000, "Projection exceeds the maximum number of points.");

    double rhumbAzimuth = Rhumb.rhumbAzimuth(start, end);

    List<LatLong> result = new ArrayList<>();
    result.add(start);

    for (double i = stepNm; i < rhumbDistanceNM; i += stepNm) {
      double radians = Spherical.distanceInRadians(i);
      LatLong projection = Rhumb.rhumbEndPosition(start, rhumbAzimuth, radians);
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
