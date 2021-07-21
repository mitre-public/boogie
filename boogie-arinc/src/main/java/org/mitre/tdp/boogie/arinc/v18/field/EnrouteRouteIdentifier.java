package org.mitre.tdp.boogie.arinc.v18.field;

public final class EnrouteRouteIdentifier extends TrimmableString {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.8";
  }
}
