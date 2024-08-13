package org.mitre.tdp.boogie.arinc.v18.field;

public class FirUirName extends TrimmableString {
  @Override
  public int fieldLength() {
    return 25;
  }

  @Override
  public String fieldCode() {
    return "5.125";
  }
}
