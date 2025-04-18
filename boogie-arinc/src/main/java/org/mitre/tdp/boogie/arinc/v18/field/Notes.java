package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

public final class Notes extends TrimmableString {
  private final int length;

  public Notes(int length) {
    this.length = length;
  }

  @Override
  public int fieldLength() {
    return length;
  }

  @Override
  public String fieldCode() {
    return "5.61";
  }
}
