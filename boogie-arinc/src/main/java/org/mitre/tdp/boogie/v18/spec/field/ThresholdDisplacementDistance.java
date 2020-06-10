package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericInteger;

/**
 * The distance from the extremity of a runway to a threshold not located at that extremity of that runway.
 */
public class ThresholdDisplacementDistance implements NumericInteger {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.69";
  }
}
