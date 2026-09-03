package org.mitre.tdp.boogie.arinc.v18.field;

import static org.apache.commons.math3.util.FastMath.abs;

import org.mitre.tdp.boogie.arinc.ArincDouble;

/**
 * he Runway Gradient field indicates an overall gradient in percent, measured from the start of take-off roll end of the runway
 * designated in the record.
 * <br>
 * The gradient is expressed as a positive or negative gradient; positive being an upward and negative being a downward gradient.
 * <br>
 * The values will be derived from official government source. The first position will be either a “+” or a “-” sign indicating
 * upward or downward gradient. Positions 2 through 5 indicate the gradient with the decimal point suppressed.
 * <br>
 * The Maximum Gradient that can be expressed in this field is (+9.000 or -9.000).
 * <br>
 * e.g. +0450, -0300
 */
public final class RunwayGradient extends ArincDouble {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.212";
  }

  @Override
  protected int suppressedDecimalPlaces() {
    return 3;
  }

  @Override
  protected boolean isValidValue(double value) {
    return abs(value) < 9.;
  }
}
