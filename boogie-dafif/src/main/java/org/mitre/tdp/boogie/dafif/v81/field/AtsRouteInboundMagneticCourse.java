package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * INDICATES DIRECTIONAL INFORMATION LEADING TO A WAYPOINT.
 * ALSO APPLIES TO ATS HOLDING PATTERNS BASED ON THE COURSE INBOUND TO THE DEFINED HOLD POINT ONCE IN THE HOLDING PATTERN.
 *
 * EXAMPLE(S):
 * 1.0
 * 56.T
 * 99.0
 * 109T
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * (LEADING ZEROS ARE SUPPRESSED UNLESS VALUE IS LESS THAN 1.0)
 * IF COURSE IS IN TRUE OR GRID, VALUE CANNOT BE LESS THAN 1.0 AND TENTHS PLACE IS FILLED WITH T OR G.
 * 0.1 - 360.0
 * OR
 * 1.G-360.G
 * OR
 * 1.T-360.T
 * OR
 * NULL
 *
 * **THE LAST CHARACTER CAN BE "T" (TRUE), "G" (GRID), OR NUMERIC (IF KNOWN).
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 *
 * INTENDED USE:
 * INBOUND COURSE TO THE WAYPOINT (IF KNOWN)
 */
public final class AtsRouteInboundMagneticCourse extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 39;
  }

  @Override
  public String regex() {
    return "(((0\\.[1-9]|360\\.0|(([1-9][0-9]{0,1}|[1-2][0-9]{1,2}|3[0-5][0-9])\\.[0-9]))|((360\\.[GT])|(([1-9][0-9]{0,1}|[1-2][0-9]{1,2}|3[0-5][0-9])\\.[GT])))?)";
  }
}