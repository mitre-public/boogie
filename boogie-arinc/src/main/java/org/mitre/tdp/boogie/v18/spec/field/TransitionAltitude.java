package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericDouble;

/**
 * The “Transition Altitude” field defines the altitude in the vicinity of an airport or heliport at or below which the vertical position
 * of an aircraft is controlled by reference to altitudes (MSL). The “Transition Level” field defines the lowest flight level available
 * for use above the transition altitude. Aircraft descending through the transition layer will use altimeters set to local station pressure,
 * while departing aircraft climbing through the layer will be using standard altimeter setting (QNE) of 29.92 inches of mercury, 1013.2
 * millibars or 1013.2 hectopascals.
 */
public class TransitionAltitude implements NumericDouble {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.53";
  }
}
