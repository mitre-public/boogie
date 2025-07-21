package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincDouble;

/**
 * The value for this field will be derived from official government
 * sources or entered into this field in feet with respect to the WGS84 ellipsoid.
 */
public final class StationElevationWgs84 extends ArincDouble {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.248";
  }
}
