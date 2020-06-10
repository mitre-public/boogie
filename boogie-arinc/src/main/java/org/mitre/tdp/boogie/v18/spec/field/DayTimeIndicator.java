package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.BooleanString;

/**
 * The “Daylight” Time Indicator” field is used to indicate if the airport observes Daylight or Summer time when such time changes
 * are in effect for the country or state the airport resides in.
 */
public class DayTimeIndicator implements BooleanString {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.179";
  }
}
