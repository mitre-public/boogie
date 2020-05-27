package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.FreeFormString;

/**
 * The “VOR/NDB Identifier” field identifies the VHF/MF/LF facility defined in the record.
 */
public class VorNdbIdentifier implements FreeFormString {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.33";
  }
}
