package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE ELEVATION AT THE POINT OF THE DISPLACED THRESHOLD RUNWAY CENTERLINE ON THE LOW END OF THE
 * RUNWAY MEASURED IN FEET AND TENTHS OF FEET FROM MEAN SEA LEVEL  (MSL).  A MINUS (-) SIGN PRECEDING
 * THE ELEVATION INDICATES BELOW MSL.
 *
 *  VALUES ARE OUTPUT TO THE TENTH OF A FOOT.
 *
 * EXAMPLE(S):
 * -182.0
 * 3002.0
 * 13325.0
 * 3825.9
 * -128.1
 * U
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * -1299.9 THRU 29029.0
 * OR
 * U (UNKNOWN)
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 *
 * INTENDED USE:
 * THIS VALUE MAY BE DERIVED IN THE ABSENSE OF HOST PROVIDED VALUES USING IMAGERY OR MENSURATION TO
 * PROVIDE PRECISE MEASUREMENT FOR THIS FIELD IN ACCORDANCE WITH DO-200B AND DO-201.
 */
public class LowEndRunwayDisplacedThresholdElevation extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 7;
  }

  @Override
  public int fieldCode() {
    return 54;
  }

  @Override
  public String regex() {
    return "((\\-(([1-9][0-9]{0,2}|1[0-2][0-9]{2})\\.[0-9]|(0\\.[1-9])))|(((0|[1-9][0-9]{0,3}|1[0-9]{4}|2[0-8][0-9]{3}|290[0-1][0-9]|2902[0-8]|29028)\\.[0-9])|29029\\.0)|U)";
  }
}
