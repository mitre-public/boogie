package org.mitre.tdp.boogie.projections;

import java.util.ArrayList;
import java.util.List;

import org.mitre.caasd.commons.LatLong;

public final class ClockwiseArc {
  private ClockwiseArc() {
  }

  /**
   * This method finds points ever 10 degrees on a clockwise arc.
   * We assume that the only things we trust are the lat/longs and all other numbers are wrong
   *
   * @param start  start of the arc in this leg of the airspace
   * @param center the center of the arc in this leg of the airspace
   * @param end    the end of the arc in the next leg of the airspace
   * @return the list of points
   */
  public static List<LatLong> project10Deg(LatLong start, LatLong center, LatLong end) {
    List<LatLong> result = new ArrayList<>();
    result.add(start);
    double startCrs = center.courseInDegrees(start);
    double radius = center.distanceInNM(start);

    double endCrs = center.courseInDegrees(end);
    double denormalizedCourse = endCrs < startCrs ? 360 + endCrs : endCrs;

    for (double i = startCrs + 10; i < denormalizedCourse; i += 10) {
      result.add(center.projectOut(i, radius));
    }

    return result;
  }
}
