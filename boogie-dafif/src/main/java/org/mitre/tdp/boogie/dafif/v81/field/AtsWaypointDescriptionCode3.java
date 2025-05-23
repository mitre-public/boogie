package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * ENABLES THE FUNCTION OF A WAYPOINTS LOCATION TO BE IDENTIFIED.
 *
 *  WAYPOINT DESCRIPTION CODE 3:
 *   C - ATC COMPULSORY WAYPOINT
 *
 *  ATC COMPULSORY REPORTING POINT:  ESSENTIAL OR NON-ESSENTIAL WAYPOINTS MAY BE CLASSIFIED AS  ATC COMPULSORY REPORTING POINTS.  ATC REQUIRES THE PILOT TO MAKE A COMMUNICATIONS REPORT
 *  AT THESE WAYPOINTS.  ALL OTHER WAYPOINTS MAY BE CLASSIFIED AS NON-COMPULSORY REPORTING  POINTS AND ARE REPORTED ONLY WHEN SPECIFICALLY REQUESTED BY ATC.
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * C, NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public final class AtsWaypointDescriptionCode3 extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 349;
  }

  @Override
  public String regex() {
    return "((C)?)";
  }
}