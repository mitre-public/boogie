package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincDecimalParser;

/**
 * The magnetic bearing of the runway identified in the “runway identifier” field of the record is specified in the “Runway Magnetic Bearing” field.
 * <br>
 * Runway magnetic bearings derived from official government sources are entered into the field in degrees and tenths of a degree, with the decimal
 * point suppressed. For runway bearings charted with true bearings, the last character of this field will contain a “T” in place of tenths of a degree.
 * <br>
 * This parser ignores runway bearings charted in degrees true.
 * <br>
 * e.g. 1800, 2302, 0605, 347T
 */
public final class RunwayMagneticBearing implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.58";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue)
        .map(String::trim)
        .filter(s -> !s.endsWith("T"))
        .flatMap(ArincDecimalParser.INSTANCE::parseDoubleWithTenths);
  }
}
