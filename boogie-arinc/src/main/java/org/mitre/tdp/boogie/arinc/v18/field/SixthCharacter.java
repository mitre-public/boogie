package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * Enroute airway records have an optional sixth character on the route name.
 */
public final class SixthCharacter extends TrimmableString {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.8b";
  }
}
