package org.mitre.tdp.boogie.arinc.v18.field;

public class FirUirIdentifier extends TrimmableString {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.116";
  }
}
