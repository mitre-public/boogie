package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * INDICATES THE AIRSPACE STRUCTURE IN WHICH THE WAYPOINT IS UTILIZED.
 *
 * B - BOTH (HIGH AND LOW)
 *  E - OFF RTE INTERSECTION IN FAA AIRSPACE
 *  H - HIGH LEVEL
 *  L - LOW LEVEL
 *  P - PITCH AND CATCH (RNAV)
 *  R - RNAV
 *  S - ATCAA AND SUAS WAYPOINTS IN FAA HIGH ALTITUDE STRUCTURE
 *  T - TERMINAL WAYPOINT
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * B, E, H, L, P, R, S, T
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * WHEN A WAYPOINT IS USED ON BOTH TYPES OF ATS ROUTES (GENERAL AND RNAV) THE WAYPOINT USAGE WILL BE B, H, OR L.
 * WHEN A WAYPOINT IS USED ONLY ON RNAV ROUTES, IT'S USAGE CODE WILL BE R.
 */
public final class WaypointUsageCode extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 343;
  }

  @Override
  public String regex() {
    return "(B|E|H|L|P|R|S|T)";
  }
}