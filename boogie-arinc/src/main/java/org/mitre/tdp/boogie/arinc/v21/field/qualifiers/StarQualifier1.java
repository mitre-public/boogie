package org.mitre.tdp.boogie.arinc.v21.field.qualifiers;

import java.util.Arrays;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

import org.mitre.tdp.boogie.arinc.FieldSpec;

public enum StarQualifier1 implements FieldSpec<StarQualifier1> {
  SPEC,
  /**
   * DME Required
   */
  D,
  /**
   * Radar Required
   */
  R,
  /**
   * GNSS Required
   */
  G,
  /**
   * Helicopter STAR to Runway
   */
  H,
  /**
   * Continuous Descent Star
   */
  P;

  @Override
  public int fieldLength() {
    return 1;
  }

  @Override
  public String fieldCode() {
    return "5.7a-STAR";
  }

  public static final Set<String> VALID = Arrays.stream(StarQualifier1.values())
      .filter(d -> !SPEC.equals(d))
      .map(StarQualifier1::name)
      .collect(Collectors.toSet());

  @Override
  public Optional<StarQualifier1> apply(String s) {
    return Optional.of(s).filter(VALID::contains).map(StarQualifier1::valueOf);
  }
}
