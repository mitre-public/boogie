package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincDecimalParser;

/**
 * “Theta” is defined as the magnetic bearing to the waypoint identified in the record’s “FIX Ident” field from the Navaid in
 * the “Recommended Navaid” field.
 * <br>
 * Theta values are derived from official government sources when available. They are provided in degrees and tenths of a degree,
 * with the decimal point suppressed. The content is controlled through requirements of the Path Terminator and coding rules
 * contained in Attachment 5 of this specification.
 * <br>
 * e.g. 0000, 0756, 1217 1800
 * <br>
 * This parser will return the values as parsed doubles.
 */
public final class Theta implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.24";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).flatMap(ArincDecimalParser.INSTANCE::parseDoubleWithTenths);
  }
}
