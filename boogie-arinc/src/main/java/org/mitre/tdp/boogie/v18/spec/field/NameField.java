package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;
import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * This field will be used to further define the record by name.
 */
public class NameField implements FreeFormString, FilterTrimEmptyInput<String> {
  @Override
  public int fieldLength() {
    return 30;
  }

  @Override
  public String fieldCode() {
    return "5.71";
  }
}
