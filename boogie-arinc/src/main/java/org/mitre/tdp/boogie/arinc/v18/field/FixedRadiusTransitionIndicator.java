package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;
import org.mitre.tdp.boogie.arinc.utils.ValidArincNumeric;

/**
 * Indicates that a specific turn radius from the inbound course to the outbound course is required by the airspace controlling
 * agency.
 * <br>
 * When a fix radius turn is required a 3 digit numeric value will be entered in this field representing the radius of the turn
 * to 1 decimal place (tenths, decimal point suppressed) in nautical miles. A blank entry in this field indicates that no fixed
 * radius transition is required.
 */
public final class FixedRadiusTransitionIndicator implements FieldSpec<Double> {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.254";
  }

  @Override
  public Optional<Double> apply(String fieldValue) {
    return Optional.of(fieldValue).map(String::trim).filter(ValidArincNumeric.INSTANCE).map(ArincStrings::parseDoubleWithTenths);
  }
}
