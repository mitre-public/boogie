package org.mitre.tdp.boogie.v18.spec.field;

import static java.lang.Double.parseDouble;
import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.apache.commons.lang3.StringUtils;
import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.utils.ArincStrings;

/**
 * Required Navigation Performance (RNP) is a statement of the Navigation Performance necessary for operation within a defined
 * airspace in accordance with ICAO Annex 15 and/or State published rules.
 */
public class Rnp implements FieldSpec<Double> {
  @Override
  public int fieldLength() {
    return 3;
  }

  @Override
  public String fieldCode() {
    return "5.211";
  }

  /**
   * The required navigational precision in nm.
   */
  @Override
  public Double parse(String fieldString) {
    checkSpec(this, fieldString, isNumeric(fieldString));
    return fieldString.startsWith("0")
        // 0 start means negative exponent - hmm string manipulation is ok here - alternatively division
        ? parseDouble("0.".concat(StringUtils.leftPad(fieldString.substring(1, 2), Integer.parseInt(fieldString.substring(2)), '0')))
        // non-zero start means normal 2 digits with tenths
        : ArincStrings.parseDoubleWithTenths(fieldString);
  }
}
