package org.mitre.tdp.boogie.arinc.v18.field;

import static java.lang.Math.abs;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.utils.ArincStrings;

/**
 * The “Vertical Angle” field defines the vertical navigation path prescribed for the procedure. The vertical angle should cause
 * the aircraft to fly at the last coded altitude and then descend on the angle, projected back from the fix and altitude code
 * for that fix at which the angle is coded. Vertical Angle information is provided only for descending vertical navigation. The
 * angle is preceded by a “–” (minus sign) to indicate the descending flight.
 */
public final class VerticalAngle implements NumericDouble, FilterTrimEmptyInput<Double> {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.70";
  }

  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, validValue(fieldValue));
    double angle = ArincStrings.parseDoubleWithHundredths(fieldValue);
    checkSpec(this, fieldValue, abs(angle) < 10.0);
    return angle;
  }
}
