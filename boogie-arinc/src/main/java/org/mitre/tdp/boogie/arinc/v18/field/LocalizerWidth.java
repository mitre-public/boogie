package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincDecimalParser;

/**
 * The "Localizer Width" field specifies the localizer course width of the ILS facility defined in the record.
 * <br>
 * Localizer course widths from official government sources are entered into the field in degrees, tenths of a degree and hundredths
 * of a degree with the decimal point suppressed.
 * <br>
 * e.g. 0500, 0400, 0350
 */
public final class LocalizerWidth implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.51";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).flatMap(ArincDecimalParser.INSTANCE::parseDoubleWithHundredths);
  }
}
