package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE HIGHEST ELEVATION IN THE FIRST 3,000 FEET OF THE USABLE LANDING SURFACE MEASURED IN FEET AND TENTHS
 * OF FEET FROM MEAN SEA LEVEL (MSL).  A MINUS (-) SIGN PRECEDING THE ELEVATION INDICATES BELOW MEAN SEA LEVEL.
 *
 * EXAMPLE(S):
 * -182.0
 * 20.0
 * 13325.0
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 *  -1299.0 to 29029.0
 * 	OR
 * 	U (UNKNOWN)
 *
 * SOURCE: HOST NATION PUBLICATION
 */
public class TDZE extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 7;
  }

  @Override
  public int fieldCode() {
    return 310;
  }

  @Override
  public String regex() {
    return "(((\\-([1-9][0-9]{0,2}|1[0-2][0-9]{2}|0))|0|[1-9][0-9]{0,3}|1[0-9]{4}|2[0-8][0-9]{3}|290[0-1][0-9]|2902[0-8])\\.[0-9]|29029\\.0|U)";
  }
}
