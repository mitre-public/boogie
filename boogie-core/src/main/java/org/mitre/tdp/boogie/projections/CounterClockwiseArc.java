package org.mitre.tdp.boogie.projections;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

import org.mitre.caasd.commons.LatLong;

public final class CounterClockwiseArc {
  private CounterClockwiseArc() {}

  /**
   * Finds points on a counterclockwise arc at the requested bearing interval, including the start and excluding the end.
   * The radius is derived from the start and center.
   * @param start start of the arc in this leg of the airspace
   * @param center the center of the arc in this leg of the airspace
   * @param end the end of the arc in the next leg of the airspace
   * @param stepDegrees the finite, positive interval between bearings in degrees
   * @return the list of points
   */
  public static List<LatLong> project(LatLong start, LatLong center, LatLong end, double stepDegrees) {
    checkArgument(Double.isFinite(stepDegrees) && stepDegrees > 0, "Step must be finite and greater than zero degrees.");
    List<LatLong> result = new ArrayList<>();
    result.add(start);
    double startCrs = center.courseInDegrees(start);
    double radius = center.distanceInNM(start);

    double endCrs = center.courseInDegrees(end);
    double denormalizedCourse = endCrs > startCrs ? 0 - (360 - endCrs) : endCrs;
    checkArgument((startCrs - denormalizedCourse) / stepDegrees <= 1_000_000,
        "Projection exceeds the maximum number of points.");
    checkArgument(startCrs - stepDegrees < startCrs, "Step is too small to advance the bearing.");

    for (double i = startCrs - stepDegrees; i > denormalizedCourse; i -= stepDegrees) {
      result.add(center.projectOut(i, radius));
    }

    return result;
  }

  /**
   * This method finds points every 10 degrees on a counterclockwise arc arc.
   * We assume that the only things we trust are the lat/longs and all other numbers are wrong
   * @param start start of the arc in this leg of the airspace
   * @param center the center of the arc in this leg of the airspace
   * @param end the end of the arc in the next leg of the airspace
   * @return the list of points
   */
  public static List<LatLong> project10Deg(LatLong start, LatLong center, LatLong end) {
    return project(start, center, end, 10.0);
  }
}
