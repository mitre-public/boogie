package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import java.util.Arrays;
import java.util.List;

/**
 * The “Altitude/Minimum Altitude” field indicates the reference altitude associated with (1) Enroute Airways (MEA, MFA or
 * other minimum altitudes as defined by source), (2) holding pattern path of Holding Pattern record, (3) altitudes at fixes
 * in terminal procedures and terminal procedure path termination defined by the Path Terminator in the Airport or Heliport
 * SID/STAR/Approach Record and (4) lowest altitude of the “blocked altitudes” for a Preferred Route.
 */
public final class MinimumAltitude implements AltitudeFlightLevel {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.30";
  }

  /**
   * Attempts to parse the given raw string of the {@link MinimumAltitude} as a double altitude in feet.
   */
  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, !specialAltitudeCodes().contains(fieldValue));
    return AltitudeFlightLevel.super.parseValue(fieldValue);
  }

  @Override
  public boolean filterInput(String fieldString) {
    return AltitudeFlightLevel.super.filterInput(fieldString) || specialAltitudeCodes().contains(fieldString);
  }

  /**
   * There are two special codes for the {@link MinimumAltitude} which don't directly parse to altitude values - these are
   * explicitly laid out in the ARINC spec and generally show up uncommonly.
   */
  public List<String> specialAltitudeCodes() {
    return Arrays.asList("UNKNN", "NESTB");
  }
}
