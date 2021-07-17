package org.mitre.tdp.boogie.arinc.v18.field;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * Required Navigation Performance (RNP) is a statement of the Navigation Performance necessary for operation within a defined
 * airspace in accordance with ICAO Annex 15 and/or State published rules.
 */
public final class Rnp implements FieldSpec<Double>, FilterTrimEmptyInput<Double> {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.211";
  }

  /**
   * The required navigational precision in nm.
   */
  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, isNumeric(fieldValue));
    int value = Integer.parseInt(fieldValue.substring(0, 2));
    int exp = -Integer.parseInt(fieldValue.substring(2));
    return value * Math.pow(10., exp);
  }
}
