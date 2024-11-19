package org.mitre.tdp.boogie.projections;

import java.util.ArrayList;
import java.util.List;

import org.mitre.caasd.commons.LatLong;

public final class GreatCircle {
  private GreatCircle() {
  }

  /**
   * Points along a great circle path every 10 nautical miles.
   * @param start the start from this leg of the airspace
   * @param end the end from the next leg of the airspace
   * @return the list of points
   */
  public static List<LatLong> project10NM(LatLong start, LatLong end) {
    List<LatLong> result = new ArrayList<>();
    result.add(start);

    double distanceNM = start.distanceInNM(end);
    double courseDeg = start.courseInDegrees(end);

    if (distanceNM > 10) {
      for (double i = 10; i < distanceNM; i = i + 10) {
        result.add(start.projectOut(courseDeg, i));
      }
    }

    return result;
  }
}
