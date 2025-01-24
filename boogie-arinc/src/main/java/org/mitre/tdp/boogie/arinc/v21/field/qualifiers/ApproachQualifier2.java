package org.mitre.tdp.boogie.arinc.v21.field.qualifiers;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum ApproachQualifier2 implements FieldSpec<ApproachQualifier2> {
  SPEC,
  /**
   * Primary Missed Approach
   */
  A,
  /**
   * Secondary Missed Approach
   */
  /**
   * Engine Out Missed Approach
   */
  E,
  /**
   * Procedure with circle to land minimums
   */
  C,
  /**
   * Helicopter with Straight in minimums
   */
  H,
  /**
   * Helicopter with circle to land minimums
   */
  I,
  /**
   * Helicopter with helicopter landing minimums
   */
  L,
  /**
   * Procedure with straight in minimums
   */
  S,
  /**
   * Procedure with VMC minimums
   */
  V,
  /**
   * PinsS Procedure proceed visually
   */
  W,
  /**
   * PinsS Procedure proceed VFR
   */
  X;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a-APPROACH";
  }

  public static final Set<String> VALID = Arrays.stream(ApproachQualifier2.values())
      .filter(i -> !SPEC.equals(i))
      .map(ApproachQualifier2::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<ApproachQualifier2> apply(String s) {
    return Optional.of(s).filter(VALID::contains).map(ApproachQualifier2::valueOf);
  }
}
