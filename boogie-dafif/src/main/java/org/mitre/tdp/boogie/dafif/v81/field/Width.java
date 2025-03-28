package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * TOTAL DISTANCE IN FEET OF THE WIDTH OF THE LANDING SURFACE (EXCLUDES SHOULDERS).
 *
 * NOTE:  VARIABLE WIDTHS WILL BE INDICATED BY ENTERING THE MINIMUM WIDTH.
 *
 * EXAMPLE(S):
 * 00000
 * 00148
 * 01135
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES: 00000 - 20000 (VALUES ARE PADDED WITH LEADING ZEROS.)
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 *
 * INTENDED USE: WHEN HELIPAD IS CIRCULAR WIDTH IS 00000.
 */
public class Width extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 345;
  }

  @Override
  public String regex() {
    return "([0-1][0-9]{4}|20000)";
  }
}
