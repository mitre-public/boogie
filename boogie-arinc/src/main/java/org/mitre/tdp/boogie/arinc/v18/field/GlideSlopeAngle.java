package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincDouble;

/**
 * The “Glide Slope Angle” field defines the glide slope angle of an ILS facility/GLS approach. The “Minimum Elevation Angle”
 * field defines the lowest elevation angle authorized for the MLS procedure.
 * <br>
 * Glide Slope and Elevation angles from official government sources are entered into the fields in degrees, tenths of a degree
 * and hundredths of a degree with the decimal point suppressed.
 */
public final class GlideSlopeAngle extends ArincDouble {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.52";
  }

  @Override
  protected int suppressedDecimalPlaces() {
    return 2;
  }
}
