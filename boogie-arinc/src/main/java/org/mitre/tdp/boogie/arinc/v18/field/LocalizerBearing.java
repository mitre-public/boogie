package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincDecimalParser;

/**
 * The “Localizer Bearing” field defines the magnetic bearing of the localizer course of the ILS facility/GLS approach described
 * in the record.
 * <br>
 * Localizer courses, derived from official government sources, are entered into the field in degrees and tenths of a degree,
 * with the decimal point suppressed. For localizer courses charted with true courses, the last character of this field will
 * contain a “T” in place of tenths of a degree.
 * <br>
 * As with {@link InboundMagneticCourse} we explicitly drop true courses because they're a hassle to deal with downstream.
 */
public final class LocalizerBearing implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.47";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim)
        // explicitly drop true course localizer bearings
        .filter(s -> !s.endsWith("T"))
        .flatMap(ArincDecimalParser.INSTANCE::parseDoubleWithTenths);
  }
}
