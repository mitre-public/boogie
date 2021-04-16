package org.mitre.tdp.boogie.arinc.v18.field;

public class SidStarIdentifier implements FreeFormString, FilterTrimEmptyInput<String> {

  @Override
  public int fieldLength() {
    return 6;
  }

  @Override
  public String fieldCode() {
    return "5.9";
  }
}
