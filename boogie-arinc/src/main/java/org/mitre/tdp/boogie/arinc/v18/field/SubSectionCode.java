package org.mitre.tdp.boogie.arinc.v18.field;

public class SubSectionCode implements FreeFormString, FilterTrimEmptyInput<String> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.5";
  }
}
