package org.mitre.tdp.boogie.arinc.v22.field.qualifiers;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum StarQualifier3 implements FieldSpec<StarQualifier3> {
  SPEC,
  /**
   * RNAV 5 PBN Nav Spec
   */
  Z,
  /**
   * RNAV 2 PBN Nav Spec
   */
  Y,
  /**
   * RNAV 1 PBN nav Spec
   */
  X,
  /**
   * B RNAV
   */
  B,
  /**
   * P RNAV
   */
  P,
  /**
   * RNP 2 PBN Nav Spec
   */
  D,
  /**
   * RNP 1 PBN Nav Spec
   */
  E,
  /**
   * RNP AR PBN Nav Spec
   */
  F,
  /**
   * A-RNP (Advanced RNP) PBN Nav Spec
   */
  A,
  /**
   * RNP 0.3 PBN Nav Spec
   */
  G,
  /**
   * RNP 1 or RNAV 1 PBN Nav Spec
   */
  M,
  /**
   * PBN Nav Spec Unspecified
   */
  U,
  /**
   * VOR/DME RNAV
   */
  V;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a-STAR";
  }

  public static final Set<String> VALID = Arrays.stream(StarQualifier3.values())
      .filter(i -> !SPEC.equals(i))
      .map(StarQualifier3::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<StarQualifier3> apply(String s) {
    return Optional.of(s).filter(VALID::contains).map(StarQualifier3::valueOf);
  }
}
