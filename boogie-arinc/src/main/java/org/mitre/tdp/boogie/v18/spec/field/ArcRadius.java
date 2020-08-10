package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.utils.ArincStrings;
import org.mitre.tdp.boogie.v18.spec.common.NumericDouble;

public class ArcRadius implements NumericDouble {
  @Override
  public int fieldLength() {
    return 6;
  }

  @Override
  public String fieldCode() {
    return "5.204";
  }

  /**
   * The radius of the precision turn in NM.
   */
  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, validValue(fieldValue));
    return ArincStrings.parseDoubleWithThousandths(fieldValue);
  }
}
