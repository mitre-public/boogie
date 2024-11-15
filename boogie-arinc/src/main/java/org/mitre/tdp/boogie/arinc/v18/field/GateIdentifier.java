package org.mitre.tdp.boogie.arinc.v18.field;

public final class GateIdentifier extends TrimmableString {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.56";
  }
}