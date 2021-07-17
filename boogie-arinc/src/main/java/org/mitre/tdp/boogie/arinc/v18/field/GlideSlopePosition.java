package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Glide Slope/Elevation Position” field defines the location of the antenna with respect to the approach end of the runway.
 */
public final class GlideSlopePosition implements NumericInteger {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.50";
  }
}
