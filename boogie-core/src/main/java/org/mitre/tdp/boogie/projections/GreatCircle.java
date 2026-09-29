package org.mitre.tdp.boogie.projections;

import static com.google.common.base.Preconditions.checkArgument;

import java.util.ArrayList;
import java.util.List;

import org.mitre.caasd.commons.LatLong;

public final class GreatCircle {
  private GreatCircle() {
  }

  /**
   * Finds points along a great circle at the requested distance interval, including the start and excluding the end.
   * @param start the start from this leg of the airspace
   * @param end the end from the next leg of the airspace
   * @param stepNm the finite, positive distance between samples in nautical miles
   * @return the list of points
   */
  public static List<LatLong> project(LatLong start, LatLong end, double stepNm) {
    checkArgument(Double.isFinite(stepNm) && stepNm > 0, "Step must be finite and greater than zero nautical miles.");
    List<LatLong> result = new ArrayList<>();
    result.add(start);

    double distanceNM = start.distanceInNM(end);
    double courseDeg = start.courseInDegrees(end);
    checkArgument(distanceNM / stepNm <= 1_000_000, "Projection exceeds the maximum number of points.");

    for (double i = stepNm; i < distanceNM; i += stepNm) {
      result.add(start.projectOut(courseDeg, i));
    }

    return result;
  }

  /**
   * Points along a great circle path every 10 nautical miles.
   * @param start the start from this leg of the airspace
   * @param end the end from the next leg of the airspace
   * @return the list of points
   */
  public static List<LatLong> project10NM(LatLong start, LatLong end) {
    return project(start, end, 10.0);
  }
}
