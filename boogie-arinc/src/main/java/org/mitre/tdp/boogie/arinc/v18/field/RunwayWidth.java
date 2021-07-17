package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The width of the runway identified in the “Runway Identifier” field is specified in the “Runway Width” field.
 */
public final class RunwayWidth implements NumericInteger {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.109";
  }
}
