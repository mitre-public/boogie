package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincDouble;

/**
 * Indicates that a specific turn radius from the inbound course to the outbound course is required by the airspace controlling
 * agency.
 * <br>
 * When a fix radius turn is required a 3 digit numeric value will be entered in this field representing the radius of the turn
 * to 1 decimal place (tenths, decimal point suppressed) in nautical miles. A blank entry in this field indicates that no fixed
 * radius transition is required.
 */
public final class FixedRadiusTransitionIndicator extends ArincDouble {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.254";
  }

  @Override
  protected int suppressedDecimalPlaces() {
    return 1;
  }
}
