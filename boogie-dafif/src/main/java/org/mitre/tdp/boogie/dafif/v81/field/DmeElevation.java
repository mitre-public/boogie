package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * MEASURED IN FEET AT THE BASE OF THE ANTENNA FROM MEAN SEA LEVEL (MSL).EXAMPLE(S):U000000645200010-0004
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * -1299 - 29029  (PADDED WITH LEADING ZEROS)
 * OR
 * U (UNKNOWN)
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 *
 * INTENDED USE:
 * IF DME ELEVATION IS NULL IN THE NGA PRODUCTION DATABASE AND NAVAID ELEVATION IS NOT NULL AND
 * NAVAID TYPE = 2, 3, 4, OR 7 THEN DME ELEVATION WILL BE POPULATED WITH NAVAID ELEVATION.
 */
public final class DmeElevation extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 101;
  }

  @Override
  public String regex() {
    return "([0-1][0-9]{4}|2[0-8][0-9]{3}|290[0-2][0-9]|\\-[0-1][0-3][0-9]{2}|U)";
  }
}