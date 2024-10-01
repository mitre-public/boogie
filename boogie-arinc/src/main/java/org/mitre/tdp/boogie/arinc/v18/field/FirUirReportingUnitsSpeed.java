package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “FIR/UIR ATC Reporting Units Speed” is used to indicate the units of measurement concerning True Air Speed used in the specific FIR/UIR to fulfill the requirements of ICAO flight plan.
 * 0 - not specified
 * 1 - knots
 * 2 - mach
 * 3 - kilometers/hr
 */
public class FirUirReportingUnitsSpeed extends TrimmableString {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.122";
  }
}
