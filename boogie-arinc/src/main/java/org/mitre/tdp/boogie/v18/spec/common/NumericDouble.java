package org.mitre.tdp.boogie.v18.spec.common;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.FieldSpec;

public interface NumericDouble extends FieldSpec<Float> {
  @Override
  default Float parse(String fieldString) {
    checkSpec(this, fieldString, (fieldString.startsWith("-") && isNumeric(fieldString.substring(1))) || isNumeric(fieldString));
    return Float.parseFloat(fieldString);
  }
}
