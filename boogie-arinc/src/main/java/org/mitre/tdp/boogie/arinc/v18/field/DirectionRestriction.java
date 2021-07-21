package org.mitre.tdp.boogie.arinc.v18.field;

import static com.google.common.collect.Sets.newHashSet;

import java.util.HashSet;
import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

/**
 * The “Direction Restriction” field, when used on Enroute Airway records, will indicate the direction an Enroute Airway is to be flown.
 * The “Direction Restriction” field, when used on Preferred Route records, will indicate whether the routing is available only in the
 * direction of “from initial fix to terminus fix” or in both directions.
 * <br>
 * There are three candidate values for this field:
 * <br>
 * 1. F - only flyable forward
 * 2. B - only flyable backwards
 * 3. blank - no direction restriction
 */
public final class DirectionRestriction implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.115";
  }

  private static final HashSet<String> allowedValues = newHashSet(" ", "F", "B");

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(allowedValues::contains);
  }
}
