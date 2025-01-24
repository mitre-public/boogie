package org.mitre.tdp.boogie.arinc.v22.field.qualifiers;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum StarQualifier2 implements FieldSpec<StarQualifier2> {
  SPEC,
  /**
   * RNAV PBN Nav Spec
   */
  D,
  /**
   * RNP PBN Nav Spec
   */
  E,
  /**
   * FMS Required
   */
  F,
  /**
   * Conventional Arrivals
   */
  G;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a-STAR";
  }

  public static final Set<String> VALID = Arrays.stream(StarQualifier2.values())
      .filter(d -> !SPEC.equals(d))
      .map(StarQualifier2::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<StarQualifier2> apply(String s) {
    return Optional.of(s).filter(VALID::contains).map(StarQualifier2::valueOf);
  }
}
