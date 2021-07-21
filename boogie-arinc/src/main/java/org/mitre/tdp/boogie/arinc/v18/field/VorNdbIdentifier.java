package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “VOR/NDB Identifier” field identifies the VHF/MF/LF facility defined in the record.
 */
public final class VorNdbIdentifier extends TrimmableString {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.33";
  }
}
