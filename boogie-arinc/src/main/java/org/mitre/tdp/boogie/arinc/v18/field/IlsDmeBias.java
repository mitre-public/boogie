package org.mitre.tdp.boogie.arinc.v18.field;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;

/**
 * This field is used to specify the DME offset.
 */
public final class IlsDmeBias implements FieldSpec<Double>, FilterTrimEmptyInput<Double> {
  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.90";
  }

  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, isNumeric(fieldValue));
    return ArincStrings.parseDoubleWithTenths(fieldValue);
  }
}
