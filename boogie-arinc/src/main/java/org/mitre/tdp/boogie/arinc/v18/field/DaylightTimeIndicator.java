package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Daylight” Time Indicator” field is used to indicate if the airport observes Daylight or Summer time when such time changes
 * are in effect for the country or state the airport resides in.
 */
public final class DaylightTimeIndicator implements BooleanString {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.179";
  }
}
