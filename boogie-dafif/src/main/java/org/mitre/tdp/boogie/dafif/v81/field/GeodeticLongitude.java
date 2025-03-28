package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE GEODETIC LONGITUDE OF A POINT EXPRESSED AS A COORDINATE BASED ON THE WORLD GRID  SYSTEM.
 *
 *
 * EXAMPLE(S):
 * W055465490
 * E000020532
 * NULL
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * E000000000 THROUGH E179595999
 * OR
 * E180000000
 * OR
 * W000000001 THROUGH W179595999
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 *
 * INTENDED USE:
 * THIS VALUE MAY BE DERIVED IN THE ABSENSE OF HOST PROVIDED VALUES USING IMAGERY OR MENSURATION TO PROVIDE PRECISE MEASUREMENT FOR THIS FIELD IN ACCORDANCE WITH DO-200B AND DO-201.
 */
public final class GeodeticLongitude extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 10;
  }

  @Override
  public int fieldCode() {
    return 135;
  }

  @Override
  public String regex() {
    return "(((E|W)[0-9]{9})?)";
  }
}