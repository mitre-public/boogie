package org.mitre.tdp.boogie.dafif.v81.field;

/**
 * "THE ""WGS DATUM"" CODE IS DETERMINED BY THE REFERENCE DATUM FOR THE GEODETIC COORDINATES.
 * (DAFIF) 	 	CODE DATUM: 	WGE - WORLD GEODETIC SYSTEM 1984 	WGX - GLOBAL POSITIONING SYSTEM (GPS)"
 *
 * FIELD TYPE: A
 *
 * ALLOWED VALUES:
 * WGE, WGX
 *
 * SOURCE: SYSTEM GENERATED
 */
public final class GeodeticDatum extends TrimmableString {

  @Override
  public int maxFieldLength() {
    return 3;
  }

  @Override
  public int fieldCode() {
    return 133;
  }

  @Override
  public String regex() {
    return "(WGE|WGX)";
  }

}
