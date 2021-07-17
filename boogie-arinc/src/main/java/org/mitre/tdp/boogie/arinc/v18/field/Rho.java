package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.utils.ArincStrings;

/**
 * “RHO” is defined as the geodesic distance in nautical miles to the waypoint identified in the record’s “Fix Ident” field
 * from the NA V AID in the “Recommended NAVAID” field.
 */
public final class Rho implements NumericDouble {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.25";
  }

  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, validValue(fieldValue));
    return ArincStrings.parseDoubleWithTenths(fieldValue);
  }
}
