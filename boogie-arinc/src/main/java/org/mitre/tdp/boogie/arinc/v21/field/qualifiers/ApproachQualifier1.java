package org.mitre.tdp.boogie.arinc.v21.field.qualifiers;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum ApproachQualifier1 implements FieldSpec<ApproachQualifier1> {
  SPEC,
  /**
   * DME Required
   */
  D,
  /**
   * GPS (GNSS) required, DNE/DME to RNP xx.x not authorized
   */
  J,
  /**
   * GBAS procedure
   */
  L,
  /**
   * DME Not Required for Procedure
   */
  N,
  /**
   * GNSS Required
   */
  P,
  /**
   * GPS (GNSS) or DME/DME to RNP xx.x required
   */
  R,
  /**
   * DME/DME Required for Procedure
   */
  T,
  /**
   * RNAV or RNP, Sensor Not Specified
   */
  U,
  /**
   * VOR/DME RNAV
   */
  V,
  /**
   * Procedure that requires SBAS FAS Data Block
   */
  W;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a-APPROACH";
  }

  public static final Set<String> VALID = Arrays.stream(ApproachQualifier1.values())
      .filter(i -> !SPEC.equals(i))
      .map(ApproachQualifier1::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<ApproachQualifier1> apply(String s) {
    return Optional.of(s).filter(VALID::contains).map(ApproachQualifier1::valueOf);
  }
}
