package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.ArincDouble;

/**
 * The “Transition Altitude” field defines the altitude in the vicinity of an airport or heliport at or below which the vertical
 * position of an aircraft is controlled by reference to altitudes (MSL).
 * <br>
 * The “Transition Level” field defines the lowest flight level available for use above the transition altitude.
 * <br>
 * Aircraft descending through the transition layer will use altimeters set to local station pressure, while departing aircraft
 * climbing through the layer will be using standard altimeter setting (QNE) of 29.92 inches of mercury, 1013.2 millibars or
 * 1013.2 hectopascals.
 */
public final class TransitionAltitude extends ArincDouble {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.53";
  }
}
