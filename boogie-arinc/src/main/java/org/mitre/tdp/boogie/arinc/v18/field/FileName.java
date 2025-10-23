package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

public final class FileName extends TrimmableString {
  @Override
  public int fieldLength() {
    return 15;
  }

  @Override
  public String fieldCode() {
    return "6.2.1c";
  }
}
