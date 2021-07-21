package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

/**
 * “RHO” is defined as the geodesic distance in nautical miles to the waypoint identified in the record’s “Fix Ident” field
 * from the NA V AID in the “Recommended NAVAID” field.
 */
public final class Rho implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.25";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(ValidArincNumeric.INSTANCE).map(ArincStrings::parseDoubleWithTenths);
  }
}
