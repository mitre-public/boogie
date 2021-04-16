package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The elevation of the Airport/Heliport specified in the record is defined in the “Airport Elevation” and “Heliport Elevation” field.
 */
public class AirportHeliportElevation implements NumericDouble {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.55";
  }
}
