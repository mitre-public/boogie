package org.mitre.tdp.boogie.arinc.v18.field;

public class ReportingUnitsAltitude extends TrimmableString {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.123";
  }
}
