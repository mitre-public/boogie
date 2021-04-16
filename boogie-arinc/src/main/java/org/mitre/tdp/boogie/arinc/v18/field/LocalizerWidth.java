package org.mitre.tdp.boogie.arinc.v18.field;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;

/**
 * The "Localizer Width" field specifies the localizer course width of the ILS facility defined in the record.
 */
public class LocalizerWidth implements FieldSpec<Double>, FilterTrimEmptyInput<Double> {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.51";
  }

  /**
   * The localizer width in degrees
   */
  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, isNumeric(fieldValue));
    return ArincStrings.parseDoubleWithHundredths(fieldValue);
  }
}
