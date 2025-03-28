package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The “FIR/UIR Address” field contains the four character communications address of the FIR/UIR to supplement the FIR/UIR Ident
 */
public final class FirUirAddress extends TrimmableString {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.151";
  }
}
