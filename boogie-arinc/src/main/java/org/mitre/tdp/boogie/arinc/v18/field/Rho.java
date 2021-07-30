package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincDecimalParser;

/**
 * “RHO” is defined as the geodesic distance in nautical miles to the waypoint identified in the record’s “Fix Ident” field
 * from the NA V AID in the “Recommended NAVAID” field.
 * <br>
 * Rho values derived from official government sources will be used when available. They are entered into the field in nautical
 * miles and tenths of a nautical mile, with the decimal point suppressed. The content is controlled through requirements of
 * the Path Terminator and coding rules contained in Attachment 5 of this specification.
 * <br>
 * e.g. 0000, 0216, 0142, 1074
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
    return Optional.of(fieldValue).map(String::trim).flatMap(ArincDecimalParser.INSTANCE::parseDoubleWithTenths);
  }
}
