package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

public final class HoldingName extends TrimmableString {

  @Override
  public int fieldLength() {
    return 25;
  }

  @Override
  public String fieldCode() {
    return "5.60";
  }
}
