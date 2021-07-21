package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

/**
 * The magnetic bearing of the runway identified in the “runway identifier” field of the record is specified in the “Runway Magnetic Bearing” field.
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
        .filter(ValidArincNumeric.INSTANCE)
        .map(ArincStrings::parseDoubleWithTenths);
  }
}
