package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * THE GEODETIC LATITUDE OF A POINT EXPRESSED AS A COORDINATE BASED ON THE WORLD GRID SYSTEM.
 *
 * IN THE CASE OF DME_WGS_LAT, THE POINT IS DERIVED BY SURVEY
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
 */
public final class DmeGeodeticLatitude extends TrimmableString {
  @Override
  public int maxFieldLength() {
    return 9;
  }

  @Override
  public int fieldCode() {
    return 359;
  }

  @Override
  public String regex() {
    return "(((N|S)[0-9]{8})?)";
  }
}