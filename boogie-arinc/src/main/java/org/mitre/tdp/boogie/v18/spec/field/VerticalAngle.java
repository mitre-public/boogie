package org.mitre.tdp.boogie.v18.spec.field;

import static java.lang.Math.abs;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;

/**
 * The “Vertical Angle” field defines the vertical navigation path prescribed for the procedure. The vertical angle should cause
 * the aircraft to fly at the last coded altitude and then descend on the angle, projected back from the fix and altitude code
 * for that fix at which the angle is coded. Vertical Angle information is provided only for descending vertical navigation. The
 * angle is preceded by a “–” (minus sign) to indicate the descending flight.
 */
public class VerticalAngle implements FieldSpec<Double>, FilterTrimEmptyInput<Double> {
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
    checkSpec(this, fieldValue, fieldValue.startsWith("-") && isNumeric(fieldValue.substring(1)));
    double angle = -1.0
        * (Double.parseDouble(fieldValue.substring(1, fieldValue.length() - 2)) // pre-decimal
        + (Double.parseDouble(fieldValue.substring(fieldValue.length() - 2)) / 100.0)); //hundreths
    checkSpec(this, fieldValue, abs(angle) < 10.0);
    return angle;
  }
}
