package org.mitre.tdp.boogie.arinc.v22.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * Flag that indicates if the Runway Landing Threshold Elevation meets Accuracy Requirements defined as follows:
 * Difference between Runway Landing Threshold Elevation (A424 PG 5.68)
 * and runway landing threshold elevation measured with an independent means is 5 meters or less.
 */
public final class LandingThresholdElevationAccuracyComplianceFlag extends TrimmableString {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.319";
  }
}
