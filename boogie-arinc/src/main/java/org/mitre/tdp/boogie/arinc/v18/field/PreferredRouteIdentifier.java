package org.mitre.tdp.boogie.arinc.v18.field;

public final class PreferredRouteIdentifier extends TrimmableString{

  @Override
  public int fieldLength() {
    return 10;
  }

  @Override
  public String fieldCode() {
    return "5.8";
  }
}
