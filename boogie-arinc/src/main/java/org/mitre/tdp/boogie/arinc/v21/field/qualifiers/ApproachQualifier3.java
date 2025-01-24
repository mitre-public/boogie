package org.mitre.tdp.boogie.arinc.v21.field.qualifiers;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum ApproachQualifier3 implements FieldSpec<ApproachQualifier3> {
  SPEC,
  /**
   * RNAV 1 PBN Nav Spec
   */
  X,
  /**
   * RNP 1 PBN Nav Spec
   */
  E,
  /**
   * RNP APCH PBN Nav Spec
   */
  H,
  /**
   * RNP 0.3 PBN Nav Spec
   */
  G,
  /**
   * A-RNP (Advance RNP) PBN Nav Spec
   */
  A,
  /**
   * RNP AR PBN Nav Spec
   */
  F,
  /**
   * RNAV Visual Procedure
   */
  B;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a-APPROACH";
  }

  public static final Set<String> VALID = Arrays.stream(ApproachQualifier3.values())
      .filter(i -> !SPEC.equals(i))
      .map(ApproachQualifier3::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<ApproachQualifier3> apply(String s) {
    return Optional.of(s).filter(VALID::contains).map(ApproachQualifier3::valueOf);
  }
}
