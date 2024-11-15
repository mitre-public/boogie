package org.mitre.tdp.boogie.arinc.v18.field;

public class GlsChannel extends ArincInteger {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.244";
  }
}