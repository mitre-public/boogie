package org.mitre.tdp.boogie.arinc.v18.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * The “FIR/UIR ATC Reporting Units Altitude” field is used to indicate the units of measurement concerning the altitude used in the specific FIR/UIR to fulfill the requirements of ICAO flight plan.
 * 0 - not specified
 * 1 - alt in flight level
 * 2 - alt in meters
 * 3 - alt in feet
 */
public final class FirUirReportingUnitsAltitude extends TrimmableString {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.123";
  }
}
