package org.mitre.tdp.boogie.arinc.v22.field.qualifiers;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum AirwayQualifier2 implements FieldSpec<AirwayQualifier2> {
  SPEC,
  /**
   * FRT (Fixed Radius Transition)Required
   */
  R,
  /**
   * Parallel Offset Required
   */
  P,
  /**
   * TOAC (Time of Arrival Control) Required
   */
  T;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a-AIRWAY";
  }

  public static final Set<String> VALID = Arrays.stream(AirwayQualifier2.values())
      .filter(d -> !SPEC.equals(d))
      .map(AirwayQualifier2::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<AirwayQualifier2> apply(String s) {
    return Optional.of(s).filter(VALID::contains).map(AirwayQualifier2::valueOf);
  }
}
