package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * DISTANCE BETWEEN WAYPOINTS TO THE NEAREST TENTH OF A NAUTICAL MILE IF INFORMATION AVAILABLE.
 *
 * EXAMPLE(S):
 * 99.0
 * 2.1
 * 119.4
 * 0.8
 * NULL
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 0.1 - 999.9  (LEADING ZEROS ARE SUPPRESSED UNLESS VALUE IS LESS THAN 1.0).
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 *
 * INTENDED USE:
 * DISTANCE IS NOT REQUIRED FOR NORTH AMERICAN ROUTES COMMON WHICH ARE IDENTIFIED AS TYPE='N'.
 */
public final class AtsRouteDistance extends DafifDouble {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 38;
  }

  @Override
  public String regex() {
    return "((([1-9][0-9]{0,2}\\.[0-9])|0\\.[1-9])?)";
  }
}