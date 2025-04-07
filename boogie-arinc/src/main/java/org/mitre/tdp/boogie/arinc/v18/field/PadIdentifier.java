package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The PAD Identifier field identifies the helipad described in the heliport records, helipad field,
 * or that pad served by ILS/MLS described in the Airport and Heliport ILS/MLS records.
 */
public final class PadIdentifier extends TrimmableString {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.180";
  }
}
