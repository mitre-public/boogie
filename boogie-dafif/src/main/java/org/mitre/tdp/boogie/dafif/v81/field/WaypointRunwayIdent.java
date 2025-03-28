package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * WHEN THE WAYPOINT IS THE END OF RUNWAY OR DISPLACED  THRESHOLD, ENTER THE TWO OR THREE CHARACTER RUNWAY IDENTIFIER.
 * FOR TERMINAL PROCEDURES ONLY.
 *
 *  EXAMPLES:
 * 02
 * 36R
 * 10L
 *
 *  NOTES:
 *    WAYPOINT MUST HAVE USAGE CODE ‘G’.
 *    WAYPOINT RUNWAY ICAO/FAA HOST COUNTRY IDENTIFIER MUST BE POPULATED.
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * 01-36
 *  OR
 *  01-36 FOLLOWED BY ONE OF THE FOLLOWING: L, R, C, S, T
 *  OR
 *  NULL
 *
 * SOURCE: HOST NATION PUBLICATION
 *
 * INTENDED USE:
 * THIS FIELD IS NO LONGER IN USE AND WILL BE NULL
 */
public final class WaypointRunwayIdent extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 3;
  }

  @Override
  public int fieldCode() {
    return 270;
  }

  @Override
  public String regex() {
    return "(^$)";
  }
}