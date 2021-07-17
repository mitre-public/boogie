package org.mitre.tdp.boogie.arinc.v18.field;

import static org.mitre.tdp.boogie.arinc.utils.Preconditions.checkSpec;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “EU Indicator” field is used to identify those Enroute Airway records that have an Airway Restriction record
 * without identifying the restriction.
 */
public final class EuIndicator implements FieldSpec<Boolean> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.164";
  }

  @Override
  public Boolean parseValue(String fieldValue) {
    checkSpec(this, fieldValue, fieldValue.trim().isEmpty() || fieldValue.equalsIgnoreCase("Y"));
    return fieldValue.equalsIgnoreCase("Y");
  }
}
