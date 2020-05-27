package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericDouble;

/**
 * The “DME Elevation” field defines the elevation of the DME component of the NAVAID described in the record.
 */
public class DmeElevation implements NumericDouble {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.40";
  }
}
