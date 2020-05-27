package org.mitre.tdp.boogie.v18.spec.field;

import static org.apache.commons.lang3.StringUtils.isNumeric;
import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.FieldSpec;

/**
 * The “Localizer Frequency” field specifies the VHF frequency of the facility identified in the “Localizer Identifier” field.
 */
public class LocalizerFrequency implements FieldSpec<Double> {
  @Override
  public int fieldLength() {
    return 5;
  }

  @Override
  public String fieldCode() {
    return "5.45";
  }

  @Override
  public Double parse(String fieldString) {
    checkSpec(this, fieldString, isNumeric(fieldString));
    return Double.parseDouble(fieldString.substring(0, 3))
        + (Double.parseDouble(fieldString.substring(3)) / 100.0);
  }
}
