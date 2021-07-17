package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Longest Runway” field permits airport to be classified on the basis of the longest operational hard-surface runway.
 */
public final class LongestRunway implements NumericInteger {

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
  public Integer parseValue(String fieldValue) {
    return NumericInteger.super.parseValue(fieldValue) * 100;
  }
}
