package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.utils.ArincStrings;

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