package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The width of the runway identified in the “Runway Identifier” field is specified in the “Runway Width” field.
 * <br>
 * Runway widths derived from Official Government Sources are entered into the field in feet, with a resolution of one foot. For
 * runways of variable width, the minimum width encountered over the runway length will be entered.
 */
public final class RunwayWidth extends ArincInteger {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.109";
  }
}
