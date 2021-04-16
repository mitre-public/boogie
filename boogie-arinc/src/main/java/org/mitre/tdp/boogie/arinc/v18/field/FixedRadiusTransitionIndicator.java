package org.mitre.tdp.boogie.arinc.v18.field;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;

/**
 * Indicates that a specific turn radius from the inbound course to the outbound course is required by the airspace controlling agency.
 */
public class FixedRadiusTransitionIndicator implements FieldSpec<Double>, FilterTrimEmptyInput<Double> {

  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.254";
  }

  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, isNumeric(fieldValue));
    return ArincStrings.parseDoubleWithTenths(fieldValue);
  }
}
