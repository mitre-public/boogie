package org.mitre.tdp.boogie.v18.spec.common;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.FieldSpec;

public interface NumericInteger extends FieldSpec<Integer> {

  @Override
  default Integer parse(String fieldString) {
    checkSpec(this, fieldString, (fieldString.startsWith("-") && isNumeric(fieldString.substring(1))) || isNumeric(fieldString));
    return Integer.parseInt(fieldString);
  }
}
