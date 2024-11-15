package org.mitre.tdp.boogie.arinc.v18.field;

public class Name extends TrimmableString {
  @Override
  public int fieldLength() {
    return 25;
  }

  @Override
  public String fieldCode() {
    return "5.60";
  }
}
