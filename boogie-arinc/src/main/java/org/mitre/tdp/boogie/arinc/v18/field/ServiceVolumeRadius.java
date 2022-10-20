package org.mitre.tdp.boogie.arinc.v18.field;

public class ServiceVolumeRadius extends ArincInteger {

  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.245";
  }
}
