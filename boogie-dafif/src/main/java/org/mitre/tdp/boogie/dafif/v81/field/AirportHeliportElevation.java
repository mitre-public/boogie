package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * AIRPORT/HELIPORT:  THE ELEVATION OF THE HIGHEST POINT ON A  LANDING SURFACE  MEASURED IN FEET FROM MEAN SEA LEVEL (MSL).  (GENERAL PLANNING)
 *
 * EXAMPLE(S):
 * 00062
 * -0035
 * -1200
 * 29011
 *
 * FIELD TYPE: N
 *
 * ALLOWED VALUES:
 * -1299 - 29029 (PADDED WITH LEADING ZEROS)
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 *
 * INTENDED USE:
 * THIS VALUE MAY BE DERIVED IN THE ABSENSE OF HOST PROVIDED VALUES USING IMAGERY OR MENSURATION TO PROVIDE PRECISE MEASUREMENT FOR THIS FIELD IN ACCORDANCE WITH DO-200A/B AND DO-201.
 */
public final class AirportHeliportElevation extends DafifInteger {
  @Override
  public int maxFieldLength() {
    return 5;
  }

  @Override
  public int fieldCode() {
    return 43;
  }

  @Override
  public String regex() {
    return "([0-1][0-9]{4}|2[0-8][0-9]{3}|290[0-2][0-9]|\\-(1[0-2][0-9]{2}|0[0-9]{2}[1-9]|0[0-9][1-9][0-9]|0[1-9][0-9]{2}))";
  }
}