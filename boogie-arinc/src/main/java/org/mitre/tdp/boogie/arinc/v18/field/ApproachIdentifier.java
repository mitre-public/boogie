package org.mitre.tdp.boogie.arinc.v18.field;

public final class ApproachIdentifier extends TrimmableString {

  @Override
  public int fieldLength() {
    return 6;
  }

  @Override
  public String fieldCode() {
    return "5.10";
  }
}
