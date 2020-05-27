package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericDouble;

/**
 * “RHO” is defined as the geodesic distance in nautical miles to the waypoint identified in the record’s “Fix Ident” field
 * from the NA V AID in the “Recommended NAVAID” field.
 */
public class Rho implements NumericDouble {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.25";
  }
}
