package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.util.CoordinateParser.sign;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.utils.ArincStrings;

/**
 * The “Magnetic Variation” field specifies the angular difference between True North and Magnetic North at the location defined in the record.
 * “Dynamic Magnetic Variation” is a computer model derived value and takes location and date into consideration. For the “Station Declination”
 * used in some record types, refer to Section 5.66.
 */
public class MagneticVariation implements FieldSpec<Double> {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.39";
  }

  @Override
  public Double parse(String fieldString) {
    checkSpec(this, fieldString, !fieldString.startsWith("T"));
    return sign(fieldString.substring(0, 1)) * ArincStrings.parseDoubleWithTenths(fieldString.substring(1));
  }
}
