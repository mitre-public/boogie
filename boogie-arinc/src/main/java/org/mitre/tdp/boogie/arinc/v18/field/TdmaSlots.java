package org.mitre.tdp.boogie.arinc.v18.field;

public class TdmaSlots extends TrimmableString {
  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.246";
  }
}