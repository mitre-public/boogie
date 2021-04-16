package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The elevation of the landing threshold of the runway described in a runway record is defined in the “Landing Threshold Elevation” field.
 */
public class LandingThresholdElevation implements NumericInteger {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.68";
  }
}
