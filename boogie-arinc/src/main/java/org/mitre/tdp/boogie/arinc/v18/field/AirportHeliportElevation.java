package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

/**
 * The elevation of the Airport/Heliport specified in the record is defined in the “Airport Elevation” and “Heliport Elevation” field.
 * <br>
 * This class trims the input field value - treating any strings which are empty when trimmed as "null/missing" values.
 */
public final class AirportHeliportElevation implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.55";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(ValidArincNumeric.INSTANCE).map(String::trim).map(Double::parseDouble);
  }
}
