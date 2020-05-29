package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.apache.commons.lang3.StringUtils;
import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.utils.ArincStrings;
import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;

/**
 * The “VOR/NDB Frequency” field specifies the frequency of the NAVAID identified in the “VOR/NDB Identifier” field of the record.
 */
public class VorNdbFrequency implements FieldSpec<Double>, FilterTrimEmptyInput<Double> {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.34";
  }

  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, StringUtils.isNumeric(fieldValue));
    return ArincStrings.parseDoubleWithTenths(fieldValue);
  }
}
