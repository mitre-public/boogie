package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * f required, additional information concerning a runway can be included in a record in the “Runway Description” field.
 */
public final class RunwayDescription implements FreeFormString {
  @Override
  public int fieldLength() {
    return 22;
  }

  @Override
  public String fieldCode() {
    return "5.59";
  }
}
