package org.mitre.tdp.boogie.arinc.v18.field;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public interface NumericInteger extends FieldSpec<Integer>, FilterTrimEmptyInput<Integer> {

  @Override
  default Integer parseValue(String fieldValue) {
    checkSpec(this, fieldValue, (fieldValue.startsWith("-") && isNumeric(fieldValue.substring(1))) || isNumeric(fieldValue));
    return Integer.parseInt(fieldValue);
  }
}
