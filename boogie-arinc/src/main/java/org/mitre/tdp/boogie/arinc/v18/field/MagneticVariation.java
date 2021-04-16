package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.util.CoordinateParser.sign;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;

/**
 * The “Magnetic Variation” field specifies the angular difference between True North and Magnetic North at the location defined in the record.
 * “Dynamic Magnetic Variation” is a computer model derived value and takes location and date into consideration. For the “Station Declination”
 * used in some record types, refer to Section 5.66.
 */
public class MagneticVariation implements FieldSpec<Double>, FilterTrimEmptyInput<Double> {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.39";
  }

  @Override
  public boolean filterInput(String rawInput) {
    return FilterTrimEmptyInput.super.filterInput(rawInput) || rawInput.startsWith("T");
  }

  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, !fieldValue.startsWith("T"));
    return sign(fieldValue.substring(0, 1)) * ArincStrings.parseDoubleWithTenths(fieldValue.substring(1));
  }
}
