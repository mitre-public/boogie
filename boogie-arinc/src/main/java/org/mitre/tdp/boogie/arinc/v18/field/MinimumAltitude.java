package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.AltitudeField;

/**
 * The “Altitude/Minimum Altitude” field indicates the reference altitude associated with.
 * <br>
 * 1. Enroute Airways (MEA, MFA or other minimum altitudes as defined by source)
 * 2. Holding pattern path of Holding Pattern record
 * 3. Altitudes at fixes in terminal procedures and terminal procedure path termination defined by the Path Terminator in the
 * Airport or Heliport SID/STAR/Approach Record.
 * 4. Lowest altitude of the “blocked altitudes” for a Preferred Route.
 * <br>
 * e.g. 05000, FL050, 18000, FL180 00600, -0012, 29000, FL290, UNKNN or NESTB (the last two on Enroute Airways only)
 */
public final class MinimumAltitude extends AltitudeField {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.30";
  }
}
