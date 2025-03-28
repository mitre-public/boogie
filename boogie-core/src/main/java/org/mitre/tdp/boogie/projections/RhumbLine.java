package org.mitre.tdp.boogie.projections;

import java.util.ArrayList;
import java.util.List;

import org.mitre.caasd.commons.LatLong;
import org.mitre.caasd.commons.Rhumb;
import org.mitre.caasd.commons.Spherical;

/**
 * This class will do projections along a rhumb line at about 10 miles each.
 */
public final class RhumbLine {
  private RhumbLine() {}

  /**
   * This will estimate a 10NM projection along a rhumb line.
   * @param start the starting point of the projection
   * @param end the ending point of the projection
   * @return a list of lgit at longs including the starting point and projections until the end of the leg.
   */
  public static List<LatLong> project10NM(LatLong start, LatLong end) {
    double rhumbDistance = Rhumb.rhumbDistance(start, end);
    double rhumbDistanceNM = Spherical.distanceInNM(rhumbDistance);

    double rhumbAzimuth = Rhumb.rhumbAzimuth(start, end);

    List<LatLong> result = new ArrayList<>();
    result.add(start);

    if (rhumbDistanceNM > 10) {
      for (double i = 10; i < rhumbDistanceNM; i = i + 10) {
        double radians = Spherical.distanceInRadians(i);
        LatLong projection = Rhumb.rhumbEndPosition(start, rhumbAzimuth, radians);
        result.add(projection);
      }
    }

    return result;
  }
}
