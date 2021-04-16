package org.mitre.tdp.boogie.arinc.v18.field;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public interface NumericDouble extends FieldSpec<Double>, FilterTrimEmptyInput<Double> {

  default boolean validValue(String fieldValue) {
    return (fieldValue.startsWith("+") && isNumeric(fieldValue.substring(1)))
        || (fieldValue.startsWith("-") && isNumeric(fieldValue.substring(1)))
        || (fieldValue.startsWith(" ") && isNumeric(fieldValue.substring(1)))
        || isNumeric(fieldValue);
  }

  @Override
  default Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, validValue(fieldValue));
    return Double.parseDouble(fieldValue);
  }
}
