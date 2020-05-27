package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericInteger;

/**
 * The “Longest Runway” field permits airport to be classified on the basis of the longest operational hard-surface runway.
 */
public class LongestRunway implements NumericInteger {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.54";
  }

  /**
   * The longest available runway at the airport in feet.
   */
  @Override
  public Integer parse(String fieldString) {
    return NumericInteger.super.parse(fieldString) * 100;
  }
}
