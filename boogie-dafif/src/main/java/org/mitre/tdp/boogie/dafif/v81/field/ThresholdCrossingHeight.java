package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * HEIGHT ABOVE THE LANDING THRESHOLD ON A NORMAL GLIDE PATH.  HEIGHT IS EXPRESSED IN FEET.EXAMPLE(S):002096NULL
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * 00 - 99
 *  OR
 *  NULL
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 *
 * INTENDED USE:
 *  TRM_SEG:  TCH IS FROM OFFICIAL GOVERNMENT SOURCE WHEN AVAILABLE.
 *  WHEN NOT AVAILABLE, 50 FT WILL BE USED.WHEN HOST PROVIDES THE TCH
 *  IN TENTHS, NGA WILL PERFORM STANDARD ROUNDING.
 *  EXAMPLES: 54.4 = 54, 37.5 = 38.
 */
public final class ThresholdCrossingHeight extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 2;
  }

  @Override
  public int fieldCode() {
    return 315;
  }

  @Override
  public String regex() {
    return "(([0-9]{2})?)";
  }
}