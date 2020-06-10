package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericDouble;

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
