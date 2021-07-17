package org.mitre.tdp.boogie.arinc.v18.field;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;

/**
 * The “Localizer Frequency” field specifies the VHF frequency of the facility identified in the “Localizer Identifier” field.
 */
public final class LocalizerFrequency implements FieldSpec<Double>, FilterTrimEmptyInput<Double> {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.45";
  }

  /**
   * The localizer frequency in MHz.
   */
  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, isNumeric(fieldValue));
    return ArincStrings.parseDoubleWithHundredths(fieldValue);
  }
}
