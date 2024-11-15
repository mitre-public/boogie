package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * A standard cruising level table is established by ICAO and is to be observed except when, on the basis of regional air navigation agreements,
 * a modified table of cruising levels is prescribed for use. This field permits the enroute airway record to identify the Cruise Table record
 * that is to be used for cruise levels.
 */
public final class CruiseTableIndicator extends TrimmableString {

  @Override
  public int fieldLength() {
    return 2;
  }

  @Override
  public String fieldCode() {
    return "5.134";
  }
}
