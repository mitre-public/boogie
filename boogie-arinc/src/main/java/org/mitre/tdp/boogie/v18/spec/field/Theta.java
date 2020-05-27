package org.mitre.tdp.boogie.v18.spec.field;

import org.mitre.tdp.boogie.v18.spec.common.NumericDouble;

/**
 * “Theta” is defined as the magnetic bearing to the waypoint identified in the record’s “FIX Ident” field from
 * the Navaid in the “Recommended Navaid” field.
 */
public class Theta implements NumericDouble {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.24";
  }
}
