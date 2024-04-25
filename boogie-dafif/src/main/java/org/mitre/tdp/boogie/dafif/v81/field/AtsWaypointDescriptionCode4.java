package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * ENABLES THE FUNCTION OF A WAYPOINTS LOCATION TO BE IDENTIFIED.
 *
 *  WAYPOINT DESCRIPTION CODE 4:
 *    F - INLAND NAVIGATION FIX
 *    C - COASTAL FIX
 *
 *  INLAND NAVIGATION FIX:  A NAVIGATION AID OR INTERSECTION ON A NORTH AMERICAN ROUTE AT  WHICH THE COMMON ROUTE BEGINS OR ENDS.
 *
 *  COASTAL FIX:  A NAVIGATION AID OR INTERSECTION WHERE AIRCRAFT TRANSITION BETWEEN THE  DOMESTIC ROUTE STRUCTURE AND THE OCEANIC ROUTE STRUCTURE.
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * F, C, NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class AtsWaypointDescriptionCode4 extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 350;
  }

  @Override
  public String regex() {
    return "((F|C)?)";
  }
}