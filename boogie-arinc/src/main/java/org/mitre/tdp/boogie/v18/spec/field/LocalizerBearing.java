package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.FieldSpec;
import org.mitre.tdp.boogie.utils.ArincStrings;

/**
 * The “Localizer Bearing” field defines the magnetic bearing of the localizer course of the ILS facility/GLS approach described in the record.
 */
public class LocalizerBearing implements FieldSpec<Double> {
  @Override
  public int fieldLength() {
    return 4;
  }

  @Override
  public String fieldCode() {
    return "5.47";
  }

  @Override
  public Double parse(String fieldString) {
    checkSpec(this, fieldString, !fieldString.endsWith("T"));
    return ArincStrings.parseDoubleWithTenths(fieldString);
  }
}
