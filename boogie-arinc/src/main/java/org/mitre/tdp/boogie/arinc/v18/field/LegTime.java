package org.mitre.tdp.boogie.arinc.v18.field;

public class LegTime extends ArincInteger {

  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.65";
  }
}
