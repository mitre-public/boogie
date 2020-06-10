package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericInteger;

/**
 * The “Threshold Crossing Height” specifies the height above the landing threshold on a normal glide path.
 */
public class ThresholdCrossingHeight implements NumericInteger {
  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.67";
  }
}
