package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE GEODETIC LATITUDE OF A POINT EXPRESSED AS A COORDINATE BASED ON THE WORLD GRID SYSTEM.
 *
 * EXAMPLE(S):
 * N52333376
 * S64140590
 * NULL
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * N00000000 - N89595999
 * OR
 * N90000000
 * OR
 * S00000001 - S89595999
 * OR
 * S90000000
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 *
 * INTENDED USE:
 * THIS VALUE MAY BE DERIVED IN THE ABSENSE OF HOST PROVIDED VALUES USING IMAGERY OR MENSURATION TO PROVIDE PRECISE MEASUREMENT FOR THIS FIELD IN ACCORDANCE WITH DO-200B AND DO-201.
 */
public final class GeodeticLatitude extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 9;
  }

  @Override
  public int fieldCode() {
    return 134;
  }

  @Override
  public String regex() {
    return "(((N|S)[0-9]{8})?)";
  }
}