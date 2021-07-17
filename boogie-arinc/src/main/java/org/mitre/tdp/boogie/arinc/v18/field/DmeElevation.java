package org.mitre.tdp.boogie.arinc.v18.field;

/**
 * The “DME Elevation” field defines the elevation of the DME component of the NAVAID described in the record.
 */
public final class DmeElevation implements NumericDouble {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.40";
  }
}
