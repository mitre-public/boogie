package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;
import org.mitre.tdp.boogie.arinc.utils.ArincStrings;

/**
 * The “Localizer Bearing” field defines the magnetic bearing of the localizer course of the ILS facility/GLS approach described in the record.
 */
public final class LocalizerBearing implements FieldSpec<Double>, FilterTrimEmptyInput<Double> {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.47";
  }

  @Override
  public Double parseValue(String fieldValue) {
    checkSpec(this, fieldValue, !fieldValue.endsWith("T"));
    return ArincStrings.parseDoubleWithTenths(fieldValue);
  }
}
