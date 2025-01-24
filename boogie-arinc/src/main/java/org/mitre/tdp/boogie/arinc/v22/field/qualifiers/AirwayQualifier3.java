package org.mitre.tdp.boogie.arinc.v22.field.qualifiers;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum AirwayQualifier3 implements FieldSpec<AirwayQualifier3> {
  SPEC,
  /**
   * RNAV 10 PBN Nav Spec
   */
  W,
  /**
   * RNAV 5 PBN Nav Spec
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
  /**
   * RNP 4 PBN Nav Spec
   */
  C,
  /**
   * RNP 2 PBN Nav Spec
   */
  D,
  /**
   * RNP 1 PBN Nav Spec
   */
  E,
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
  V,
  /**
   * Non RNAV/RNP segment in an RNAV/RNP airway
   * <br>
   * Note: The N will be coded if an airway is coded with Route Type R but
   * includes non PBN segments. In these cases, Qualifier 1 and 2
   * will be blank (i.e. null).
   */
  N;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a-AIRWAY";
  }

  public static final Set<String> VALID = Arrays.stream(AirwayQualifier3.values())
      .filter(d -> !SPEC.equals(d))
      .map(AirwayQualifier3::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<AirwayQualifier3> apply(String s) {
    return Optional.of(s).filter(VALID::contains).map(AirwayQualifier3::valueOf);
  }
}
