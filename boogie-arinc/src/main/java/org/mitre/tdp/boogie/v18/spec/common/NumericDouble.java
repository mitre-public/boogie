package org.mitre.tdp.boogie.v18.spec.common;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.FieldSpec;

public interface NumericDouble extends FieldSpec<Double>, FilterTrimEmptyInput<Double> {

  default boolean validValue(String fieldValue) {
    return (fieldValue.startsWith("+") && isNumeric(fieldValue.substring(1)))
        || (fieldValue.startsWith("-") && isNumeric(fieldValue.substring(1)))
        || isNumeric(fieldValue.trim());
  }

  @Override
  default Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, validValue(fieldValue));
    return Double.parseDouble(fieldValue.trim());
  }
}
