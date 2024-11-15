package org.mitre.tdp.boogie.arinc.v18.field;

public class LegLength extends ArincDouble {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.64";
  }
}
