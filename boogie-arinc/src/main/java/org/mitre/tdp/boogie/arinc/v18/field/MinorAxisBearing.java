package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincDecimalParser;

/**
 * The “Localizer Bearing” field defines the magnetic bearing of the localizer course of the ILS facility/GLS approach described
 * in the record.
 * <br>
 * This field will contain the true bearing in degrees and tenths of a degree, with the decimal point suppressed.
 * <br>
 * e.g. 0900, 2715
 */
public final class MinorAxisBearing implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.100";
  }


  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).flatMap(ArincDecimalParser.INSTANCE::parseDoubleWithTenths);
  }
}
