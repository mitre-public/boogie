package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE MAGNETIC BEARING TO THE REFERENCE NAVAID IN DEGREES AND TENTHS OF DEGREES WHEN A WAYPOINTS LOCATION IS DEFINED.
 *
 * POPULATED WHEN A WAYPOINT'S LOCATION IS DEFINED BY A BEARING AND DISTANCE FROM A NAVAID.
 *
 * EXAMPLE(S):
 * 1.0
 * 97.9
 * 359.7
 * 0.7
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 0.1 - 360.0 (LEADING ZEROS ARE SUPPRESSED UNLESS VALUE IS LESS THAN 1.0)
 * OR
 * NULL
 *
 * SOURCE: TRANSLATED/FORMATTED FROM HOST NATION PUBLICATION
 */
public final class WaypointBearing extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 175;
  }

  @Override
  public String regex() {
    return "((0\\.[1-9]|360\\.0|([1-9][0-9]{0,1}|[1-2][0-9]{1,2}|3[0-5][0-9])\\.[0-9])?)";
  }
}