package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.utils.ArincStrings;
import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;
import org.mitre.tdp.boogie.v18.spec.common.NumericDouble;

import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

public class GlideSlopeAngle implements NumericDouble, FilterTrimEmptyInput<Double> {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.52";
  }

  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, validValue(fieldValue));
    return ArincStrings.parseDoubleWithHundredths(fieldValue);
  }
}
