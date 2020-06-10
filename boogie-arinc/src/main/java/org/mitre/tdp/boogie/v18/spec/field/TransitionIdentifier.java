package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FilterTrimEmptyInput;
import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The “Transition Identifier” field describes the type of transition to be made from the enroute environment into
 * the terminal area and vice versa, and from the terminal area to the approach or from the runway or helipad to the
 * terminal area.
 */
public class TransitionIdentifier implements FreeFormString, FilterTrimEmptyInput<String> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.11";
  }
}
