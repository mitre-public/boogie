package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * “Stopway” means the length of an area beyond the take-off runway, no less wide than the runway and centered upon the extended
 * centerline of the runway, and designated for use in decelerating the airplane during an aborted takeoff.
 */
public class Stopway implements NumericInteger {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.79";
  }
}
