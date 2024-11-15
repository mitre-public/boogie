package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “Runway Length” field defines the total length of the runway surface declared suitable and available for ground
 * operations of aircraft for the runway identified in the records’ Runway Identifier field.
 * <br>
 * Runway lengths are derived from official government sources and are entered in feet with a resolution of one foot. The value
 * represents the overall length of the runway, with no regard for displaced thresholds. It does not include stopways, overruns
 * or clearways. Available landing lengths and take-off runs are not necessarily identical to this runway length. Analysis of
 * the content of Section 5.69, Displaced Threshold and 5.79, Stopway is required to determine these operational lengths. As the
 * latitude/longitude information in the runway record reflects the Landing Threshold Point of the runway identified in the record,
 * which may or may not be displaced, there is no direct correlation between the Runway Length and a value calculated based on
 * these latitude/longitude values.
 */
public final class RunwayLength extends ArincInteger {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.57";
  }
}
