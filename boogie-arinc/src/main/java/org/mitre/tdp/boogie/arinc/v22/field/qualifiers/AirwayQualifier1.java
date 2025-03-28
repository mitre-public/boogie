package org.mitre.tdp.boogie.arinc.v22.field.qualifiers;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum AirwayQualifier1 implements FieldSpec<AirwayQualifier1> {
  SPEC,
  /**
   * GNSS Required
   */
  G,
  /**
   * GNSS or DME/DME/IRU Required
   */
  F,
  /**
   * GNSS, DME/DME/IRU or DME/DME Required
   */
  A,
  /**
   * Equipment requirements unspecified
   */
  U;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a-AIRWAY";
  }

  public static final Set<String> VALID = Arrays.stream(AirwayQualifier1.values())
      .filter(d -> !SPEC.equals(d))
      .map(AirwayQualifier1::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<AirwayQualifier1> apply(String s) {
    return Optional.of(s).filter(VALID::contains).map(AirwayQualifier1::valueOf);
  }
}
