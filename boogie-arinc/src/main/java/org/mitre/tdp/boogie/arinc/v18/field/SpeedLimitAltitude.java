package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * “Speed Limit Altitude” is the altitude below which speed limits may be imposed.
 * <br>
 * The “Speed Limit Altitude” will be derived from official government sources in feet MSL or FL’s.
 */
public final class SpeedLimitAltitude implements AltitudeFlightLevel {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.73";
  }
}
