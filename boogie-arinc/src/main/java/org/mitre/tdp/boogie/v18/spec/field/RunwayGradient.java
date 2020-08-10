package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.utils.ArincStrings;
import org.mitre.tdp.boogie.v18.spec.common.NumericDouble;

public class RunwayGradient implements NumericDouble {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.212";
  }

  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, validValue(fieldValue));
    return ArincStrings.parseDoubleWithThousandths(fieldValue);
  }
}
