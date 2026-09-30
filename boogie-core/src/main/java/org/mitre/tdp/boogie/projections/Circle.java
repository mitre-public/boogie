package org.mitre.tdp.boogie.projections;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

import org.mitre.caasd.commons.LatLong;

/**
 * This class provides a method to turn a circle defined by a center and radius into a list points on that circle
 */
public final class Circle {

  /**
   * Finds points on a circle at the requested bearing interval.
   * Returns an open ring in clockwise bearing order; connect its last point to its first to close it.
   * @param arcRadius the radius of the arc in NM
   * @param arcCenter the latlong of the center point
   * @param stepDegrees the finite, positive interval between bearings in degrees
   * @return the projected points, beginning at a bearing of zero degrees
   */
  public static List<LatLong> project(double arcRadius, LatLong arcCenter, double stepDegrees) {
    checkArgument(Double.isFinite(stepDegrees) && stepDegrees > 0, "Step must be finite and greater than zero degrees.");
    checkArgument(360 / stepDegrees <= 1_000_000, "Projection exceeds the maximum number of points.");
    List<LatLong> points = new ArrayList<>();
    for (double i = 0; i < 360; i += stepDegrees) {
      points.add(arcCenter.projectOut(i, arcRadius));
    }
    return points;
  }

  /**
   * This method finds points on a circle every 10 degrees.
   * @param arcRadius the radius of the arc in NM
   * @param arcCenter the latlong of the center point
   * @return a list of points on that circle projected out every 10 degrees
   */
  public static List<LatLong> project10Deg(double arcRadius, LatLong arcCenter) {
    return project(arcRadius, arcCenter, 10.0);
  }
}
