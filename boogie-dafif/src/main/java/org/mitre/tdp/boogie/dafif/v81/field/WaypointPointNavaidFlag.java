package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * (Y) INDICATES THE WAYPOINT/POINT IS COLLOCATED WITH A NAVAID.
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * Y
 * OR
 * NULL
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * THIS FIELD IS USED TO IDENTIFY WAYPOINTS THAT ARE CO-LOCATED WITH A NAVAID. PER DAFIF LOGIC RULES FOR MULTIPLE TABLES THAT USE NAVAIDS AS WAYPOINTS (INCLUDING ATS AND TERMINALS), A WAYPOINT WILL BE CO-LOCATED WITH NAVAIDS. THESE WAYPOINTS ARE CREATED BY NGA AND ARE NOT SOURCED FROM ANSP.
 */
public final class WaypointPointNavaidFlag extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 1;
  }

  @Override
  public int fieldCode() {
    return 340;
  }

  @Override
  public String regex() {
    return "((Y)?)";
  }
}