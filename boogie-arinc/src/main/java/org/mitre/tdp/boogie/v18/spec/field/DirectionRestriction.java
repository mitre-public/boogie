package org.mitre.tdp.boogie.v18.spec.field;

import static org.mitre.tdp.boogie.utils.Preconditions.checkSpec;

import java.util.Arrays;
import java.util.List;

import org.mitre.tdp.boogie.FieldSpec;

/**
 * The “Direction Restriction” field, when used on Enroute Airway records, will indicate the direction an Enroute Airway is to be flown.
 * The “Direction Restriction” field, when used on Preferred Route records, will indicate whether the routing is available only in the
 * direction of “from initial fix to terminus fix” or in both directions.
 */
public class DirectionRestriction implements FieldSpec<String> {
  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.115";
  }

  @Override
  public String parseValue(String fieldValue) {
    checkSpec(this, fieldValue, allowedValues().contains(fieldValue));
    return fieldValue;
  }

  /**
   * Allowed values for the field.
   */
  public List<String> allowedValues() {
    return Arrays.asList(" ", "F", "B");
  }
}
