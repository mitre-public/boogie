package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;
import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The identification of a DME facility, a TACAN facility or the DME (or TACAN) component of a VORDME or VORTAC facility.
 */
public class DmeIdentifier implements FreeFormString, FilterTrimEmptyInput<String> {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.38";
  }
}
