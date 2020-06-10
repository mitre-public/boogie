package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;
import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

public class SubSectionCode implements FreeFormString, FilterTrimEmptyInput<String> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.5";
  }
}
