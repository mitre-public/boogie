package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericInteger;

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
