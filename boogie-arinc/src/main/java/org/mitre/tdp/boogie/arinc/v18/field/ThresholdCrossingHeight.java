package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Threshold Crossing Height” specifies the height above the landing threshold on a normal glide path.
 */
public final class ThresholdCrossingHeight implements NumericInteger {
  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.67";
  }
}
