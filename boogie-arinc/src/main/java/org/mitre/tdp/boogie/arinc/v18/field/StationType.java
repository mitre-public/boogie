package org.mitre.tdp.boogie.arinc.v18.field;

public class StationType extends TrimmableString {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.247";
  }
}
