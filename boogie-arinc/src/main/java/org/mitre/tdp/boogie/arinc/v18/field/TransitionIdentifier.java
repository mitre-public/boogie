package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The “Transition Identifier” field describes the type of transition to be made from the enroute environment into
 * the terminal area and vice versa, and from the terminal area to the approach or from the runway or helipad to the
 * terminal area.
 */
public final class TransitionIdentifier extends TrimmableString {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.11";
  }
}
