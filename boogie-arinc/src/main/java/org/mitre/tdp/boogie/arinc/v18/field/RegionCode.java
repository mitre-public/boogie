package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Region Code” permits the categorization of waypoints and holding patterns as either enroute or terminal area waypoints.
 * In the latter case the terminal area airport is identified in the field.
 */
public final class RegionCode extends TrimmableString {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.41";
  }
}
