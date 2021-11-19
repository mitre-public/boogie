package org.mitre.tdp.boogie.arinc.v18.field;

import java.util.Optional;

import org.mitre.tdp.boogie.arinc.FieldSpec;

import com.google.common.collect.ImmutableSet;

/**
 * The “Speed Limit Description” field will designate whether the speed limit coded at a fix in a terminal procedure
 * description is a mandatory, minimum or maximum speed.
 * <br>
 * " " or "@" - indicate the limit value should be interpreted as the speed to be AT (if provided)
 * "+" - AT OR ABOVE
 * "-" - AT OR BELOW
 */
public final class SpeedLimitDescription implements FieldSpec<String> {

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.261";
  }

  @Override
  public Optional<String> apply(String fieldValue) {
    return Optional.of(fieldValue).filter(allowedValues::contains);
  }

  static final ImmutableSet<String> allowedValues = ImmutableSet.of(
      " ", // AT
      "@", // also AT
      "+",
      "-"
  );
}
