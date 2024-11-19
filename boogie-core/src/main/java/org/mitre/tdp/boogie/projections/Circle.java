package org.mitre.tdp.boogie.projections;

import java.util.ArrayList;
import java.util.List;

import org.mitre.caasd.commons.LatLong;

/**
 * This class provides a method to turn a circle defined by a center and radius into a list points on that circle
 */
public final class Circle {

  /**
   * This method finds points on a circle every 10 degrees.
   * @param arcRadius the radius of the arc in NM
   * @param arcCenter the latlong of the center point
   * @return a list of points on that circle projected out every 10 degrees
   */
  public static List<LatLong> project10Deg(double arcRadius, LatLong arcCenter) {
    List<LatLong> points = new ArrayList<>();
    for (int i = 0; i < 360; i += 10) {
      points.add(arcCenter.projectOut((double) i, arcRadius));
    }
    return points;
  }
}
