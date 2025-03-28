package org.mitre.tdp.boogie.arinc.v21.field.qualifiers;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum SidQualifier3 implements FieldSpec<SidQualifier3> {
  SPEC,
  /**
   * RNAV 5 PBN Nav Sec
   */
  Z,
  /**
   * RNAV 2 PBN Nav Spec
   */
  Y,
  /**
   * RNAV 1 PBN Nav Spec
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
  /***
   * RNP 2 PBN Nav Spec
   */
  D,
  /**
   * RNP 1 PBN Nav Spec
   */
  E,
  /**
   * RNP AR PBN Nav Spec
   * <br>
   * The Qualifier F indicates that the departure is an RNP AR procedure. Implied GNSS required.
   * Qualifier F used with SID route type 0 will designate an RNP AR Engine Out SID.
   * Qualifier F can be used in conjunction with SID route type 1, 2 or 3, provided the corresponding SID transition is AR.
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
   * PBN Nav Spec unspecified
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
    return "5.7a-SID";
  }

  public static final Set<String> VALID = Arrays.stream(SidQualifier3.values())
      .filter(d -> !SPEC.equals(d))
      .map(SidQualifier3::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<SidQualifier3> apply(String s) {
    return Optional.of(s).filter(VALID::contains).map(SidQualifier3::valueOf);
  }
}
