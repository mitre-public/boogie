package org.mitre.tdp.boogie.arinc.assemble;

import java.util.Arrays;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.v21.field.qualifiers.ApproachQualifier3;

public enum PbnNavSpec {
  /**
   * W
   */
  RNAV_10,
  /**
   * Z
   */
  RNAV_5,
  /**
   * Y
   */
  RNAV_2,
  /**
   * X
   */
  RNAV_1,
  /**
   * B
   */
  B_RNAV,
  /**
   * P
   */
  P_RNAV,
  /**
   * C
   */
  RNP_4,
  /**
   * D
   */
  RNP_2,
  /**
   * E
   */
  RNP_1,
  /**
   * H
   */
  RNP_APCH,
  /**
   * A
   */
  ADVANCED_RNP,
  /**
   * F
   */
  RNP_AR,
  /**
   * G
   */
  RNP_03,
  /**
   * B
   */
  RNAV_VISUAL,
  /**
   * V
   */
  VOR_DME_RNAV,
  /**
   * N
   */
  CONVENTIONAL_IN_RNAV_RNP,
  /**
   * M
   */
  EITHER_RNP1_RNAV1,
  /**
   * U
   */
  NOT_SPECIFIED;

  public static final Set<String> VALID = Arrays.stream(PbnNavSpec.values())
      .map(PbnNavSpec::name)
      .collect(Collectors.toSet());
}
