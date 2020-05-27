package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.AltitudeFlightLevel;

/**
 * “Speed Limit Altitude” is the altitude below which speed limits may be imposed.
 */
public class SpeedLimitAltitude implements AltitudeFlightLevel {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.73";
  }
}
