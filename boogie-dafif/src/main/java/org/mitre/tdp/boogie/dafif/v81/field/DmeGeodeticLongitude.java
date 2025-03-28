package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE GEODETIC LONGITUDE OF A POINT EXPRESSED AS A COORDINATE BASED ON THE WORLD GRID  SYSTEM.
 *
 * IN THE CASE OF DME_WGS_LONG, THE POINT IS DERIVED BY SURVEY.
 *
 * FIELD TYPE: A/N
 *
 * ALLOWED VALUES:
 * E000000000 - E179595999
 * OR
 * E180000000
 * OR
 * W000000001 - W179595999
 * OR
 * NULL
 *
 * SOURCE: HOST NATION PUBLICATION OR NGA ORIGINATED
 */
public final class DmeGeodeticLongitude extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 10;
  }

  @Override
  public int fieldCode() {
    return 360;
  }

  @Override
  public String regex() {
    return "(((E|W)[0-9]{9})?)";
  }
}