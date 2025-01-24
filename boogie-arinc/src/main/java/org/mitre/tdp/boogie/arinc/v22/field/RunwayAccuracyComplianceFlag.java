package org.mitre.tdp.boogie.arinc.v22.field;

import org.mitre.tdp.boogie.arinc.TrimmableString;

/**
 * Definition/Description: Flag that indicates if runway parameters meet Runway
 * Accuracy Requirements defined as follows:
 * • Difference between coded Runway Length (PG 5.57) and runway length measured with an independent means (e.g., satellite imagery) is 5 meters or less.
 * • Difference between coded Runway Threshold Position (PG 5.36 and5.37) and runway landing threshold location measured with an independent means (e.g., satellite imagery) is 5 meters or less.
 * • Difference between coded Runway Threshold Displacement Distance (PG 5.69) and runway threshold displacement distance measured with an independent means (e.g., satellite imagery) is 5 meters or less.
 * • Difference between runway true bearing computed using coded Runway Magnetic Bearing (PG 5.58) and coded Airport Magnetic Variation (PA 5.39) and runway true bearing measured with anindependent means (e.g., satellite imagery) is less than 0.5°.
 */
public class RunwayAccuracyComplianceFlag extends TrimmableString {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.318";
  }
}
